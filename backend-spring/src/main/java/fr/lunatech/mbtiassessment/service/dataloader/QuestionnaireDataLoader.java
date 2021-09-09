package fr.lunatech.mbtiassessment.service.dataloader;

import fr.lunatech.mbtiassessment.dto.QuestionDTO;
import fr.lunatech.mbtiassessment.service.QuestionService;
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
public class QuestionnaireDataLoader implements ApplicationRunner {
    private static final Logger log = LoggerFactory.getLogger(QuestionnaireDataLoader.class);
    private final QuestionService questionService;

    public QuestionnaireDataLoader(QuestionService questionService) {
        this.questionService = questionService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Function<String, QuestionDTO> lineIntoQuestion = line -> {
            String question = line.split(";")[0];
            String factorName = line.split(";")[1];
            return new QuestionDTO(question, factorName);
        };
        Path filePath = Path.of(Objects.requireNonNull(this.getClass().getClassLoader().getResource("files/questions.csv")).getPath());

        if (Objects.requireNonNullElse(this.questionService.count().block(), 0L) == 0L) {
            Supplier<Stream<String>> lines = () -> {
                try {
                    return Files.lines(filePath, StandardCharsets.UTF_8);
                } catch (IOException e) {
                    log.error("Cannot open or read file " + filePath + " while loading Questions data", e);
                    return Stream.empty();
                }
            };
            this.questionService
                    .addQuestions(
                            Flux.fromStream(
                                    lines.get().skip(1).filter(l -> !l.trim().isEmpty()).map(lineIntoQuestion)
                            )
                    )
                    .doOnComplete(() -> log.info("QuestionRepository contains now {} entries.", this.questionService.count().block()))
                    .subscribe(questionDTO -> log.info("New question  loaded: {}", questionDTO));
        }
    }
}
