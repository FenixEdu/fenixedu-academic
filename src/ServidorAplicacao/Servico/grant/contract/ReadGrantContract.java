/*
 * Created on 18/12/2003
 *  
 */
package ServidorAplicacao.Servico.grant.contract;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoObject;
import DataBeans.grant.contract.InfoGrantContract;
import DataBeans.grant.contract.InfoGrantOrientationTeacher;
import DataBeans.grant.contract.InfoGrantResponsibleTeacher;
import DataBeans.util.Cloner;
import Dominio.IDomainObject;
import Dominio.grant.contract.GrantContract;
import Dominio.grant.contract.IGrantContract;
import Dominio.grant.contract.IGrantOrientationTeacher;
import Dominio.grant.contract.IGrantResponsibleTeacher;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.framework.ReadDomainObjectService;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.grant.IPersistentGrantOrientationTeacher;
import ServidorPersistente.grant.IPersistentGrantResponsibleTeacher;

/**
 * @author Barbosa
 * @author Pica
 *  
 */
public class ReadGrantContract extends ReadDomainObjectService implements IService
{
	/**
	 * The constructor of this class.
	 */
	public ReadGrantContract()
	{
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.Servico.framework.ReadDomainObjectService#getIPersistentObject(ServidorPersistente.ISuportePersistente)
	 */
	protected IPersistentObject getIPersistentObject(ISuportePersistente sp)   
	{
		return sp.getIPersistentGrantContract();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.Servico.framework.ReadDomainObjectService#clone2InfoObject(Dominio.IDomainObject)
	 */
	protected InfoObject clone2InfoObject(IDomainObject domainObject)
	{
		return Cloner.copyIGrantContract2InfoGrantContract((IGrantContract) domainObject);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.Servico.framework.ReadDomainObjectService#getDomainObjectClass()
	 */
	protected Class getDomainObjectClass()
	{
		return GrantContract.class;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.Servico.framework.ReadDomainObjectService#run(java.lang.Integer)
	 */
	public InfoObject run(Integer objectId) throws FenixServiceException
	{
		try
		{
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IPersistentGrantResponsibleTeacher pgrt = sp.getIPersistentGrantResponsibleTeacher();
			IPersistentGrantOrientationTeacher pgot = sp.getIPersistentGrantOrientationTeacher();

			InfoGrantContract infoGrantContract = (InfoGrantContract) super.run(objectId);
			IGrantContract contract = Cloner.copyInfoGrantContract2IGrantContract(infoGrantContract);

			//get the GrantResponsibleTeacher for the contract
			IGrantResponsibleTeacher responsibleTeacher =
				pgrt.readActualGrantResponsibleTeacherByContract(contract, new Integer(0));
			InfoGrantResponsibleTeacher infoResponsibleTeacher =
				Cloner.copyIGrantResponsibleTeacher2InfoGrantResponsibleTeacher(responsibleTeacher);
			infoGrantContract.setGrantResponsibleTeacherInfo(infoResponsibleTeacher);

			//get the GrantOrientationTeacher for the contract
			IGrantOrientationTeacher orientationTeacher =
				pgot.readActualGrantOrientationTeacherByContract(contract, new Integer(0));
			InfoGrantOrientationTeacher infoOrientationTeacher =
				Cloner.copyIGrantOrientationTeacher2InfoGrantOrientationTeacher(orientationTeacher);
			infoGrantContract.setGrantOrientationTeacherInfo(infoOrientationTeacher);
			
			return infoGrantContract;
		} catch (ExcepcaoPersistencia e)
		{
			throw new FenixServiceException(e.getMessage());
		}
	}
}