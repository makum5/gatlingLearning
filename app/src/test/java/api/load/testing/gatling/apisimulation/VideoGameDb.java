package api.load.testing.gatling.apisimulation;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import java.time.Duration;
import java.util.List;


public class VideoGameDb extends Simulation {

    private HttpProtocolBuilder httpProtocolBuilder = http
            .baseUrl("https://videogamedb.uk/api")
            .acceptHeader("application/json");

    private ScenarioBuilder scenarioBuilder = scenario("Video Game DB")
            .exec(
                    http("Get all video games")
                            .get("/videogame")
                            .check(status().is(200))
                            .check(jsonPath("$[?(@.id==1)].name").is("Resident Evil 4"))
                            .check(jsonPath("$[?(@.id==1)].id").is("1").saveAs("gameId"))
                            .check(jmesPath("[? id == `1`].name").ofList().is(List.of("Resident Evil 4")))
            )
            .pause(5)

            .exec(http("Get Specific Game")
                    .get("/videogame/1")
                    .check(status().in(200,201,202)))
            .pause(1,10)

            .exec(http("Get Specific Game with - #{gameId}")
                    .get("/videogame/#{gameId}")
                    .check(status().in(200,201,202))
                    .check(jmesPath("name").is("Resident Evil 4")))
            .pause(1,10)

            .exec(
                    http("Get all video games")
                            .get("/videogame")
                            .check(status().not(404),status().not(500))
                            .check(bodyString().saveAs("responseBody")))
            .pause(Duration.ofMillis(4000))
            .exec(
                    session -> {
                        System.out.println(session);
                        System.out.println("Game Id set to: "+session.getString("gameId"));
                        System.out.println("Response Body: "+session.getString("responseBody"));
                        return session;
                    }
            );

    {
        setUp(
                scenarioBuilder.injectOpen(atOnceUsers(1))
        ).protocols(httpProtocolBuilder);
    }

}
