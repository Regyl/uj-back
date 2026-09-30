package com.github.regyl.unfriendlyjarvis.service.impl.deserializer;

import com.github.regyl.unfriendlyjarvis.enumeration.Scope;
import org.apache.commons.lang3.StringUtils;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.ValueDeserializer;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Deserializer for {@link Scope} collection.
 */
public class ScopeCollectionDeserializer extends ValueDeserializer<Collection<Scope>> {

    private static final String DELIMITER = ",";

    @Override
    public Collection<Scope> deserialize(JsonParser parser, DeserializationContext context) {
        String value = parser.getValueAsString();
        if (StringUtils.isEmpty(value)) {
            return Collections.emptySet();
        }

        return Stream.of(value.split(DELIMITER))
                .map(Scope::fromName)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
    }
}
