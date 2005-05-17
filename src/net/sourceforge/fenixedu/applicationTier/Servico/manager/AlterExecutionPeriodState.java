/*
 * Created on 2003/07/25
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.PeriodState;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Luis Crus & Sara Ribeiro
 */

public class AlterExecutionPeriodState implements IService {

    /**
     * The actor of this class.
     */
    public AlterExecutionPeriodState() {
    }

    public Boolean run(InfoExecutionPeriod infoExecutionPeriod, PeriodState periodState)
            throws FenixServiceException {

        Boolean result = new Boolean(false);

        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentExecutionPeriod executionPeriodDAO = sp.getIPersistentExecutionPeriod();
            IPersistentExecutionYear executionYearDAO = sp.getIPersistentExecutionYear();

            IExecutionPeriod executionPeriod = executionPeriodDAO.readBySemesterAndExecutionYear(
                    infoExecutionPeriod.getSemester(), executionYearDAO
                            .readExecutionYearByName(infoExecutionPeriod.getInfoExecutionYear()
                                    .getYear()).getYear());

            if (executionPeriod == null) {
                throw new InvalidExecutionPeriod();
            }

            if (periodState.getStateCode().equals(PeriodState.CURRENT.getStateCode())) {
                // Deactivate the current
                IExecutionPeriod currentExecutionPeriod = executionPeriodDAO.readActualExecutionPeriod();

                executionPeriodDAO.simpleLockWrite(currentExecutionPeriod);
                currentExecutionPeriod.setState(new PeriodState(PeriodState.OPEN));

            }

            executionPeriodDAO.simpleLockWrite(executionPeriod);
            executionPeriod.setState(periodState);

        } catch (ExcepcaoPersistencia ex) {
            throw new FenixServiceException(ex.getMessage());
        }

        return result;
    }

    public class InvalidExecutionPeriod extends FenixServiceException {

        InvalidExecutionPeriod() {
            super();
        }

    }

}