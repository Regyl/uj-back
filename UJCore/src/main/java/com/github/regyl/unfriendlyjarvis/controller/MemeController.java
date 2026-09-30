package com.github.regyl.unfriendlyjarvis.controller;

import com.github.regyl.unfriendlyjarvis.controller.dto.meme.MemeDto;
import com.github.regyl.unfriendlyjarvis.service.meme.MemeService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;

/**
 * Controller for meme management.
 */
@RestController
@RequestMapping("/memes")
@RequiredArgsConstructor
public class MemeController {

    private final MemeService service;

    /**
     * Upload meme image.
     *
     * @param file   meme image file
     * @param source optional source of the meme
     * @return created meme entity
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MemeDto uploadMeme(@NotNull @RequestParam("file") MultipartFile file,
                                 @RequestParam(value = "source", required = false) String source) {
        return service.uploadMeme(file, source);
    }

    /**
     * Get all memes for current user.
     *
     * @return collection of meme entities
     */
    @GetMapping
    public Collection<MemeDto> findAll() {
        return service.findAll();
    }
}

