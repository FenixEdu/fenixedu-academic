/**
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.lists;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;

import org.apache.commons.beanutils.BeanComparator;

/**
 * @author - Ângela Almeida (argelina@ist.utl.pt)
 * 
 */
public class SearchStudentByCriteria extends Service {

    public List<Enrolment> run(final ExecutionYear executionYear, final CurricularCourse curricularCourse, final Integer semester) {

	final ExecutionSemester executionSemester = executionYear.readExecutionPeriodForSemester(semester);

	final List<Enrolment> enrolments = new ArrayList<Enrolment>();
	for (final Enrolment enrolment : curricularCourse.getEnrolmentsByExecutionPeriod(executionSemester)) {
	    enrolments.add(enrolment);
	}

	Collections.sort(enrolments, new BeanComparator("studentCurricularPlan.registration.number"));
	return enrolments;

    }

}
