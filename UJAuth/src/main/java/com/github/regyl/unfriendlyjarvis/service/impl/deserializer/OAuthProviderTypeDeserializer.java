package com.github.regyl.unfriendlyjarvis.service.impl.deserializer;

import com.github.regyl.unfriendlyjarvis.enumeration.OAuthProviderType;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.ValueDeserializer;

/**
 * Deserializer for {@link OAuthProviderType}.
 */
public class OAuthProviderTypeDeserializer extends ValueDeserializer<OAuthProviderType> {

    @Override
    public OAuthProviderType deserialize(JsonParser parser, DeserializationContext context) {
        return OAuthProviderType.fromName(parser.getValueAsString());
    }
}
