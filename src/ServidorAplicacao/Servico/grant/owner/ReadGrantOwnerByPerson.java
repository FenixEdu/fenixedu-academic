/*
 * Created on 12/12/2003
 * 
 */
package ServidorAplicacao.Servico.grant.owner;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.grant.owner.InfoGrantOwner;
import DataBeans.util.Cloner;
import Dominio.grant.owner.IGrantOwner;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.grant.IPersistentGrantOwner;

/**
 * @author Barbosa
 * @author Pica
 *  
 */
public class ReadGrantOwnerByPerson implements IService {

    public ReadGrantOwnerByPerson() {
    }

    public InfoGrantOwner run(Integer personId) throws FenixServiceException {
        InfoGrantOwner infoGrantOwner = null;
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentGrantOwner persistentGrantOwner = sp.getIPersistentGrantOwner();
            IGrantOwner grantOwner = persistentGrantOwner.readGrantOwnerByPerson(personId);

            infoGrantOwner = Cloner.copyIGrantOwner2InfoGrantOwner(grantOwner);
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e.getMessage());
        }
        return infoGrantOwner;
    }

}