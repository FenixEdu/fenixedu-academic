package org.fenixedu.academic.domain.util.i18n;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.apache.commons.lang.StringUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

public class Languages implements Serializable {

    public static final Function<Locale, String> LOCALE_FORMATTER =
            loc -> String.format("%s (%s)", StringUtils.capitalize(loc.getDisplayLanguage()), loc.toString());

    private final Set<Locale> languages;

    public Languages() {
        this.languages = Set.of();
    }

    public Languages(final Stream<Locale> languages) {
        this.languages = languages.collect(Collectors.toUnmodifiableSet());
    }

    public Languages(final JsonElement json) {
        languages = StreamSupport.stream(json.getAsJsonArray().spliterator(), false).map(JsonElement::getAsString)
                .map(Locale::new).collect(Collectors.toUnmodifiableSet());
    }

    public Collection<Locale> getValues() {
        return this.languages;
    }

    public String getFormattedValue() {
        return languages.stream().map(LOCALE_FORMATTER).sorted().collect(Collectors.joining(", "));
    }

    public JsonElement toJson() {
        JsonArray result = new JsonArray();
        languages.stream().map(Locale::getLanguage).map(JsonPrimitive::new).forEach(type -> result.add(type));
        return result;
    }

    public static Collection<Locale> findAllAvailableLanguages() {
        return Arrays.asList(Locale.getAvailableLocales()).stream().filter(l -> !StringUtils.isBlank(l.getLanguage()))
                .filter(l -> StringUtils.isBlank(l.getCountry())).collect(Collectors.toUnmodifiableList());
    }

}
