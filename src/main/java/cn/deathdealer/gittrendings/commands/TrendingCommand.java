package cn.deathdealer.gittrendings.commands;

import cn.deathdealer.gittrendings.entities.Repository;
import cn.deathdealer.gittrendings.exceptions.TrendingException;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.netflix.config.ConfigurationManager;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixObservableCommand;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import rx.Observable;
import rx.schedulers.Schedulers;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Collections;
import java.util.List;

/** Created by XiongChen on 2017/3/9. */
public class TrendingCommand extends HystrixObservableCommand<List<Repository>> {
  Logger logger = LoggerFactory.getLogger(TrendingCommand.class);
  private static final int TIMEOUT = 20000;
  private static final boolean ENABLE_TIMEOUT = true;
  private static final int POOL_SIZE = 25;
  private String trendingUrl;

  public TrendingCommand(String trendingUrl) {
    super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("GithubTrending")));
    /* 20秒超时 */
    ConfigurationManager.getConfigInstance()
        .setProperty(
            "hystrix.command.default.execution.isolation.thread.timeoutInMilliseconds", TIMEOUT);

    /* 启用超时 */
    ConfigurationManager.getConfigInstance()
        .setProperty("hystrix.command.default.execution.timeout.enabled", ENABLE_TIMEOUT);

    /* 线程池大小 */
    ConfigurationManager.getConfigInstance()
        .setProperty("hystrix.threadpool.default.coreSize", POOL_SIZE);

    this.trendingUrl = trendingUrl;
  }

  @Override
  protected Observable<List<Repository>> construct() {
    return Observable.create(
		    (Observable.OnSubscribe<List<Repository>>) subscriber -> {
		      if (!subscriber.isUnsubscribed()) {
		        try {
		          subscriber.onNext(getTrendingRepository());
		          subscriber.onCompleted();
		        } catch (TrendingException e) {
		          logger.error("get trending failed:" + e.getMessage());
		          subscriber.onError(e);
		        }
		      }
		    })
        .subscribeOn(Schedulers.immediate());
  }

  private List<Repository> getTrendingRepository() throws TrendingException {
    if (StringUtils.isEmpty(trendingUrl)) {
      throw new TrendingException("invalid github trending url.");
    }

    List<Repository> repositoryList = Lists.newLinkedList();
    try {
      
      Document document = Jsoup.connect(trendingUrl).timeout(15000).get();
      Elements repositories = document.select("article.Box-row");
      if (repositories == null || repositories.size() == 0) {
        return Collections.emptyList();
      }

      repositories.forEach(
          repo -> {
            /* parse basic info */
            Elements item = repo.select("h1 a");
            String title = item.text();
            String url = "https://github.com" + item.attr("href");
            String owner = "";
            String repositoryName = "";
            Splitter splitter = Splitter.on("/").omitEmptyStrings().trimResults();
            if (title != null && title.contains("/")) {
              List<String> basicInfoList = splitter.splitToList(title);
              owner = basicInfoList.get(0);
              repositoryName = basicInfoList.get(1);
            }

            /* parse description */
            String description = repo.select("p.col-9").text();

            /* parse language */
            String language = repo.select("span[itemprop]").text();

            /* parse stars total */
            int totalStars = 0;
            /* parse forks */
            int forks = 0;

            /* deprecated since 2017/03/31 */
            // String strStars = repo.select("a.tooltipped[aria-label=Stargazers]").text();
            // String forksStr = repo.select("a.tooltipped[aria-label=Forks]").text();

            String strStars = repo.select("a.muted-link[href$=stargazers]").text();
            String forksStr = repo.select("a.muted-link[href$=network/members]").text();
            try {
              /* 2,017(string) -> 2017(number) */
              totalStars = NumberFormat.getInstance().parse(strStars).intValue();
              forks = NumberFormat.getInstance().parse(forksStr).intValue();
            } catch (ParseException e) {
              logger.error(
                  "repository:[{}] parse stars/forks error:{}(stars={},forks={})",
                  title,
                  e.getMessage(),
                  strStars,
                  forksStr);
            }

            /* parse contributor list */
            List<String> contributors = Lists.newArrayList();
            Elements conItem = repo.select("a.d-inline-block img.avatar");
            if (conItem != null && conItem.size() > 0) {
              conItem.forEach(contributor -> contributors.add(contributor.attr("alt")));
            }

            /* parse delta */
            String delta = repo.select("span.float-sm-right").text();

            /* create repository instance */
            Repository repository = new Repository();
            repository.setOwner(owner);
            repository.setRepositoryName(repositoryName);
            repository.setUrl(url);
            repository.setDescription(description);
            repository.setLanguage(language);
            repository.setTotalStars(totalStars);
            repository.setForks(forks);
            repository.setContributors(contributors);
            repository.setDelta(delta);
            repositoryList.add(repository);
          });
    } catch (IOException e) {
      throw new TrendingException(e);
    }

    return repositoryList;
  }

	@Override
	protected Observable<List<Repository>> resumeWithFallback() {
		return Observable.empty();
	}
}
