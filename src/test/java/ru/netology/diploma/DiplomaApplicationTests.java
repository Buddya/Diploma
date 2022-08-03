package ru.netology.diploma;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import ru.netology.diploma.dto.FileName;
import ru.netology.diploma.dto.RequestLogin;
import ru.netology.diploma.dto.ResponseLogin;
import ru.netology.diploma.entities.File;
import ru.netology.diploma.entities.Token;
import ru.netology.diploma.entities.User;
import ru.netology.diploma.repository.FileRepository;
import ru.netology.diploma.repository.TokenRepository;
import ru.netology.diploma.repository.UserRepository;

import java.util.Optional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:test.properties")
class DiplomaApplicationTests {
    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    UserRepository userRepository;

    @Autowired
    FileRepository fileRepository;

    @Autowired
    TokenRepository tokenRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        fileRepository.deleteAll();
        tokenRepository.deleteAll();
    }

    @Test
    void testLogin() {
        userRepository.save(new User("admin", "admin"));
        final HttpHeaders headers = new HttpHeaders();
        headers.set("Content-type", "application/json");

        final RequestLogin requestLogin = new RequestLogin("admin", "admin");
        final HttpEntity<RequestLogin> requestLoginHttpEntity = new HttpEntity<>(requestLogin, headers);

        final ResponseEntity<ResponseLogin> loginResponseEntity = this.restTemplate.postForEntity("/login", requestLoginHttpEntity, ResponseLogin.class);
        Assertions.assertNotNull(loginResponseEntity.getBody());
        Assertions.assertNotNull(loginResponseEntity.getBody().getAuthToken());
    }

    @Test
    public void testLogout() {
        final String authToken = "test123321";
        tokenRepository.save(new Token(authToken));

        final HttpHeaders headers = new HttpHeaders();
        headers.set("auth-token", authToken);
        final HttpEntity<Void> request = new HttpEntity<>(null, headers);

        this.restTemplate.postForEntity("/logout", request, Void.class);
        Assertions.assertFalse(tokenRepository.existsById("test123321"));
    }

    @Test
    public void testUploadFile() {
        final String authToken = "test123321";
        tokenRepository.save(new Token(authToken));

        final HttpHeaders headers = new HttpHeaders();
        headers.set("auth-token", authToken);

        final MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();
        parts.add("file", new ClassPathResource("testing.txt"));

        final HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(parts, headers);

        this.restTemplate.postForEntity("/file?filename=testing.txt", request, Void.class);

        final Optional<File> fileInRepository = fileRepository.findById("testing.txt");
        Assertions.assertTrue(fileInRepository.isPresent());
        Assertions.assertEquals(new File("testing.txt", new byte[]{49, 51, 50}), fileInRepository.get());
    }

    @Test
    public void testDeleteFile() {
        fileRepository.save(new File("testing.txt", new byte[]{49, 51, 50}));

        final String authToken = "test123321";
        tokenRepository.save(new Token(authToken));

        final HttpHeaders headers = new HttpHeaders();
        headers.set("auth-token", authToken);

        final HttpEntity<Void> request = new HttpEntity<>(null, headers);

        this.restTemplate.exchange("/file?filename=testing.txt", HttpMethod.DELETE, request, Void.class);

        Assertions.assertFalse(fileRepository.existsById("testing.txt"));
    }

    @Test
    public void testGetFile() {
        fileRepository.save(new File("testing.txt", new byte[]{49, 51, 50}));

        final String authToken = "test123321";
        tokenRepository.save(new Token(authToken));

        final HttpHeaders headers = new HttpHeaders();
        headers.set("auth-token", authToken);

        final HttpEntity<Void> request = new HttpEntity<>(null, headers);

        final ResponseEntity<byte[]> result = this.restTemplate.exchange("/file?filename=anotherFile2.txt", HttpMethod.GET, request, byte[].class);

        Assertions.assertNotNull(result.getBody());
        Assertions.assertArrayEquals(new byte[]{49, 51, 50}, result.getBody());
    }

    @Test
    public void testEditFile() {
        fileRepository.save(new File("testing.txt", new byte[]{49, 50, 51}));

        final String authToken = "test123321";
        tokenRepository.save(new Token(authToken));

        final HttpHeaders headers = new HttpHeaders();
        headers.set("auth-token", authToken);

        final HttpEntity<FileName> request = new HttpEntity<>(new FileName("editedFile.txt"), headers);

        this.restTemplate.exchange("/file?filename=testing.txt", HttpMethod.PUT, request, Void.class);

        Assertions.assertFalse(fileRepository.existsById("testing.txt"));
        final Optional<File> fileInRepository = fileRepository.findById("editedFile.txt");
        Assertions.assertTrue(fileInRepository.isPresent());
        Assertions.assertEquals(new File("testing.txt", new byte[]{49, 50, 51}), fileInRepository.get());
    }

    @Test
    public void testGetFileList() {
        fileRepository.save(new File("testing.txt", new byte[]{49, 51, 50}));

        final String authToken = "test123321";
        tokenRepository.save(new Token(authToken));

        final HttpHeaders headers = new HttpHeaders();
        headers.set("auth-token", authToken);

        final HttpEntity<Void> request = new HttpEntity<>(null, headers);

        final ResponseEntity<Object> result = this.restTemplate.exchange("/list?limit=10", HttpMethod.GET, request, Object.class);

        Assertions.assertNotNull(result.getBody());
        Assertions.assertEquals("[{filename=testing.txt, size=3}]", result.getBody().toString());
    }


}
