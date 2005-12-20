/**
 * Dec 20, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.department;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.credits.CreditLineDTO;
import net.sourceforge.fenixedu.dataTransferObject.credits.TeacherWithCreditsDTO;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.IDepartment;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.teacher.ICategory;
import net.sourceforge.fenixedu.domain.teacher.ITeacherService;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class ReadDepartmentTeachersCreditsByExecutionPeriod implements IService {

    public List<TeacherWithCreditsDTO> run(Integer executionPeriodID, IPerson user) throws ExcepcaoPersistencia {

        ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IExecutionPeriod executionPeriod = (IExecutionPeriod) persistentSupport
                .getIPersistentExecutionPeriod().readByOID(ExecutionPeriod.class, executionPeriodID);

        List<TeacherWithCreditsDTO> teachersCredits = new ArrayList<TeacherWithCreditsDTO>();
        for (IDepartment department : user.getManageableDepartmentCredits()) {
            List<ITeacher> teachers = department.getTeachers(executionPeriod.getBeginDate(),
                    executionPeriod.getEndDate());
            for (ITeacher teacher : teachers) {
                double managementCredits = teacher.getManagementFunctionsCredits(executionPeriod);
                double serviceExemptionsCredits = teacher.getServiceExemptionCredits(executionPeriod);
                int mandatoryLessonHours = teacher.getHoursByCategory(executionPeriod.getBeginDate(), executionPeriod.getEndDate());
                ITeacherService teacherService = teacher.getTeacherServiceByExecutionPeriod(executionPeriod);
                CreditLineDTO creditLineDTO = new CreditLineDTO(executionPeriod, teacherService,
                        managementCredits, serviceExemptionsCredits, mandatoryLessonHours);
                ICategory category = teacher.getCategoryByPeriod(executionPeriod.getBeginDate(),executionPeriod.getEndDate());
                TeacherWithCreditsDTO teacherWithCreditsDTO = new TeacherWithCreditsDTO(teacher,category,creditLineDTO);
                teachersCredits.add(teacherWithCreditsDTO);
            }
        }
        return teachersCredits;
    }
}
