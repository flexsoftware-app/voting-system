package pl.kkaczynski.election;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum SelectionType {

    SINGLE_VOTE("Single choice", "Voter can select only one option"), MULTI_VOTE("Multiple choice","Voter can select few option");

    private final String displayName;
    private final String description;
}
