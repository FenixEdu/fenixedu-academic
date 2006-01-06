/**
 * Dec 7, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.credits;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.credits.CreditLineDTO;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.teacher.ICategory;
import net.sourceforge.fenixedu.domain.teacher.ITeacherService;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.PeriodState;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class ReadAllTeacherCredits implements IService {

    private static final Integer POINT_ZERO_EP_ID = 1;

    public List<CreditLineDTO> run(Integer teacherID) throws ExcepcaoPersistencia {

        ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        ITeacher teacher = (ITeacher) persistentSupport.getIPersistentTeacher().readByOID(Teacher.class,
                teacherID);

        List<IExecutionPeriod> allExecutionPeriods = (List<IExecutionPeriod>) persistentSupport
                .getIPersistentExecutionPeriod().readAll(ExecutionPeriod.class);
        IExecutionPeriod executionPeriod = (IExecutionPeriod) CollectionUtils.find(allExecutionPeriods,
                new Predicate() {
                    public boolean evaluate(Object arg0) {
                        IExecutionPeriod executionPeriod = (IExecutionPeriod) arg0;
                        return executionPeriod.getIdInternal().equals(POINT_ZERO_EP_ID);
                    }
                });

        List<ICategory> categories = persistentSupport.getIPersistentCategory().readAll();
        List<ICategory> monitorCategories = (List<ICategory>) CollectionUtils.select(categories, new Predicate(){
            public boolean evaluate(Object object) {
                ICategory category = (ICategory) object;
                return category.getCode().equals("MNL") || category.getCode().equals("MNT");
            }});
        
        IExecutionPeriod tempExecutionPeriod = executionPeriod;
        List<CreditLineDTO> creditLines = new ArrayList<CreditLineDTO>();
        while (tempExecutionPeriod.getNextExecutionPeriod() != null) {
            double managementCredits = teacher.getManagementFunctionsCredits(tempExecutionPeriod);
            double serviceExemptionsCredits = teacher.getServiceExemptionCredits(tempExecutionPeriod);
            int mandatoryLessonHours = 0;
            ICategory category = teacher.getCategoryByPeriod(tempExecutionPeriod.getBeginDate(),
                    tempExecutionPeriod.getEndDate());
            if(!monitorCategories.contains(category)){
                mandatoryLessonHours = teacher.getHoursByCategory(tempExecutionPeriod.getBeginDate(),
                        tempExecutionPeriod.getEndDate());
            }                      
            ITeacherService teacherService = teacher.getTeacherServiceByExecutionPeriod(tempExecutionPeriod);
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
