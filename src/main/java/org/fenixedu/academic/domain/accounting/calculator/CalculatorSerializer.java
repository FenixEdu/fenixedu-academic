package org.fenixedu.academic.domain.accounting.calculator;

import java.io.IOException;
import java.math.BigDecimal;

import org.fenixedu.academic.domain.accounting.InterestRate.InterestRateBean;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;

/**
 * Created by Sérgio Silva (hello@fenixedu.org).
 */
public class CalculatorSerializer {
    private ObjectMapper mapper = new ObjectMapper();
    private SimpleModule module = new SimpleModule();

    public CalculatorSerializer() {
        module.addSerializer(new JsonSerializer<DateTime>() {
            @Override
            public void serialize(DateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
                gen.writeString(value.toString("yyyy-MM-dd hh:mm:ss"));
            }

            @Override
            public Class<DateTime> handledType() {
                return DateTime.class;
            }
        });

        module.addSerializer(new JsonSerializer<LocalDate>() {
            @Override
            public void serialize(LocalDate value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
                gen.writeString(value.toString());
            }

            @Override
            public Class<LocalDate> handledType() {
                return LocalDate.class;
            }
        });
        module.addSerializer(new JsonSerializer<BigDecimal>() {
            @Override
            public void serialize(BigDecimal value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
                gen.writeNumber(value.setScale(2, BigDecimal.ROUND_HALF_UP));
            }

            @Override
            public Class<BigDecimal> handledType() {
                return BigDecimal.class;
            }
        });
        module.addSerializer(new JsonSerializer<InterestRateBean>() {
            @Override
            public void serialize(InterestRateBean value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
                gen.writeString(value.toString());
            }

            @Override
            public Class<InterestRateBean> handledType() {
                return InterestRateBean.class;
            }
        });
        mapper.registerModule(module);
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.disable(MapperFeature.DEFAULT_VIEW_INCLUSION);
    }

    public ObjectMapper getMapper() {
        return mapper;
    }
}
