package pl.kkaczynski.voter;

import org.jetbrains.annotations.NotNull;

public interface VoterUtilService {

    VoterStatus getStatus(@NotNull Long voterId);
}
