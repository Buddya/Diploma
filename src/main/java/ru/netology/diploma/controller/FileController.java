package ru.netology.diploma.controller;

import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.netology.diploma.dto.FileName;
import ru.netology.diploma.service.AuthorizationService;
import ru.netology.diploma.service.FileService;


import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.IOException;

@RestController
@RequestMapping("/file")
@Validated
public class FileController {
    private final FileService fileService;
    private final AuthorizationService authorizationService;

    public FileController(FileService fileService, AuthorizationService authorizationService) {
        this.fileService = fileService;
        this.authorizationService = authorizationService;
    }

    @PostMapping
    public void uploadFile(@RequestHeader("auth-token") @NotBlank String authToken, @NotBlank String fileName, @NotNull @RequestBody MultipartFile file) throws IOException {
        authorizationService.tokenValidation(authToken);
        fileService.addFile(fileName, file.getBytes());
    }

    @DeleteMapping
    public void deleteFile(@RequestHeader("auth-token") @NotBlank String authToken, @NotBlank String fileName) {
        authorizationService.tokenValidation(authToken);
        fileService.deleteFile(fileName);
    }

    @GetMapping(produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public byte[] getFile(@RequestHeader("auth-token") @NotBlank String authToken, @NotBlank String fileName) {
        authorizationService.tokenValidation(authToken);
        return fileService.getFile(fileName);
    }

    @PutMapping
    public void editFile(@RequestHeader("auth-token") @NotBlank String authToken, @NotBlank String filename, @Valid @RequestBody FileName fileName) {
        authorizationService.tokenValidation(authToken);
        fileService.editFile(filename, fileName.getName());
    }
}
