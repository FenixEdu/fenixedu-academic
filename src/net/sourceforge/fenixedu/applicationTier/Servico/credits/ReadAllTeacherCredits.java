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

/**
 * @author Ricardo Rodrigues
 * 
 */

public class ReadAllTeacherCredits extends Service {

    public List<CreditLineDTO> run(Integer teacherID) throws ExcepcaoPersistencia, ParseException {

	List<CreditLineDTO> creditLines = new ArrayList<CreditLineDTO>();
        final Teacher teacher = rootDomainObject.readTeacherByOID(teacherID);
        
        ExecutionPeriod executionPeriod = TeacherService.getStartExecutionPeriodForCredits();                              
                
        while (executionPeriod != null) {
                        
            double managementCredits = teacher.getManagementFunctionsCredits(executionPeriod);
            double serviceExemptionsCredits = teacher.getServiceExemptionCredits(executionPeriod);
            double thesesCredits = teacher.getThesesCredits(executionPeriod);
            int mandatoryLessonHours = teacher.getMandatoryLessonHours(executionPeriod);                               
            TeacherService teacherService = teacher.getTeacherServiceByExecutionPeriod(executionPeriod);
            
            CreditLineDTO creditLineDTO = new CreditLineDTO(executionPeriod, teacherService, managementCredits, serviceExemptionsCredits, mandatoryLessonHours, teacher, thesesCredits);
            creditLines.add(creditLineDTO);
            
            if (executionPeriod.isCurrent()) {
                break;
            }
                        
            executionPeriod = executionPeriod.getNextExecutionPeriod();
        }
        
        return creditLines;
    }
}
