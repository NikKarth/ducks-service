package edu.iu.habahram.ducksservice.repository;

import edu.iu.habahram.ducksservice.model.DuckData;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

@Component
public class DucksRepository {

    private static final String IMAGES_FOLDER_PATH = "ducks/images/";
    private static final String AUDIO_FOLDER_PATH = "ducks/audio/";
    private static final String NEW_LINE = System.lineSeparator();
    private static final String DATABASE_NAME = "ducks/db.txt";

    public DucksRepository() {
        File ducksImagesDirectory = new File("ducks/images");
        if (!ducksImagesDirectory.exists()) {
            ducksImagesDirectory.mkdirs();
        }
        File ducksAudioDirectory = new File("ducks/audio");
        if (!ducksAudioDirectory.exists()) {
            ducksAudioDirectory.mkdirs();
        }
    }

    private static void appendToFile(Path path, String content) throws IOException {
        Files.write(path,
                content.getBytes(StandardCharsets.UTF_8),
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND);
    }

    public int add(DuckData duckData) throws IOException {
        List<DuckData> ducks = findAll();

        int maxId = 0;
        for (DuckData duck : ducks) {
            if (duck.id() > maxId) {
                maxId = duck.id();
            }
        }

        int id = maxId + 1;
        Path path = Paths.get(DATABASE_NAME);
        String data = duckData.toLine(id);
        appendToFile(path, data + NEW_LINE);
        return id;
    }

    public List<DuckData> findAll() throws IOException {
        List<DuckData> result = new ArrayList<>();

        Path path = Paths.get(DATABASE_NAME);
        if (!Files.exists(path)) {
            // If db.txt doesn't exist yet, return empty list instead of crashing
            return result;
        }

        List<String> data = Files.readAllLines(path);
        for (String line : data) {
            if (!line.trim().isEmpty()) {
                DuckData d = DuckData.fromLine(line);
                result.add(d);
            }
        }
        return result;
    }

    public DuckData find(int id) throws IOException {
        List<DuckData> ducks = findAll();
        for (DuckData duck : ducks) {
            if (duck.id() == id) {
                return duck;
            }
        }
        return null;
    }

    public List<DuckData> search(String type) throws IOException {
        List<DuckData> ducks = findAll();
        List<DuckData> result = new ArrayList<>();

        for (DuckData duck : ducks) {
            if (type != null && !duck.type().equalsIgnoreCase(type)) {
                continue;
            }
            result.add(duck);
        }
        return result;
    }

    public boolean updateImage(int id, MultipartFile file) throws IOException {
        String fileExtension = ".png";
        Path path = Paths.get(IMAGES_FOLDER_PATH + id + fileExtension);
        file.transferTo(path);
        return true;
    }

    public boolean updateAudio(int id, MultipartFile file) throws IOException {
        String fileExtension = ".mp3";
        Path path = Paths.get(AUDIO_FOLDER_PATH + id + fileExtension);
        file.transferTo(path);
        return true;
    }

    public byte[] getImage(int id) throws IOException {
        Path path = Paths.get(IMAGES_FOLDER_PATH + id + ".png");
        return Files.readAllBytes(path);
    }

    public byte[] getAudio(int id) throws IOException {
        Path path = Paths.get(AUDIO_FOLDER_PATH + id + ".mp3");
        return Files.readAllBytes(path);
    }
}