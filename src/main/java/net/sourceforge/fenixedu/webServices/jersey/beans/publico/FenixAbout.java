package net.sourceforge.fenixedu.webServices.jersey.beans.publico;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.util.FenixConfigurationManager;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.util.CoreConfiguration;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;

public class FenixAbout {

    public static class FenixRSSFeed {
        String description;
        String url;

        public FenixRSSFeed(final String description, final String uri) {
            this.description = description;
            this.url = uri;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String uri) {
            this.url = uri;
        }
    }

    String institutionName = null;
    String institutionUrl = null;
    List<FenixRSSFeed> rssFeeds = new ArrayList<>();
    String currentAcademicTerm;
    Set<String> languages;
    String language;

    private FenixAbout() {
        final Bennu instance = Bennu.getInstance();
        final Unit unit = instance.getInstitutionUnit();
        if (unit != null) {
            institutionName = unit.getName();
            institutionUrl = unit.getDefaultWebAddressUrl();
        }
        currentAcademicTerm = ExecutionSemester.readActualExecutionSemester().getQualifiedName();
        rssFeeds.add(new FenixRSSFeed("News", FenixConfigurationManager.getConfiguration().getFenixApiNewsRSSUrl()));
        rssFeeds.add(new FenixRSSFeed("Events", FenixConfigurationManager.getConfiguration().getFenixApiEventsRSSUrl()));
        languages = FluentIterable.from(CoreConfiguration.supportedLocales()).transform(new Function<Locale, String>() {

            @Override
            public String apply(Locale input) {
                return input.toLanguageTag();
            }
        }).toSet();

        language = Locale.getDefault().toLanguageTag();
    }

    public static FenixAbout getInstance() {
        return new FenixAbout();
    }

    public String getInstitutionName() {
        return institutionName;
    }

    public String getInstitutionUrl() {
        return institutionUrl;
    }

    public List<FenixRSSFeed> getRssFeeds() {
        return rssFeeds;
    }

    public String getCurrentAcademicTerm() {
        return currentAcademicTerm;
    }

    public Set<String> getLanguages() {
        return languages;
    }

    public String getLanguage() {
        return language;
    }

}
