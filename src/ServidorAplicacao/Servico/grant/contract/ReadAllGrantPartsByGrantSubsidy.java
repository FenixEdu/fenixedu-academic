/*
 * Created on 23/Jan/2004
 *  
 */
package ServidorAplicacao.Servico.grant.contract;

import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.grant.IPersistentGrantPart;

/**
 * @author Barbosa
 * @author Pica
 *  
 */
public class ReadAllGrantPartsByGrantSubsidy implements IService
{
    /**
     * The constructor of this class.
     */
    public ReadAllGrantPartsByGrantSubsidy()
    {
    }

    public List run(Integer grantSubsidyId) throws FenixServiceException
    {
        List grantParts = null;
        IPersistentGrantPart pgp = null;
        try
        {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            pgp = sp.getIPersistentGrantPart();
            grantParts = pgp.readAllGrantPartsByGrantSubsidy(grantSubsidyId);
        } catch (ExcepcaoPersistencia e)
        {
            throw new FenixServiceException(e.getMessage());
        }

        return grantParts;
    }
}