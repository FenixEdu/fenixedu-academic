package net.sourceforge.fenixedu.webServices.jersey;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@Provider
public class JacksonJerseyProvider implements ContextResolver<ObjectMapper> {

    @Override
    public ObjectMapper getContext(Class<?> type) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        return objectMapper;
    }

}
