/*
 * Created on 14/Ago/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author lmac1
 */

public class ReadAllTeachers implements IService {

    /**
     * Executes the service. Returns the current collection of infoTeachers.
     */
    public List run() throws FenixServiceException {
        ISuportePersistente sp;
        List allTeachers = null;

        try {
            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            allTeachers = sp.getIPersistentTeacher().readAll();
        } catch (ExcepcaoPersistencia excepcaoPersistencia) {
            throw new FenixServiceException(excepcaoPersistencia);
        }

        if (allTeachers == null || allTeachers.isEmpty())
            return allTeachers;

        // build the result of this service
        Iterator iterator = allTeachers.iterator();
        List result = new ArrayList(allTeachers.size());

        while (iterator.hasNext())
            result.add(Cloner.copyITeacher2InfoTeacher((ITeacher) iterator.next()));

        return result;
    }
}