/*
 * Created on Jun 21, 2004
 *
 */
package ServidorAplicacao.Servico.grant.list;

import java.util.ArrayList;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.grant.list.InfoListGrantOwnerByOrder;
import DataBeans.grant.list.InfoSpanByCriteriaListGrantContract;
import Dominio.grant.contract.IGrantContract;
import Dominio.grant.contract.IGrantContractRegime;
import Dominio.grant.contract.IGrantInsurance;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.grant.utils.SessionConstants;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.grant.IPersistentGrantContract;
import ServidorPersistente.grant.IPersistentGrantContractRegime;
import ServidorPersistente.grant.IPersistentGrantInsurance;
import Util.NameUtils;

/**
 * @author Pica
 * @author Barbosa
 */
public class ListGrantContractByCriteria implements IService {

	public ListGrantContractByCriteria() {
	}

	/**
	 * Query the grant owner by criteria of grant contract
	 * @returns an array of objects
	 *    object[0] List of result
	 *    object[1] IndoSpanCriteriaListGrantOwner
	 */
	public Object[] run(
			InfoSpanByCriteriaListGrantContract infoSpanByCriteriaListGrantOwner)
			throws FenixServiceException {

		//Read the grant contracts ordered by span
		List grantContractBySpanAndCriteria = null;
		IPersistentGrantContract persistentGrantContract = null;
		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			persistentGrantContract = sp.getIPersistentGrantContract();
			grantContractBySpanAndCriteria = persistentGrantContract
					.readAllContractsByCriteria(
							propertyOrderBy(infoSpanByCriteriaListGrantOwner.getOrderBy()),
							infoSpanByCriteriaListGrantOwner.getJustActiveContract(),
							infoSpanByCriteriaListGrantOwner.getJustDesactiveContract(),
							infoSpanByCriteriaListGrantOwner.getBeginContract(),
							infoSpanByCriteriaListGrantOwner.getEndContract(),
							infoSpanByCriteriaListGrantOwner.getSpanNumber(),
							SessionConstants.NUMBER_OF_ELEMENTS_IN_SPAN);

			List listGrantContract = null;
			if (grantContractBySpanAndCriteria != null
					&& grantContractBySpanAndCriteria.size() != 0) {

				/*
				 * Construct the info list and add to the result.
				 */
				listGrantContract = new ArrayList();
				for (int i = 0; i < grantContractBySpanAndCriteria.size(); i++) {
					IGrantContract grantContract = (IGrantContract) grantContractBySpanAndCriteria.get(i);

					convertToInfoListGrantOwnerByOrder(grantContract,infoSpanByCriteriaListGrantOwner, sp, listGrantContract);
				}
			}

			if(infoSpanByCriteriaListGrantOwner.getTotalElements() == null) {
				//Setting the search attributes
	            infoSpanByCriteriaListGrantOwner.setTotalElements(persistentGrantContract.countAllByCriteria(infoSpanByCriteriaListGrantOwner.getJustActiveContract(),
						infoSpanByCriteriaListGrantOwner.getJustDesactiveContract(),
						infoSpanByCriteriaListGrantOwner.getBeginContract(),
						infoSpanByCriteriaListGrantOwner.getEndContract(), null, null));
			}

			Object[] result = {listGrantContract , infoSpanByCriteriaListGrantOwner};
            return result;
            
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e.getMessage());
		}
	}

	/**
	 * For each Grant Owner 1- Read all grant contracts that are in the criteria
	 * 1.1 - Read The active regime of each contract 1.2 - Read the insurance of
	 * each contract 2- Construct the info and put it on the list result
	 */
	private void convertToInfoListGrantOwnerByOrder(IGrantContract grantContract,
			InfoSpanByCriteriaListGrantContract infoSpanByCriteriaListGrantOwner,
			ISuportePersistente sp, List result) throws ExcepcaoPersistencia {

		IPersistentGrantContractRegime persistentGrantContractRegime = sp.getIPersistentGrantContractRegime();
		IPersistentGrantInsurance persistentGrantInsurance = sp.getIPersistentGrantInsurance();

		//Read the actual regime and insurance
		List grantContractRegimeList = persistentGrantContractRegime.readGrantContractRegimeByGrantContractAndState(grantContract.getIdInternal(), new Integer(1));
		IGrantContractRegime grantContractRegime = (IGrantContractRegime) grantContractRegimeList.get(0);
		IGrantInsurance grantInsurance = persistentGrantInsurance.readGrantInsuranceByGrantContract(grantContract.getIdInternal());

		InfoListGrantOwnerByOrder infoListGrantOwnerByOrder = new InfoListGrantOwnerByOrder();

		infoListGrantOwnerByOrder.setGrantOwnerId(grantContract.getGrantOwner().getIdInternal());
		infoListGrantOwnerByOrder.setGrantOwnerNumber(grantContract.getGrantOwner().getNumber());
		infoListGrantOwnerByOrder.setFirstName(NameUtils.getFirstName(grantContract.getGrantOwner().getPerson().getNome()));
		infoListGrantOwnerByOrder.setLastName(NameUtils.getLastName(grantContract.getGrantOwner().getPerson().getNome()));

		infoListGrantOwnerByOrder.setContractNumber(grantContract.getContractNumber());
		infoListGrantOwnerByOrder.setGrantType(grantContract.getGrantType().getSigla());

		infoListGrantOwnerByOrder.setBeginContract(grantContractRegime.getDateBeginContract());
		infoListGrantOwnerByOrder.setEndContract(grantContractRegime.getDateEndContract());

		if(grantInsurance != null) {
			infoListGrantOwnerByOrder.setInsurancePaymentEntity(grantInsurance.getGrantPaymentEntity().getNumber());
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