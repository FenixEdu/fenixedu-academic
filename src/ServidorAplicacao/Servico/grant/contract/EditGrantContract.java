/*
 * Created on 18/Nov/2003
 *  
 */
package ServidorAplicacao.Servico.grant.contract;

import org.apache.commons.beanutils.PropertyUtils;

import DataBeans.InfoObject;
import DataBeans.InfoTeacher;
import DataBeans.grant.contract.InfoGrantContract;
import DataBeans.grant.contract.InfoGrantType;
import DataBeans.util.Cloner;
import Dominio.IDomainObject;
import Dominio.ITeacher;
import Dominio.Teacher;
import Dominio.grant.contract.GrantOrientationTeacher;
import Dominio.grant.contract.GrantType;
import Dominio.grant.contract.IGrantContract;
import Dominio.grant.contract.IGrantOrientationTeacher;
import Dominio.grant.contract.IGrantType;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.grant.GrantOrientationTeacherNotFoundException;
import ServidorAplicacao.Servico.exceptions.grant.GrantTypeNotFoundException;
import ServidorAplicacao.Servico.framework.EditDomainObjectService;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.grant.IPersistentGrantContract;
import ServidorPersistente.grant.IPersistentGrantOrientationTeacher;
import ServidorPersistente.grant.IPersistentGrantType;

/**
 * @author Barbosa
 * @author Pica
 */
public class EditGrantContract extends EditDomainObjectService
{
	/**
	 * The constructor of this class.
	 */
	public EditGrantContract()
	{
	}

	protected IDomainObject clone2DomainObject(InfoObject infoObject)
	{
		return Cloner.copyInfoGrantContract2IGrantContract((InfoGrantContract) infoObject);
	}

	protected IPersistentObject getIPersistentObject(ISuportePersistente sp)
	{
		return sp.getIPersistentGrantContract();
	}

//	protected IDomainObject readObjectByUnique(IDomainObject domainObject, ISuportePersistente sp)
//		throws ExcepcaoPersistencia
//	{
//		IPersistentGrantContract pgc = sp.getIPersistentGrantContract();
//		IGrantContract grantContract = (IGrantContract) domainObject;
//
//		return pgc.readGrantContractByNumberAndGrantOwner(
//			grantContract.getContractNumber(),
//			grantContract.getGrantOwner().getIdInternal());
//	}

//	protected void checkIfGrantTeacherPeriodConflict(
//		IGrantContract grantContract,
//		InfoGrantContract infoGrantContract,
//		ISuportePersistente sp)
//		throws FenixServiceException
//	{
//		Date beginOrientationDate = infoGrantContract.getGrantOrientationTeacherInfo().getBeginDate();
//		try
//		{
//			//check that new ORIENTATION period does NOT CONFLICT with any other
//			IPersistentGrantOrientationTeacher got = sp.getIPersistentGrantOrientationTeacher();
//			IGrantOrientationTeacher orientationTeacher =
//				got.readActualGrantOrientationTeacherByContract(
//					grantContract,
//					infoGrantContract.getGrantOrientationTeacherInfo().getIdInternal());
//
//			if ((orientationTeacher != null)
//				&& (beginOrientationDate.before(orientationTeacher.getEndDate())))
//				throw new GrantOrientationTeacherPeriodConflictException();
//
//		}
//		catch (ExcepcaoPersistencia e)
//		{
//			throw new FenixServiceException();
//		}
//	}

//	protected void checkIfGrantTeacherPeriodWithinContractPeriod(InfoGrantContract infoGrantContract)
//		throws FenixServiceException
//	{
//		Date beginContractDate = infoGrantContract.getDateBeginContract();
//		Date endContractDate = infoGrantContract.getDateEndContract();
//		Date beginOrientationDate = infoGrantContract.getGrantOrientationTeacherInfo().getBeginDate();
//		Date endOrientationDate = infoGrantContract.getGrantOrientationTeacherInfo().getEndDate();
//
//		//check if ORIENTATION period is WITHIN contract period
//		if (!((beginOrientationDate.after(beginContractDate)
//			|| beginContractDate.equals(beginOrientationDate))
//			&& (endOrientationDate.before(endContractDate) || endContractDate.equals(endOrientationDate))))
//			throw new GrantOrientationTeacherPeriodNotWithinContractPeriodException();
//	}

	protected void doBeforeLock(
		IDomainObject domainObjectToLock,
		InfoObject infoObject,
		ISuportePersistente sp)
		throws FenixServiceException
	{
//		InfoGrantContract infoGrantContract = (InfoGrantContract) infoObject;

//		Date beginContractDate = infoGrantContract.getDateBeginContract();
//		Date endContractDate = infoGrantContract.getDateEndContract();
//		Date beginOrientationDate = infoGrantContract.getGrantOrientationTeacherInfo().getBeginDate();
//		Date endOrientationDate = infoGrantContract.getGrantOrientationTeacherInfo().getEndDate();

		try
		{
			//check that endDate is after beginDate (GrantContract)
//			if (endContractDate.before(beginContractDate))
//				throw new GrantContractEndDateBeforeBeginDateException();
			
			//check that endDate is after beginDate (GrantOrientationTeacher)
//			if (endOrientationDate.before(beginOrientationDate))
//				throw new GrantOrientationTeacherEndDateBeforeBeginDateException();

			//TODO: DOES THIS VERIFICATION REALLY NEEDS TO BE DONE??
			//TODO (pica): comentei isto já que a secretaria deve poder alterar para os valores que
			// quiserem
			//... perguntar depois na reuniao se isto é necessário ou nao
			//if (!isNew(grantContract))
			//	checkIfGrantTeacherPeriodConflict(grantContract, infoGrantContract, sp);

			//checkIfGrantTeacherPeriodWithinContractPeriod(infoGrantContract);
			checkIfGrantTeacherRelationExists(domainObjectToLock, infoObject, sp);

		}
		catch (Exception e)
		{
			if (e instanceof FenixServiceException)
				throw (FenixServiceException) e;
			throw new FenixServiceException();
		}
	}

