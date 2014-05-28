/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
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
