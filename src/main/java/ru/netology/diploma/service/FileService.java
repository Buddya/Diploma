package ru.netology.diploma.service;

import org.springframework.stereotype.Service;
import ru.netology.diploma.dto.FileListResponse;
import ru.netology.diploma.entities.File;
import ru.netology.diploma.repository.FileRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FileService {
    private final FileRepository fileRepository;

    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    //добавление файла
    public void addFile(String fileName, byte[] fileContent) {
        fileRepository.save(new File(fileName, fileContent));
    }

    //удаление файла
    public void deleteFile(String fileName) {
        if (!fileRepository.existsById(fileName)) {
            throw new RuntimeException("File " + fileName + " not found");
        }
        fileRepository.deleteById(fileName);
    }

    //получение файла
    public byte[] getFile(String fileName) {
        final File file = getFileByName(fileName);
        return file.getFileContent();
    }

    //изменение имени файла
    public void editFile(String fileName, String newFileName) {
        final File file = getFileByName(fileName);
        final File newFile = new File(newFileName, file.getFileContent());
        fileRepository.save(newFile);
        fileRepository.delete(file);
    }

    //получение списка файлов
    public List<FileListResponse> getFileList(int limit) {
        final List<File> fileList = fileRepository.getFiles(limit);
        return fileList.stream()
                .map(file -> new FileListResponse(file.getFileName(), file.getFileContent().length))
                .collect(Collectors.toList());
    }

    public File getFileByName(String fileName) {
        return fileRepository.findById(fileName).orElseThrow(() -> new RuntimeException("File not found"));
    }
}
