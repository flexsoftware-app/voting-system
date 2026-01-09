package pl.kkaczynski.votingweb;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.kkaczynski.election.ElectionNotFoundException;
import pl.kkaczynski.election.ElectionStatusException;
import pl.kkaczynski.vote.DuplicateVoteException;
import pl.kkaczynski.vote.VoteNotFoundException;
import pl.kkaczynski.voter.VoterNotFoundException;
import pl.kkaczynski.voter.VoterStatusException;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({ElectionNotFoundException.class, VoteNotFoundException.class, VoterNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFound(RuntimeException ex) {
        return new ErrorResponse(ex.getMessage());
    }

    @ExceptionHandler({
            VoterStatusException.class,
            ElectionStatusException.class,
            DuplicateVoteException.class,
            IllegalArgumentException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequest(RuntimeException ex) {
        return new ErrorResponse(ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidation(MethodArgumentNotValidException ex) {
        String errors = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return new ErrorResponse("Validation failed: " + errors);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleConflict(DataIntegrityViolationException ex) {
        return new ErrorResponse("Data integrity violation: " + ex.getMessage());
    }
}