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
package net.sourceforge.fenixedu.applicationTier.Servico.coordinator.tutor;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Filtro.TutorshipAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.coordinator.tutor.ChangeTutorshipByEntryYearBean.ChangeTutorshipBean;
import net.sourceforge.fenixedu.dataTransferObject.coordinator.tutor.TutorshipErrorBean;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Tutorship;
import net.sourceforge.fenixedu.domain.student.Registration;

import org.joda.time.DateTimeFieldType;
import org.joda.time.Partial;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class ChangeTutorship extends TutorshipManagement {

    public List<TutorshipErrorBean> run(String executionDegreeID, List<ChangeTutorshipBean> beans) throws FenixServiceException {

        final ExecutionDegree executionDegree = FenixFramework.getDomainObject(executionDegreeID);
        final DegreeCurricularPlan degreeCurricularPlan = executionDegree.getDegreeCurricularPlan();

        List<TutorshipErrorBean> studentsWithErrors = new ArrayList<TutorshipErrorBean>();

        for (ChangeTutorshipBean bean : beans) {
            Tutorship tutorship = bean.getTutorship();

            Partial newTutorshipEndDate =
                    new Partial(new DateTimeFieldType[] { DateTimeFieldType.year(), DateTimeFieldType.monthOfYear() }, new int[] {
                            bean.getTutorshipEndYear(), bean.getTutorshipEndMonth().getNumberOfMonth() });

            if (tutorship.getEndDate() != null && tutorship.getEndDate().isEqual(newTutorshipEndDate)) {
                continue;
            }

            Registration registration = tutorship.getStudentCurricularPlan().getRegistration();
            Integer studentNumber = registration.getNumber();

            try {
                validateStudentRegistration(registration, executionDegree, degreeCurricularPlan, studentNumber);

                tutorship.setEndDate(newTutorshipEndDate);

            } catch (FenixServiceException ex) {
                studentsWithErrors.add(new TutorshipErrorBean(ex.getMessage(), ex.getArgs()));
            }
        }

        return studentsWithErrors;
    }

    // Service Invokers migrated from Berserk

    private static final ChangeTutorship serviceInstance = new ChangeTutorship();

    @Atomic
    public static List<TutorshipErrorBean> runChangeTutorship(String executionDegreeID, List<ChangeTutorshipBean> beans)
            throws FenixServiceException, NotAuthorizedException {
        TutorshipAuthorizationFilter.instance.execute();
        return serviceInstance.run(executionDegreeID, beans);
    }

}