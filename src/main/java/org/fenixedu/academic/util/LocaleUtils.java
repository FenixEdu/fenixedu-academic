package org.fenixedu.academic.util;

import java.util.Locale;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.commons.i18n.I18N;
import org.fenixedu.commons.i18n.LocalizedString;

import com.google.gson.JsonParser;

public class LocaleUtils {

    public static final Locale EN = new Locale("en", "UK");

    public static final Locale PT = new Locale("pt", "PT");

    public static boolean equalInAnyLanguage(final LocalizedString ls1, final LocalizedString ls2) {
        if (ls1 != null && ls2 != null) {
            for (final Locale l : ls1.getLocales()) {
                final String c1 = ls1.getContent(l);
                final String c2 = ls2.getContent(l);
                if (c1 != null && c1.equals(c2)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static String getPreferedContent(final LocalizedString ls) {
        final String content = ls.getContent(Locale.getDefault());
        return content != null ? content : ls.getContent();
    }

    public static Locale getContentLocale(final LocalizedString ls) {
        final Set<Locale> locales = ls.getLocales();

        final Locale locale = I18N.getLocale();
        if (locales.contains(locale)) {
            return locale;
        }

        final Locale defaultLocale = Locale.getDefault();
        if (locales.contains(defaultLocale)) {
            return defaultLocale;
        }

        return locales.isEmpty() ? null : locales.iterator().next();
    }

    public static LocalizedString importFromString(String string) {
        if (string == null) {
            return null;
        }
        if (string.startsWith("{")) {
            JsonParser parser = new JsonParser();
            return LocalizedString.fromJson(parser.parse(string));
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

        return localized;
    }

}
