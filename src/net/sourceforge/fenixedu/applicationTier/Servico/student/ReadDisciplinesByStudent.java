package net.sourceforge.fenixedu.applicationTier.Servico.student;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IServico;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IAttends;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

/**
 * @author Ricardo Nortadas & Rui Figueiredo
 *  
 */

public class ReadDisciplinesByStudent implements IServico {

    private static ReadDisciplinesByStudent _servico = new ReadDisciplinesByStudent();

    /**
     * The singleton access method of this class.
     */
    public static ReadDisciplinesByStudent getService() {
        return _servico;
    }

    /**
     * The actor of this class.
     */
    private ReadDisciplinesByStudent() {
    }

    /**
     * Devolve o nome do servico
     */
    public final String getNome() {
        return "ReadDisciplinesByStudent";
    }

    public Object run(Integer number, DegreeType degreeType) {
        List disciplines = new ArrayList();
        List courses = new ArrayList();
        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IStudent student = sp.getIPersistentStudent().readStudentByNumberAndDegreeType(number,
                    degreeType);

            if (student != null) {
                List frequencies = sp.getIFrequentaPersistente()
                        .readByStudentNumberInCurrentExecutionPeriod(number);
                for (int i = 0; i < frequencies.size(); i++) {
                    IAttends frequent = (IAttends) frequencies.get(i);
                    IExecutionCourse executionCourse = frequent.getDisciplinaExecucao();

                    disciplines.add(executionCourse);

                }
            }
            if (disciplines != null)
                for (int i = 0; i < disciplines.size(); i++) {
                    IExecutionCourse executionCourse = (IExecutionCourse) disciplines.get(i);
                    InfoExecutionCourse infoExecutionCourse = (InfoExecutionCourse) Cloner
                            .get(executionCourse);
                    courses.add(infoExecutionCourse);
                }

        } catch (ExcepcaoPersistencia e) {
            e.printStackTrace();
        }
        return courses;

    }

}