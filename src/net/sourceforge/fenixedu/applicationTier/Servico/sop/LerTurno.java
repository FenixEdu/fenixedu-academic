package net.sourceforge.fenixedu.applicationTier.Servico.sop;

/**
 * Servi�o LerTurno
 * 
 * @author tfc130
 * @version
 */

import net.sourceforge.fenixedu.applicationTier.IServico;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.ShiftKey;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

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

            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

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