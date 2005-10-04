package net.sourceforge.fenixedu.applicationTier.Servico.departmentMember;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionYear;
import net.sourceforge.fenixedu.domain.ITeacher;
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

    public List<IExecutionCourse> run(Integer teacherID, Integer executionYearID, DegreeType degreeType)
            throws ExcepcaoPersistencia, FenixServiceException {

        ISuportePersistente persistenceSupport = PersistenceSupportFactory
                .getDefaultPersistenceSupport();

        IPersistentExecutionYear persistentExecutionYear = persistenceSupport
                .getIPersistentExecutionYear();
        IPersistentTeacher persistentTeacher = persistenceSupport.getIPersistentTeacher();

        ITeacher teacher = (ITeacher) persistentTeacher.readByOID(Teacher.class, teacherID);
        IExecutionYear executionYear = (IExecutionYear) persistentExecutionYear.readByOID(
                ExecutionYear.class, executionYearID);

        List<IExecutionCourse> lecturedExecutionCourses = teacher
                .getLecturedExecutionCoursesByExecutionYear(executionYear);

        List<IExecutionCourse> result;

        if (degreeType == DegreeType.DEGREE) {
            result = filterExecutionCourses(lecturedExecutionCourses, false);
        } else {
            // master degree
            result = filterExecutionCourses(lecturedExecutionCourses, true);
        }

        return result;

    }

    private List<IExecutionCourse> filterExecutionCourses(List<IExecutionCourse> executionCourses,
            boolean masterDegreeOnly) {
        List<IExecutionCourse> masterDegreeExecutionCourses = new ArrayList<IExecutionCourse>();

        for (IExecutionCourse executionCourse : executionCourses) {
            if (executionCourse.isMasterDegreeOnly() == masterDegreeOnly) {
                masterDegreeExecutionCourses.add(executionCourse);
            }
        }

        return masterDegreeExecutionCourses;
    }

}