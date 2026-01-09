package pl.kkaczynski.election;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ElectionServiceTest {

    @Mock
    private ElectionPort electionPort;

    @Mock
    private ElectionMapper mapper;

    @InjectMocks
    private ElectionService electionService;

    private ElectionCreateCommand createCommand;
    private Election testElection;
    private Instant now;
    private Instant future;

    @BeforeEach
    void setUp() {
        now = Instant.now();
        future = now.plusSeconds(3600);

        createCommand = new ElectionCreateCommand(
                "Test Election",
                SelectionType.MULTI_VOTE,
                now,
                future,
                List.of(new ElectionOptionCreateCommand("Option 1", "Description 1"))
        );

        testElection = new Election(
                1L,
                "Test Election",
                SelectionType.MULTI_VOTE,
                now,
                future,
                List.of(new ElectionOption(1L, "Option 1", "Description 1"))
        );
    }

    @Test
    void shouldCreateElection() {
        // given
        when(mapper.toDomain(createCommand)).thenReturn(testElection);
        when(electionPort.save(testElection)).thenReturn(1L);

        // when
        Long electionId = electionService.create(createCommand);

        // then
        assertThat(electionId).isEqualTo(1L);
        verify(mapper).toDomain(createCommand);
        verify(electionPort).save(testElection);
    }

    @Test
    void shouldGetElectionById() {
        // given
        when(electionPort.findById(1L)).thenReturn(Optional.of(testElection));

        // when
        Election result = electionService.getById(1L);

        // then
        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.name()).isEqualTo("Test Election");
        verify(electionPort).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenElectionNotFound() {
        // given
        when(electionPort.findById(999L)).thenReturn(Optional.empty());

        // when
        assertThatThrownBy(() -> electionService.getById(999L))
                .isInstanceOf(ElectionNotFoundException.class)
                .hasMessageContaining("not found");
    }

    @Test
    void shouldReturnTrueWhenElectionIsActive() {
        // given
        Election activeElection = new Election(
                1L,
                "Active Election",
                SelectionType.MULTI_VOTE,
                now.minusSeconds(3600),
                future,
                List.of()
        );
        when(electionPort.findById(1L)).thenReturn(Optional.of(activeElection));

        // when
        boolean isActive = electionService.isActive(1L);

        // then
        assertThat(isActive).isTrue();
    }

    @Test
    void shouldReturnFalseWhenElectionIsNotStarted() {
        // given
        Election futureElection = new Election(
                1L,
                "Future Election",
                SelectionType.MULTI_VOTE,
                future,
                future.plusSeconds(3600),
                List.of()
        );
        when(electionPort.findById(1L)).thenReturn(Optional.of(futureElection));

        // when
        boolean isActive = electionService.isActive(1L);

        // then
        assertThat(isActive).isFalse();
    }

    @Test
    void shouldReturnFalseWhenElectionIsEnded() {
        // given
        Election endedElection = new Election(
                1L,
                "Ended Election",
                SelectionType.MULTI_VOTE,
                now.minusSeconds(7200),
                now.minusSeconds(3600),
                List.of()
        );
        when(electionPort.findById(1L)).thenReturn(Optional.of(endedElection));

        // when
        boolean isActive = electionService.isActive(1L);

        // then
        assertThat(isActive).isFalse();
    }

    @Test
    void shouldReturnTrueWhenElectionExists() {
        // given
        when(electionPort.exist(1L)).thenReturn(true);

        // when
        boolean exists = electionService.exist(1L);

        // then
        assertThat(exists).isTrue();
        verify(electionPort).exist(1L);
    }

    @Test
    void shouldReturnFalseWhenElectionDoesNotExist() {
        // given
        when(electionPort.exist(999L)).thenReturn(false);

        // when
        boolean exists = electionService.exist(999L);

        // then
        assertThat(exists).isFalse();
        verify(electionPort).exist(999L);
    }
}
