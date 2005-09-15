package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriodWithInfoExecutionYear;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class ReadPreviousExecutionPeriod implements IService {

    public InfoExecutionPeriod run(final Integer oid) throws ExcepcaoPersistencia {
        final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        final IPersistentObject persistentObject = sp.getIPersistentObject();

        final IExecutionPeriod executionPeriod = (IExecutionPeriod) persistentObject.readByOID(
                ExecutionPeriod.class, oid);
        return (executionPeriod != null) ? InfoExecutionPeriodWithInfoExecutionYear
                .newInfoFromDomain(executionPeriod.getPreviousExecutionPeriod()) : null;
    }

}