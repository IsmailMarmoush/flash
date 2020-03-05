package com.marmoush.pulse;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.util.Random;

import static io.vavr.API.Some;
import static io.vavr.control.Option.none;

public class EtcdTest {
  private EtcdClient client = new EtcdClient("http://localhost:9001");
  String keyPrefix = "myKey";
  String value = "myValue";

  @Test
  public void putTest() {
    String key = keyPrefix + new Random().nextInt(1000);
    StepVerifier.create(client.put(key, value)).expectNext("").expectComplete().verify();
    StepVerifier.create(client.put(key, "new_value")).expectNext(value).expectComplete().verify();
  }

  @Test
  public void getTest() {
    String key = keyPrefix + new Random().nextInt(1000);
    StepVerifier.create(client.put(key, value)).expectNext("").expectComplete().verify();
    StepVerifier.create(client.get(key)).expectNext(Some(value)).expectComplete().verify();
  }

  @Test
  public void deletionTest() {
    String key = keyPrefix + new Random().nextInt(1000);
    StepVerifier.create(client.put(key, value)).expectNext("").expectComplete().verify();
    StepVerifier.create(client.delete(key)).expectComplete().verify();
    StepVerifier.create(client.get(key)).expectNext(none()).expectComplete().verify();
  }
}
