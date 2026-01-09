package pl.kkaczynski.votingweb.vote;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import pl.kkaczynski.vote.Vote;
import pl.kkaczynski.vote.VoteCreate;
import pl.kkaczynski.vote.VoteGet;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VoteController.class)
@Import(VoteRequestMapper.class)
class VoteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private VoteCreate voteCreate;

    @MockitoBean
    private VoteGet voteGet;


    @Test
    void shouldCreateVote() throws Exception {
        // given
        VoteCreateRequest request = new VoteCreateRequest(1L, List.of(1L, 2L));
        when(voteCreate.create(any())).thenReturn(100L);

        // when
        mockMvc.perform(post("/api/elections/1/votes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/elections/1/votes/100"));

        verify(voteCreate).create(any());
    }

    @Test
    void shouldReturnBadRequestWhenNoSelections() throws Exception {
        // given
        VoteCreateRequest request = new VoteCreateRequest(1L, List.of());

        // when
        mockMvc.perform(post("/api/elections/1/votes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldGetVote() throws Exception {
        // given
        Vote vote = new Vote(100L, 1L, 1L, List.of(1L, 2L));
        when(voteGet.getById(1L, 100L)).thenReturn(vote);

        // when
        mockMvc.perform(get("/api/elections/1/votes/100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.voteId").value(100L))
                .andExpect(jsonPath("$.electionId").value(1L))
                .andExpect(jsonPath("$.voterId").value(1L))
                .andExpect(jsonPath("$.selectionsIds").isArray());

        verify(voteGet).getById(1L, 100L);
    }

    @Test
    void shouldReturnNotFoundWhenVoteDoesNotExist() throws Exception {
        // given
        when(voteGet.getById(1L, 999L)).thenThrow(new pl.kkaczynski.vote.VoteNotFoundException("Vote not found"));

        // when
        mockMvc.perform(get("/api/elections/1/votes/999"))
                .andExpect(status().isNotFound());

        verify(voteGet).getById(1L, 999L);
    }
}
