package cn.deathdealer.gittrendings.controllers;

import cn.deathdealer.gittrendings.entities.Repository;
import cn.deathdealer.gittrendings.entities.ResultVO;
import cn.deathdealer.gittrendings.services.TrendingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
public class TrendingController {

  @Autowired TrendingService trendingService;

  @RequestMapping(
    path = "/trendings",
    method = RequestMethod.GET,
    produces = "application/json; charset=UTF-8"
  )
  public ResultVO<List<Repository>> getTrendingListOfGithub(
      @RequestParam(name = "since", required = false) String since,
      @RequestParam(name = "language", required = false) String language) {

    if (StringUtils.isEmpty(since)) {
      //如果没有传获取的区间，默认拉取当天的Trending
      since = "daily";
    }
    if (StringUtils.isEmpty(language)) {
      //如果没有指定编程语言，默认拉取所有类型的Trending
      language = "";
    }
    ResultVO<List<Repository>> result = new ResultVO<>();
    List<Repository> trendings = trendingService.scrapeGithubTrendings(since, language);
    result.setStatus(0);
    result.setData(trendings);
    result.setCount(trendings.size());
    return result;
  }
}
