/*
 * Created on 2003/09/06
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.dataTransferObject.InfoAdvisory;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IAdvisory;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import net.sourceforge.fenixedu.util.AdvisoryRecipients;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Luis Cruz
 */
public class CreateAdvisory implements IService {

    public Boolean run(InfoAdvisory infoAdvisory, AdvisoryRecipients advisoryRecipients)
            throws FenixServiceException {

        Boolean result = new Boolean(false);

        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

            IAdvisory advisory = Cloner.copyInfoAdvisory2IAdvisory(infoAdvisory);

            sp.getIPersistentAdvisory().write(advisory, advisoryRecipients);

            result = new Boolean(true);
        } catch (ExcepcaoPersistencia ex) {
            throw new FenixServiceException(ex.getMessage());
        }

        return result;
    }

}