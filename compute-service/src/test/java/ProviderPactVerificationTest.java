import au.com.dius.pact.provider.junit.Provider;
import au.com.dius.pact.provider.junit.State;
import au.com.dius.pact.provider.junit.loader.PactFolder;
import au.com.dius.pact.provider.junit.target.HttpTarget;
import au.com.dius.pact.provider.junit.target.Target;
import au.com.dius.pact.provider.junit.target.TestTarget;
import au.com.dius.pact.provider.spring.SpringRestPactRunner;
import com.fedomn.computeservice.ComputeServiceApplication;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;

@RunWith(SpringRestPactRunner.class)
@Provider("compute-service")
@PactFolder("../feign-service/target/pacts")
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
    properties = {
      "server.port=8091",
      "eureka.client.enabled=false",
    },
    classes = ComputeServiceApplication.class)
public class ProviderPactVerificationTest {

  @TestTarget public final Target target = new HttpTarget(8091);

  @State({"provider two numbers"})
  public void toSumTwoNumber() {}
}
