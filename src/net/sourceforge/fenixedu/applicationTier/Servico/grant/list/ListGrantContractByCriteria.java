/*
 * Created on Jun 21, 2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.grant.list;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.grant.list.InfoListGrantOwnerByOrder;
import net.sourceforge.fenixedu.dataTransferObject.grant.list.InfoSpanByCriteriaListGrantContract;
import net.sourceforge.fenixedu.domain.grant.contract.GrantContract;
import net.sourceforge.fenixedu.domain.grant.contract.GrantContractRegime;
import net.sourceforge.fenixedu.domain.grant.contract.GrantInsurance;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantContract;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantContractRegime;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantInsurance;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.grant.contract.GrantContractVO;
import net.sourceforge.fenixedu.presentationTier.Action.grant.utils.SessionConstants;
import net.sourceforge.fenixedu.util.NameUtils;

/**
 * @author Pica
 * @author Barbosa
 */
public class ListGrantContractByCriteria extends Service {

	public ListGrantContractByCriteria() {
	}

	/**
	 * Query the grant owner by criteria of grant contract
	 * @throws ExcepcaoPersistencia 
	 * 
	 * @returns an array of objects object[0] List of result object[1]
	 *          IndoSpanCriteriaListGrantOwner
	 */
	public Object[] run(InfoSpanByCriteriaListGrantContract infoSpanByCriteriaListGrantOwner)
			throws FenixServiceException, ExcepcaoPersistencia {

		// Read the grant contracts ordered by persistentSupportan
		List grantContractBySpanAndCriteria = null;

		IPersistentGrantContract persistentGrantContract = new GrantContractVO();
		grantContractBySpanAndCriteria = persistentGrantContract.readAllContractsByCriteria(
				propertyOrderBy(infoSpanByCriteriaListGrantOwner.getOrderBy()),
				infoSpanByCriteriaListGrantOwner.getJustActiveContract(),
				infoSpanByCriteriaListGrantOwner.getJustDesactiveContract(),
				infoSpanByCriteriaListGrantOwner.getBeginContract(), infoSpanByCriteriaListGrantOwner
						.getEndContract(), infoSpanByCriteriaListGrantOwner.getSpanNumber(),
				SessionConstants.NUMBER_OF_ELEMENTS_IN_SPAN, infoSpanByCriteriaListGrantOwner
						.getGrantTypeId());

		List listGrantContract = null;
		if (grantContractBySpanAndCriteria != null && grantContractBySpanAndCriteria.size() != 0) {

			/*
			 * Construct the info list and add to the result.
			 */
			listGrantContract = new ArrayList();
			for (int i = 0; i < grantContractBySpanAndCriteria.size(); i++) {
				GrantContract grantContract = (GrantContract) grantContractBySpanAndCriteria.get(i);

				convertToInfoListGrantOwnerByOrder(grantContract, infoSpanByCriteriaListGrantOwner, listGrantContract);
			}
		}

		if (infoSpanByCriteriaListGrantOwner.getTotalElements() == null) {
			// Setting the search attributes
			infoSpanByCriteriaListGrantOwner.setTotalElements(persistentGrantContract
					.countAllByCriteria(infoSpanByCriteriaListGrantOwner.getJustActiveContract(),
							infoSpanByCriteriaListGrantOwner.getJustDesactiveContract(),
							infoSpanByCriteriaListGrantOwner.getBeginContract(),
							infoSpanByCriteriaListGrantOwner.getEndContract(),
							infoSpanByCriteriaListGrantOwner.getGrantTypeId()));
		}

		Object[] result = { listGrantContract, infoSpanByCriteriaListGrantOwner };
		return result;
	}

	/**
	 * For each Grant Owner 1- Read all grant contracts that are in the criteria
	 * 1.1 - Read The active regime of each contract 1.2 - Read the insurance of
	 * each contract 2- Construct the info and put it on the list result
	 */
	private void convertToInfoListGrantOwnerByOrder(GrantContract grantContract,
			InfoSpanByCriteriaListGrantContract infoSpanByCriteriaListGrantOwner,
			List result) throws ExcepcaoPersistencia {

		IPersistentGrantContractRegime persistentGrantContractRegime = persistentSupport
				.getIPersistentGrantContractRegime();
		IPersistentGrantInsurance persistentGrantInsurance = persistentSupport.getIPersistentGrantInsurance();

		// Read the actual regime and insurance
		List grantContractRegimeList = persistentGrantContractRegime
				.readGrantContractRegimeByGrantContractAndState(grantContract.getIdInternal(),
						new Integer(1));
		GrantContractRegime grantContractRegime = (GrantContractRegime) grantContractRegimeList.get(0);
		GrantInsurance grantInsurance = persistentGrantInsurance
				.readGrantInsuranceByGrantContract(grantContract.getIdInternal());

		InfoListGrantOwnerByOrder infoListGrantOwnerByOrder = new InfoListGrantOwnerByOrder();

		infoListGrantOwnerByOrder.setGrantOwnerId(grantContract.getGrantOwner().getIdInternal());
		infoListGrantOwnerByOrder.setGrantOwnerNumber(grantContract.getGrantOwner().getNumber());
		infoListGrantOwnerByOrder.setFirstName(NameUtils.getFirstName(grantContract.getGrantOwner()
				.getPerson().getNome()));
		infoListGrantOwnerByOrder.setLastName(NameUtils.getLastName(grantContract.getGrantOwner()
				.getPerson().getNome()));

		infoListGrantOwnerByOrder.setContractNumber(grantContract.getContractNumber());
		infoListGrantOwnerByOrder.setGrantType(grantContract.getGrantType().getSigla());

		infoListGrantOwnerByOrder.setBeginContract(grantContractRegime.getDateBeginContract());
		infoListGrantOwnerByOrder.setEndContract(grantContractRegime.getDateEndContract());

		if (grantInsurance != null) {
			infoListGrantOwnerByOrder.setInsurancePaymentEntity(grantInsurance.getGrantPaymentEntity()
					.getNumber());
			infoListGrantOwnerByOrder.setNumberPaymentEntity(grantInsurance.getGrantPaymentEntity().getNumber());
			infoListGrantOwnerByOrder.setDesignation(grantInsurance.getGrantPaymentEntity().getDesignation());
		}

		result.add(infoListGrantOwnerByOrder);
	}

	/*
	 * Returns the order string to add to the criteria
	 */
	private String propertyOrderBy(String orderBy) {
		String result = null;
		if (orderBy.equals("orderByGrantOwnerNumber")) {
			result = "grantOwner.number";
		} else if (orderBy.equals("orderByGrantContractNumber")) {
			result = "contractNumber";
		} else if (orderBy.equals("orderByFirstName")) {
			result = "grantOwner.person.nome";
		} else if (orderBy.equals("orderByGrantType")) {
			result = "grantType.sigla";
		} else if (orderBy.equals("orderByDateBeginContract")) {
			result = "contractRegimes.dateBeginContract";
		} else if (orderBy.equals("orderByDateEndContract")) {
			result = "contractRegimes.dateEndContract";
		}
		return result;
	}
}