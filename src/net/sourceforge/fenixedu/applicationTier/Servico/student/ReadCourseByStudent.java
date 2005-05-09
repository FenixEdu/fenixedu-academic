package net.sourceforge.fenixedu.applicationTier.Servico.student;

import net.sourceforge.fenixedu.applicationTier.IServico;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegree;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

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

    public Object run(Integer number, DegreeType degreeType) {

        InfoDegree infoDegree = null;

        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
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