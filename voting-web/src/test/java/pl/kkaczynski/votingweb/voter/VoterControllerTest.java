package pl.kkaczynski.votingweb.voter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import pl.kkaczynski.voter.Voter;
import pl.kkaczynski.voter.VoterCreate;
import pl.kkaczynski.voter.VoterGet;
import pl.kkaczynski.voter.VoterUpdate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VoterController.class)
@Import(VoterRequestMapper.class)
class VoterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private VoterCreate voterCreate;

    @MockitoBean
    private VoterGet voterGet;

    @MockitoBean
    private VoterUpdate voterUpdate;

    @Test
    void shouldCreateVoter() throws Exception {
        // given
        VoterCreateRequest request = new VoterCreateRequest(
                "test@example.com",
                "Jan",
                "Kowalski"
        );

        when(voterCreate.create(any())).thenReturn(1L);

        // when
        mockMvc.perform(post("/api/voters")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/voters/1"));
        // then
        verify(voterCreate).create(any());
    }

    @Test
    void shouldReturnBadRequestWhenEmailInvalid() throws Exception {
        // given
        VoterCreateRequest request = new VoterCreateRequest(
                "invalid-email",
                "Jan",
                "Kowalski"
        );

        // when
        mockMvc.perform(post("/api/voters")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenFieldsAreBlank() throws Exception {
        // given
        VoterCreateRequest request = new VoterCreateRequest(
                "",
                "",
                ""
        );

        // then
        mockMvc.perform(post("/api/voters")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldGetVoter() throws Exception {
        // given
        Voter voter = new Voter(1L, "test@example.com", "Jan", "Kowalski", false);
        when(voterGet.getById(1L)).thenReturn(voter);

        // then
        mockMvc.perform(get("/api/voters/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.firstName").value("Jan"))
                .andExpect(jsonPath("$.lastName").value("Kowalski"))
                .andExpect(jsonPath("$.blocked").value(false));

        verify(voterGet).getById(1L);
    }

    @Test
    void shouldReturnNotFoundWhenVoterDoesNotExist() throws Exception {
        // given
        when(voterGet.getById(999L)).thenThrow(new pl.kkaczynski.voter.VoterNotFoundException("Voter not found"));

        // when
        mockMvc.perform(get("/api/voters/999"))
                .andExpect(status().isNotFound());
        // then
        verify(voterGet).getById(999L);
    }

    @Test
    void shouldBlockVoter() throws Exception {
        // given
        doNothing().when(voterUpdate).block(1L);

        // when
        mockMvc.perform(put("/api/voters/1/block"))
                .andExpect(status().isNoContent());
        // then
        verify(voterUpdate).block(1L);
    }

    @Test
    void shouldUnblockVoter() throws Exception {
        // given
        doNothing().when(voterUpdate).unblock(1L);

        // when
        mockMvc.perform(put("/api/voters/1/unblock"))
                .andExpect(status().isNoContent());
        // then
        verify(voterUpdate).unblock(1L);
    }
}
