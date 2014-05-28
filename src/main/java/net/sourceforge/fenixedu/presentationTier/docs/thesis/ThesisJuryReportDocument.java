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

import net.sourceforge.fenixedu.domain.thesis.Thesis;

import org.joda.time.DateTime;

/**
 * 
 * @author Joao Carvalho
 * 
 */

public class ThesisJuryReportDocument extends ThesisDocument {

    private static final long serialVersionUID = 1L;

    public ThesisJuryReportDocument(Thesis thesis) {
        super(thesis);
    }

    @Override
    public void fillGeneric() {
        Thesis thesis = getThesis();
        String discussed;

        if (thesis.getDiscussed() != null) {
            DateTime discussedTime = thesis.getDiscussed();
            discussed = discussedTime.getDayOfMonth() + " / " + discussedTime.getMonthOfYear() + " / " + discussedTime.getYear();
        } else {
            discussed = EMPTY_STR;
        }

        addParameter("date", discussed);
        addParameter("grade", neverNull(getThesis().getMark()));
    }

    @Override
    public String getReportFileName() {
        return "acta-juri-tese-" + getThesis().getStudent().getNumber();
    }

    @Override
    protected String neverNull(String value) {
        return value == null ? EMPTY_STR : value;
    }

    protected String neverNull(Integer value) {
        return value == null ? EMPTY_STR : value.toString();
    }

}
