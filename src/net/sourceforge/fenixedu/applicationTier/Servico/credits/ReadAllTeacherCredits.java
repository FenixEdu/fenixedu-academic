/**
 * Dec 7, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.credits;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.credits.CreditLineDTO;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.teacher.TeacherService;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.util.PeriodState;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class ReadAllTeacherCredits extends Service {

    public List<CreditLineDTO> run(Integer teacherID) throws ExcepcaoPersistencia, ParseException {

        final Teacher teacher = rootDomainObject.readTeacherByOID(teacherID);        
        ExecutionPeriod executionPeriod = TeacherService.getStartExecutionPeriodForCredits();        
        
        ExecutionPeriod tempExecutionPeriod = executionPeriod;
        List<CreditLineDTO> creditLines = new ArrayList<CreditLineDTO>();
        while (tempExecutionPeriod.getNextExecutionPeriod() != null) {
            double managementCredits = teacher.getManagementFunctionsCredits(tempExecutionPeriod);
            double serviceExemptionsCredits = teacher.getServiceExemptionCredits(tempExecutionPeriod);
            int mandatoryLessonHours = teacher.getMandatoryLessonHours(tempExecutionPeriod);                               
            TeacherService teacherService = teacher.getTeacherServiceByExecutionPeriod(tempExecutionPeriod);
            CreditLineDTO creditLineDTO = new CreditLineDTO(tempExecutionPeriod, teacherService,
                    managementCredits, serviceExemptionsCredits, mandatoryLessonHours, teacher);
            creditLines.add(creditLineDTO);
            if (tempExecutionPeriod.getState().equals(PeriodState.CURRENT)) {
                break;
            }
            tempExecutionPeriod = tempExecutionPeriod.getNextExecutionPeriod();
        }
        return creditLines;
    }
}
