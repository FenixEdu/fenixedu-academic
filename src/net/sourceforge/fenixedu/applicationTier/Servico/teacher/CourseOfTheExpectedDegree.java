package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.IDegree;
import net.sourceforge.fenixedu.applicationTier.IServico;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularCourse;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;

/**
 * @author Tânia Pousão Create on 3/Dez/2003
 */
public class CourseOfTheExpectedDegree implements IServico {
    private static CourseOfTheExpectedDegree service = new CourseOfTheExpectedDegree();

    public static CourseOfTheExpectedDegree getService() {
        return service;
    }

    public CourseOfTheExpectedDegree() {

    }

    public final String getNome() {
        return "CourseOfTheExpectedDegree";
    }

    public Boolean run(Integer curricularCourseCode, String degreeCode) throws FenixServiceException {
        boolean result = false;

        try {
            result = CurricularCourseDegree(curricularCourseCode, degreeCode)
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
    private boolean CurricularCourseDegree(Integer curricularCourseCode, String degreeCode)
            throws FenixServiceException {
        boolean result = false;

        ICurricularCourse curricularCourse = null;
        IDegree degree = null;

        ISuportePersistente sp;
        try {

            sp = SuportePersistenteOJB.getInstance();
            IPersistentCurricularCourse persistentCurricularCourse = sp.getIPersistentCurricularCourse();

            curricularCourse = (ICurricularCourse) persistentCurricularCourse.readByOID(
                    CurricularCourse.class, curricularCourseCode);

            degree = curricularCourse.getDegreeCurricularPlan().getDegree();

            result = degree.getSigla().equals(degreeCode);
        } catch (Exception e) {
            throw new FenixServiceException(e);
        }
        return result; //codigo do curso de Aeroespacial
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