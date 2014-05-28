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

import java.util.Locale;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.thesis.Thesis;
import net.sourceforge.fenixedu.domain.thesis.ThesisEvaluationParticipant;

import org.apache.commons.lang.StringUtils;

public class ApproveJuryDocument extends ThesisDocument {

    private static final long serialVersionUID = 1L;

    public ApproveJuryDocument(Thesis thesis) {
        super(thesis);
    }

    @Override
    protected void fillGeneric() {
        super.fillGeneric();

        Thesis thesis = getThesis();

        final ThesisEvaluationParticipant thesisEvaluationParticipant = thesis.getProposalApprover();
        final String author;
        final String date;
        final String ccAuthor;
        final String ccDate;
        if (thesisEvaluationParticipant == null) {
            author = date = ccAuthor = ccDate = StringUtils.EMPTY;
        } else {
            final Person person = thesisEvaluationParticipant.getPerson();
            if (person != null && person.hasRole(RoleType.SCIENTIFIC_COUNCIL)) {
                author = date = StringUtils.EMPTY;
                ccAuthor = thesisEvaluationParticipant.getPersonName();
                ccDate = String.format(new Locale("pt"), "%1$td de %1$tB de %1$tY", thesis.getApproval().toDate());
            } else {
                ccAuthor = ccDate = StringUtils.EMPTY;
                author = thesisEvaluationParticipant.getPersonName();
                date = String.format(new Locale("pt"), "%1$td de %1$tB de %1$tY", thesis.getApproval().toDate());
            }
        }

        addParameter("author", author);
        addParameter("date", date);
        addParameter("ccAuthor", ccAuthor);
        addParameter("ccDate", ccDate);
    }

    @Override
    public String getReportFileName() {
        Thesis thesis = getThesis();
        return "pedido-homologacao-aluno-" + thesis.getStudent().getNumber();
    }

}
