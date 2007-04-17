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
import net.sourceforge.fenixedu.dataTransferObject.grant.list.InfoSpanListGrantOwner;
import net.sourceforge.fenixedu.domain.grant.owner.GrantOwner;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.presentationTier.Action.grant.utils.SessionConstants;
import net.sourceforge.fenixedu.util.NameUtils;

/**
 * @author Pica
 * @author Barbosa
 */
public class ListGrantOwners extends Service {

	public ListGrantOwners() {
	}

	/**
	 * Query the grant owner by criteria of grant contract
	 * 
	 * @throws ExcepcaoPersistencia
	 * 
	 * @returns an array of objects object[0] List of result object[1]
	 *          IndoSpanListGrantOwner
	 */

	public Object[] run(InfoSpanListGrantOwner infoSpanListGrantOwner)
			throws FenixServiceException, ExcepcaoPersistencia {
		// Read the grant owners ordered by persistentSupportan
		List grantOwnerBySpan = null;

		grantOwnerBySpan = GrantOwner.readAllGrantOwnersBySpan(
				infoSpanListGrantOwner.getSpanNumber(),
				100,
				propertyOrderBy(infoSpanListGrantOwner.getOrderBy()));
	
		List listGrantOwner = null;
		if (grantOwnerBySpan != null && grantOwnerBySpan.size() != 0) {
			listGrantOwner = new ArrayList();

			// For each Grant Owner construct the info list object.
			for (int i = 0; i < grantOwnerBySpan.size(); i++) {
				GrantOwner grantOwner = (GrantOwner) grantOwnerBySpan.get(i);
				if (grantOwner.getPerson() != null) {
					listGrantOwner
							.add(convertToInfoListGrantOwnerByOrder(grantOwner));
				}
			}
		}

		if (infoSpanListGrantOwner.getTotalElements() == null) {
			// Setting the search attributes
			List<GrantOwner> grantOwners = rootDomainObject.getGrantOwners();
			infoSpanListGrantOwner.setTotalElements(grantOwners.size());
		}
		Object[] result = { listGrantOwner, infoSpanListGrantOwner };
		return result;
	}

	/*
	 * Returns the order string to add to the criteria
	 */
	private String propertyOrderBy(String orderBy) {
		String result = null;
		if (orderBy.equals("orderByNumber")) {
			result = "number";
		} else if (orderBy.equals("orderByFirstName")) {
			result = "person.name";
		}
		return result;
	}

	private InfoListGrantOwnerByOrder convertToInfoListGrantOwnerByOrder(
			GrantOwner grantOwner) {
		InfoListGrantOwnerByOrder infoListGrantOwnerByOrder = new InfoListGrantOwnerByOrder();

		infoListGrantOwnerByOrder.setGrantOwnerId(grantOwner.getIdInternal());
		infoListGrantOwnerByOrder.setGrantOwnerNumber(grantOwner.getNumber());

		infoListGrantOwnerByOrder.setFirstName(NameUtils
				.getFirstName(grantOwner.getPerson().getName()));
		infoListGrantOwnerByOrder.setLastName(NameUtils.getLastName(grantOwner
				.getPerson().getName()));

		return infoListGrantOwnerByOrder;
	}
}