package ServidorAplicacao.Servico.student;

import DataBeans.InfoDegree;
import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;
import ServidorAplicacao.IServico;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.TipoCurso;

/**
 * @author Ricardo Nortadas & Rui Figueiredo
 *  
 */

public class ReadCourseByStudent implements IServico {

    private static ReadCourseByStudent _servico = new ReadCourseByStudent();

    /**
     * The singleton access method of this class.
     */
    public static ReadCourseByStudent getService() {
        return _servico;
    }

    /**
     * The actor of this class.
     */
    private ReadCourseByStudent() {
    }

    /**
     * Devolve o nome do servico
     */
    public final String getNome() {
        return "ReadCourseByStudent";
    }

    public Object run(Integer number, TipoCurso degreeType) {

        InfoDegree infoDegree = null;

        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IStudent student = sp.getIPersistentStudent().readStudentByNumberAndDegreeType(number,
                    degreeType);
            if (student != null) {
                IStudentCurricularPlan StudentCurricularPlan = sp.getIStudentCurricularPlanPersistente()
                        .readActiveStudentCurricularPlan(number, degreeType);
                if (StudentCurricularPlan != null) {
                    infoDegree = new InfoDegree(StudentCurricularPlan.getDegreeCurricularPlan()
                            .getDegree().getSigla(), StudentCurricularPlan.getDegreeCurricularPlan()
                            .getDegree().getNome());
                }

            }

        } catch (ExcepcaoPersistencia e) {
            e.printStackTrace();
        }
        return infoDegree;

    }

}