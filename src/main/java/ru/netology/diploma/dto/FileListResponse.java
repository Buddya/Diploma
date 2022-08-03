package ru.netology.diploma.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FileListResponse {
    private final String fileName;
    private final int size;
}
