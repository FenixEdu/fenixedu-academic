package ServidorAplicacao.Servico.sop;

/**
 * Servi�o LerTurno
 * 
 * @author tfc130
 * @version
 */

import DataBeans.InfoShift;
import DataBeans.ShiftKey;
import DataBeans.util.Cloner;
import Dominio.IExecutionCourse;
import Dominio.IShift;
import ServidorAplicacao.IServico;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class LerTurno implements IServico {

    private static LerTurno _servico = new LerTurno();

    /**
     * The singleton access method of this class.
     */
    public static LerTurno getService() {
        return _servico;
    }

    /**
     * The actor of this class.
     */
    private LerTurno() {
    }

    /**
     * Devolve o nome do servico
     */
    public final String getNome() {
        return "LerTurno";
    }

    public Object run(ShiftKey keyTurno) {

        InfoShift infoTurno = null;

        try {

            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

            IExecutionCourse executionCourse = Cloner.copyInfoExecutionCourse2ExecutionCourse(keyTurno
                    .getInfoExecutionCourse());

            IShift turno = sp.getITurnoPersistente().readByNameAndExecutionCourse(
                    keyTurno.getShiftName(), executionCourse);

            if (turno != null) {
                infoTurno = (InfoShift) Cloner.get(turno);
            }
        } catch (ExcepcaoPersistencia ex) {
            ex.printStackTrace();
        }
        return infoTurno;
    }

}