package ServidorAplicacao.Servico.student;

import java.util.ArrayList;
import java.util.List;

import DataBeans.InfoExecutionCourse;
import DataBeans.InfoShift;
import DataBeans.util.Cloner;
import Dominio.IExecutionCourse;
import Dominio.ITurno;
import ServidorAplicacao.IServico;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.TipoAula;

/**
 * @author João Mota
 *  
 */
public class ReadShiftsByTypeFromExecutionCourse implements IServico {

    private static ReadShiftsByTypeFromExecutionCourse _servico = new ReadShiftsByTypeFromExecutionCourse();

    /**
     * The singleton access method of this class.
     */
    public static ReadShiftsByTypeFromExecutionCourse getService() {
        return _servico;
    }

    /**
     * The actor of this class.
     */
    private ReadShiftsByTypeFromExecutionCourse() {
    }

    /**
     * 
     * Devolve o nome do servico
     */
    public final String getNome() {
        return "ReadShiftsByTypeFromExecutionCourse";
    }

    public List run(InfoExecutionCourse iDE, TipoAula type) {
        List shifts = new ArrayList();

        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IExecutionCourse executionCourse = Cloner.copyInfoExecutionCourse2ExecutionCourse(iDE);
            List dshifts = sp.getITurnoPersistente().readByExecutionCourseAndType(executionCourse,
                    type.getTipo());

            if (dshifts != null)
                for (int i = 0; i < dshifts.size(); i++) {
                    ITurno dshift = (ITurno) dshifts.get(i);
                    InfoShift shift = (InfoShift) Cloner.get(dshift);
                    shifts.add(shift);
                }
        } catch (ExcepcaoPersistencia e) {
            e.printStackTrace();
        }
        return shifts;

    }

}