package api.load.testing.gatling.apisimulation;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

public class MyFirstTest extends Simulation {

    // 1. Http Configuration

    private HttpProtocolBuilder httpProtocolBuilder = http
            .baseUrl("https://videogamedb.uk/api")
            .acceptHeader("application/json");

    // 2. Scenario Definition

    private ScenarioBuilder scenarioBuilder = scenario("My First Test")
            .exec(
                    http("Get all games")
                            .get("/videogame")
            );

    // 3. Load simulation

    {
        setUp(
                scenarioBuilder.injectOpen(atOnceUsers(1))
        ).protocols(httpProtocolBuilder);
    }
}
