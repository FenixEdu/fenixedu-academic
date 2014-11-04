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
package org.fenixedu.academic.report.thesis;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.fenixedu.academic.domain.thesis.Thesis;
import org.fenixedu.academic.domain.thesis.ThesisFile;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.joda.time.DateTime;

public class StudentThesisIdentificationDocument extends ThesisDocument {

    private static final long serialVersionUID = 1L;

    public StudentThesisIdentificationDocument(Thesis thesis) {
        super(thesis);
    }

    @Override
    protected void fillThesisInfo() {
        Thesis thesis = getThesis();

        ThesisFile file = thesis.getDissertation();
        if (file != null) {
            addParameter("thesisTitle", file.getTitle());
            addParameter("thesisSubtitle", neverNull(file.getSubTitle()));
            addParameter("thesisLanguage", getLanguage(file));
        } else {
            addParameter("thesisTitle", thesis.getTitle().getContent());
            addParameter("thesisSubtitle", EMPTY_STR);
            addParameter("thesisLanguage", EMPTY_STR);
        }

        String date = null;

        DateTime discussion = thesis.getDiscussed();
        if (discussion != null) {
            date = String.format(new Locale("pt"), "%1$td/%1$tm/%1$tY", discussion.toDate());
        }

        addParameter("discussion", neverNull(date));

        int index = 0;
        for (String keyword : splitKeywords(thesis.getKeywordsPt())) {
            addParameter("keywordPt" + index++, keyword);
        }

        while (index < 6) {
            addParameter("keywordPt" + index++, EMPTY_STR);
        }

        index = 0;
        for (String keyword : splitKeywords(thesis.getKeywordsEn())) {
            addParameter("keywordEn" + index++, keyword);
        }

        while (index < 6) {
            addParameter("keywordEn" + index++, EMPTY_STR);
        }

        addParameter("keywordsPt", thesis.getKeywordsPt());
        addParameter("keywordsEn", thesis.getKeywordsEn());

        addParameter("abstractPt", neverNull(thesis.getThesisAbstractPt()));
        addParameter("abstractEn", neverNull(thesis.getThesisAbstractEn()));
    }

    private String getLanguage(ThesisFile file) {
        Locale language = file.getLanguage();

        if (language == null) {
            return EMPTY_STR;
        }

        return BundleUtil.getString(Bundle.ENUMERATION, language.getLanguage());
    }

    private List<String> splitKeywords(String keywords) {
        List<String> result = new ArrayList<String>();

        if (keywords == null) {
            return result;
        }

        for (String part : keywords.split(",")) {
            String trimmed = part.trim();

            if (trimmed.length() > 0) {
                result.add(trimmed);
            }
        }

        return result;
    }

    @Override
    public String getReportFileName() {
        Thesis thesis = getThesis();
        return "identificacao-aluno-" + thesis.getStudent().getNumber();
    }

}
