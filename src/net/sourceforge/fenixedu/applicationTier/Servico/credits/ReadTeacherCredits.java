package net.sourceforge.fenixedu.applicationTier.Servico.credits;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriodWithInfoExecutionYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacherWithPerson;
import net.sourceforge.fenixedu.dataTransferObject.credits.InfoCredits;
import net.sourceforge.fenixedu.dataTransferObject.credits.InfoCreditsWithTeacherAndExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadTeacherCredits extends Service {

    public List run(Integer teacherOID) throws FenixServiceException, ExcepcaoPersistencia {

        final Teacher teacher = rootDomainObject.readTeacherByOID(teacherOID);
        final ExecutionPeriod startExecutionPeriod = rootDomainObject.readExecutionPeriodByOID(Integer.valueOf(2));
        ExecutionPeriod executionPeriod = ExecutionPeriod.readActualExecutionPeriod();

        InfoTeacherWithPerson infoTeacher = new InfoTeacherWithPerson();
        infoTeacher.copyFromDomain(teacher);

        List list = new ArrayList();
        ExecutionPeriod executionPeriod2 = null;
        do {
            InfoExecutionPeriodWithInfoExecutionYear infoExecutionPeriod = new InfoExecutionPeriodWithInfoExecutionYear();
            infoExecutionPeriod.copyFromDomain(executionPeriod);

            InfoCredits infoCredits = teacher.getExecutionPeriodCredits(executionPeriod);
            InfoCreditsWithTeacherAndExecutionPeriod infoCreditsWrapper = new InfoCreditsWithTeacherAndExecutionPeriod(
                    infoCredits);
            infoCreditsWrapper.setInfoExecutionPeriod(infoExecutionPeriod);
            infoCreditsWrapper.setInfoTeacher(infoTeacher);
            list.add(infoCreditsWrapper);
            executionPeriod2 = executionPeriod;
            executionPeriod = executionPeriod.getPreviousExecutionPeriod();
        } while (!startExecutionPeriod.equals(executionPeriod2));
        return list;
    }

}