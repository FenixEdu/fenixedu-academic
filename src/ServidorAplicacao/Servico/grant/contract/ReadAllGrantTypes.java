/*
 * Created on 09/01/2004
 *  
 */
package ServidorAplicacao.Servico.grant.contract;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.grant.contract.InfoGrantType;
import Dominio.grant.contract.IGrantType;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.grant.IPersistentGrantType;

/**
 * @author Barbosa
 * @author Pica
 *  
 */
public class ReadAllGrantTypes implements IService
{
    /**
	 * The constructor of this class.
	 */
    public ReadAllGrantTypes()
    {
    }
    
    public List run() throws FenixServiceException
    {
        List grantTypes = null;
        try
        {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentGrantType pgt = sp.getIPersistentGrantType();
            grantTypes = pgt.readAll();
        } catch (ExcepcaoPersistencia e)
        {
            throw new FenixServiceException(e.getMessage());
        }

        if(grantTypes == null)
            return new ArrayList();
        
        ArrayList grantTypeList = (ArrayList) CollectionUtils.collect(grantTypes, new Transformer()
        {
            public Object transform(Object input)
            {
                IGrantType grantType = (IGrantType) input;
                InfoGrantType infoGrantType =
                    InfoGrantType.newInfoFromDomain(grantType);
                return infoGrantType;
            }
        });

        return grantTypeList;
    }

}