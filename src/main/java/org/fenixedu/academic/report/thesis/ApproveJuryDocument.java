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

import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.domain.Coordinator_Base;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.thesis.Thesis;
import org.fenixedu.academic.domain.thesis.ThesisEvaluationParticipant;

import java.util.Locale;

public class ApproveJuryDocument extends ThesisDocument {

    private static final long serialVersionUID = 1L;

    public ApproveJuryDocument(Thesis thesis) {
        super(thesis);
    }

    @Override
    protected void fillGeneric() {
        super.fillGeneric();

        Thesis thesis = getThesis();

        final ThesisEvaluationParticipant proposalApprover = thesis.getProposalApprover();
        final String author;
        final String date;
        final String ccAuthor;
        final String ccDate;
        if (proposalApprover == null) {
            author = date = ccAuthor = ccDate = StringUtils.EMPTY;
        } else {
            final Person person = proposalApprover.getPerson();
            boolean isDegreeCoordinator = checkCoordinator(thesis, person);

            if (person != null && !isDegreeCoordinator) {
                author = date = StringUtils.EMPTY;
                ccAuthor = proposalApprover.getName();
                ccDate = String.format(new Locale("pt"), "%1$td de %1$tB de %1$tY", thesis.getApproval().toDate());
            } else {
                ccAuthor = ccDate = StringUtils.EMPTY;
                author = proposalApprover.getName();
                date = String.format(new Locale("pt"), "%1$td de %1$tB de %1$tY", thesis.getApproval().toDate());
            }
        }

        getPayload().addProperty("author", author);
        getPayload().addProperty("date", date);
        getPayload().addProperty("ccAuthor", ccAuthor);
        getPayload().addProperty("ccDate", ccDate);
    }

    private boolean checkCoordinator(Thesis thesis, final Person person) {
        return thesis.getDegree().getCurrentCoordinators().stream().map(Coordinator_Base::getPerson)
                .anyMatch(coordinator -> coordinator.equals(person));
    }

    @Override
    public String getReportFileName() {
        Thesis thesis = getThesis();
        return "pedido-homologacao-aluno-" + thesis.getStudent().getNumber();
    }

}
