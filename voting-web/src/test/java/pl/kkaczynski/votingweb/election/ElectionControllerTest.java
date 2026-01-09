package pl.kkaczynski.votingweb.election;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import pl.kkaczynski.election.*;

import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ElectionController.class)
@Import(ElectionRequestMapper.class)
class ElectionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ElectionCreate electionCreate;

    @MockitoBean
    private ElectionGet electionGet;

    @Test
    void shouldCreateElection() throws Exception {
        // given
        ElectionCreateRequest request = new ElectionCreateRequest(
                "Test Election",
                SelectionType.MULTI_VOTE,
                Instant.now(),
                Instant.now().plusSeconds(3600),
                List.of(new CreateElectionOptionRequest("Option 1", "Description 1"))
        );

        when(electionCreate.create(any())).thenReturn(1L);

        // then
        mockMvc.perform(post("/api/elections")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/elections/1"));

        verify(electionCreate).create(any());
    }

    @Test
    void shouldGetElection() throws Exception {
        // given
        Election election = new Election(
                1L,
                "Test Election",
                SelectionType.MULTI_VOTE,
                Instant.now(),
                Instant.now().plusSeconds(3600),
                List.of(new ElectionOption(1L, "Option 1", "Description 1"))
        );

        when(electionGet.getById(1L)).thenReturn(election);

        // then
        mockMvc.perform(get("/api/elections/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Test Election"));

        verify(electionGet).getById(1L);
    }

    @Test
    void shouldReturnNotFoundWhenElectionDoesNotExist() throws Exception {
        // given
        when(electionGet.getById(999L)).thenThrow(new pl.kkaczynski.election.ElectionNotFoundException("Election not found"));

        // then
        mockMvc.perform(get("/api/elections/999"))
                .andExpect(status().isNotFound());
        verify(electionGet).getById(999L);
    }
}
