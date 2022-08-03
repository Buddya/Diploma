package ru.netology.diploma.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.netology.diploma.dto.FileListResponse;
import ru.netology.diploma.entities.File;
import ru.netology.diploma.repository.FileRepository;


import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

public class FileServiceTest {
    public static final String FILE = "file";
    public static final String FAKE_FILE = "fakeFile";

    private final File FileEntity = new File(FILE, new byte[]{1, 1, 1});

    private final FileRepository fileRepository = createFileRepositoryMock();
    private final FileService fileService = new FileService(fileRepository);

    private FileRepository createFileRepositoryMock() {
        final FileRepository fileRepository = Mockito.mock(FileRepository.class);

        when(fileRepository.findById(FILE)).thenReturn(Optional.of(FileEntity));
        when(fileRepository.findById(FAKE_FILE)).thenReturn(Optional.empty());

        when(fileRepository.existsById(FILE)).thenReturn(true);
        when(fileRepository.existsById(FAKE_FILE)).thenReturn(false);

        when(fileRepository.getFiles(1)).thenReturn(List.of(FileEntity));

        return fileRepository;
    }

    @Test
    void getFile() {
        final byte[] expectedFile = new byte[]{1, 1, 1};
        final byte[] file = fileService.getFile(FILE);
        Assertions.assertArrayEquals(expectedFile, file);
    }

    @Test
    void getFileFailure() {
        Assertions.assertThrows(RuntimeException.class, () -> fileService.getFile(FAKE_FILE));
    }

    @Test
    void deleteFile() {
        Assertions.assertDoesNotThrow(() -> fileService.deleteFile(FILE));
    }

    @Test
    void deleteFileFailure() {
        Assertions.assertThrows(RuntimeException.class, () -> fileService.deleteFile(FAKE_FILE));
    }

    @Test
    void editFileName() {
        Assertions.assertDoesNotThrow(() -> fileService.editFile(FILE, FAKE_FILE));
    }

    @Test
    void editFileNameFailure() {
        Assertions.assertThrows(RuntimeException.class, () -> fileService.editFile(FAKE_FILE, FILE));
    }

    @Test
    void getFileList() {
        final List<FileListResponse> expectedFileList = List.of(new FileListResponse(FILE, 3));
        final List<FileListResponse> fileList = fileService.getFileList(1);
        Assertions.assertEquals(expectedFileList, fileList);
    }
}
