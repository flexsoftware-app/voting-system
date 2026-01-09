package pl.kkaczynski.voter;

public interface VoterUpdate {
    void block(Long id);
    void unblock(Long id);
}
