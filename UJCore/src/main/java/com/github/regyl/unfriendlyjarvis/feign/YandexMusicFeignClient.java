package com.github.regyl.unfriendlyjarvis.feign;

import com.github.regyl.unfriendlyjarvis.controller.dto.yandexmusic.feign.lickedtracks.LikedTracksResponseDto;
import com.github.regyl.unfriendlyjarvis.controller.dto.yandexmusic.feign.track.TrackResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;

@FeignClient(name = "yandex-music", url = "https://api.music.yandex.net")
public interface YandexMusicFeignClient {

    @GetMapping("/users/{userId}/likes/tracks")
    LikedTracksResponseDto getLikedTracks(@PathVariable("userId") Long userId);

    @GetMapping("/tracks")
    TrackResponse getTrack(@RequestParam("track-ids") Collection<String> ids);
}
