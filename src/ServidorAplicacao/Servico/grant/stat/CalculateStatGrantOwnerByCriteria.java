/*
 * Created on Jul 6, 2004
 *
 */
package ServidorAplicacao.Servico.grant.stat;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.grant.stat.InfoStatGrantOwner;
import Dominio.grant.contract.GrantType;
import Dominio.grant.contract.IGrantType;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.grant.IPersistentGrantContract;
import ServidorPersistente.grant.IPersistentGrantOwner;
import ServidorPersistente.grant.IPersistentGrantType;

/**
 * @author Pica
 * @author Barbosa
 */
public class CalculateStatGrantOwnerByCriteria implements IService {

    public CalculateStatGrantOwnerByCriteria() {
    }

    public Object[] run(InfoStatGrantOwner infoStatGrantOwner) throws FenixServiceException {
        try {
            ISuportePersistente suportePersistente = SuportePersistenteOJB.getInstance();
            IPersistentGrantContract persistentGrantContract = suportePersistente
                    .getIPersistentGrantContract();
            IPersistentGrantOwner persistentGrantOwner = suportePersistente.getIPersistentGrantOwner();

            Integer totalNumberOfGrantOwners = persistentGrantOwner.countAll();
            Integer numberOfGrantOwnersByCriteria = persistentGrantContract.countAllByCriteria(
                    infoStatGrantOwner.getJustActiveContracts(), infoStatGrantOwner
                            .getJustInactiveContracts(), infoStatGrantOwner.getDateBeginContract(),
                    infoStatGrantOwner.getDateEndContract(), infoStatGrantOwner.getGrantType(),
                    "grantOwner.number");

            if(infoStatGrantOwner.getGrantType() != null) {
                //Read the sigla for presentation reasons
                IPersistentGrantType persistentGrantType = suportePersistente.getIPersistentGrantType();
                IGrantType granttype = (IGrantType)persistentGrantType.readByOID(GrantType.class, infoStatGrantOwner.getGrantType());
                infoStatGrantOwner.setGrantTypeSigla(granttype.getSigla()); 
            }
            Object[] result = { totalNumberOfGrantOwners, numberOfGrantOwnersByCriteria , infoStatGrantOwner};
            return result;
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
    }
}