package pl.kkaczynski.voter;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

@Service
public class VoterService implements VoterUtilService{
    @Override // todo
    public VoterStatus getStatus(@NotNull Long voterId) {
        return null;
    }
}
