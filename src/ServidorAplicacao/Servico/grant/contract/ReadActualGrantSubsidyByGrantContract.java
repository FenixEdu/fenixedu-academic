/*
 * Created on 22/Jan/2004
 *  
 */
package ServidorAplicacao.Servico.grant.contract;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.grant.contract.InfoGrantSubsidy;
import DataBeans.util.Cloner;
import Dominio.grant.contract.IGrantSubsidy;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.grant.IPersistentGrantSubsidy;

/**
 * @author Barbosa
 * @author Pica
 *  
 */
public class ReadActualGrantSubsidyByGrantContract implements IService
{
    /**
     * The constructor of this class.
     */
    public ReadActualGrantSubsidyByGrantContract()
    {
    }

    public InfoGrantSubsidy run(Integer grantContractId) throws FenixServiceException
    {
        IGrantSubsidy grantSubsidy = null;
        InfoGrantSubsidy infoGrantSubsidy = new InfoGrantSubsidy();
        IPersistentGrantSubsidy pgs = null;
        try
        {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            pgs = sp.getIPersistentGrantSubsidy();
            grantSubsidy = pgs.readActualGrantSubsidyByContract(grantContractId);
        } catch (ExcepcaoPersistencia e)
        {
            throw new FenixServiceException(e.getMessage());
        }
        infoGrantSubsidy = Cloner.copyIGrantSubsidy2InfoGrantSubsidy(grantSubsidy);
        return infoGrantSubsidy;
    }
}