/*
 * Created on 14/Ago/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.IExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author lmac1
 */

public class ReadAllExecutionYears implements IService {

    /**
     * The constructor of this class.
     */
    public ReadAllExecutionYears() {
    }

    /**
     * Executes the service. Returns the current collection of infoTeachers.
     */
    public List run() throws FenixServiceException {
        ISuportePersistente sp;
        List allExecutionYears = null;

        try {
            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            allExecutionYears = (List)sp.getIPersistentExecutionYear().readAll(ExecutionYear.class);
        } catch (ExcepcaoPersistencia excepcaoPersistencia) {
            throw new FenixServiceException(excepcaoPersistencia);
        }

        if (allExecutionYears == null || allExecutionYears.isEmpty())
            return allExecutionYears;

        // build the result of this service
        Iterator iterator = allExecutionYears.iterator();
        List result = new ArrayList(allExecutionYears.size());

        while (iterator.hasNext())
            result.add(Cloner.get((IExecutionYear) iterator.next()));

        return result;
    }
}