package pl.kkaczynski.election;

import io.micrometer.common.util.StringUtils;
import org.springframework.stereotype.Component;


@Component
public class ElectionOptionMapper {
    static ElectionOptionEntity toEntity(ElectionOption electionOption, ElectionEntity election) {
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
