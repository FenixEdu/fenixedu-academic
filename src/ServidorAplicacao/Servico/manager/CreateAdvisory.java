/*
 * Created on 2003/09/06
 * 
 */
package ServidorAplicacao.Servico.manager;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoAdvisory;
import DataBeans.util.Cloner;
import Dominio.IAdvisory;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.AdvisoryRecipients;

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