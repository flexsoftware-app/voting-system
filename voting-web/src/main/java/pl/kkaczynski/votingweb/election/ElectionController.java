package pl.kkaczynski.votingweb.election;


import org.springframework.http.HttpStatus;
import pl.kkaczynski.election.Election;
import pl.kkaczynski.election.ElectionCreate;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kkaczynski.election.ElectionGet;


@RestController
@RequestMapping("/api/elections")
@RequiredArgsConstructor
public class ElectionController {

    private final ElectionRequestMapper requestMapper;
    private final ElectionCreate electionCreate;
    private final ElectionGet electionGet;

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody @Valid ElectionCreateRequest request) {
        Long electionId = electionCreate.create(requestMapper.toCommand(request));

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header("Location", "/api/elections/" + electionId)
                .build();
    }

    @GetMapping("/{electionId}")
    public ElectionResponse getElection(@PathVariable Long electionId) {
        Election election = electionGet.getById(electionId);
        return requestMapper.toResponse(election);
    }
}