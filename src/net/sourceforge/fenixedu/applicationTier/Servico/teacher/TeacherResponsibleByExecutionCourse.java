package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IResponsibleFor;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.applicationTier.IServico;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentResponsibleFor;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;

/**
 * @author Tânia Pousão Create on 18/Dez/2003
 */
public class TeacherResponsibleByExecutionCourse implements IServico {
    private static TeacherResponsibleByExecutionCourse service = new TeacherResponsibleByExecutionCourse();

    public static TeacherResponsibleByExecutionCourse getService() {
        return service;
    }

    public TeacherResponsibleByExecutionCourse() {

    }

    public final String getNome() {
        return "TeacherResponsibleByExecutionCourse";
    }

    public Boolean run(String teacherUserName, Integer executionCourseCode, Integer curricularCourseCode)
            throws FenixServiceException {
        boolean result = false;

        try {
            result = ExecutionCourseResponsibleTeacher(teacherUserName, executionCourseCode)
                    && CurricularCourseNotBasic(curricularCourseCode);

        } catch (Exception e) {
            e.printStackTrace();
            throw new FenixServiceException(e);
        }

        return new Boolean(result);
    }

    /**
     * @param argumentos
     * @return
     */
    private boolean ExecutionCourseResponsibleTeacher(String teacherUserName, Integer executionCourseCode)
            throws FenixServiceException {
        boolean result = false;

        ITeacher teacher = null;
        IResponsibleFor responsibleFor = null;
        IExecutionCourse executionCourse = null;

        ISuportePersistente sp;
        try {
            sp = SuportePersistenteOJB.getInstance();

            IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();
            teacher = persistentTeacher.readTeacherByUsername(teacherUserName);

            IPersistentExecutionCourse persistentExecutionCourse = sp.getIPersistentExecutionCourse();
            executionCourse = (IExecutionCourse) persistentExecutionCourse.readByOID(
                    ExecutionCourse.class, executionCourseCode);

            IPersistentResponsibleFor persistentResponsibleFor = sp.getIPersistentResponsibleFor();
            responsibleFor = persistentResponsibleFor.readByTeacherAndExecutionCoursePB(teacher,
                    executionCourse);
            if (responsibleFor == null) {
                result = false;
            } else {
                result = true;
            }
        } catch (Exception e) {
            throw new FenixServiceException(e);
        }
        return result;
    }

    /**
     * @param argumentos
     * @return
     */
    private boolean CurricularCourseNotBasic(Integer curricularCourseCode) throws FenixServiceException {
        boolean result = false;
        ICurricularCourse curricularCourse = null;

        ISuportePersistente sp;
        try {

            sp = SuportePersistenteOJB.getInstance();
            IPersistentCurricularCourse persistentCurricularCourse = sp.getIPersistentCurricularCourse();
            curricularCourse = (ICurricularCourse) persistentCurricularCourse.readByOID(
                    CurricularCourse.class, curricularCourseCode);
            result = curricularCourse.getBasic().equals(Boolean.FALSE);
        } catch (Exception e) {
            throw new FenixServiceException(e);
        }
        return result;
    }
}