package ServidorAplicacao.Servico.student;

import java.util.ArrayList;
import java.util.List;

import DataBeans.InfoExecutionCourse;
import DataBeans.util.Cloner;
import Dominio.IExecutionCourse;
import Dominio.IFrequenta;
import Dominio.IStudent;
import ServidorAplicacao.IServico;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.TipoCurso;

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

    public Object run(Integer number, TipoCurso degreeType) {
        List disciplines = new ArrayList();
        List courses = new ArrayList();
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IStudent student = sp.getIPersistentStudent().readStudentByNumberAndDegreeType(number,
                    degreeType);

            if (student != null) {
                List frequencies = sp.getIFrequentaPersistente()
                        .readByStudentNumberInCurrentExecutionPeriod(number);
                for (int i = 0; i < frequencies.size(); i++) {
                    IFrequenta frequent = (IFrequenta) frequencies.get(i);
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