package cn.deathdealer.gittrendings.services;

import cn.deathdealer.gittrendings.commands.TrendingCommand;
import cn.deathdealer.gittrendings.entities.Repository;
import com.google.common.collect.ImmutableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import rx.Observable;
import rx.Observer;

import java.util.ArrayList;
import java.util.List;

@Component
public class TrendingService {
  private Logger logger = LoggerFactory.getLogger(TrendingService.class);
  private static final String BASE_URL = "https://github.com/trending";

  private static final ImmutableList<String> allowSince =
      ImmutableList.of("", "daily", "weekly", "monthly");

  private static final ImmutableList<String> allowLanguage =
      ImmutableList.of(
          "",
          "unknown",
          "css",
          "html",
          "java",
          "javascript",
          "php",
          "python",
          "ruby",
          "actionscript",
          "asp",
          "aspectj",
          "c",
          "c%23",
          "c++",
          "clojure",
          "cobol",
          "coffeescript",
          "coldfusion",
          "dart",
          "elixir",
          "erlang",
          "fortran",
          "freemarker",
          "go",
          "gradle",
          "groovy",
          "haml",
          "haskell",
          "http",
          "json",
          "jsx",
          "kotlin",
          "less",
          "lua",
          "markdown",
          "matlab",
          "nginx",
          "object-c",
          "object-c++",
          "object-j",
          "pascal",
          "perl",
          "plsql",
          "r",
          "rust",
          "sass",
          "scala",
          "shell",
          "sql",
          "svg",
          "swift",
          "typescript",
          "visual-basic",
          "xml",
          "yaml",
          "vue");

  public List<Repository> scrapeGithubTrendings(String since, String language) {
    if (StringUtils.isEmpty(since) || !allowSince.contains(since.toLowerCase())) {
      since = "daily";
    }
    if (StringUtils.isEmpty(language) || !allowLanguage.contains(language.toLowerCase())) {
      language = "";
    }
    String trendingUrl = getQueryTrendingUrl(since, language);
    List<Repository> trendingRepository = new ArrayList<>();
    Observable<List<Repository>> command = new TrendingCommand(trendingUrl).observe();
    command.subscribe(
        new Observer<List<Repository>>() {
          @Override
          public void onCompleted() {
            logger.info("get trending completed.");
          }

          @Override
          public void onError(Throwable e) {
            logger.error("get trending failed:"+e.getMessage());
          }

          @Override
          public void onNext(List<Repository> repositories) {
            if (repositories != null && repositories.size() > 0) {
              trendingRepository.addAll(repositories);
            }
          }
        });
    return trendingRepository;
  }

  private String getQueryTrendingUrl(String since, String language) {
    StringBuilder urlBuilder = new StringBuilder(BASE_URL);
    if (!"".equals(language)) {
      urlBuilder.append("/").append(language);
    }
    urlBuilder.append("?since=").append(since);
    return urlBuilder.toString();
  }
}