	protected void checkIfGrantTeacherRelationExists(
		IDomainObject newDomainObject,
		InfoObject infoObject,
		ISuportePersistente sp)
		throws FenixServiceException
	{
		try
		{
			IPersistentGrantOrientationTeacher ot = sp.getIPersistentGrantOrientationTeacher();
			InfoGrantContract infoGrantContract = (InfoGrantContract) infoObject;
			IGrantOrientationTeacher oldGrantOrientationTeacher = null;
			IGrantOrientationTeacher newGrantOrientationTeacher = new GrantOrientationTeacher();

			//check if the GrantOrientation relation exists
			Integer orientationId = infoGrantContract.getGrantOrientationTeacherInfo().getIdInternal();
			if ((orientationId != null) && !(orientationId.equals(new Integer(0))))
			{
				//lock the existent object to write (EDIT)
				newGrantOrientationTeacher =
					(IGrantOrientationTeacher) ot.readByOId(
						Cloner.copyInfoGrantOrientationTeacher2IGrantOrientationTeacher(
							infoGrantContract.getGrantOrientationTeacherInfo()),
						true);
			}
			else
				ot.simpleLockWrite(newGrantOrientationTeacher);
			oldGrantOrientationTeacher =
				Cloner.copyInfoGrantOrientationTeacher2IGrantOrientationTeacher(
					infoGrantContract.getGrantOrientationTeacherInfo());
			Integer ack_opt_lock = newGrantOrientationTeacher.getAckOptLock();
			PropertyUtils.copyProperties(newGrantOrientationTeacher, oldGrantOrientationTeacher);
			newGrantOrientationTeacher.setGrantContract((IGrantContract) newDomainObject);
			newGrantOrientationTeacher.setAckOptLock(ack_opt_lock);
		}
		catch (ExcepcaoPersistencia e)
		{
			throw new FenixServiceException(e);
		}
		catch (Exception e)
		{
			throw new FenixServiceException(e);
		}
	}

	protected InfoGrantType checkIfGrantTypeExists(String sigla, IPersistentGrantType pt)
		throws FenixServiceException
	{
		InfoGrantType infoGrantType = new InfoGrantType();
		IGrantType grantType = new GrantType();
		try
		{
			grantType = pt.readGrantTypeBySigla(sigla);
			infoGrantType = Cloner.copyIGrantType2InfoGrantType(grantType);
			if (infoGrantType == null)
				throw new GrantTypeNotFoundException();
		}
		catch (ExcepcaoPersistencia persistentException)
		{
			throw new FenixServiceException(persistentException.getMessage());
		}
		return infoGrantType;
	}

	private InfoTeacher checkIfGrantOrientationTeacherExists(
		Integer teacherNumber,
		IPersistentTeacher pt)
		throws FenixServiceException
	{
		//TODO.. esta mal!! ele tem é que ir ler o GrantOrientationTeacher associado ao
		//contrato e nao o professor!!!!

		
		InfoTeacher infoTeacher = new InfoTeacher();
		ITeacher teacher = new Teacher();
		try
		{
			teacher = pt.readByNumber(teacherNumber);
			if (teacher == null)
				throw new GrantOrientationTeacherNotFoundException();
			infoTeacher = Cloner.copyITeacher2InfoTeacher(teacher);
		}
		catch (ExcepcaoPersistencia persistentException)
		{
			throw new FenixServiceException(persistentException.getMessage());
		}
		return infoTeacher;
	}

	/**
	 * Executes the service.
	 *  
	 */
	public void run(InfoGrantContract infoGrantContract) throws FenixServiceException
	{
		try
		{
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IPersistentTeacher pTeacher = sp.getIPersistentTeacher();
			IPersistentGrantType pGrantType = sp.getIPersistentGrantType();
			IPersistentGrantContract pGrantContract = sp.getIPersistentGrantContract();

			infoGrantContract.setGrantTypeInfo(checkIfGrantTypeExists(infoGrantContract.getGrantTypeInfo().getSigla(), pGrantType));

			infoGrantContract.getGrantOrientationTeacherInfo().setOrientationTeacherInfo(
				checkIfGrantOrientationTeacherExists(
					infoGrantContract
						.getGrantOrientationTeacherInfo()
						.getOrientationTeacherInfo()
						.getTeacherNumber(),
					pTeacher));

			if (infoGrantContract.getContractNumber() == null)
			{
				// set the contract number!
				Integer maxNumber =
					pGrantContract.readMaxGrantContractNumberByGrantOwner(
						infoGrantContract.getGrantOwnerInfo().getIdInternal());
				int aux = maxNumber.intValue() + 1;
				Integer newContractNumber = new Integer(aux);
				infoGrantContract.setContractNumber(newContractNumber);
			}

			super.run(infoGrantContract.getIdInternal(), infoGrantContract);
		}
		catch (ExcepcaoPersistencia e)
		{
			throw new FenixServiceException(e);
		}
		catch (Exception e)
		{
			if (e instanceof FenixServiceException)
			{
				throw (FenixServiceException) e;
			}
			throw new FenixServiceException(e);
		}
	}
}