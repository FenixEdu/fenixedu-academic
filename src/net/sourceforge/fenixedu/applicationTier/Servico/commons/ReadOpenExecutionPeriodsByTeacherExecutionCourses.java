/*
 * Created on 25/01/2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.util.PeriodState;

/**
 * @author joaosa and rmalo
 */
public class ReadOpenExecutionPeriodsByTeacherExecutionCourses extends Service {

    public List run(IUserView userView) throws FenixServiceException, ExcepcaoPersistencia {

        final List<InfoExecutionPeriod> result = new ArrayList<InfoExecutionPeriod>();
        final Person person = userView.getPerson();
        final Teacher teacher = person != null ? person.getTeacher() : null;
        final List<ExecutionPeriod> executionPeriods = new ArrayList<ExecutionPeriod>();

        for (final Professorship professorship : teacher.getProfessorshipsSet()) {
            final ExecutionPeriod executionPeriod = professorship.getExecutionCourse()
                    .getExecutionPeriod();
            final PeriodState periodState = executionPeriod.getState();
            if (!executionPeriods.contains(executionPeriod)
                    && (periodState.getStateCode().equals("C") || periodState.getStateCode().equals("O"))) {
                executionPeriods.add(executionPeriod);
            }
        }

        for (final ExecutionPeriod executionPeriod : executionPeriods) {
            result.add(InfoExecutionPeriod.newInfoFromDomain(executionPeriod));
        }
        return result;
    }

}
