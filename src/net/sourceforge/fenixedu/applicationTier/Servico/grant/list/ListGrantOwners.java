/*
 * Created on Jun 21, 2004
 *
 */

package net.sourceforge.fenixedu.applicationTier.Servico.grant.list;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.grant.list.InfoListGrantOwnerByOrder;
import net.sourceforge.fenixedu.dataTransferObject.grant.list.InfoSpanListGrantOwner;
import net.sourceforge.fenixedu.domain.grant.owner.IGrantOwner;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantOwner;
import net.sourceforge.fenixedu.presentationTier.Action.grant.utils.SessionConstants;
import net.sourceforge.fenixedu.util.NameUtils;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Pica
 * @author Barbosa
 */
public class ListGrantOwners implements IService {

    public ListGrantOwners() {
    }

    /**
     * Query the grant owner by criteria of grant contract
     * 
     * @returns an array of objects object[0] List of result object[1]
     *          IndoSpanListGrantOwner
     */

    public Object[] run(InfoSpanListGrantOwner infoSpanListGrantOwner) throws FenixServiceException {
        //Read the grant owners ordered by span
        List grantOwnerBySpan = null;
        IPersistentGrantOwner persistentGrantOwner = null;
        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            persistentGrantOwner = sp.getIPersistentGrantOwner();
            grantOwnerBySpan = persistentGrantOwner.readAllGrantOwnersBySpan(infoSpanListGrantOwner
                    .getSpanNumber(), SessionConstants.NUMBER_OF_ELEMENTS_IN_SPAN,
                    propertyOrderBy(infoSpanListGrantOwner.getOrderBy()));

            List listGrantOwner = null;
            if (grantOwnerBySpan != null && grantOwnerBySpan.size() != 0) {
                listGrantOwner = new ArrayList();

                //For each Grant Owner construct the info list object.
                for (int i = 0; i < grantOwnerBySpan.size(); i++) {
                    IGrantOwner grantOwner = (IGrantOwner) grantOwnerBySpan.get(i);
                    listGrantOwner.add(convertToInfoListGrantOwnerByOrder(grantOwner));
                }
            }

            if (infoSpanListGrantOwner.getTotalElements() == null) {
                //Setting the search attributes
                infoSpanListGrantOwner.setTotalElements(persistentGrantOwner.countAll());
            }
            Object[] result = { listGrantOwner, infoSpanListGrantOwner };
            return result;
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e.getMessage());
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
        }
        return result;
    }

    private InfoListGrantOwnerByOrder convertToInfoListGrantOwnerByOrder(IGrantOwner grantOwner) {
        InfoListGrantOwnerByOrder infoListGrantOwnerByOrder = new InfoListGrantOwnerByOrder();

        infoListGrantOwnerByOrder.setGrantOwnerId(grantOwner.getIdInternal());
        infoListGrantOwnerByOrder.setGrantOwnerNumber(grantOwner.getNumber());

        infoListGrantOwnerByOrder.setFirstName(NameUtils.getFirstName(grantOwner.getPerson().getNome()));
        infoListGrantOwnerByOrder.setLastName(NameUtils.getLastName(grantOwner.getPerson().getNome()));

        return infoListGrantOwnerByOrder;
    }
}