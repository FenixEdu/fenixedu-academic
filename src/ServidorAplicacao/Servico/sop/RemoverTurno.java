package ServidorAplicacao.Servico.sop;

/**
 * Serviço RemoverTurno
 * 
 * @author tfc130
 * @version
 */

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoClass;
import DataBeans.InfoShift;
import DataBeans.util.Cloner;
import Dominio.ITurma;
import Dominio.ITurmaTurno;
import Dominio.ITurno;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class RemoverTurno implements IService {

    public RemoverTurno() {
    }

    public Object run(InfoShift infoShift, InfoClass infoClass) {

        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

            ITurno shift = Cloner.copyInfoShift2IShift(infoShift);
            ITurma classTemp = Cloner.copyInfoClass2Class(infoClass);

            // Read From Database

            ITurno shiftToDelete = sp.getITurnoPersistente().readByNameAndExecutionCourse(
                    shift.getNome(), shift.getDisciplinaExecucao());
            ITurma classToDelete = sp.getITurmaPersistente()
                    .readByNameAndExecutionDegreeAndExecutionPeriod(classTemp.getNome(),
                            classTemp.getExecutionDegree(), classTemp.getExecutionPeriod());
            ITurmaTurno turmaTurnoToDelete = null;
            if ((shiftToDelete != null) && (classToDelete != null)) {
                turmaTurnoToDelete = sp.getITurmaTurnoPersistente().readByTurmaAndTurno(classToDelete,
                        shiftToDelete);
            } else
                return Boolean.FALSE;

            // Check if exists
            if (turmaTurnoToDelete != null)
                sp.getITurmaTurnoPersistente().delete(turmaTurnoToDelete);
            else
                return Boolean.FALSE;

        } catch (ExcepcaoPersistencia ex) {
            ex.printStackTrace();
        }
        return Boolean.TRUE;
    }

}