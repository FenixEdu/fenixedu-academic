package ServidorAplicacao.Servico.teacher;

import Dominio.CurricularCourse;
import Dominio.ICurricularCourse;
import Dominio.ICurso;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Tânia Pousão Create on 3/Dez/2003
 */
public class CourseOfTheExpectedDegree implements IServico
{
    private static CourseOfTheExpectedDegree service = new CourseOfTheExpectedDegree();

    public static CourseOfTheExpectedDegree getService()
    {
        return service;
    }

    public CourseOfTheExpectedDegree()
    {

    }

    public final String getNome()
    {
        return "CourseOfTheExpectedDegree";
    }

    public Boolean run(Integer curricularCourseCode) throws FenixServiceException
    {
		System.out.println("CourseOfTheExpectedDegree Service");
        boolean result = false;
        
        try
        {
            result =
                CurricularCourseAeroDegree(curricularCourseCode)
                    && CurricularCourseNotBasic(curricularCourseCode);
            System.out.println("CourseOfTheExpectedDegree: result = " + result);

        } catch (Exception e)
        {
            e.printStackTrace();
            throw new FenixServiceException(e);
        }
        
        return new Boolean(result);
    }

    /**
	 * @param argumentos
	 * @return
	 */
    private boolean CurricularCourseAeroDegree(Integer curricularCourseCode) throws FenixServiceException
    {
        boolean result = false;

        ICurricularCourse curricularCourse = null;
        ICurso degree = null;

        ISuportePersistente sp;
        try
        {

            sp = SuportePersistenteOJB.getInstance();
            IPersistentCurricularCourse persistentCurricularCourse = sp.getIPersistentCurricularCourse();

            curricularCourse =
                (ICurricularCourse) persistentCurricularCourse.readByOId(
                    new CurricularCourse(curricularCourseCode),
                    false);

            degree = curricularCourse.getDegreeCurricularPlan().getDegree();

            result = degree.getSigla().equals("LEA");
        } catch (Exception e)
        {
			throw new FenixServiceException(e);
        }
        return result; //codigo do curso de Aeroespacial
    }

    /**
	 * @param argumentos
	 * @return
	 */
    private boolean CurricularCourseNotBasic(Integer curricularCourseCode) throws FenixServiceException
    {
        boolean result = false;
        ICurricularCourse curricularCourse = null;

        ISuportePersistente sp;
        try
        {

            sp = SuportePersistenteOJB.getInstance();
            IPersistentCurricularCourse persistentCurricularCourse = sp.getIPersistentCurricularCourse();
            curricularCourse =
                (ICurricularCourse) persistentCurricularCourse.readByOId(
                    new CurricularCourse(curricularCourseCode),
                    false);
            result = curricularCourse.getBasic().equals(Boolean.FALSE);
        } catch (Exception e)
        {
			throw new FenixServiceException(e);
        }
        return result;
    }
}
