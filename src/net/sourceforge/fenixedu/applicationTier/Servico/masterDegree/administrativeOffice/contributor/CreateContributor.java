/*
 * Created on 21/Mar/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.contributor;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import net.sourceforge.fenixedu.dataTransferObject.InfoContributor;
import net.sourceforge.fenixedu.domain.Contributor;
import net.sourceforge.fenixedu.domain.IContributor;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentContributor;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import net.sourceforge.fenixedu.persistenceTier.exceptions.ExistingPersistentException;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class CreateContributor implements IService {

    /**
     * The actor of this class.
     */
    public CreateContributor() {
    }

    public void run(InfoContributor newContributor) throws Exception {

        IContributor contributor = new Contributor();

        ISuportePersistente sp = null;
        try {
            sp = SuportePersistenteOJB.getInstance();
            IPersistentContributor persistentContributor = sp.getIPersistentContributor();

            contributor = persistentContributor.readByContributorNumber(newContributor
                    .getContributorNumber());

            if (contributor != null) {
                throw new ExistingServiceException();
            }
            //CLONER
            //contributor =
            // Cloner.copyInfoContributor2IContributor(newContributor);
            contributor = InfoContributor.newDomainFromInfo(newContributor);

            sp.getIPersistentContributor().simpleLockWrite(contributor);
        } catch (ExistingPersistentException ex) {
            throw new ExistingServiceException(ex);
        } catch (ExcepcaoPersistencia ex) {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx;
        }
    }

}