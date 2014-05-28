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
package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.commons.candidate;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Filtro.WriteCandidateEnrolmentsAuhorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.CandidateEnrolment;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.MasterDegreeCandidate;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class WriteCandidateEnrolments {

    protected void run(Set<String> selectedCurricularCoursesIDs, String candidateID, Double credits, String givenCreditsRemarks)
            throws FenixServiceException {

        MasterDegreeCandidate masterDegreeCandidate = FenixFramework.getDomainObject(candidateID);
        if (masterDegreeCandidate == null) {
            throw new NonExistingServiceException();
        }

        masterDegreeCandidate.setGivenCredits(credits);

        if (credits.floatValue() != 0) {
            masterDegreeCandidate.setGivenCreditsRemarks(givenCreditsRemarks);
        }

        Collection<CandidateEnrolment> candidateEnrolments = masterDegreeCandidate.getCandidateEnrolments();
        List<String> candidateEnrolmentsCurricularCoursesIDs =
                (List<String>) CollectionUtils.collect(candidateEnrolments, new Transformer() {
                    @Override
                    public Object transform(Object arg0) {
                        CandidateEnrolment candidateEnrolment = (CandidateEnrolment) arg0;
                        return candidateEnrolment.getCurricularCourse().getExternalId();
                    }
                });

        Collection<String> curricularCoursesToEnroll =
                CollectionUtils.subtract(selectedCurricularCoursesIDs, candidateEnrolmentsCurricularCoursesIDs);

        final Collection<Integer> curricularCoursesToDelete =
                CollectionUtils.subtract(candidateEnrolmentsCurricularCoursesIDs, selectedCurricularCoursesIDs);

        Collection<CandidateEnrolment> candidateEnrollmentsToDelete =
                CollectionUtils.select(candidateEnrolments, new Predicate() {
                    @Override
                    public boolean evaluate(Object arg0) {
                        CandidateEnrolment candidateEnrolment = (CandidateEnrolment) arg0;
                        return (curricularCoursesToDelete.contains(candidateEnrolment.getCurricularCourse().getExternalId()));
                    }
                });

        writeFilteredEnrollments(masterDegreeCandidate, curricularCoursesToEnroll);

        for (CandidateEnrolment candidateEnrolmentToDelete : candidateEnrollmentsToDelete) {
            candidateEnrolmentToDelete.delete();
        }
    }

    /**
     * @param persistentSupport
     * @param masterDegreeCandidate
     * @param curricularCoursesToEnroll
     * @throws NonExistingServiceException
     * @throws ExcepcaoPersistencia
     */
    private void writeFilteredEnrollments(MasterDegreeCandidate masterDegreeCandidate,
            Collection<String> curricularCoursesToEnroll) throws NonExistingServiceException {
        Iterator<String> iterCurricularCourseIds = curricularCoursesToEnroll.iterator();
        while (iterCurricularCourseIds.hasNext()) {

            CurricularCourse curricularCourse = (CurricularCourse) FenixFramework.getDomainObject(iterCurricularCourseIds.next());

            if (curricularCourse == null) {
                throw new NonExistingServiceException();
            }

            CandidateEnrolment candidateEnrolment = new CandidateEnrolment();

            masterDegreeCandidate.addCandidateEnrolments(candidateEnrolment);
            candidateEnrolment.setCurricularCourse(curricularCourse);
        }
    }

    // Service Invokers migrated from Berserk

    private static final WriteCandidateEnrolments serviceInstance = new WriteCandidateEnrolments();

    @Atomic
    public static void runWriteCandidateEnrolments(Set<String> selectedCurricularCoursesIDs, String candidateID, Double credits,
            String givenCreditsRemarks) throws FenixServiceException, NotAuthorizedException {
        WriteCandidateEnrolmentsAuhorizationFilter.instance.execute(selectedCurricularCoursesIDs, candidateID, credits,
                givenCreditsRemarks);
        serviceInstance.run(selectedCurricularCoursesIDs, candidateID, credits, givenCreditsRemarks);
    }

}