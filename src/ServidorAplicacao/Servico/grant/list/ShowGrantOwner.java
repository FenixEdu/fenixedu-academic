/*
 * Created on Jun 21, 2004
 *
 */

package ServidorAplicacao.Servico.grant.list;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.grant.contract.InfoGrantContractRegime;
import DataBeans.grant.contract.InfoGrantContractRegimeWithTeacherAndContract;
import DataBeans.grant.contract.InfoGrantContractWithGrantOwnerAndGrantType;
import DataBeans.grant.contract.InfoGrantOrientationTeacherWithTeacherAndGrantContract;
import DataBeans.grant.contract.InfoGrantPart;
import DataBeans.grant.contract.InfoGrantSubsidy;
import DataBeans.grant.contract.InfoGrantSubsidyWithContract;
import DataBeans.grant.list.InfoListGrantContract;
import DataBeans.grant.list.InfoListGrantOwnerComplete;
import DataBeans.grant.list.InfoListGrantSubsidy;
import DataBeans.grant.owner.InfoGrantOwnerWithPerson;
import DataBeans.util.Cloner;
import Dominio.grant.contract.IGrantContract;
import Dominio.grant.contract.IGrantContractRegime;
import Dominio.grant.contract.IGrantOrientationTeacher;
import Dominio.grant.contract.IGrantPart;
import Dominio.grant.contract.IGrantSubsidy;
import Dominio.grant.owner.GrantOwner;
import Dominio.grant.owner.IGrantOwner;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentQualification;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.grant.IPersistentGrantContract;
import ServidorPersistente.grant.IPersistentGrantContractRegime;
import ServidorPersistente.grant.IPersistentGrantOrientationTeacher;
import ServidorPersistente.grant.IPersistentGrantOwner;
import ServidorPersistente.grant.IPersistentGrantPart;
import ServidorPersistente.grant.IPersistentGrantSubsidy;
/**
 * @author Pica
 * @author Barbosa
 */
public class ShowGrantOwner implements IService
{

