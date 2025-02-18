package com.travel.portal.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@Schema(description = "User Model")
public class UserModel {
    @Schema(description = "Unique identifier of the user", accessMode = Schema.AccessMode.READ_ONLY)
    int userId;

    @Schema(
            description = "Name of the user",
            example = "John Doe",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "Username is required")
    String userName;

    @Schema(
            description = "Email address of the user",
            example = "john.doe@example.com",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    String userEmail;

    @Schema(
            description = "User's band/level in the organization",
            example = "L2",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "User band is required")
    String userBand;

    @Override
    public String toString() {
        return "UserModel{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", userBand='" + userBand + '\'' +
                '}';
    }
}
