package cn.deathdealer.gittrendings.controllers;

import cn.deathdealer.gittrendings.entities.Repository;
import cn.deathdealer.gittrendings.entities.ResultVO;
import cn.deathdealer.gittrendings.services.TrendingService;
import com.google.common.base.Stopwatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.text.MessageFormat;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
public class TrendingController {

  private final TrendingService trendingService;

  @Autowired
  public TrendingController(TrendingService trendingService) {
    this.trendingService = trendingService;
  }

  @RequestMapping(
    path = "/trending",
    method = RequestMethod.GET,
    produces = "application/json; charset=UTF-8"
  )
  public ResultVO<List<Repository>> getTrendingListOfGithub(
      @RequestParam(name = "since", required = false) String since,
      @RequestParam(name = "language", required = false) String language) {

    Stopwatch watcher = Stopwatch.createStarted();
    if (StringUtils.isEmpty(since)) {
      //如果没有传获取的区间，默认拉取当天的Trending
      since = "daily";
    }
    if (StringUtils.isEmpty(language)) {
      //如果没有指定编程语言，默认拉取所有类型的Trending
      language = "";
    }
    ResultVO<List<Repository>> result = new ResultVO<>();
    List<Repository> trendingList = trendingService.scrapeGithubTrendings(since, language);
    result.setStatus(0);
    result.setData(trendingList);
    result.setCount(trendingList.size());
    String elapsed = watcher.elapsed(TimeUnit.MILLISECONDS) + "ms";
    result.setElapsed(elapsed);
    result.setMessage(
        MessageFormat.format(
            "parse github trending(since={0},language={1}) successfully.", since, language));
    return result;
  }
}
