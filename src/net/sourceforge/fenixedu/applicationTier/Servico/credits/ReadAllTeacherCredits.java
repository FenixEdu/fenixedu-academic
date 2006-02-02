/**
 * Dec 7, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.credits;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.credits.CreditLineDTO;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.teacher.Category;
import net.sourceforge.fenixedu.domain.teacher.TeacherService;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.util.PeriodState;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class ReadAllTeacherCredits extends Service {

    private static final Integer POINT_ZERO_EP_ID = 1;

    public List<CreditLineDTO> run(Integer teacherID) throws ExcepcaoPersistencia {

        Teacher teacher = (Teacher) persistentObject.readByOID(Teacher.class,
                teacherID);

        List<ExecutionPeriod> allExecutionPeriods = (List<ExecutionPeriod>) persistentSupport
                .getIPersistentExecutionPeriod().readAll(ExecutionPeriod.class);
        ExecutionPeriod executionPeriod = (ExecutionPeriod) CollectionUtils.find(allExecutionPeriods,
                new Predicate() {
                    public boolean evaluate(Object arg0) {
                        ExecutionPeriod executionPeriod = (ExecutionPeriod) arg0;
                        return executionPeriod.getIdInternal().equals(POINT_ZERO_EP_ID);
                    }
                });

        List<Category> categories = persistentSupport.getIPersistentCategory().readAll();
        List<Category> monitorCategories = (List<Category>) CollectionUtils.select(categories, new Predicate(){
            public boolean evaluate(Object object) {
                Category category = (Category) object;
                return category.getCode().equals("MNL") || category.getCode().equals("MNT");
            }});
        
        ExecutionPeriod tempExecutionPeriod = executionPeriod;
        List<CreditLineDTO> creditLines = new ArrayList<CreditLineDTO>();
        while (tempExecutionPeriod.getNextExecutionPeriod() != null) {
            double managementCredits = teacher.getManagementFunctionsCredits(tempExecutionPeriod);
            double serviceExemptionsCredits = teacher.getServiceExemptionCredits(tempExecutionPeriod);
            int mandatoryLessonHours = 0;
            Category category = teacher.getCategoryForCreditsByPeriod(tempExecutionPeriod);
            if(!monitorCategories.contains(category)){
                mandatoryLessonHours = teacher.getMandatoryLessonHours(tempExecutionPeriod);
            }                      
            TeacherService teacherService = teacher.getTeacherServiceByExecutionPeriod(tempExecutionPeriod);
            CreditLineDTO creditLineDTO = new CreditLineDTO(tempExecutionPeriod, teacherService,
                    managementCredits, serviceExemptionsCredits, mandatoryLessonHours);
            creditLines.add(creditLineDTO);
            if (tempExecutionPeriod.getState().equals(PeriodState.CURRENT)) {
                break;
            }
            tempExecutionPeriod = tempExecutionPeriod.getNextExecutionPeriod();
        }
        return creditLines;
    }
}
