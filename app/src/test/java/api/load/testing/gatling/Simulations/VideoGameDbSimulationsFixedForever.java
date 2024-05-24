package api.load.testing.gatling.Simulations;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;

public class VideoGameDbSimulationsFixedForever extends Simulation {
    private HttpProtocolBuilder httpProtocol = http
            .baseUrl("https://videogamedb.uk/api")
            .acceptHeader("application/json");

    private static ChainBuilder getAllVideoGames =
            exec(http("Get all video games")
                    .get("/videogame"));

    private static ChainBuilder getSpecificGame =
            exec(http("Get specific game")
                    .get("/videogame/2"));

    private ScenarioBuilder scn = scenario("Video game db - Section 7 code")
            .forever().on(
                    exec(getAllVideoGames)
                            .pause(5)
                            .exec(getSpecificGame)
                            .pause(5)
                            .exec(getAllVideoGames)
            );


    {
        setUp(
                scn.injectOpen(
                        nothingFor(5),
                        atOnceUsers(10),
                        rampUsers(20).during(30)
                ).protocols(httpProtocol)
        ).maxDuration(60);
    }
}