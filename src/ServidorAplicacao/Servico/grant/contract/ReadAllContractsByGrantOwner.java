/*
 * Created on 18/12/2003
 *  
 */
package ServidorAplicacao.Servico.grant.contract;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.grant.contract.InfoGrantContract;
import DataBeans.grant.contract.InfoGrantOrientationTeacher;
import DataBeans.grant.contract.InfoGrantResponsibleTeacher;
import DataBeans.util.Cloner;
import Dominio.grant.contract.IGrantContract;
import Dominio.grant.contract.IGrantOrientationTeacher;
import Dominio.grant.contract.IGrantResponsibleTeacher;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.grant.IPersistentGrantContract;
import ServidorPersistente.grant.IPersistentGrantOrientationTeacher;
import ServidorPersistente.grant.IPersistentGrantResponsibleTeacher;

/**
 * @author Barbosa
 * @author Pica
 *  
 */
public class ReadAllContractsByGrantOwner implements IService
{
	/**
	 * The constructor of this class.
	 */
	public ReadAllContractsByGrantOwner()
	{
    }

	public List run(Integer grantOwnerId) throws FenixServiceException
	{
		List contracts = null;
		IPersistentGrantResponsibleTeacher pgrt = null;
		IPersistentGrantOrientationTeacher pgot = null;
		IPersistentGrantContract pgc = null;
		try
		{
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			pgc = sp.getIPersistentGrantContract();
			pgrt = sp.getIPersistentGrantResponsibleTeacher();
			pgot = sp.getIPersistentGrantOrientationTeacher();
			contracts = pgc.readAllContractsByGrantOwner(grantOwnerId);
		} catch (ExcepcaoPersistencia e)
		{
			throw new FenixServiceException(e.getMessage());
		}

		if (contracts == null)
			return new ArrayList();

		Iterator contractIter = contracts.iterator();
		ArrayList contractList = new ArrayList();
        
		//gather information related to each contract
		while (contractIter.hasNext())
		{
			try
			{
				IGrantContract grantContract = (IGrantContract) contractIter.next();
				InfoGrantContract infoGrantContract = Cloner.copyIGrantContract2InfoGrantContract(grantContract);
				
				//get the GrantResponsibleTeacher for each contract
				IGrantResponsibleTeacher responsibleTeacher =
					pgrt.readActualGrantResponsibleTeacherByContract(grantContract, new Integer(0));
				InfoGrantResponsibleTeacher infoResponsibleTeacher =
					Cloner.copyIGrantResponsibleTeacher2InfoGrantResponsibleTeacher(responsibleTeacher);
				infoGrantContract.setGrantResponsibleTeacherInfo(infoResponsibleTeacher);
			
				//get the GrantOrientationTeacher for each contract
				IGrantOrientationTeacher orientationTeacher =
					pgot.readActualGrantOrientationTeacherByContract(grantContract, new Integer(0));
				InfoGrantOrientationTeacher infoOrientationTeacher =
				Cloner.copyIGrantOrientationTeacher2InfoGrantOrientationTeacher(orientationTeacher);
				infoGrantContract.setGrantOrientationTeacherInfo(infoOrientationTeacher);
				
				contractList.add(infoGrantContract);
			} catch (ExcepcaoPersistencia e)
			{
				throw new FenixServiceException(e.getMessage());
			}
		}
		return contractList;
	}
}