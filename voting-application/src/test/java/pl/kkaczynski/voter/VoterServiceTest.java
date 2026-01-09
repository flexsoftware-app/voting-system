package pl.kkaczynski.voter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VoterServiceTest {

    @Mock
    private VoterPort voterPort;

    @InjectMocks
    private VoterService voterService;

    private VoterCreateCommand createCommand;
    private Voter testVoter;

    @BeforeEach
    void setUp() {
        createCommand = new VoterCreateCommand(
                "test@example.com",
                "Jan",
                "Kowalski"
        );
        testVoter = new Voter(1L, "test@example.com", "Jan", "Kowalski", false);
    }

    @Test
    void shouldCreateVoter() {
        // given
        when(voterPort.existsByEmail("test@example.com")).thenReturn(false);
        when(voterPort.save(any(Voter.class))).thenReturn(1L);

        // when
        Long voterId = voterService.create(createCommand);

        // then
        assertThat(voterId).isEqualTo(1L);
        verify(voterPort).existsByEmail("test@example.com");
        verify(voterPort).save(any(Voter.class));
    }

    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists() {
        // given
        when(voterPort.existsByEmail("test@example.com")).thenReturn(true);

        // when
        assertThatThrownBy(() -> voterService.create(createCommand))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("already exists");
        //then
        verify(voterPort, never()).save(any());
    }

    @Test
    void shouldGetVoterById() {
        // given
        when(voterPort.findById(1L)).thenReturn(Optional.of(testVoter));

        // when
        Voter result = voterService.getById(1L);

        // then
        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.email()).isEqualTo("test@example.com");
        verify(voterPort).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenVoterNotFound() {
        // given
        when(voterPort.findById(999L)).thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> voterService.getById(999L))
                .isInstanceOf(VoterNotFoundException.class)
                .hasMessageContaining("not found");
    }

    @Test
    void shouldBlockVoter() {
        // given
        Voter unblockedVoter = new Voter(1L, "test@example.com", "Jan", "Kowalski", false);
        when(voterPort.findById(1L)).thenReturn(Optional.of(unblockedVoter));
        when(voterPort.save(any(Voter.class))).thenReturn(1L);

        // when
        voterService.block(1L);

        // then
        verify(voterPort).findById(1L);
        verify(voterPort).save(argThat(voter -> voter.blocked() == true));
    }

    @Test
    void shouldUnblockVoter() {
        // given
        Voter blockedVoter = new Voter(1L, "test@example.com", "Jan", "Kowalski", true);
        when(voterPort.findById(1L)).thenReturn(Optional.of(blockedVoter));
        when(voterPort.save(any(Voter.class))).thenReturn(1L);

        // when
        voterService.unblock(1L);

        // then
        verify(voterPort).findById(1L);
        verify(voterPort).save(argThat(voter -> voter.blocked() == false));
    }

    @Test
    void shouldReturnAvailableStatusForUnblockedVoter() {
        // given
        Voter unblockedVoter = new Voter(1L, "test@example.com", "Jan", "Kowalski", false);
        when(voterPort.findById(1L)).thenReturn(Optional.of(unblockedVoter));

        // when
        VoterStatus status = voterService.getStatus(1L);

        // then
        assertThat(status).isEqualTo(VoterStatus.AVAILABLE);
    }

    @Test
    void shouldReturnBlockStatusForBlockedVoter() {
        // given
        Voter blockedVoter = new Voter(1L, "test@example.com", "Jan", "Kowalski", true);
        when(voterPort.findById(1L)).thenReturn(Optional.of(blockedVoter));

        // when
        VoterStatus status = voterService.getStatus(1L);

        // then
        assertThat(status).isEqualTo(VoterStatus.BLOCK);
    }

    @Test
    void shouldReturnNotExistStatusWhenVoterNotFound() {
        // given
        when(voterPort.findById(999L)).thenReturn(Optional.empty());

        // when
        VoterStatus status = voterService.getStatus(999L);

        // then
        assertThat(status).isEqualTo(VoterStatus.NOT_EXIST);
    }
}
