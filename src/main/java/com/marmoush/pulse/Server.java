package com.marmoush.pulse;

import com.marmoush.jutils.core.utils.yaml.YamlConfigMap;
import org.slf4j.*;

import java.time.Duration;
import java.util.stream.Stream;

import static com.marmoush.jutils.core.utils.yaml.YamlUtils.parseYamlFile;
import static com.marmoush.jutils.core.utils.yaml.YamlUtils.parseYamlResource;

public class Server {
  private static final Logger LOG = LoggerFactory.getLogger(Server.class.getName());
  private static final String DEFAULT_CONFIGS = "app.yaml";

  public static void main(String[] args) {
    YamlConfigMap configs = Stream.of(args)
                                  .filter(s -> s.contains("--config="))
                                  .findFirst()
                                  .map(s -> s.split("=")[1])
                                  .map(file -> parseYamlFile(file, true))
                                  .orElse(parseYamlResource(DEFAULT_CONFIGS))
                                  .get();
    AppConfig appConfig = new AppConfig(configs);
    Dependencies deps = new Dependencies(appConfig);
    deps.httpServer.bindNow(Duration.ofSeconds(10)).onDispose().block();
    LOG.info("Server Shutdown!");
  }
}