package cn.deathdealer.gittrendings.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
public class ResultVO<T> {
    @Getter @Setter private int status;
    @Getter @Setter private String code;
    @Getter @Setter private String message;
    @Getter @Setter private String devloperMessage;
    @Getter @Setter private T data;
    @Getter @Setter private int count;
}
