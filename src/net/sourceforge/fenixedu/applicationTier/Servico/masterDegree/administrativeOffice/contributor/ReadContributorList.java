/*
 * Created on 14/Mar/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.contributor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoContributor;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IContributor;
import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ReadContributorList implements IService {

    public List run(Integer contributorNumber) throws FenixServiceException {

        ISuportePersistente sp = null;
        List contributors = null;
        try {
            sp = SuportePersistenteOJB.getInstance();

            // Read the contributor

            contributors = sp.getIPersistentContributor().readContributorListByNumber(contributorNumber);
        } catch (ExcepcaoPersistencia ex) {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx;
        }

        if (contributors == null)
            throw new ExcepcaoInexistente("No Contributors Found !!");

        List result = new ArrayList();
        Iterator iterator = contributors.iterator();
        while (iterator.hasNext()) {
            InfoContributor infoContributor = null;
            infoContributor = Cloner.copyIContributor2InfoContributor((IContributor) iterator.next());
            result.add(infoContributor);
        }

        return result;
    }
}