/*
 * Created on 25/01/2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.util.PeriodState;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author joaosa and rmalo
 */
public class ReadOpenExecutionPeriodsByTeacherExecutionCourses extends FenixService {

    @Service
    public static List run(IUserView userView) throws FenixServiceException {

        final List<InfoExecutionPeriod> result = new ArrayList<InfoExecutionPeriod>();
        final Person person = userView.getPerson();
        final List<ExecutionSemester> executionSemesters = new ArrayList<ExecutionSemester>();

        for (final Professorship professorship : person.getProfessorshipsSet()) {
            final ExecutionSemester executionSemester = professorship.getExecutionCourse().getExecutionPeriod();
            final PeriodState periodState = executionSemester.getState();
            if (!executionSemesters.contains(executionSemester)
                    && (periodState.getStateCode().equals("C") || periodState.getStateCode().equals("O"))) {
                executionSemesters.add(executionSemester);
            }
        }

        for (final ExecutionSemester executionSemester : executionSemesters) {
            result.add(InfoExecutionPeriod.newInfoFromDomain(executionSemester));
        }
        return result;
    }

}