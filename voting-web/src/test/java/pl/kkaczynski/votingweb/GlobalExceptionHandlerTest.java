package pl.kkaczynski.votingweb;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.kkaczynski.election.ElectionNotFoundException;
import pl.kkaczynski.election.ElectionStatusException;
import pl.kkaczynski.vote.DuplicateVoteException;
import pl.kkaczynski.vote.VoteNotFoundException;
import pl.kkaczynski.voter.VoterNotFoundException;
import pl.kkaczynski.voter.VoterStatusException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = {GlobalExceptionHandlerTest.TestController.class, GlobalExceptionHandler.class})
@Import(GlobalExceptionHandlerTest.TestController.class)
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @RestController
    static class TestController {
        @GetMapping("/test/election-not-found")
        void throwElectionNotFound() {
            throw new ElectionNotFoundException("Election not found");
        }

        @GetMapping("/test/vote-not-found")
        void throwVoteNotFound() {
            throw new VoteNotFoundException("Vote not found");
        }

        @GetMapping("/test/voter-not-found")
        void throwVoterNotFound() {
            throw new VoterNotFoundException("Voter not found");
        }

        @GetMapping("/test/voter-status")
        void throwVoterStatus() {
            throw new VoterStatusException("Voter has status=BLOCK");
        }

        @GetMapping("/test/election-status")
        void throwElectionStatus() {
            throw new ElectionStatusException("Election is not active");
        }

        @GetMapping("/test/duplicate-vote")
        void throwDuplicateVote() {
            throw new DuplicateVoteException("Voter has already voted");
        }

        @GetMapping("/test/illegal-argument")
        void throwIllegalArgument() {
            throw new IllegalArgumentException("Invalid argument");
        }

        @PostMapping("/test/data-integrity")
        void throwDataIntegrity(@RequestBody String body) {
            throw new DataIntegrityViolationException("Data integrity violation");
        }
    }

    @Test
    void shouldHandleElectionNotFound() throws Exception {
        mockMvc.perform(get("/test/election-not-found"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Election not found"));
    }

    @Test
    void shouldHandleVoteNotFound() throws Exception {
        mockMvc.perform(get("/test/vote-not-found"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Vote not found"));
    }

    @Test
    void shouldHandleVoterNotFound() throws Exception {
        mockMvc.perform(get("/test/voter-not-found"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Voter not found"));
    }

    @Test
    void shouldHandleVoterStatusException() throws Exception {
        mockMvc.perform(get("/test/voter-status"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Voter has status=BLOCK"));
    }

    @Test
    void shouldHandleElectionStatusException() throws Exception {
        mockMvc.perform(get("/test/election-status"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Election is not active"));
    }

    @Test
    void shouldHandleDuplicateVoteException() throws Exception {
        mockMvc.perform(get("/test/duplicate-vote"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Voter has already voted"));
    }

    @Test
    void shouldHandleIllegalArgumentException() throws Exception {
        mockMvc.perform(get("/test/illegal-argument"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid argument"));
    }

    @Test
    void shouldHandleDataIntegrityViolationException() throws Exception {
        mockMvc.perform(post("/test/data-integrity")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("Data integrity violation")));
    }
}
