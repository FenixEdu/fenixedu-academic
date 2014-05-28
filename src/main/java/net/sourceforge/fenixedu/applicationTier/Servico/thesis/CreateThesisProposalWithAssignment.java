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
package net.sourceforge.fenixedu.applicationTier.Servico.thesis;

import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Proposal;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.thesis.Thesis;
import net.sourceforge.fenixedu.domain.thesis.ThesisEvaluationParticipant;
import pt.ist.fenixframework.Atomic;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class CreateThesisProposalWithAssignment {

    protected Thesis run(DegreeCurricularPlan degreeCurricularPlan, Student student, Proposal proposal, Thesis previousThesis) {

        Integer orientatorsCreditsPercentage;
        Person coorientator;
        Person orientator;
        MultiLanguageString title;

        if (previousThesis != null) {
            orientatorsCreditsPercentage = previousThesis.getOrientatorCreditsDistribution();
            ThesisEvaluationParticipant coorientatorObj = previousThesis.getCoorientator();
            coorientator = coorientatorObj == null ? null : coorientatorObj.getPerson();

            ThesisEvaluationParticipant orientatorObj = previousThesis.getOrientator();
            orientator = orientatorObj == null ? null : previousThesis.getOrientator().getPerson();

            title = previousThesis.getTitle();
        } else {
            orientatorsCreditsPercentage = proposal.getOrientatorsCreditsPercentage();
            coorientator = proposal.getCoorientator();
            orientator = proposal.getOrientator();
            title = new MultiLanguageString(proposal.getTitle());
        }

        Thesis thesis =
                new Thesis(degreeCurricularPlan.getDegree(), student.getDissertationEnrolment(degreeCurricularPlan), title);

        thesis.setOrientator(orientator);
        thesis.setCoorientator(coorientator);
        thesis.setOrientatorCreditsDistribution(orientatorsCreditsPercentage);

        return thesis;
    }

    // Service Invokers migrated from Berserk

    private static final CreateThesisProposalWithAssignment serviceInstance = new CreateThesisProposalWithAssignment();

    @Atomic
    public static Thesis runCreateThesisProposalWithAssignment(DegreeCurricularPlan degreeCurricularPlan, Student student,
            Proposal proposal, Thesis previousThesis) {
        return serviceInstance.run(degreeCurricularPlan, student, proposal, previousThesis);
    }

}