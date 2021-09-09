package fr.lunatech.mbtiassessment.model.util;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@Builder(toBuilder = true)
public class CustomHttpResponse {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss", timezone = "Europe/Paris")
    @Builder.Default()
    private Date timeStamp = new Date();
    private int statusCode;
    private HttpStatus httpStatus;
    private String reason;
    private List<String> messages;

    public static CustomHttpResponse renderCustomResponse(HttpStatus httpStatus, String message) {
        return CustomHttpResponse.builder()
                .httpStatus(httpStatus)
                .statusCode(httpStatus.value())
                .reason(httpStatus.getReasonPhrase())
                .messages(List.of(message))
                .build();
    }

    public static CustomHttpResponse renderCustomResponse(HttpStatus httpStatus, List<String> messages) {
        return CustomHttpResponse.builder()
                .httpStatus(httpStatus)
                .statusCode(httpStatus.value())
                .reason(httpStatus.getReasonPhrase())
                .messages(messages)
                .build();
    }
}
