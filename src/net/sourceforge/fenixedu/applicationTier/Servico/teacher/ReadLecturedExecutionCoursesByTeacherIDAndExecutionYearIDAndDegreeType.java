package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author naat
 */
public class ReadLecturedExecutionCoursesByTeacherIDAndExecutionYearIDAndDegreeType implements IService {

    public List<ExecutionCourse> run(Integer teacherID, Integer executionYearID, DegreeType degreeType)
            throws ExcepcaoPersistencia, FenixServiceException {

        ISuportePersistente persistenceSupport = PersistenceSupportFactory
                .getDefaultPersistenceSupport();

        IPersistentExecutionYear persistentExecutionYear = persistenceSupport
                .getIPersistentExecutionYear();
        IPersistentTeacher persistentTeacher = persistenceSupport.getIPersistentTeacher();

        Teacher teacher = (Teacher) persistentTeacher.readByOID(Teacher.class, teacherID);

        List<ExecutionCourse> lecturedExecutionCourses;

        if (executionYearID == null) {
            lecturedExecutionCourses = teacher.getAllLecturedExecutionCourses();

        } else {
            ExecutionYear executionYear = (ExecutionYear) persistentExecutionYear.readByOID(
                    ExecutionYear.class, executionYearID);
            lecturedExecutionCourses = teacher.getLecturedExecutionCoursesByExecutionYear(executionYear);
        }

        List<ExecutionCourse> result;

        if (degreeType == DegreeType.DEGREE) {
            result = filterExecutionCourses(lecturedExecutionCourses, false);
        } else {
            // master degree
            result = filterExecutionCourses(lecturedExecutionCourses, true);
        }

        return result;

    }

    private List<ExecutionCourse> filterExecutionCourses(List<ExecutionCourse> executionCourses,
            boolean masterDegreeOnly) {
        List<ExecutionCourse> masterDegreeExecutionCourses = new ArrayList<ExecutionCourse>();

        for (ExecutionCourse executionCourse : executionCourses) {
            if (executionCourse.isMasterDegreeOnly() == masterDegreeOnly) {
                masterDegreeExecutionCourses.add(executionCourse);
            }
        }

        return masterDegreeExecutionCourses;
    }

}