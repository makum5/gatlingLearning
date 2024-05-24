package api.load.testing.gatling.apisimulation;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;


public class VideoGameDbLoopingHttp extends Simulation {

    private HttpProtocolBuilder httpProtocolBuilder = http
            .baseUrl("https://videogamedb.uk/api")
            .acceptHeader("application/json");

    private static ChainBuilder getAllVideoGames =
            repeat(3).on(
                    exec(http("Get all video games")
                            .get("/videogame")
                            .check(status().not(404), status().not(500)))
            );

    private static ChainBuilder getSpecificVideoGame =
            repeat(5, "myCounter").on(
                    exec(http("Get specific video game with id: #{myCounter}")
                            .get("/videogame/#{myCounter}")
                            .check(status().is(200)))
            );

    private ScenarioBuilder scn = scenario("Video Game Db - Section 5 code")
            .exec(getAllVideoGames)
            .pause(5)
            .exec(getSpecificVideoGame)
            .pause(5)
            .repeat(2).on(
                    exec(getAllVideoGames)
            );

    {
        setUp(
                scn.injectOpen(atOnceUsers(1))
        ).protocols(httpProtocolBuilder);
    }

}
