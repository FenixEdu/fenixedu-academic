/*
 * Created on Jun 21, 2004
 *
 */

package ServidorAplicacao.Servico.grant.list;

import java.util.ArrayList;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.grant.list.InfoListGrantOwnerByOrder;
import DataBeans.grant.list.InfoSpanListGrantOwner;
import Dominio.grant.owner.IGrantOwner;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.grant.IPersistentGrantOwner;
import Util.NameUtils;

/**
 * @author Pica
 * @author Barbosa
 */
public class ListGrantOwners implements IService {

    public ListGrantOwners() {
    }

	/**
	 * Query the grant owner by criteria of grant contract
	 * @returns an array of objects
	 *    object[0] List of result
	 *    object[1] IndoSpanListGrantOwner
	 */

    public Object[] run(InfoSpanListGrantOwner infoSpanListGrantOwner)
            throws FenixServiceException {
        //Read the grant owners ordered by span
        List grantOwnerBySpan = null;
        IPersistentGrantOwner persistentGrantOwner = null;
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            persistentGrantOwner = sp.getIPersistentGrantOwner();
            grantOwnerBySpan = persistentGrantOwner.readAllGrantOwnersBySpan(
                    infoSpanListGrantOwner.getSpanNumber(),
                    infoSpanListGrantOwner.getNumberOfElementsInSpan(),
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

            if(infoSpanListGrantOwner.getTotalElements() == null) {
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

    private InfoListGrantOwnerByOrder convertToInfoListGrantOwnerByOrder(
            IGrantOwner grantOwner) {
        InfoListGrantOwnerByOrder infoListGrantOwnerByOrder = new InfoListGrantOwnerByOrder();

        infoListGrantOwnerByOrder.setGrantOwnerId(grantOwner.getIdInternal());
        infoListGrantOwnerByOrder.setGrantOwnerNumber(grantOwner.getNumber());

        infoListGrantOwnerByOrder.setFirstName(NameUtils
                .getFirstName(grantOwner.getPerson().getNome()));
        infoListGrantOwnerByOrder.setLastName(NameUtils.getLastName(grantOwner
                .getPerson().getNome()));

        return infoListGrantOwnerByOrder;
    }

    //TODO: ordenacao por ultimo nome!
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
}