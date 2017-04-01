package cn.deathdealer.gittrendings.entities;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
public class ResultVO<T> {

  @Getter
  @Setter
  @JSONField(name = "status", ordinal = 0)
  private int status; //接口返回状态值

  @Getter
  @Setter
  @JSONField(name = "code", ordinal = 1)
  private String code; //如有错误，会设置错误Code

  @Getter
  @Setter
  @JSONField(name = "message", ordinal = 2)
  private String message; //接口消息

  @Getter
  @Setter
  @JSONField(name = "developerMessage", ordinal = 3)
  private String developerMessage; //异常消息，方便开发人员查错

  @Getter
  @Setter
  @JSONField(name = "count", ordinal = 4)
  private int count; //接口返回数据条数

  @Getter
  @Setter
  @JSONField(name = "elapsed", ordinal = 5)
  private String elapsed; //接口处理Trending的时间（毫秒）

  @Getter
  @Setter
  @JSONField(name = "data", ordinal = 6)
  private T data; //数据
}
