package pl.kkaczynski.election;

import jakarta.validation.constraints.NotNull;

public interface ElectionGet {
    Election getById(@NotNull Long id);
}
