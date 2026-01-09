package pl.kkaczynski.votingweb.voter;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kkaczynski.voter.Voter;
import pl.kkaczynski.voter.VoterCreate;
import pl.kkaczynski.voter.VoterGet;
import pl.kkaczynski.voter.VoterUpdate;

@RestController
@RequestMapping("/api/voters")
@RequiredArgsConstructor
public class VoterController {

    private final VoterCreate voterCreate;
    private final VoterGet voterGet;
    private final VoterUpdate voterUpdate;
    private final VoterRequestMapper voterRequestMapper;

    @PostMapping
    public ResponseEntity<Void> createVoter(@RequestBody @Valid VoterCreateRequest request) {
        Long voterId = voterCreate.create(voterRequestMapper.toCommand(request));

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header("Location", "/api/voters/" + voterId)
                .build();
    }

    @GetMapping("/{voterId}")
    public VoterResponse getVoter(@PathVariable Long voterId) {
        Voter voter = voterGet.getById(voterId);
        return voterRequestMapper.toResponse(voter);
    }

    @PutMapping("/{voterId}/block")
    public ResponseEntity<Void> blockVoter(@PathVariable Long voterId) {
        voterUpdate.block(voterId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{voterId}/unblock")
    public ResponseEntity<Void> unblockVoter(@PathVariable Long voterId) {
        voterUpdate.unblock(voterId);
        return ResponseEntity.noContent().build();
    }
}