package cn.deathdealer.gittrendings.exceptions;

/**
 * Created by XiongChen on 2017/3/9.
 */
public class TrendingException extends Exception {
    public TrendingException(){
        super();
    }

    public TrendingException(Throwable t){
        super(t);
    }

    public TrendingException(String message){
        super(message);
    }
}
