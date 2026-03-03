package edu.iu.habahram.ducksservice.controllers;

import edu.iu.habahram.ducksservice.repository.DuckRepository;
import edu.iu.habahram.ducksservice.model.DuckData;
import edu.iu.habahram.ducksservice.model.DuckEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/ducks")
public class DuckController {

    private final DuckRepository duckRepository;

    private static final String IMAGES_FOLDER_PATH = "ducks/images/";
    private static final String AUDIO_FOLDER_PATH = "ducks/audio/";

    public DuckController(DuckRepository duckRepository) {
        this.duckRepository = duckRepository;
    }

    // ✅ SAVE TO POSTGRES
    @PostMapping
    public int add(@RequestBody DuckData duck) {
        DuckEntity entity = new DuckEntity();
        entity.setName(duck.name());
        entity.setType(duck.type());

        DuckEntity saved = duckRepository.save(entity);
        return saved.getId();
    }

    // ✅ READ ALL FROM POSTGRES
    @GetMapping
    public List<DuckData> findAll() {
        return duckRepository.findAll()
                .stream()
                .map(DuckData::fromEntity)
                .toList();
    }

    // ✅ READ ONE FROM POSTGRES
    @GetMapping("/{id}")
    public ResponseEntity<DuckData> find(@PathVariable int id) {
        return duckRepository.findById(id)
                .map(e -> ResponseEntity
                        .status(HttpStatus.FOUND)
                        .body(DuckData.fromEntity(e)))
                .orElseGet(() -> ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(null));
    }

    // ✅ SAVE IMAGE TO DISK + UPDATE DB
    @PostMapping("/{id}/image")
    public boolean updateImage(@PathVariable int id,
                               @RequestParam MultipartFile file) {
        try {
            String fileExtension = ".png";
            Path path = Paths.get(IMAGES_FOLDER_PATH + id + fileExtension);
            file.transferTo(path);

            DuckEntity duck = duckRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Duck not found: " + id));
            duck.setImagePath(path.toString());
            duckRepository.save(duck);

            return true;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // ✅ SAVE AUDIO TO DISK + UPDATE DB
    @PostMapping("/{id}/audio")
    public boolean updateAudio(@PathVariable int id,
                               @RequestParam MultipartFile file) {
        try {
            String fileExtension = ".mp3";
            Path path = Paths.get(AUDIO_FOLDER_PATH + id + fileExtension);
            file.transferTo(path);

            DuckEntity duck = duckRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Duck not found: " + id));
            duck.setAudioPath(path.toString());
            duckRepository.save(duck);

            return true;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // ✅ READ IMAGE FROM DISK
    @GetMapping("/{id}/image")
    public ResponseEntity<?> getImage(@PathVariable int id) {
        try {
            Path path = Paths.get(IMAGES_FOLDER_PATH + id + ".png");
            byte[] image = Files.readAllBytes(path);

            return ResponseEntity.status(HttpStatus.FOUND)
                    .contentType(MediaType.IMAGE_PNG)
                    .body(image);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // ✅ READ AUDIO FROM DISK
    @GetMapping("/{id}/audio")
    public ResponseEntity<?> getAudio(@PathVariable int id) {
        try {
            Path path = Paths.get(AUDIO_FOLDER_PATH + id + ".mp3");
            byte[] file = Files.readAllBytes(path);

            return ResponseEntity.status(HttpStatus.FOUND)
                    .contentType(MediaType.valueOf("audio/mp3"))
                    .body(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}