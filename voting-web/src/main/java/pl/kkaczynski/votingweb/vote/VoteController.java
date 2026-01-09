package pl.kkaczynski.votingweb.vote;


import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kkaczynski.vote.Vote;
import pl.kkaczynski.vote.VoteCreate;
import pl.kkaczynski.vote.VoteGet;

@RestController
@RequestMapping("/api/elections/{electionId}/votes")
@AllArgsConstructor
public class VoteController {

    private final VoteCreate voteCreate;
    private final VoteGet voteGet;
    private final VoteRequestMapper voteRequestMapper;

    @PostMapping
    public ResponseEntity<Void> createVote(
            @PathVariable Long electionId,
            @RequestBody @Valid VoteCreateRequest request
    ) {
        Long voteId = voteCreate.create(voteRequestMapper.toCommand(electionId, request));

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header("Location", "/api/elections/" + electionId + "/votes/" + voteId)
                .build();
    }

    @GetMapping("/{voteId}")
    public VoteResponse getVote(
            @PathVariable Long electionId,
            @PathVariable Long voteId
    ) {
        Vote vote = voteGet.getById(electionId, voteId);
        return voteRequestMapper.toResponse(vote);
    }
}