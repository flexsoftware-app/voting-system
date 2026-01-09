package pl.kkaczynski.election;

import pl.kkaczynski.election.Election;

public interface ElectionCreate {
    Long create(ElectionCreateCommand electionCreateCommand);

}
