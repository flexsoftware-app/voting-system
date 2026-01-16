package pl.kkaczynski.election;

import io.micrometer.common.util.StringUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ElectionOptionMapper {
    public static ElectionOptionEntity toEntity(ElectionOption electionOption, ElectionEntity election) {
        ElectionOptionEntity electionOptionEntity;
        if (StringUtils.isBlank(electionOption.description())) {
            electionOptionEntity = new ElectionOptionEntity(electionOption.name(), election);
        } else {
            electionOptionEntity = new ElectionOptionEntity(electionOption.name(), electionOption.description(), election);
        }

        return electionOptionEntity;
    }

    public static ElectionOption toDomain(ElectionOptionEntity electionOptionEntity) {
        return new ElectionOption(electionOptionEntity.getId(), electionOptionEntity.getName(), electionOptionEntity.getDescription());
    }
}
