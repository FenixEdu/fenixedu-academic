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
import Dominio.grant.owner.IGrantOwner;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.grant.IPersistentGrantContract;
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
                    .readAllGrantOwnersBySpanAndCriteria(infoSpanByCriteriaListGrantOwner);

            List result = null;
            if (grantOwnerBySpanAndCriteria != null
                    && grantOwnerBySpanAndCriteria.size() != 0) {

                /*
                 * Construct the info list and add to the result.
                 */
                result = new ArrayList();
                for (int i = 0; i < grantOwnerBySpanAndCriteria.size(); i++) {
                    IGrantOwner grantOwner = (IGrantOwner) grantOwnerBySpanAndCriteria
                            .get(i);

                    convertToInfoListGrantOwnerByOrder(grantOwner,
                            infoSpanByCriteriaListGrantOwner, sp, result);
                }
            }
            return result;
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e.getMessage());
        }
    }

    private void convertToInfoListGrantOwnerByOrder(IGrantOwner grantOwner,
            InfoSpanByCriteriaListGrantOwner infoSpanByCriteriaListGrantOwner,
            ISuportePersistente sp, List result) throws ExcepcaoPersistencia{

        IPersistentGrantContract persistentGrantContract = sp
                .getIPersistentGrantContract();
        
        List contractsByCriteria = persistentGrantContract.readAllContractsByGrantOwnerAndCriteria(grantOwner.getIdInternal(), infoSpanByCriteriaListGrantOwner);

        if (contractsByCriteria != null && contractsByCriteria.size() != 0) {
            //By each contract is a new entry
            for (int i = 0; i < contractsByCriteria.size(); i++) { 
                InfoListGrantOwnerByOrder infoListGrantOwnerByOrder = new InfoListGrantOwnerByOrder();
                infoListGrantOwnerByOrder.setGrantOwnerId(grantOwner.getIdInternal());
                infoListGrantOwnerByOrder.setGrantOwnerNumber(grantOwner.getNumber());
                infoListGrantOwnerByOrder.setFirstName(NameUtils.getFirstName(grantOwner.getPerson().getNome()));
                infoListGrantOwnerByOrder.setLastName(NameUtils.getLastName(grantOwner.getPerson().getNome()));
                
                IGrantContract grantContract = (IGrantContract) contractsByCriteria.get(i);
                infoListGrantOwnerByOrder.setContractNumber(grantContract.getContractNumber());
                infoListGrantOwnerByOrder.setGrantType(grantContract.getGrantType().getName());

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
