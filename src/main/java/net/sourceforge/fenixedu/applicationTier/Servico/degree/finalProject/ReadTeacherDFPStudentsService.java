/*
 * Created on Nov 24, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.degree.finalProject;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Filtro.credits.CreditsServiceWithInfoTeacherArgumentAuthorization;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.dataTransferObject.degree.finalProject.InfoTeacherDegreeFinalProjectStudent;
import net.sourceforge.fenixedu.dataTransferObject.degree.finalProject.InfoTeacherDegreeFinalProjectStudentWithStudentAndPerson;
import net.sourceforge.fenixedu.dataTransferObject.degree.finalProject.TeacherDegreeFinalProjectStudentsDTO;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.degree.finalProject.TeacherDegreeFinalProjectStudent;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author jpvl
 */
public class ReadTeacherDFPStudentsService {

    protected TeacherDegreeFinalProjectStudentsDTO run(InfoTeacher infoTeacher, Integer executionPeriodId) {
        TeacherDegreeFinalProjectStudentsDTO teacherDfpStudentsDTO = new TeacherDegreeFinalProjectStudentsDTO();

        ExecutionSemester executionSemester = getExecutionPeriod(executionPeriodId);

        Teacher teacher = RootDomainObject.getInstance().readTeacherByOID(infoTeacher.getIdInternal());
        InfoTeacher infoTeacher2 = InfoTeacher.newInfoFromDomain(teacher);

        final Set<TeacherDegreeFinalProjectStudent> teacherDegreeFinalProjectStudents =
                teacher.findTeacherDegreeFinalProjectStudentsByExecutionPeriod(executionSemester);
        final List infoteacherDFPStudentList = new ArrayList();
        for (final TeacherDegreeFinalProjectStudent teacherDegreeFinalProjectStudent : teacherDegreeFinalProjectStudents) {
            final InfoTeacherDegreeFinalProjectStudent infoTeacherDegreeFinalProjectStudent =
                    InfoTeacherDegreeFinalProjectStudentWithStudentAndPerson.newInfoFromDomain(teacherDegreeFinalProjectStudent);
            infoteacherDFPStudentList.add(infoTeacherDegreeFinalProjectStudent);
        }

        teacherDfpStudentsDTO.setInfoTeacher(infoTeacher2);
        InfoExecutionPeriod infoExecutionPeriod = InfoExecutionPeriod.newInfoFromDomain(executionSemester);
        teacherDfpStudentsDTO.setInfoExecutionPeriod(infoExecutionPeriod);
        teacherDfpStudentsDTO.setInfoTeacherDegreeFinalProjectStudentList(infoteacherDFPStudentList);

        return teacherDfpStudentsDTO;

    }

    private ExecutionSemester getExecutionPeriod(Integer executionPeriodId) {

        final ExecutionSemester executionSemester;
        if ((executionPeriodId == null) || (executionPeriodId.intValue() == 0)) {
            executionSemester = ExecutionSemester.readActualExecutionSemester();
        } else {
            executionSemester = RootDomainObject.getInstance().readExecutionSemesterByOID(executionPeriodId);
        }
        return executionSemester;
    }

    // Service Invokers migrated from Berserk

    private static final ReadTeacherDFPStudentsService serviceInstance = new ReadTeacherDFPStudentsService();

    @Service
    public static TeacherDegreeFinalProjectStudentsDTO runReadTeacherDFPStudents(InfoTeacher infoTeacher,
            Integer executionPeriodId) throws FenixServiceException {
        CreditsServiceWithInfoTeacherArgumentAuthorization.instance.execute(infoTeacher);
        return serviceInstance.run(infoTeacher, executionPeriodId);
    }

}