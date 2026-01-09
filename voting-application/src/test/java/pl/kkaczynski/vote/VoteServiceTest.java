package pl.kkaczynski.vote;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.kkaczynski.election.Election;
import pl.kkaczynski.election.ElectionOption;
import pl.kkaczynski.election.ElectionStatusException;
import pl.kkaczynski.election.ElectionUtilService;
import pl.kkaczynski.election.SelectionType;
import pl.kkaczynski.voter.VoterStatus;
import pl.kkaczynski.voter.VoterStatusException;
import pl.kkaczynski.voter.VoterUtilService;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VoteServiceTest {

    @Mock
    private VotePort votePort;

    @Mock
    private ElectionUtilService electionService;

    @Mock
    private VoterUtilService voterService;

    @InjectMocks
    private VoteService voteService;

    private VoteCreateCommand createCommand;
    private Election testElection;
    private Instant now;
    private Instant future;

    @BeforeEach
    void setUp() {
        now = Instant.now();
        future = now.plusSeconds(3600);

        createCommand = new VoteCreateCommand(1L, 1L, List.of(1L, 2L));

        ElectionOption option1 = new ElectionOption(1L, "Option 1", "Description 1");
        ElectionOption option2 = new ElectionOption(2L, "Option 2", "Description 2");

        testElection = new Election(
                1L,
                "Test Election",
                SelectionType.MULTI_VOTE,
                now.minusSeconds(3600),
                future,
                List.of(option1, option2)
        );
    }

    @Test
    void shouldCreateVote() {
        // given
        when(voterService.getStatus(1L)).thenReturn(VoterStatus.AVAILABLE);
        when(electionService.isActive(1L)).thenReturn(true);
        when(votePort.existsByElectionIdAndVoterId(1L, 1L)).thenReturn(false);
        when(electionService.getById(1L)).thenReturn(testElection);
        when(votePort.save(any(Vote.class))).thenReturn(100L);

        // when
        Long voteId = voteService.create(createCommand);

        // then
        assertThat(voteId).isEqualTo(100L);
        verify(voterService).getStatus(1L);
        verify(electionService).isActive(1L);
        verify(votePort).existsByElectionIdAndVoterId(1L, 1L);
        verify(electionService).getById(1L);
        verify(votePort).save(any(Vote.class));
    }

    @Test
    void shouldThrowExceptionWhenVoterIsBlocked() {
        // given
        when(voterService.getStatus(1L)).thenReturn(VoterStatus.BLOCK);

        // when
        assertThatThrownBy(() -> voteService.create(createCommand))
                .isInstanceOf(VoterStatusException.class)
                .hasMessageContaining("status=BLOCK");
        verify(votePort, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenElectionIsNotActive() {
        // given
        when(voterService.getStatus(1L)).thenReturn(VoterStatus.AVAILABLE);
        when(electionService.isActive(1L)).thenReturn(false);

        // when
        assertThatThrownBy(() -> voteService.create(createCommand))
                .isInstanceOf(ElectionStatusException.class)
                .hasMessageContaining("not active");
        verify(votePort, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenVoterAlreadyVoted() {
        // given
        when(voterService.getStatus(1L)).thenReturn(VoterStatus.AVAILABLE);
        when(electionService.isActive(1L)).thenReturn(true);
        when(votePort.existsByElectionIdAndVoterId(1L, 1L)).thenReturn(true);

        // when
        assertThatThrownBy(() -> voteService.create(createCommand))
                .isInstanceOf(DuplicateVoteException.class)
                .hasMessageContaining("already voted");
        verify(votePort, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenNoOptionsSelected() {
        // given
        VoteCreateCommand commandWithNoOptions = new VoteCreateCommand(1L, 1L, List.of());
        when(voterService.getStatus(1L)).thenReturn(VoterStatus.AVAILABLE);
        when(electionService.isActive(1L)).thenReturn(true);
        when(votePort.existsByElectionIdAndVoterId(1L, 1L)).thenReturn(false);
        when(electionService.getById(1L)).thenReturn(testElection);

        // then
        assertThatThrownBy(() -> voteService.create(commandWithNoOptions))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("At least one option");
    }

    @Test
    void shouldThrowExceptionWhenOptionDoesNotBelongToElection() {
        // given
        VoteCreateCommand commandWithInvalidOption = new VoteCreateCommand(1L, 1L, List.of(999L));
        when(voterService.getStatus(1L)).thenReturn(VoterStatus.AVAILABLE);
        when(electionService.isActive(1L)).thenReturn(true);
        when(votePort.existsByElectionIdAndVoterId(1L, 1L)).thenReturn(false);
        when(electionService.getById(1L)).thenReturn(testElection);

        // then
        assertThatThrownBy(() -> voteService.create(commandWithInvalidOption))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("do not belong to this election");
    }

    @Test
    void shouldThrowExceptionWhenMultipleOptionsSelectedForSingleVote() {
        // given
        ElectionOption option1 = new ElectionOption(1L, "Option 1", "Desc");
        ElectionOption option2 = new ElectionOption(2L, "Option 2", "Desc");
        Election singleVoteElection = new Election(
                1L,
                "Single Vote Election",
                SelectionType.SINGLE_VOTE,
                now.minusSeconds(3600),
                future,
                List.of(option1, option2)
        );
        when(voterService.getStatus(1L)).thenReturn(VoterStatus.AVAILABLE);
        when(electionService.isActive(1L)).thenReturn(true);
        when(votePort.existsByElectionIdAndVoterId(1L, 1L)).thenReturn(false);
        when(electionService.getById(1L)).thenReturn(singleVoteElection);

        // then
        assertThatThrownBy(() -> voteService.create(createCommand))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Only one option can be selected");
    }

    @Test
    void shouldGetVoteById() {
        // given
        Vote testVote = new Vote(100L, 1L, 1L, List.of(1L, 2L));
        when(electionService.exist(1L)).thenReturn(true);
        when(votePort.findById(100L)).thenReturn(testVote);

        // when
        Vote result = voteService.getById(1L, 100L);

        // then
        assertThat(result).isNotNull();
        assertThat(result.voteId()).isEqualTo(100L);
        assertThat(result.electionId()).isEqualTo(1L);
        verify(electionService).exist(1L);
        verify(votePort).findById(100L);
    }

    @Test
    void shouldThrowExceptionWhenElectionDoesNotExist() {
        // given
        when(electionService.exist(999L)).thenReturn(false);

        // then
        assertThatThrownBy(() -> voteService.getById(999L, 100L))
                .isInstanceOf(ElectionStatusException.class)
                .hasMessageContaining("does not exist");
    }

    @Test
    void shouldThrowExceptionWhenVoteDoesNotBelongToElection() {
        // given
        Vote testVote = new Vote(100L, 2L, 1L, List.of(1L));
        when(electionService.exist(1L)).thenReturn(true);
        when(votePort.findById(100L)).thenReturn(testVote);

        // then
        assertThatThrownBy(() -> voteService.getById(1L, 100L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("does not belong to this election");
    }
}
