package pl.kkaczynski.election;

import org.jetbrains.annotations.NotNull;

public interface ElectionUtilService {
    boolean isActive(@NotNull Long electionId);

    boolean exist(@NotNull Long electionId);
}