	public ShowGrantOwner()
	{
	}
	private void buildInfoListGrantOwnerComplete(InfoListGrantOwnerComplete infoListGrantOwnerComplete,
			IGrantOwner grantOwner, ISuportePersistente sp) throws FenixServiceException
	{
		List listInfoListGrantContracts = new ArrayList();
		try
		{
			//set grantOwner info
			IPersistentGrantContract persistentGrantContract = sp.getIPersistentGrantContract();
			infoListGrantOwnerComplete.setInfoGrantOwner(InfoGrantOwnerWithPerson
					.newInfoFromDomain(grantOwner));
			List grantContractsList = persistentGrantContract.readAllContractsByGrantOwner(grantOwner
					.getIdInternal());
			Iterator contractsIter = grantContractsList.iterator();
			//set list of qualifications
			IPersistentQualification persistentQualification = sp.getIPersistentQualification();
			List infoQualificationsList = persistentQualification.readQualificationsByPerson(grantOwner
					.getPerson());
			if (infoQualificationsList != null)
				infoListGrantOwnerComplete.setInfoQualifications(infoQualificationsList);
			while (contractsIter.hasNext())
			{
				InfoListGrantContract infoListGrantContract = buildInfoListGrantContract(
						(IGrantContract) contractsIter.next(), sp);
				listInfoListGrantContracts.add(infoListGrantContract);
			}
			Collections.reverse(listInfoListGrantContracts);
			infoListGrantOwnerComplete.setInfoListGrantContracts(listInfoListGrantContracts);
		}
		catch (Exception e)
		{
			throw new FenixServiceException();
		}
	}
	private InfoListGrantContract buildInfoListGrantContract(IGrantContract grantContract,
			ISuportePersistente sp) throws FenixServiceException
	{
		InfoListGrantContract newInfoListGrantContract = new InfoListGrantContract();
		List listInfoListGrantSubsidies = new ArrayList();
		List infoContractRegimes = new ArrayList();
		List infoSubsidyParts = new ArrayList();
		try
		{
			//Set the grant contract
			newInfoListGrantContract.setInfoGrantContract(InfoGrantContractWithGrantOwnerAndGrantType
					.newInfoFromDomain(grantContract));
			//Set the grant orientation teacher for the contract
			IPersistentGrantOrientationTeacher persistentGrantOrientationTeacher = sp
					.getIPersistentGrantOrientationTeacher();
			IGrantOrientationTeacher grantOrientationTeacher = persistentGrantOrientationTeacher
					.readActualGrantOrientationTeacherByContract(grantContract, new Integer(0));
			newInfoListGrantContract.getInfoGrantContract().setGrantOrientationTeacherInfo(
					InfoGrantOrientationTeacherWithTeacherAndGrantContract
							.newInfoFromDomain(grantOrientationTeacher));
			//Read the contract regimes
			IPersistentGrantContractRegime persistentGrantContractRegime = sp
					.getIPersistentGrantContractRegime();
			List contractRegimes = persistentGrantContractRegime
					.readGrantContractRegimeByGrantContract(grantContract.getIdInternal());
			Iterator regimesIter = contractRegimes.iterator();
			while (regimesIter.hasNext())
			{
				InfoGrantContractRegime newInfoGrantContractRegime = InfoGrantContractRegimeWithTeacherAndContract
						.newInfoFromDomain((IGrantContractRegime) regimesIter.next());
				infoContractRegimes.add(newInfoGrantContractRegime);
			}
			newInfoListGrantContract.setInfoGrantContractRegimes(infoContractRegimes);
			//read the contract subsidies
			IPersistentGrantSubsidy persistentGrantSubsidy = sp.getIPersistentGrantSubsidy();
			List contractSubsidies = persistentGrantSubsidy
					.readAllSubsidiesByGrantContract(grantContract.getIdInternal());
			Iterator subsidiesIter = contractSubsidies.iterator();
			while (subsidiesIter.hasNext())
			{
				InfoListGrantSubsidy newInfoListGrantSubsidy = new InfoListGrantSubsidy();
				InfoGrantSubsidy newInfoGrantSubsidy = InfoGrantSubsidyWithContract
						.newInfoFromDomain((IGrantSubsidy) subsidiesIter.next());
				newInfoListGrantSubsidy.setInfoGrantSubsidy(newInfoGrantSubsidy);
				//read the subsidy grant parts
				IPersistentGrantPart persistentGrantPart = sp.getIPersistentGrantPart();
				List subsidyParts = persistentGrantPart
						.readAllGrantPartsByGrantSubsidy(newInfoGrantSubsidy.getIdInternal());
				Iterator partsIter = subsidyParts.iterator();
				while (partsIter.hasNext())
				{
					InfoGrantPart newInfoGrantPart = Cloner.copyIGrantPart2InfoGrantPart((IGrantPart) partsIter.next());
					infoSubsidyParts.add(newInfoGrantPart);
				}
				newInfoListGrantSubsidy.setInfoGrantParts(infoSubsidyParts);
				listInfoListGrantSubsidies.add(newInfoListGrantSubsidy);
			}
			newInfoListGrantContract.setInfoListGrantSubsidys(listInfoListGrantSubsidies);
		}
		catch (ExcepcaoPersistencia e)
		{
			throw new FenixServiceException();
		}
		return newInfoListGrantContract;
	}
	public InfoListGrantOwnerComplete run(Integer grantOwnerId) throws FenixServiceException
	{
		ISuportePersistente sp = null;
		InfoListGrantOwnerComplete infoListGrantOwnerComplete = null;
		try
		{
			sp = SuportePersistenteOJB.getInstance();
			IPersistentGrantOwner persistentGrantOwner = sp.getIPersistentGrantOwner();
			IGrantOwner grantOwner = (IGrantOwner) persistentGrantOwner.readByOID(GrantOwner.class,
					grantOwnerId);
			if (grantOwner != null)
			{
				infoListGrantOwnerComplete = new InfoListGrantOwnerComplete();
				buildInfoListGrantOwnerComplete(infoListGrantOwnerComplete, grantOwner, sp);
			}
		}
		catch (ExcepcaoPersistencia e)
		{
			throw new FenixServiceException();
		}
		return infoListGrantOwnerComplete;
	}
}