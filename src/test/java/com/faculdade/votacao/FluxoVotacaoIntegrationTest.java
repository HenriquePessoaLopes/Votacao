package com.faculdade.votacao;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Teste de ponta a ponta: percorre a API HTTP real (sem mocks) do cadastro ate o voto.
 */
@SpringBootTest
@AutoConfigureMockMvc
class FluxoVotacaoIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private long criar(String url, String json) throws Exception {
        MvcResult result = mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isCreated())
                .andReturn();
        JsonNode body = objectMapper.readTree(result.getResponse().getContentAsString());
        return body.get("id").asLong();
    }

    @Test
    void fluxoCompletoDeVotacao() throws Exception {
        long associadoId = criar("/api/v1/associados", "{\"nome\":\"Maria Souza\",\"cpf\":\"11122233344\"}");
        long pautaId = criar("/api/v1/pautas", "{\"titulo\":\"Orcamento\",\"descricao\":\"Aprovar orcamento\"}");
        criar("/api/v1/sessoes", "{\"pautaId\":" + pautaId + ",\"minutos\":5}");

        String voto = "{\"pautaId\":" + pautaId + ",\"associadoId\":" + associadoId + ",\"opcao\":\"SIM\"}";

        // primeiro voto e aceito
        mockMvc.perform(post("/api/v1/votos").contentType(MediaType.APPLICATION_JSON).content(voto))
                .andExpect(status().isCreated());

        // segundo voto do mesmo associado na mesma pauta e rejeitado como conflito (409)
        mockMvc.perform(post("/api/v1/votos").contentType(MediaType.APPLICATION_JSON).content(voto))
                .andExpect(status().isConflict());

        // resultado so pode ser consultado depois que a sessao encerra
        mockMvc.perform(get("/api/v1/votos/resultado/" + pautaId))
                .andExpect(status().isBadRequest());
    }

    @Test
    void naoPermiteDuasSessoesParaAMesmaPauta() throws Exception {
        long pautaId = criar("/api/v1/pautas", "{\"titulo\":\"Pauta unica\",\"descricao\":\"x\"}");
        String sessao = "{\"pautaId\":" + pautaId + ",\"minutos\":5}";
        criar("/api/v1/sessoes", sessao);

        mockMvc.perform(post("/api/v1/sessoes").contentType(MediaType.APPLICATION_JSON).content(sessao))
                .andExpect(status().isConflict());
    }

    @Test
    void rejeitaCpfInvalidoNoCadastroDeAssociado() throws Exception {
        mockMvc.perform(post("/api/v1/associados").contentType(MediaType.APPLICATION_JSON)
                .content("{\"nome\":\"Fulano\",\"cpf\":\"123\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensagem").exists());
    }
}
