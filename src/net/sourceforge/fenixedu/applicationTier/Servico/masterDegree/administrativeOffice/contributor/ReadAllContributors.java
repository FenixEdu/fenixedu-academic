/*
 * Created on 14/Mar/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.contributor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IContributor;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ReadAllContributors implements IService {

    public List run() throws FenixServiceException {

        ISuportePersistente sp = null;
        List result = new ArrayList();
        try {
            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

            // Read the contributors

            result = sp.getIPersistentContributor().readAll();
        } catch (ExcepcaoPersistencia ex) {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx;
        }

        List contributors = new ArrayList();
        Iterator iterator = result.iterator();
        while (iterator.hasNext())
            contributors.add(Cloner.copyIContributor2InfoContributor((IContributor) iterator.next()));

        return contributors;

    }
}