package com.resizer.imageresizer;

import com.resizer.imageresizer.property.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({
    AppProperties.class
})
public class ImageResizerApplication {

  public static void main(String[] args) {
    SpringApplication.run(ImageResizerApplication.class, args);
  }

}
