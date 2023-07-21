package com.matheuszanatta.desafiovotacao.controller;

import com.matheuszanatta.desafiovotacao.controller.dto.request.AssociadoRequest;
import com.matheuszanatta.desafiovotacao.controller.dto.request.PautaRequest;
import com.matheuszanatta.desafiovotacao.controller.dto.request.SessaoRequest;
import com.matheuszanatta.desafiovotacao.controller.dto.request.VotoRequest;
import com.matheuszanatta.desafiovotacao.controller.dto.response.AssociadoResponse;
import com.matheuszanatta.desafiovotacao.controller.dto.response.PautaResponse;
import com.matheuszanatta.desafiovotacao.domain.enums.VotoComputado;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static com.matheuszanatta.desafiovotacao.BaseIntegrationTest.*;
import static com.matheuszanatta.desafiovotacao.domain.enums.ResultadoVoto.*;
import static com.matheuszanatta.desafiovotacao.domain.enums.VotoComputado.NAO;
import static com.matheuszanatta.desafiovotacao.domain.enums.VotoComputado.SIM;
import static java.lang.Long.MAX_VALUE;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest(webEnvironment = RANDOM_PORT)
class PautaControllerIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Value("${app.sessao.duracao}")
    private Integer duracaoPadrao;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    @DisplayName("Busca pauta com resultado aprovado")
    void buscaPautaAprovada() throws Exception {

        var pautaId = incluirPauta("Pauta de teste", "Uma pauta utilizada para testes");

        var joao = incluirAssociado("36686044520", "João da Silva");
        var maria = incluirAssociado("75431122800", "Maria Rita");
        var diego = incluirAssociado("88138327456", "Diego Goulart");

        incluirSessao(pautaId);

        votar(pautaId, joao.getId(), SIM);
        votar(pautaId, maria.getId(), SIM);
        votar(pautaId, diego.getId(), NAO);

        Runnable buscaPautaAprovada = () -> {
            try {
                buscarResultadoDaVotacao(pautaId)
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.votos").value("3"))
                        .andExpect(jsonPath("$.votosSim").value("2"))
                        .andExpect(jsonPath("$.votosNao").value("1"))
                        .andExpect(jsonPath("$.resultado").value(APROVADA.name()));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };

        executaAposDuracaoPadrao(buscaPautaAprovada);
    }

    @Test
    @DisplayName("Busca pauta com resultado reprovado")
    void buscaPautaReprovada() throws Exception {

        var pautaId = incluirPauta("Pauta de teste", "Uma pauta utilizada para testes");

        var joao = incluirAssociado("92539621279", "João da Silva");
        var maria = incluirAssociado("25410771222", "Maria Rita");
        var diego = incluirAssociado("42688458280", "Diego Goulart");

        incluirSessao(pautaId);

        votar(pautaId, joao.getId(), NAO);
        votar(pautaId, maria.getId(), NAO);
        votar(pautaId, diego.getId(), SIM);

        Runnable buscaPautaReprovada = () -> {
            try {
                buscarResultadoDaVotacao(pautaId)
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.votos").value("3"))
                        .andExpect(jsonPath("$.votosSim").value("1"))
                        .andExpect(jsonPath("$.votosNao").value("2"))
                        .andExpect(jsonPath("$.resultado").value(REPROVADA.name()));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };

        executaAposDuracaoPadrao(buscaPautaReprovada);
    }

    @Test
    @DisplayName("Busca pauta com resultado empatado")
    void buscaPautaEmpatada() throws Exception {

        var pautaId = incluirPauta("Pauta de teste", "Uma pauta utilizada para testes");

        var joao = incluirAssociado("98824425283", "João da Silva");
        var maria = incluirAssociado("27917612503", "Maria Rita");

        incluirSessao(pautaId);

        votar(pautaId, joao.getId(), NAO);
        votar(pautaId, maria.getId(), SIM);

        Runnable buscaPautaEmpatada = () -> {
            try {
                buscarResultadoDaVotacao(pautaId)
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.votos").value("2"))
                        .andExpect(jsonPath("$.votosSim").value("1"))
                        .andExpect(jsonPath("$.votosNao").value("1"))
                        .andExpect(jsonPath("$.resultado").value(EMPATE.name()));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };

        executaAposDuracaoPadrao(buscaPautaEmpatada);
    }

    @Test
    @DisplayName("Busca pauta com resultado em andamento")
    void buscaPautaEmAndamento() throws Exception {

        var pautaId = incluirPauta("Pauta de teste", "Uma pauta utilizada para testes");

        incluirSessao(pautaId);

        buscarResultadoDaVotacao(pautaId)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.votos").value("0"))
                .andExpect(jsonPath("$.votosSim").value("0"))
                .andExpect(jsonPath("$.votosNao").value("0"))
                .andExpect(jsonPath("$.resultado").value(EM_ANDAMENTO.name()));
    }

    @Test
    @DisplayName("Busca pauta com resultado sem votos")
    void buscaPautaSemVotos() throws Exception {

        var pautaId = incluirPauta("Pauta de teste", "Uma pauta utilizada para testes");

        incluirSessao(pautaId);

        Runnable buscaPautaSemVotos = () -> {
            try {
                buscarResultadoDaVotacao(pautaId)
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.votos").value("0"))
                        .andExpect(jsonPath("$.votosSim").value("0"))
                        .andExpect(jsonPath("$.votosNao").value("0"))
                        .andExpect(jsonPath("$.resultado").value(SEM_VOTOS.name()));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };

        executaAposDuracaoPadrao(buscaPautaSemVotos);
    }

    @Test
    @DisplayName("Busca pauta aprovada em sessão com duração customizada")
    void buscaPautaAprovadaComSessaoCustomizada() throws Exception {

        var pautaId = incluirPauta("Pauta de teste", "Uma pauta utilizada para testes");

        var joao = incluirAssociado("95617885944", "João da Silva");
        var maria = incluirAssociado("85696852203", "Maria Rita");
        var diego = incluirAssociado("45225449018", "Diego Goulart");

        var duracao = 5;
        incluirSessao(pautaId, duracao);

        votar(pautaId, joao.getId(), SIM);
        votar(pautaId, maria.getId(), SIM);
        votar(pautaId, diego.getId(), NAO);

        Runnable buscaPautaAprovada = () -> {
            try {
                buscarResultadoDaVotacao(pautaId)
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.votos").value("3"))
                        .andExpect(jsonPath("$.votosSim").value("2"))
                        .andExpect(jsonPath("$.votosNao").value("1"))
                        .andExpect(jsonPath("$.resultado").value(APROVADA.name()));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };

        executaAposDuracao(buscaPautaAprovada, duracao);
    }

    @Test
    @DisplayName("Deve lançar erro quando busca resultado com pauta inexistente")
    void deveLancarErroQuandoBuscaResultadoComPautaInexistente() throws Exception {

        var statusEsperado = NOT_FOUND.value();
        var mensagemEsperada = "Pauta não encontrada";

        buscarResultadoDaVotacao(MAX_VALUE)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(statusEsperado))
                .andExpect(jsonPath("$.message").value(mensagemEsperada))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    @DisplayName("Deve lançar erro quando busca resultado com sessão inexistente")
    void deveLancarErroQuandoBuscaResultadoComSessaoInexistente() throws Exception {

        var pautaId = incluirPauta("Pauta de teste", "Uma pauta utilizada para testes");

        var statusEsperado = NOT_FOUND.value();
        var mensagemEsperada = "Sessão não encontrada";

        buscarResultadoDaVotacao(pautaId)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(statusEsperado))
                .andExpect(jsonPath("$.message").value(mensagemEsperada))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    @DisplayName("Deve lançar erro quando incluir sessão em pauta inexistente")
    void deveLancarErroQuandoIncluirSessaoEmPautaInexistente() throws Exception {

        var request = new AssociadoRequest();
        var statusEsperado = NOT_FOUND.value();
        var mensagemEsperada = "Pauta não encontrada";

        mockMvc.perform(post("/pautas/" + MAX_VALUE + "/sessao")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(statusEsperado))
                .andExpect(jsonPath("$.message").value(mensagemEsperada))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    @DisplayName("Deve lançar erro quando incluir sessão já existente")
    void deveLancarErroQuandoIncluirSessaoJaExistente() throws Exception {

        var pautaId = incluirPauta("Pauta de teste", "Uma pauta utilizada para testes");

        incluirSessao(pautaId);

        var request = new AssociadoRequest();
        var statusEsperado = UNPROCESSABLE_ENTITY.value();
        var mensagemEsperada = "Já existe uma sessão para esta pauta";

        mockMvc.perform(post("/pautas/" + pautaId + "/sessao")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(request)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.status").value(statusEsperado))
                .andExpect(jsonPath("$.message").value(mensagemEsperada))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    @DisplayName("Deve lançar erro quando votar com valor inválido")
    void deveLancarErroQuandoVotarComValorInvalido() throws Exception {

        var pautaId = 1L;
        var statusEsperado = BAD_REQUEST.value();
        var mensagemEsperada = "Requisição inválida";

        var votoRequestEnumInvalido = "{\"idAssociado\":1,\"voto\":\"INVALIDO\"}";

        mockMvc.perform(post("/pautas/" + pautaId + "/voto")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(votoRequestEnumInvalido))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(statusEsperado))
                .andExpect(jsonPath("$.message").value(mensagemEsperada))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    @DisplayName("Deve lançar erro quando votar em pauta inexistente")
    void deveLancarErroQuandoVotarEmPautaInexistente() throws Exception {

        var request = new VotoRequest();
        request.setVoto(SIM);
        request.setIdAssociado(1L);

        var statusEsperado = NOT_FOUND.value();
        var mensagemEsperada = "Pauta não encontrada";

        mockMvc.perform(post("/pautas/" + MAX_VALUE + "/voto")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(statusEsperado))
                .andExpect(jsonPath("$.message").value(mensagemEsperada))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    @DisplayName("Deve lançar erro quando votar com sessão inexistente")
    void deveLancarErroQuandoVotarComSessaoInexistente() throws Exception {

        var pautaId = incluirPauta("Pauta de teste", "Uma pauta utilizada para testes");

        var request = new VotoRequest();
        request.setVoto(SIM);
        request.setIdAssociado(1L);

        var statusEsperado = NOT_FOUND.value();
        var mensagemEsperada = "Sessão não encontrada";

        mockMvc.perform(post("/pautas/" + pautaId + "/voto")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(statusEsperado))
                .andExpect(jsonPath("$.message").value(mensagemEsperada))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    @DisplayName("Deve lançar erro quando votar com sessão encerrada")
    void deveLancarErroQuandoVotarComSessaoEncerrada() throws Exception {

        var duracaoSessao = 1;

        var pautaId = incluirPauta("Pauta de teste", "Uma pauta utilizada para testes");

        incluirSessao(pautaId, duracaoSessao);

        var request = new VotoRequest();
        request.setVoto(SIM);
        request.setIdAssociado(1L);

        var statusEsperado = UNPROCESSABLE_ENTITY.value();
        var mensagemEsperada = "Sessão de votação encerrada";

        Runnable votarComSessaoEncerrada = () -> {
            try {
                mockMvc.perform(post("/pautas/" + pautaId + "/voto")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(toJson(request)))
                        .andExpect(status().isUnprocessableEntity())
                        .andExpect(jsonPath("$.status").value(statusEsperado))
                        .andExpect(jsonPath("$.message").value(mensagemEsperada))
                        .andExpect(jsonPath("$.timestamp").exists());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };

        executaAposDuracao(votarComSessaoEncerrada, duracaoSessao);
    }

    @Test
    @DisplayName("Deve lançar erro quando votar com associado inexistente")
    void deveLancarErroQuandoVotarComAssociadoInexistente() throws Exception {

        var pautaId = incluirPauta("Pauta de teste", "Uma pauta utilizada para testes");

        incluirSessao(pautaId);

        var request = new VotoRequest();
        request.setVoto(SIM);
        request.setIdAssociado(MAX_VALUE);

        var statusEsperado = NOT_FOUND.value();
        var mensagemEsperada = "Associado não encontrado";

        mockMvc.perform(post("/pautas/" + pautaId + "/voto")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(statusEsperado))
                .andExpect(jsonPath("$.message").value(mensagemEsperada))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    @DisplayName("Deve lançar erro quando votar na mesma pauta")
    void deveLancarErroQuandoVotarNaMesmaPauta() throws Exception {

        var pautaId = incluirPauta("Pauta de teste", "Uma pauta utilizada para testes");

        incluirSessao(pautaId);

        var joao = incluirAssociado("85952764045", "João da Silva");

        votar(pautaId, joao.getId(), SIM);

        var request = new VotoRequest();
        request.setVoto(SIM);
        request.setIdAssociado(joao.getId());

        var statusEsperado = UNPROCESSABLE_ENTITY.value();
        var mensagemEsperada = "Associado já votou nesta pauta";

        mockMvc.perform(post("/pautas/" + pautaId + "/voto")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(request)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.status").value(statusEsperado))
                .andExpect(jsonPath("$.message").value(mensagemEsperada))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    @DisplayName("Deve lançar erro quando incluir associado existente")
    void deveLancarErroQuandoIncluirAssociadoExistente() throws Exception {

        var joao = incluirAssociado("44053013011", "João da Silva");

        var associadoRequest = new AssociadoRequest();
        associadoRequest.setCpf(joao.getCpf());
        associadoRequest.setNome(joao.getNome());

        var statusEsperado = UNPROCESSABLE_ENTITY.value();
        var mensagemEsperada = "Já existe um associado com este CPF";

        mockMvc.perform(post("/associados")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(associadoRequest)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.status").value(statusEsperado))
                .andExpect(jsonPath("$.message").value(mensagemEsperada))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    private Long incluirPauta(String titulo, String descricao) throws Exception {
        var pautaRequest = new PautaRequest();
        pautaRequest.setTitulo(titulo);
        pautaRequest.setDescricao(descricao);

        var pautaResponse = toObject(mockMvc.perform(post("/pautas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(pautaRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andReturn(), PautaResponse.class);

        return pautaResponse.getId();
    }

    private void incluirSessao(Long pautaId) throws Exception {
        incluirSessao(pautaId, new SessaoRequest());
    }

    private void incluirSessao(Long pautaId, int duracao) throws Exception {
        var request = new SessaoRequest();
        request.setDuracao(duracao);
        incluirSessao(pautaId, request);
    }

    private void incluirSessao(Long pautaId, SessaoRequest request) throws Exception {
        mockMvc.perform(post("/pautas/" + pautaId + "/sessao")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.dataInicial").exists())
                .andExpect(jsonPath("$.dataFinal").exists());
    }

    private AssociadoResponse incluirAssociado(String cpf, String nome) throws Exception {

        var associadoRequest = new AssociadoRequest();
        associadoRequest.setCpf(cpf);
        associadoRequest.setNome(nome);

        return toObject(mockMvc.perform(post("/associados")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(associadoRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andReturn(), AssociadoResponse.class);
    }

    private void votar(Long pautaId, Long associadoId, VotoComputado votoComputado) throws Exception {

        var votoRequest = new VotoRequest();
        votoRequest.setVoto(votoComputado);
        votoRequest.setIdAssociado(associadoId);

        mockMvc.perform(post("/pautas/" + pautaId + "/voto")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(votoRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists());
    }

    private ResultActions buscarResultadoDaVotacao(Long pautaId) throws Exception {
        return mockMvc.perform(get("/pautas/" + pautaId + "/resultado")
                .contentType(MediaType.APPLICATION_JSON));
    }

    private void executaAposDuracaoPadrao(Runnable runnable) throws Exception {
        executaAposDuracao(runnable, duracaoPadrao);
    }
}
