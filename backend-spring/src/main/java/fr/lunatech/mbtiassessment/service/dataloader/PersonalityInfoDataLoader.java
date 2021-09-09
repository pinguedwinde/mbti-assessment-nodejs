package fr.lunatech.mbtiassessment.service.dataloader;

import fr.lunatech.mbtiassessment.dto.PersonalityInfoDTO;
import fr.lunatech.mbtiassessment.service.PersonalityInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

@Component
@Profile({"!test"})
public class PersonalityInfoDataLoader implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(PersonalityInfoDataLoader.class);
    private final PersonalityInfoService service;

    public PersonalityInfoDataLoader(PersonalityInfoService service) {
        this.service = service;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Function<String, PersonalityInfoDTO> lineIntoInfo = line -> {
            String personalityType = line.split(";")[0];
            String profile = line.split(";")[1];
            String group = line.split(";")[2];
            String description = line.split(";")[3];
            return new PersonalityInfoDTO(personalityType, profile, group, description);
        };
        Path filePath = Path.of(Objects.requireNonNull(this.getClass().getClassLoader().getResource("files/personality-infos.csv")).getPath());

        if (Objects.requireNonNullElse(this.service.count().block(), 0L) == 0L) {
            Supplier<Stream<String>> lines = () -> {
                try {
                    return Files.lines(filePath, StandardCharsets.UTF_8);
                } catch (IOException e) {
                    log.error("Cannot open or read file " + filePath + " while loading PersonalityInfos data", e);
                    return Stream.empty();
                }
            };
            this.service
                    .addPersonalityInfos(
                            Flux.fromStream(
                                    lines.get().skip(1).filter(l -> !l.trim().isEmpty()).map(lineIntoInfo)
                            )
                    )
                    .doOnComplete(() -> log.info("PersonalityInfoRepository contains now {} entries.", this.service.count().block()))
                    .subscribe(personalityInfoDTO -> log.info("New PersonalityInfo loaded: {}", personalityInfoDTO));

        }
    }
}
