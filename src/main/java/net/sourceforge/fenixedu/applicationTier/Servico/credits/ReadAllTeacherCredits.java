/**
 * Dec 7, 2005
 */
package net.sourceforge.fenixedu.applicationTier.Servico.credits;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.credits.CreditLineDTO;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.TeacherCredits;
import net.sourceforge.fenixedu.domain.teacher.TeacherService;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class ReadAllTeacherCredits {

    @Service
    public static List<CreditLineDTO> run(Integer teacherID) throws ParseException {

        List<CreditLineDTO> creditLines = new ArrayList<CreditLineDTO>();
        final Teacher teacher = RootDomainObject.getInstance().readTeacherByOID(teacherID);

        ExecutionSemester executionSemester = ExecutionSemester.readStartExecutionSemesterForCredits();

        ExecutionSemester lastExecutionSemester = ExecutionSemester.readLastExecutionSemesterForCredits();

        while (executionSemester != null && executionSemester.isBeforeOrEquals(lastExecutionSemester)) {

            creditLines.add(readCreditLineDTO(executionSemester, teacher));

            if (executionSemester.isCurrent()) {
                break;
            }

            executionSemester = executionSemester.getNextExecutionPeriod();
        }

        return creditLines;
    }

    public static CreditLineDTO readCreditLineDTO(ExecutionSemester executionSemester, Teacher teacher) throws ParseException {
        TeacherCredits teacherCredits = TeacherCredits.readTeacherCredits(executionSemester, teacher);
        if (teacherCredits == null || teacherCredits.getTeacherCreditsState().isOpenState()) {
            return calculateCreditLineDTO(executionSemester, teacher);
        } else {
            if (teacherCredits.getTeacherCreditsState().isCloseState()) {
                return getCreditLineDTOFromTeacherCredits(executionSemester, teacherCredits);
            }
        }
        return null;
    }

    public static CreditLineDTO calculateCreditLineDTO(ExecutionSemester executionSemester, Teacher teacher)
            throws ParseException {
        double managementCredits = teacher.getManagementFunctionsCredits(executionSemester);
        double serviceExemptionsCredits = teacher.getServiceExemptionCredits(executionSemester);
        double thesesCredits = teacher.getThesesCredits(executionSemester);
        double mandatoryLessonHours = teacher.getMandatoryLessonHours(executionSemester);
        TeacherService teacherService = teacher.getTeacherServiceByExecutionPeriod(executionSemester);

        return new CreditLineDTO(executionSemester, teacherService, managementCredits, serviceExemptionsCredits,
                mandatoryLessonHours, teacher, thesesCredits);

    }

    private static CreditLineDTO getCreditLineDTOFromTeacherCredits(ExecutionSemester executionSemester,
            TeacherCredits teacherCredits) throws ParseException {
        return new CreditLineDTO(executionSemester, teacherCredits);
    }

}