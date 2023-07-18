package gatling.desafiovotacao;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.FeederBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class DesafioVotacaoSimulation extends Simulation {

    // RUNTIME PARAMETERS
    private static final int USER_COUNT = Integer.parseInt(System.getProperty("USERS", "1000"));
    private static final int RAMP_DURATION = Integer.parseInt(System.getProperty("RAMP_DURATION", "10"));

    // FEEDER FOR TEST DATA
    private static String idPauta = "";
    private static FeederBuilder.FileBased<Object> associadoFeeder = jsonFile("gatling/data/associadosJsonFile.json").queue();

    // HTTP Calls
    private static ChainBuilder criaAssociado =
            feed(associadoFeeder)
                    .exec(http("Cria associado - #{cpf}")
                            .post("/associados")
                            .body(ElFileBody("gatling/bodies/criaAssociadoTemplate.json")).asJson()
                            .check(
                                    status().is(201),
                                    jmesPath("id").saveAs("idAssociado")
                            ));
    private static ChainBuilder criaPauta = exec(http("Cria pauta")
            .post("/pautas")
            .body(StringBody(
                    "{\n" +
                            "  \"titulo\": \"Pauta " + (Math.random() * 10) + "\",\n" +
                            "  \"descricao\": \"Teste de carga\"\n" +
                            "}"))
            .check(
                    status().is(201),
                    jmesPath("id").saveAs("idPauta")
            ));
    private static ChainBuilder criaSessao = exec(http("Cria sessao")
            .post("/pautas/#{idPauta}/sessao")
            .body(StringBody(
                    "{\n" +
                            "  \"duracao\": 60\n" +
                            "}"))
            .check(status().is(201)));
    private static ChainBuilder votaEmPauta = exec(http("Vota em pauta")
            .post(session -> "/pautas/" + idPauta + "/voto")
            .body(StringBody(
                    "{\n" +
                            "  \"idAssociado\": #{idAssociado},\n" +
                            "  \"voto\": \"" + getRandomSimNao() + "\"\n" +
                            "}"))
            .check(status().is(200)));
    private static ChainBuilder buscaResultadoVotacao = exec(http("Busca resultado de votacao de pauta")
            .get(session -> "/pautas/" + idPauta + "/resultado")
            .check(status().is(200)));


    // Http Configuration
    private HttpProtocolBuilder httpProtocolBuilder = http
            .baseUrl("http://localhost:8080")
            .acceptHeader("application/json")
            .contentTypeHeader("application/json;charset=UTF-8");

    // Scenario Definition
    // 1. Cria pauta
    // 2. Cria sessao
    // 3. Cria associado
    // 4. Vota em pauta
    // 5. Busca resultado de votacao de pauta

    private ScenarioBuilder scenarioBefore = scenario("Cria Pauta e Sessão à serem testadas")
            .exec(criaPauta)
            .pause(2)
            .exec(criaSessao)
            .pause(1)
            .exec(session -> {
                idPauta = session.getString("idPauta");
                return session;
            });

    private ScenarioBuilder scenarioAfter = scenario("Busca Resultado da Pauta testada")
            .pause(10)
            .exec(buscaResultadoVotacao);

    private ScenarioBuilder scenarioVotacao = scenario("Simulando vários associados votando na mesma pauta")
            .pause(1)
            .exec(criaAssociado)
            .pause(1)
            .exec(votaEmPauta);

    // Load Simulation
    {
        setUp(
                scenarioBefore.injectOpen(atOnceUsers(1)),
                scenarioVotacao.injectOpen(
                        nothingFor(5),
                        rampUsers(USER_COUNT).during(RAMP_DURATION)
                ),
                scenarioAfter.injectOpen(
                        nothingFor(30),
                        atOnceUsers(1)
                )
        ).protocols(httpProtocolBuilder);
    }

    private static String getRandomSimNao() {
        String[] simNao = {"SIM", "NAO"};
        return simNao[(int) (Math.random() * simNao.length)];
    }

    // BEFORE BLOCK
    @Override
    public void before() {
        System.out.printf("Iniciando teste de carga com %d usuários%n", USER_COUNT);
        System.out.printf("Aumentando usuários depois de %d segundos%n", RAMP_DURATION);
    }
}
