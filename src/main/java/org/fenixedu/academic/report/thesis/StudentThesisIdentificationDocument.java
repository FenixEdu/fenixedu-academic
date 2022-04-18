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

import com.google.gson.JsonArray;
import org.fenixedu.academic.domain.thesis.Thesis;
import org.fenixedu.academic.domain.thesis.ThesisFile;

import java.util.*;
import java.util.stream.Collectors;

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
            getPayload().addProperty("thesisTitle", file.getTitle());
        } else {
            getPayload().addProperty("thesisTitle", thesis.getTitle().getContent());
        }

        String date = Optional.of(thesis.getDiscussed()).map(
                discussion -> {
                    return String.format(new Locale("pt"), "%1$td/%1$tm/%1$tY", discussion.toDate());
                }
        ).orElse(EMPTY_STR);
        getPayload().addProperty("discussion", date);

        getPayload().add("keywordsPt", getKeywordsJson(thesis.getKeywordsPt()));
        getPayload().add("keywordsEn",  getKeywordsJson(thesis.getKeywordsEn()));

        getPayload().addProperty("abstractPt", Optional.ofNullable(thesis.getThesisAbstractPt()).orElse(EMPTY_STR));
        getPayload().addProperty("abstractEn", Optional.ofNullable(thesis.getThesisAbstractEn()).orElse(EMPTY_STR));
    }

    private JsonArray getKeywordsJson(String keywords) {
        JsonArray keywordsJson = new JsonArray();
        splitKeywords(keywords).stream().limit(6).forEach(keywordsJson::add);
        return keywordsJson;
    }

    private List<String> splitKeywords(String keywords) {
        return Optional.of(keywords)
                .map(k -> Arrays.stream(k.split(","))
                        .map(String::trim)
                        .filter(s -> !s.isEmpty())
                        .collect(Collectors.toList()))
                .orElse(new ArrayList<>());
    }

    @Override
    public String getReportFileName() {
        Thesis thesis = getThesis();
        return "identificacao-aluno-" + thesis.getStudent().getNumber();
    }

}
