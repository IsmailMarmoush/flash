package com.marmoush.pulse;

import com.marmoush.jutils.core.adapter.generator.id.SerialIdGenerator;
import com.marmoush.jutils.core.domain.port.IdGenerator;
import com.marmoush.jutils.core.utils.yaml.YamlConfigMap;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicInteger;

public class AppConfig {

  public final Server server;
  public final Consul consul;
  public final Security security;

  public AppConfig(YamlConfigMap configs) {
    server = new Server(configs.asYamlConfigMap("server"));
    consul = new Consul(configs.asYamlConfigMap("consul"));
    security = new Security(configs.asYamlConfigMap("security"));
  }

  public class Server {
    public final String host;
    public final int port;
    public final int maxStartupTime;
    public final boolean isWireTapping;
    public final String apiRoot;
    public final IdGenerator idGenerator;

    private Server(YamlConfigMap configs) {
      host = configs.asString("host");
      port = configs.asInteger("port");
      maxStartupTime = configs.asInteger("maxStartupTime");
      isWireTapping = configs.asBoolean("isWiretapping");
      apiRoot = configs.asString("api.root");
      idGenerator = new SerialIdGenerator(new AtomicInteger());
    }
  }

  public class Consul {
    public final String uri;

    private Consul(YamlConfigMap configs) {
      uri = configs.asString("consul.uri");
    }
  }

  public class Security {
    public final boolean isSecure;

    public final File serverSslCertificate;
    public final File serverPrivateKey;

    public final Path clientSslCertificate;
    public final File clientPrivateKey;

    private Security(YamlConfigMap configs) {
      isSecure = Boolean.parseBoolean(configs.asString("server.isSecure"));
      // server
      serverSslCertificate = Paths.get(configs.asString("server.sslCertificatePath")).toFile();
      serverPrivateKey = Paths.get(configs.asString("server.privateKeyPath")).toFile();
      // Client
      clientSslCertificate = Paths.get(configs.asString("client.sslCertificatePath"));
      clientPrivateKey = Paths.get(configs.asString("client.privateKeyPath")).toFile();
    }
  }
}
