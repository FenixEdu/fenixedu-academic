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
package net.sourceforge.fenixedu.presentationTier.docs.thesis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JasperPrint;
import net.sourceforge.fenixedu.domain.thesis.Thesis;
import net.sourceforge.fenixedu.domain.thesis.ThesisFile;
import net.sourceforge.fenixedu.util.Bundle;
import net.sourceforge.fenixedu.util.JasperPrintProcessor;

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
            addParameter("thesisTitle", EMPTY_STR);
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

    @Override
    public JasperPrintProcessor getPreProcessor() {
        return LineProcessor.instance;
    }

    private static class LineProcessor implements JasperPrintProcessor {

        private static LineProcessor instance = new LineProcessor();

        private static String[][] FIELD_LINE_MAP = { { "textField-title", "13", "line-title-2" },
                { "textField-subtitle", "13", "line-subtitle-2" } };

        private static float LINE_DISTANCE = 11.5f;

        @Override
        public JasperPrint process(JasperPrint jasperPrint) {
            Map<String, JRPrintElement> map = getElementsMap(jasperPrint);

            for (String[] element2 : FIELD_LINE_MAP) {
                String elementKey = element2[0];
                Integer height = new Integer(element2[1]);

                JRPrintElement element = map.get(elementKey);

                if (element == null) {
                    continue;
                }

                if (element.getHeight() > height.intValue()) {
                    // height increased
                    for (int j = 2; j < element2.length; j++) {
                        JRPrintElement line = map.get(element2[j]);

                        if (line != null) {
                            line.setY(element.getY() + ((int) (j * LINE_DISTANCE)));
                        }
                    }
                } else {
                    // height is the same
                    for (int j = 2; j < element2.length; j++) {
                        JRPrintElement line = map.get(element2[j]);

                        if (line != null) {
                            removeElement(jasperPrint, line);
                        }
                    }
                }
            }

            return jasperPrint;
        }

        private Map<String, JRPrintElement> getElementsMap(JasperPrint jasperPrint) {
            Map<String, JRPrintElement> map = new HashMap<String, JRPrintElement>();

            Iterator pages = jasperPrint.getPages().iterator();
            while (pages.hasNext()) {
                JRPrintPage page = (JRPrintPage) pages.next();

                Iterator elements = page.getElements().iterator();
                while (elements.hasNext()) {
                    JRPrintElement element = (JRPrintElement) elements.next();

                    map.put(element.getKey(), element);
                }
            }

            return map;
        }

        private void removeElement(JasperPrint jasperPrint, JRPrintElement target) {
            Iterator pages = jasperPrint.getPages().iterator();
            while (pages.hasNext()) {
                JRPrintPage page = (JRPrintPage) pages.next();

                Iterator elements = page.getElements().iterator();
                while (elements.hasNext()) {
                    JRPrintElement element = (JRPrintElement) elements.next();

                    if (element == target) {
                        elements.remove();
                    }
                }
            }
        }

    }
}
