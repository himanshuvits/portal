package com.travel.portal.controller;

import com.travel.portal.constant.ApiConstants;
import com.travel.portal.entity.UserEntity;
import com.travel.portal.exception.exceptionDetails.UserNotFoundException;
import com.travel.portal.model.UserModel;
import com.travel.portal.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(ApiConstants.USER_CONTROLLER_MAPPING)
@Tag(name = "User Management", description = "APIs for managing users")
public class UserController {
    Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Operation(
            summary = "Get all users",
            description = "Retrieves a list of all users in the system",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved users",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = UserModel.class)
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            }
    )
    @GetMapping(value = ApiConstants.USER_CONTROLLER_MAPPING_FIND_ALL, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserModel>> getAllUsers() {
        logger.info("getUsers method called");
        List<UserEntity> lstUserDetails = userRepository.findAll();
        List<UserModel> lstUserModel = lstUserDetails.stream().map(this::convertEntityToModel).toList();
        return new ResponseEntity<>(lstUserModel, HttpStatus.OK);
    }

    @Operation(
            summary = "Get user by ID",
            description = "Retrieves a specific user based on the provided user ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User found successfully",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = UserModel.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content
            )
    })
    @GetMapping(value = ApiConstants.USER_CONTROLLER_MAPPING_FIND_USER_BY_ID, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserModel> getUserById(@Parameter(
            description = "ID of the user to retrieve",
            required = true,
            example = "1"
    ) @RequestParam int userId) {
        logger.info("Get User By Id method called");
        Optional<UserEntity> userDetails = userRepository.findById(userId);
        return userDetails.map(userEntity -> new ResponseEntity<>(convertEntityToModel(userEntity), HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(
            summary = "Add a new user",
            description = "Creates a new user in the system",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = UserModel.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "userName": "John Doe",
                                                "userEmail": "john.doe@example.com",
                                                "userBand": "L2"
                                            }
                                            """
                            )
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "User successfully created",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = UserModel.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid input data",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "User already exists with given email",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content(schema = @Schema(hidden = true))
                    )
            }
    )
    @PostMapping(value = ApiConstants.USER_CONTROLLER_MAPPING_ADD_USER, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserModel> addUser(@RequestBody UserModel userModel) {
        logger.info("addUser method called");
        UserEntity savedUser = userRepository.save(UserEntity.builder()
                .userName(userModel.getUserName())
                .userEmail(userModel.getUserEmail())
                .userBand(userModel.getUserBand())
                .build());
        return new ResponseEntity<>(convertEntityToModel(savedUser), HttpStatus.OK);
    }

    @Operation(
            summary = "Delete a user",
            description = "Deletes a user from the system by their ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User successfully deleted",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = UserModel.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @DeleteMapping(value = ApiConstants.USER_CONTROLLER_MAPPING_DELETE_USER, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserModel> deleteUser(@Parameter(
            description = "ID of the user to delete",
            required = true,
            example = "1"
    ) @RequestParam int userId) {
        logger.info("Delete User By Id method called");
        Optional<UserEntity> userDetails = userRepository.findById(userId);
        if (userDetails.isPresent()) {
            userRepository.deleteById(userId);
            return new ResponseEntity<>(convertEntityToModel(userDetails.get()), HttpStatus.OK);
        } else {
            throw new UserNotFoundException(userId);
        }
    }

    @Operation(
            summary = "Update an existing user",
            description = "Updates a user's information in the system"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User successfully updated",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = UserModel.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @PutMapping(value = ApiConstants.USER_CONTROLLER_MAPPING_UPDATE_USER, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserModel> updateUser(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "User details to update",
            required = true,
            content = @Content(
                    schema = @Schema(implementation = UserModel.class)
            )
    ) @RequestBody UserModel userModel) {
        logger.info("updateUser method called");
        UserEntity updatedUser = userRepository.save(UserEntity.builder()
                .userId(userModel.getUserId())
                .userName(userModel.getUserName())
                .userEmail(userModel.getUserEmail())
                .userBand(userModel.getUserBand())
                .build());
        return new ResponseEntity<>(convertEntityToModel(updatedUser), HttpStatus.OK);
    }


    private UserModel convertEntityToModel(@NotNull UserEntity userEntity) {
        return UserModel.builder()
                .userId(userEntity.getUserId())
                .userName(userEntity.getUserName())
                .userEmail(userEntity.getUserEmail())
                .userBand(userEntity.getUserBand())
                .build();
    }
}
