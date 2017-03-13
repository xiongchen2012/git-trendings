package cn.deathdealer.gittrendings;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter4;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.context.annotation.Bean;

/**
 * Spring boot application,The entry of application.
 *
 * @author deathdealer
 * @since 1.0repository
 */
@SpringBootApplication
public class Application {
  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @Bean
  protected HttpMessageConverters fastJsonHttpMessageConverter() {
    FastJsonHttpMessageConverter4 fastConverter = new FastJsonHttpMessageConverter4();
    FastJsonConfig fastJsonConfig = new FastJsonConfig();
    fastJsonConfig.setSerializerFeatures(SerializerFeature.QuoteFieldNames);
    fastConverter.setFastJsonConfig(fastJsonConfig);
    return new HttpMessageConverters(fastConverter);
  }
}
