/*
 * Created on 14/Mar/2003
 *
 */
package ServidorAplicacao.Servico.masterDegree.administrativeOffice.contributor;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoContributor;
import DataBeans.util.Cloner;
import Dominio.IContributor;
import ServidorAplicacao.Servico.ExcepcaoInexistente;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ReadContributor implements IService {

    public InfoContributor run(Integer contributorNumber) throws FenixServiceException {

        ISuportePersistente sp = null;
        IContributor contributor = null;
        try {
            sp = SuportePersistenteOJB.getInstance();

            // Read the contributor

            contributor = sp.getIPersistentContributor().readByContributorNumber(contributorNumber);
        } catch (ExcepcaoPersistencia ex) {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx;
        }

        if (contributor == null)
            throw new ExcepcaoInexistente("Unknown Contributor !!");

        return Cloner.copyIContributor2InfoContributor(contributor);
    }
}