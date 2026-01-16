package pl.kkaczynski.election;

import java.util.List;

public interface ElectionGetResult {
    List<ElectionResult> electionResult(Long electionId);
}
