import static org.assertj.core.api.Assertions.assertThat;

import au.com.dius.pact.consumer.Pact;
import au.com.dius.pact.consumer.PactProviderRuleMk2;
import au.com.dius.pact.consumer.PactVerification;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.model.RequestResponsePact;
import com.fedomn.feignservice.ComputeClient;
import com.fedomn.feignservice.ComputeResponse;
import com.fedomn.feignservice.FeignServiceApplication;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(
    properties = {
      "eureka.client.enabled: false",
      "compute-service.ribbon.listOfServers: localhost:8181",
    },
    classes = FeignServiceApplication.class)
public class ConsumerPactVerificationTest {

  @Rule
  public PactProviderRuleMk2 stubProvider =
      new PactProviderRuleMk2("compute-service", "localhost", 8181, this);

  @Autowired private ComputeClient computeClient;

  @Pact(state = "provider two numbers", provider = "compute-service", consumer = "computeClient")
  public RequestResponsePact createAddPact(PactDslWithProvider builder) {
    return builder
        .given("provider two numbers")
        .uponReceiving("a request to GET the sum of two numbers")
        .path("/add")
        .query("a=1&b=2")
        .method("GET")
        .willRespondWith()
        .status(200)
        .body(new PactDslJsonBody().booleanValue("status", true).numberValue("computeResult", 3))
        .toPact();
  }

  @Test
  @PactVerification(fragment = "createAddPact")
  public void verifyCreatePersonPact() {
    ComputeResponse addResponse = computeClient.add(1, 2);
    assertThat(addResponse.getStatus()).isTrue();
    assertThat(addResponse.getComputeResult()).isEqualTo(3);
  }
}
