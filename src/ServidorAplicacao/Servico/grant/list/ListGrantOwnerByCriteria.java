/*
 * Created on Jun 21, 2004
 *
 */
package ServidorAplicacao.Servico.grant.list;

import java.util.ArrayList;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.grant.list.InfoListGrantOwnerByOrder;
import DataBeans.grant.list.InfoSpanByCriteriaListGrantOwner;
import Dominio.grant.contract.IGrantContract;
import Dominio.grant.contract.IGrantContractRegime;
import Dominio.grant.contract.IGrantInsurance;
import Dominio.grant.owner.IGrantOwner;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.grant.IPersistentGrantContract;
import ServidorPersistente.grant.IPersistentGrantContractRegime;
import ServidorPersistente.grant.IPersistentGrantInsurance;
import ServidorPersistente.grant.IPersistentGrantOwner;
import Util.NameUtils;

/**
 * @author Pica
 * @author Barbosa
 */
public class ListGrantOwnerByCriteria implements IService {

	public ListGrantOwnerByCriteria() {
	}

	/*
	 * Query the grant owner by criteria
	 */
	public List run(
			InfoSpanByCriteriaListGrantOwner infoSpanByCriteriaListGrantOwner)
			throws FenixServiceException {

		//Read the grant owners ordered by span
		List grantOwnerBySpanAndCriteria = null;
		IPersistentGrantOwner persistentGrantOwner = null;
		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			persistentGrantOwner = sp.getIPersistentGrantOwner();
			grantOwnerBySpanAndCriteria = persistentGrantOwner
					.readAllGrantOwnersBySpanAndCriteria(
							infoSpanByCriteriaListGrantOwner.getOrderBy(),
							infoSpanByCriteriaListGrantOwner.getJustActiveContract(),
							infoSpanByCriteriaListGrantOwner.getJustDesactiveContract(),
							infoSpanByCriteriaListGrantOwner.getBeginContract(),
							infoSpanByCriteriaListGrantOwner.getEndContract(),
							infoSpanByCriteriaListGrantOwner.getSpanNumber(),
							infoSpanByCriteriaListGrantOwner.getNumberOfElementsInSpan());

			List result = null;
			if (grantOwnerBySpanAndCriteria != null
					&& grantOwnerBySpanAndCriteria.size() != 0) {

				/*
				 * Construct the info list and add to the result.
				 */
				result = new ArrayList();
				for (int i = 0; i < grantOwnerBySpanAndCriteria.size(); i++) {
					IGrantOwner grantOwner = (IGrantOwner) grantOwnerBySpanAndCriteria.get(i);

					convertToInfoListGrantOwnerByOrder(grantOwner,infoSpanByCriteriaListGrantOwner, sp, result);
				}
			}
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
	private void convertToInfoListGrantOwnerByOrder(IGrantOwner grantOwner,
			InfoSpanByCriteriaListGrantOwner infoSpanByCriteriaListGrantOwner,
			ISuportePersistente sp, List result) throws ExcepcaoPersistencia {

		IPersistentGrantContract persistentGrantContract = sp.getIPersistentGrantContract();
		IPersistentGrantContractRegime persistentGrantContractRegime = sp.getIPersistentGrantContractRegime();
		IPersistentGrantInsurance persistentGrantInsurance = sp.getIPersistentGrantInsurance();

		//Read All the contract that are in the criteria
		List contractsByCriteria = persistentGrantContract
				.readAllContractsByGrantOwnerAndCriteria(grantOwner
						.getIdInternal(), infoSpanByCriteriaListGrantOwner.getJustActiveContract(),
						infoSpanByCriteriaListGrantOwner.getJustDesactiveContract(),
						infoSpanByCriteriaListGrantOwner.getBeginContract(),
						infoSpanByCriteriaListGrantOwner.getEndContract());

		if (contractsByCriteria != null && contractsByCriteria.size() != 0) {
			for (int i = 0; i < contractsByCriteria.size(); i++) {

				IGrantContract grantContract = (IGrantContract) contractsByCriteria
						.get(i);
				//Read the actual regime and insurance
				List grantContractRegimeList = (List) persistentGrantContractRegime
						.readGrantContractRegimeByGrantContractAndState(
								grantContract.getIdInternal(), new Integer(1));
				IGrantContractRegime grantContractRegime = (IGrantContractRegime) grantContractRegimeList
						.get(0);
				IGrantInsurance grantInsurance = (IGrantInsurance) persistentGrantInsurance
						.readGrantInsuranceByGrantContract(grantContract
								.getIdInternal());

				//By each contract is a new entry
				InfoListGrantOwnerByOrder infoListGrantOwnerByOrder = new InfoListGrantOwnerByOrder();

				infoListGrantOwnerByOrder.setGrantOwnerId(grantOwner.getIdInternal());
				infoListGrantOwnerByOrder.setGrantOwnerNumber(grantOwner.getNumber());
				infoListGrantOwnerByOrder.setFirstName(NameUtils.getFirstName(grantOwner.getPerson().getNome()));
				infoListGrantOwnerByOrder.setLastName(NameUtils.getLastName(grantOwner.getPerson().getNome()));

				infoListGrantOwnerByOrder.setContractNumber(grantContract.getContractNumber());
				infoListGrantOwnerByOrder.setGrantType(grantContract.getGrantType().getName());

				infoListGrantOwnerByOrder.setBeginContract(grantContractRegime.getDateBeginContract());
				infoListGrantOwnerByOrder.setEndContract(grantContractRegime.getDateEndContract());

				infoListGrantOwnerByOrder.setInsurancePaymentEntity(grantInsurance.getGrantPaymentEntity().getNumber());

				result.add(infoListGrantOwnerByOrder);
			}
		}
	}

	/*
	 * Returns the order string to add to the criteria
	 */
	private String propertyOrderBy(String orderBy) {
		String result = null;
		if (orderBy.equals("orderByNumber")) {
			result = "number";
		} else if (orderBy.equals("orderByFirstName")) {
			result = "person.nome";
		} else if (orderBy.equals("orderByGrantType")) {
			result = "grantContracts.grantType.name";
		}
		return result;
	}

	//			ArrayList infoGrantOwnerList = (ArrayList)
	// CollectionUtils.collect(grantOwnerBySpan,
	//					new Transformer()
	//					{
	//
	//						public Object transform(Object input)
	//						{
	//							IGrantOwner grantOwner = (IGrantOwner) input;
	//							InfoGrantOwner infoGrantOwner = Cloner
	//									.copyIGrantOwner2InfoGrantOwner(grantOwner);
	//							return infoGrantOwner;
	//						}
	//					});
	//			Collections.sort(infoGrantOwnerList, new Comparator() {
	//
	//				public int compare(Object arg0, Object arg1)
	//				{
	//					InfoGrantOwner grantOwner0 = (InfoGrantOwner) arg0;
	//					InfoGrantOwner grantOwner1 = (InfoGrantOwner) arg1;
	//					
	//					return
	// grantOwner0.getPersonInfo().getPersonLastName().compareTo(grantOwner1.getPersonInfo().getPersonLastName());
	//				}});
	//
	//Read All elements, and insert the result at the end of the list
	//		Integer countAllGrantOwner = persistentGrantOwner.countAll();
	//		infoGrantOwnerList.add(countAllGrantOwner);

}