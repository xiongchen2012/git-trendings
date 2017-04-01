package cn.deathdealer.gittrendings.entities;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@ToString
public class Repository {
  @Setter
  @Getter
  @JSONField(name = "repository_name", ordinal = 0)
  private String repositoryName; //项目名

  @Setter
  @Getter
  @JSONField(name = "owner", ordinal = 1)
  private String owner; //作者

  @Setter
  @Getter
  @JSONField(name = "url", ordinal = 2)
  private String url; //Github访问地址

  @Setter
  @Getter
  @JSONField(name = "description", ordinal = 3)
  private String description; //简述

  @Setter
  @Getter
  @JSONField(name = "language", ordinal = 4)
  private String language; //所属语言

  @Setter
  @Getter
  @JSONField(name = "total_stars", ordinal = 5)
  private int totalStars; //总星数

  @Setter
  @Getter
  @JSONField(name = "forks", ordinal = 6)
  private int forks; //总星数

  @Setter
  @Getter
  @JSONField(name="contributors",ordinal = 7)
  private List<String> contributors; //贡献者列表

  @Setter
  @Getter
  @JSONField(name = "delta",ordinal = 8)
  private String delta; //动态信息，如本日获得多少Star，本周获得多少Star,本月获得多少Star
}
