package net.sourceforge.fenixedu.applicationTier.Servico.publico;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IServico;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

/**
 * @author João Mota
 *  
 */
public class SelectShifts implements IServico {

    private static SelectShifts _servico = new SelectShifts();

    /**
     * The actor of this class.
     */

    private SelectShifts() {

    }

    /**
     * Returns Service Name
     */
    public String getNome() {
        return "SelectShifts";
    }

    /**
     * Returns the _servico.
     * 
     * @return SelectShifts
     */
    public static SelectShifts getService() {
        return _servico;
    }

    public Object run(InfoShift infoShift) {

        List shifts = new ArrayList();
        List infoShifts = new ArrayList();

        IShift shift = Cloner.copyInfoShift2IShift(infoShift);

        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            shifts = sp.getITurnoPersistente().readByExecutionCourse(shift.getDisciplinaExecucao().getIdInternal());

            for (int i = 0; i < shifts.size(); i++) {
                IShift taux = (IShift) shifts.get(i);
                infoShifts.add(InfoShift.newInfoFromDomain(taux));
            }
        } catch (ExcepcaoPersistencia e) {
        }

        return infoShifts;
    }

}