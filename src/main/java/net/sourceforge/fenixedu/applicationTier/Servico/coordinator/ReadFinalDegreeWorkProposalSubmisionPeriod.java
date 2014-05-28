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
/*
 * Created on 2004/03/10
 */
package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.finalDegreeWork.InfoScheduleing;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Scheduleing;
import pt.ist.fenixframework.Atomic;

/**
 * @author Luis Cruz
 */
public class ReadFinalDegreeWorkProposalSubmisionPeriod {

    @Atomic
    public static InfoScheduleing run(final ExecutionDegree executionDegree) throws FenixServiceException {

        InfoScheduleing infoScheduleing = null;

        if (executionDegree != null) {
            Scheduleing scheduleing = executionDegree.getScheduling();

            if (scheduleing != null) {
                infoScheduleing = new InfoScheduleing();
                infoScheduleing.setExternalId(scheduleing.getExternalId());
                infoScheduleing.setStartOfProposalPeriod(scheduleing.getStartOfProposalPeriod());
                infoScheduleing.setEndOfProposalPeriod(scheduleing.getEndOfProposalPeriod());
                infoScheduleing.setStartOfCandidacyPeriod(scheduleing.getStartOfCandidacyPeriod());
                infoScheduleing.setEndOfCandidacyPeriod(scheduleing.getEndOfCandidacyPeriod());
                infoScheduleing.setMinimumNumberOfCompletedCourses(scheduleing.getMinimumNumberOfCompletedCourses());
                infoScheduleing.setMaximumCurricularYearToCountCompletedCourses(scheduleing
                        .getMaximumCurricularYearToCountCompletedCourses());
                infoScheduleing.setMinimumCompletedCurricularYear(scheduleing.getMinimumCompletedCurricularYear());
                infoScheduleing.setMinimumNumberOfStudents(scheduleing.getMinimumNumberOfStudents());
                infoScheduleing.setMaximumNumberOfStudents(scheduleing.getMaximumNumberOfStudents());
                infoScheduleing.setMaximumNumberOfProposalCandidaciesPerGroup(scheduleing
                        .getMaximumNumberOfProposalCandidaciesPerGroup());
                infoScheduleing.setAttributionByTeachers(scheduleing.getAttributionByTeachers());
                infoScheduleing.setAllowSimultaneousCoorientationAndCompanion(scheduleing
                        .getAllowSimultaneousCoorientationAndCompanion());
                infoScheduleing.setMinimumCompletedCreditsFirstCycle(scheduleing.getMinimumCompletedCreditsFirstCycle());
                infoScheduleing.setMinimumCompletedCreditsSecondCycle(scheduleing.getMinimumCompletedCreditsSecondCycle());
            }
        }

        return infoScheduleing;
    }

}