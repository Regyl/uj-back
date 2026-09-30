package com.github.regyl.unfriendlyjarvis.service.impl.meme;

import com.github.regyl.unfriendlyjarvis.entity.MemeEntity;
import com.github.regyl.unfriendlyjarvis.entity.enums.Source;
import com.github.regyl.unfriendlyjarvis.model.MemeModel;
import com.github.regyl.unfriendlyjarvis.service.SecurityContextService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.OffsetDateTime;
import java.util.function.Function;
import java.util.function.Supplier;

@Slf4j
@Component
@RequiredArgsConstructor
public class MemeModelToMemeEntityMapperServiceImpl implements Function<MemeModel, MemeEntity> {

    private final Supplier<OffsetDateTime> dateTimeSupplier;
    private final SecurityContextService securityContextService;

    @Override
    public MemeEntity apply(MemeModel model) {
        Source source = parseSource(model.getSource());
        return MemeEntity.builder()
                .accountId(securityContextService.getUserId())
                .bucketPath(model.getBucketPath())
                .source(source)
                .created(dateTimeSupplier.get())
                .fileName(model.getFileName())
                .build();
    }

    /**
     * Parse source string to Source enum.
     * Returns OTHER if source is null or invalid.
     *
     * @param source source string
     * @return Source enum
     */
    private Source parseSource(String source) {
        if (!StringUtils.hasLength(source)) {
            return Source.OTHER;
        }
        try {
            return Source.valueOf(source.toUpperCase());
        } catch (IllegalArgumentException e) {
            log.warn("Invalid source value: {}, using OTHER", source);
            return Source.OTHER;
        }
    }
}
