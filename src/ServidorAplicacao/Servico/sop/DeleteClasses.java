/*
 *
 * Created on 2003/08/15
 */

package ServidorAplicacao.Servico.sop;

/**
 * 
 * @author Luis Cruz & Sara Ribeiro
 */
import java.util.List;

import Dominio.ITurma;
import Dominio.ITurno;
import Dominio.Turma;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class DeleteClasses implements IServico {

    private static DeleteClasses _servico = new DeleteClasses();

    /**
     * The singleton access method of this class.
     */
    public static DeleteClasses getService() {
        return _servico;
    }

    /**
     * The actor of this class.
     */
    private DeleteClasses() {
    }

    /**
     * Devolve o nome do servico
     */
    public final String getNome() {
        return "DeleteClasses";
    }

    public Object run(List classOIDs) throws FenixServiceException {

        boolean result = false;

        try {

            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

            for (int j = 0; j < classOIDs.size(); j++) {
                ITurma schoolClass = (ITurma) sp.getITurmaPersistente().readByOID(Turma.class,
                        (Integer) classOIDs.get(j));

                for (int i = 0; i < schoolClass.getAssociatedShifts().size(); i++) {
                    ITurno shift = (ITurno) schoolClass.getAssociatedShifts().get(i);
                    sp.getITurnoPersistente().simpleLockWrite(shift);
                    shift.getAssociatedClasses().remove(schoolClass);
                }

                sp.getITurmaPersistente().delete(schoolClass);
            }

            result = true;
        } catch (ExcepcaoPersistencia ex) {
            throw new FenixServiceException("Error deleting class");
        }

        return new Boolean(result);

    }

}