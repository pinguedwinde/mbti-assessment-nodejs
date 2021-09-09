package fr.lunatech.mbtiassessment.service.dataloader;

import fr.lunatech.mbtiassessment.dto.PersonageDTO;
import fr.lunatech.mbtiassessment.service.PersonageService;
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
public class PersonageDataLoader implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(PersonalityInfoDataLoader.class);
    private final PersonageService service;

    public PersonageDataLoader(PersonageService service) {
        this.service = service;
    }

    @Override
    public void run(ApplicationArguments args) {


        Function<String, PersonageDTO> lineIntoInfo = line -> {
            String name = line.split(";")[0];
            String about = line.split(";")[1];
            String personalityType = line.split(";")[2];
            String universe = line.split(";")[3];
            return new PersonageDTO(name, about, personalityType, universe);

        };

        Path filePath = Path.of(Objects.requireNonNull(this.getClass().getClassLoader().getResource("files/personages.csv")).getPath());

        if (Objects.requireNonNullElse(this.service.count().block(), 0L) == 0L) {
            Supplier<Stream<String>> lines = () -> {
                try {
                    return Files.lines(filePath, StandardCharsets.UTF_8);
                } catch (IOException e) {
                    log.error("Cannot open or read file " + filePath + " while loading Personages data", e);
                    return Stream.empty();
                }
            };
            this.service
                    .addPersonages(
                            Flux.fromStream(
                                    lines.get().skip(1).filter(l -> !l.trim().isEmpty()).map(lineIntoInfo)
                            )
                    )
                    .doOnComplete(() -> log.info("PersonageRepository contains now {} entries.", this.service.count().block()))
                    .subscribe(PersonageDTO -> log.info("New Personage loaded: {}", PersonageDTO));
        }
    }
}
