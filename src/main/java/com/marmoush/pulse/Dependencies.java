package com.marmoush.pulse;

import com.marmoush.jutils.core.adapter.generator.id.SerialIdGenerator;
import com.marmoush.jutils.core.domain.port.IdGenerator;
import com.marmoush.jutils.core.utils.netty.NettyHttpUtils;
import io.netty.handler.ssl.SslContextBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.netty.http.HttpProtocol;
import reactor.netty.http.client.HttpClient;
import reactor.netty.http.server.HttpServer;
import reactor.netty.http.server.HttpServerRoutes;

import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

public final class Dependencies {
  private static final Logger LOG = LoggerFactory.getLogger(Dependencies.class.getName());
  // Dependencies
  public final IdGenerator idGenerator;
  public final HttpServer httpServer;
  public final HttpClient testingClient;

  public Dependencies(AppConfig appConfig) {
    this.idGenerator = new SerialIdGenerator(new AtomicLong());
    // Setup Services
    Consumer<HttpServerRoutes> routes = r -> r.get(appConfig.server.apiRoot,
                                                   (req, resp) -> NettyHttpUtils.send(resp,
                                                                                      200,
                                                                                      this.idGenerator.generate()));
    // Setup Server & Client
    HttpServer tmpServer = HttpServer.create()
                                     .host(appConfig.server.host)
                                     .port(appConfig.server.port)
                                     .route(routes)
                                     .wiretap(appConfig.server.isWireTapping);
    HttpClient tmpClient = HttpClient.create()
                                     .baseUrl(appConfig.server.host + ":" + appConfig.server.port)
                                     .wiretap(appConfig.server.isWireTapping);
    if (appConfig.security.isSecure) {
      SslContextBuilder serverSSLContextBuilder = SslContextBuilder.forServer(appConfig.security.serverSslCertificate,
                                                                              appConfig.security.serverPrivateKey);
      SslContextBuilder clientSSLContextBuilder = SslContextBuilder.forClient();
      tmpServer = tmpServer.protocol(HttpProtocol.H2).secure(spec -> spec.sslContext(serverSSLContextBuilder));
      tmpClient = tmpClient.protocol(HttpProtocol.H2).secure(spec -> spec.sslContext(clientSSLContextBuilder));
    }
    this.httpServer = tmpServer;
    this.testingClient = tmpClient;
  }
}