package com.marmoush.pulse;

import io.vavr.collection.HashMap;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static io.vavr.control.Option.some;

public class MaxIndexTest {
  private EtcdStoreClient client = new EtcdStoreClient("http://localhost:9001");
  private MaxIndex maxIndex = new MaxIndex(client);

  @Test
  public void maxTest() {
    var map = HashMap.of( "key7", "7", "key1", "1", "key3", "3" , "key2", "2");
    var flux = Flux.fromIterable(map).flatMap(e -> client.put(e._1, e._2));
    StepVerifier.create(flux).expectNextCount(4).expectComplete().verify();
    StepVerifier.create(maxIndex.getMax("key")).expectNext(some(7L)).expectComplete().verify();
  }
}
