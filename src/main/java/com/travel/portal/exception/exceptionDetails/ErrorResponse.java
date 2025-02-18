package com.travel.portal.exception.exceptionDetails;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Schema(
        name = "ErrorResponse",
        description = "Standard error response structure for API errors",
        example = """
            {
                "timestamp": "2024-01-20T10:15:30.123",
                "status": 404,
                "error": "Not Found",
                "message": "User not found with id: 1",
                "path": "/api/users"
            }
            """
)
public class ErrorResponse {
    @Schema(
            description = "Timestamp when the error occurred",
            example = "2024-01-20T10:15:30.123",
            type = "string",
            format = "date-time"
    )
    private LocalDateTime timestamp;

    @Schema(
            description = "HTTP status code",
            example = "404",
            type = "integer",
            minimum = "100",
            maximum = "599"
    )
    private int status;

    @Schema(
            description = "Error type or classification",
            example = "Not Found",
            type = "string"
    )
    private String error;

    @Schema(
            description = "Detailed error message",
            example = "User not found with id: 1",
            type = "string"
    )
    private String message;

    @Schema(
            description = "The path of the request that generated the error",
            example = "/api/users",
            type = "string"
    )
    private String path;
}
