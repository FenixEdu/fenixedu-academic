/*
 * Created on 18/12/2003
 *  
 */
package ServidorAplicacao.Servico.grant.contract;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.grant.contract.InfoGrantContract;
import DataBeans.util.Cloner;
import Dominio.grant.contract.IGrantContract;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.grant.IPersistentGrantContract;

/**
 * @author Barbosa
 * @author Pica
 *  
 */
public class ReadAllContractsByGrantOwner implements IService
{
    private static ReadAllContractsByGrantOwner service = new ReadAllContractsByGrantOwner();
    /**
	 * The singleton access method of this class.
	 */
    public static ReadAllContractsByGrantOwner getService()
    {
        return service;
    }
    /**
	 * The constructor of this class.
	 */
    private ReadAllContractsByGrantOwner()
    {
    }
    /**
	 * The name of the service
	 */
    public final String getNome()
    {
        return "ReadAllContractsByGrantOwner";
    }

    public List run(Integer grantOwnerId) throws FenixServiceException
    {
        List contracts = null;
        try
        {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentGrantContract pgc = sp.getIPersistentGrantContract();
            contracts = pgc.readAllContractsByGrantOwner(grantOwnerId);
        } catch (ExcepcaoPersistencia e)
        {
            throw new FenixServiceException(e.getMessage());
        }

        if(contracts == null)
            return new ArrayList();
        
        ArrayList contractList = (ArrayList) CollectionUtils.collect(contracts, new Transformer()
        {
            public Object transform(Object input)
            {
                IGrantContract contract = (IGrantContract) input;
                InfoGrantContract infoGrantContract =
                    Cloner.copyIGrantContract2InfoGrantContract(contract);
                return infoGrantContract;
            }
        });

        return contractList;
    }

}