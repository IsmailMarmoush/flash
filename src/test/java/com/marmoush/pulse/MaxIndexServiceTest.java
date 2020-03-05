package com.marmoush.pulse;

import com.marmoush.jutils.etcd.EtcdStoreClient;
import com.marmoush.jutils.keyvaluestore.KeyValueStoreClient;
import io.etcd.jetcd.Client;
import io.vavr.collection.HashMap;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static io.vavr.control.Option.some;

public class MaxIndexServiceTest {
  private final Client clientBuilt = Client.builder().endpoints("http://localhost:2379").build();
  private final KeyValueStoreClient client = new EtcdStoreClient(clientBuilt);
  private final MaxIndexService maxIndexService = new MaxIndexService(client);

  @Test
  public void maxTest() {
    var map = HashMap.of("key7", "7", "key1", "1", "key3", "3", "key2", "2");
    var flux = Flux.fromIterable(map).flatMap(e -> client.put(e._1, e._2));
    StepVerifier.create(flux).expectNextCount(4).expectComplete().verify();
    StepVerifier.create(maxIndexService.getMax("key")).expectNext(some(7L)).expectComplete().verify();
  }
}
