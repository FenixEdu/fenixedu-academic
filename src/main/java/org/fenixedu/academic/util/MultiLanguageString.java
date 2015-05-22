/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.util;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;
import java.util.Locale.Builder;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.commons.i18n.I18N;
import org.fenixedu.commons.i18n.LocalizedString;

import com.google.gson.JsonParser;

@Deprecated
public class MultiLanguageString implements Serializable, Comparable<MultiLanguageString> {
    public static final Locale en = new Builder().setLanguage("en").setRegion("UK").build();
    public static final Locale pt = new Builder().setLanguage("pt").setRegion("PT").build();

    private final LocalizedString localized;

    public MultiLanguageString() {
        this.localized = new LocalizedString();
    }

    public MultiLanguageString(final String content) {
        this.localized = new LocalizedString(I18N.getLocale(), content);
    }

    public MultiLanguageString(final Locale locale, final String content) {
        if (locale == null) {
            throw new IllegalArgumentException("locale cannot be null");
        }
        this.localized = new LocalizedString(locale, content);
    }

    private MultiLanguageString(LocalizedString localized) {
        this.localized = localized;
    }

    /**
     * 
     * @param locale
     *            the locale of the content
     * @param content
     *            the String with the content in the specified locale
     * @return a <b>new</b> {@link MultiLanguageString} with the given content
     *         in the given locale added to the already existing content NOTE:
     *         it does not change the content of this instance
     */
    public MultiLanguageString with(final Locale locale, final String content) {
        return new MultiLanguageString(localized.with(locale, content));
    }

    public MultiLanguageString without(Locale locale) {
        if (locale != null) {
            return new MultiLanguageString(localized.without(locale));
        }
        return this;
    }

    public Collection<String> getAllContents() {
        Set<String> contents = new HashSet<>();
        for (Locale locale : localized.getLocales()) {
            contents.add(localized.getContent(locale));
        }
        return contents;
    }

    public Collection<Locale> getAllLocales() {
        return localized.getLocales();
    }

    public Locale getContentLocale() {
        Locale locale = I18N.getLocale();
        if (hasLocale(locale)) {
            return locale;
        }

        Locale defaultLocale = Locale.getDefault();
        if (hasLocale(defaultLocale)) {
            return defaultLocale;
        }

        return localized.getLocales().isEmpty() ? null : localized.getLocales().iterator().next();
    }

    public String getContent() {
        return localized.getContent();
    }

    public String getContent(Locale locale) {
        return localized.getContent(locale);
    }

    public String getPreferedContent() {
        return hasLocale(Locale.getDefault()) ? getContent(Locale.getDefault()) : getContent();
    }

    public boolean hasContent() {
        // return getContent() != null;
        return !isEmpty();
    }

    public boolean hasContent(Locale locale) {
        return !StringUtils.isEmpty(getContent(locale));
    }

    public boolean hasLocale(Locale locale) {
        return localized.getLocales().contains(locale);
    }

    public String exportAsString() {
        return localized.json().toString();
    }

    public MultiLanguageString append(MultiLanguageString string) {
        return new MultiLanguageString(localized.append(string.toLocalizedString()));
    }

    public MultiLanguageString append(String string) {
        return new MultiLanguageString(localized.append(string));
    }

    /**
     * @return true if this {@link MultiLanguageString} contains no content
     */
    public boolean isEmpty() {
        return localized.isEmpty();
    }

    public static MultiLanguageString importFromString(String string) {
        if (string == null) {
            return null;
        }
        if (string.startsWith("{")) {
            JsonParser parser = new JsonParser();
            return new MultiLanguageString(LocalizedString.fromJson(parser.parse(string)));
        }

        LocalizedString.Builder builder = new LocalizedString.Builder();

        String nullContent = StringUtils.EMPTY;

        for (int i = 0; i < string.length();) {

            int length = 0;
            int collonPosition = string.indexOf(':', i + 2);

            if (!StringUtils.isNumeric(string.substring(i + 2, collonPosition))) {
                length = Integer.parseInt(string.substring(i + 4, collonPosition));
                nullContent = string.substring(collonPosition + 1, collonPosition + 1 + length);

            } else {
                length = Integer.parseInt(string.substring(i + 2, collonPosition));
                String language = string.substring(i, i + 2);
                String content = string.substring(collonPosition + 1, collonPosition + 1 + length);
                builder.with(new Locale.Builder().setLanguage(language).build(), content);
            }

            i = collonPosition + 1 + length;
        }

        LocalizedString localized = builder.build();
        // HACK: MultiLanguageString should not allow null values as language
        if (localized.isEmpty()) {
            localized = localized.with(Locale.getDefault(), nullContent);
        }

        return new MultiLanguageString(localized);
    }

    @Override
    public String toString() {
        final String content = getContent();
        return content == null ? StringUtils.EMPTY : content;
    }

    @Override
    public int compareTo(MultiLanguageString mls) {
        return localized.compareTo(mls.localized);
    }

    public boolean equalInAnyLanguage(Object obj) {
        if (obj instanceof MultiLanguageString) {
            MultiLanguageString multiLanguageString = (MultiLanguageString) obj;
            Set<Locale> locales = new HashSet<Locale>();
            locales.addAll(this.getAllLocales());
            locales.addAll(multiLanguageString.getAllLocales());
            for (Locale locale : locales) {
                if (this.getContent(locale) != null
                        && this.getContent(locale).equalsIgnoreCase(multiLanguageString.getContent(locale))) {
                    return true;
                }
            }
        } else if (obj instanceof String) {
            for (final String string : getAllContents()) {
                if (string.equals(obj)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof MultiLanguageString) {
            MultiLanguageString multiLanguageString = (MultiLanguageString) obj;
            return localized.equals(multiLanguageString.localized);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return localized.hashCode();
    }

    public LocalizedString toLocalizedString() {
        return localized;
    }

    public static MultiLanguageString fromLocalizedString(LocalizedString localized) {
        return new MultiLanguageString(localized);
    }
}
