/*
 * Created on Jun 21, 2004
 *
 */

package ServidorAplicacao.Servico.grant.list;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoPerson;
import DataBeans.grant.list.InfoListGrantOwnerByOrder;
import Dominio.grant.contract.IGrantContract;
import Dominio.grant.owner.IGrantOwner;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.grant.IPersistentGrantContract;
import ServidorPersistente.grant.IPersistentGrantOwner;
/**
 * @author Pica
 * @author Barbosa
 */
public class ListGrantOwnerByCriteria implements IService
{

	public ListGrantOwnerByCriteria()
	{
	}
	public List run(String orderBy, Integer numberOfElementsInSpan, Integer numberOfSpan)
	throws FenixServiceException
{
//Read the grant owners ordered by span
List grantOwnerBySpan = null;
IPersistentGrantOwner persistentGrantOwner = null;
IPersistentGrantContract persistentGrantContract = null;
try
{
	ISuportePersistente sp = SuportePersistenteOJB.getInstance();
	persistentGrantOwner = sp.getIPersistentGrantOwner();
	persistentGrantContract = sp.getIPersistentGrantContract();
	grantOwnerBySpan = persistentGrantOwner.readAllGrantOwnersBySpan(
			numberOfSpan, numberOfElementsInSpan, propertyOrderBy(orderBy));
	
	List result = null;
	if (grantOwnerBySpan != null && grantOwnerBySpan.size() != 0)
	{
		/*
		 * For each Grant Owner in the list read it's active contracts
		 * (olny one if everithing is ok!. Than construct the info list
		 * and add to the result.
		 */
	    result = new ArrayList();
		for (int i = 0; i < grantOwnerBySpan.size(); i++)
		{
			System.out.println("Construir o info n." + i);
			IGrantOwner grantOwner = (IGrantOwner) grantOwnerBySpan.get(i);
			//Read the respective active contracts... (it should be only one!)
			List activeContracts = persistentGrantContract
					.readAllActiveContractsByGrantOwner(grantOwner.getIdInternal());
			
			convertToInfoListGrantOwnerByOrder(grantOwner, activeContracts, result);
			System.out.println("Result size:" + result.size());
		}
	}
	System.out.println("Final Result size:" + result.size());
	return result;
}
catch (ExcepcaoPersistencia e)
{
	throw new FenixServiceException(e.getMessage());
}
}
/*
* Returns the order string to add to the criteria
*/
private String propertyOrderBy(String orderBy)
{
String result = null;
if (orderBy.equals("orderByNumber"))
{
	result = "number";
}
else if (orderBy.equals("orderByFirstName"))
{
	result = "person.nome";
}
return result;
}
private void convertToInfoListGrantOwnerByOrder(IGrantOwner grantOwner, List activeContracts,
	List result)
{
if (activeContracts != null && activeContracts.size() != 0)
{
	//By each contract is a new entry
	for (int i = 0; i < activeContracts.size(); i++)
	{
		InfoListGrantOwnerByOrder infoListGrantOwnerByOrder = new InfoListGrantOwnerByOrder();
		infoListGrantOwnerByOrder.setGrantOwnerId(grantOwner.getIdInternal());
		infoListGrantOwnerByOrder.setGrantOwnerNumber(grantOwner.getNumber());

		//TODO.. se calhar passamos os metodos do first name a last name para o Person...
		InfoPerson infoPerson = new InfoPerson();
		infoPerson.setNome(grantOwner.getPerson().getNome());
		//infoListGrantOwnerByOrder.setFirstName(infoPerson.getFirstName());
		//infoListGrantOwnerByOrder.setLastName(infoPerson.getPersonLastName());
		
		IGrantContract grantContract = (IGrantContract)activeContracts.get(i);
		infoListGrantOwnerByOrder.setGrantType(grantContract.getGrantType().getSigla());

		//infoListGrantOwnerByOrder.setInsurancePaymentEntity(); TODO.. to be made
		
		/*
		 * Setting this boolean for presentation reasons only. Every entry that is repeated
		 * is set to be red so that the correction can be made.
		 */
		if(i == 0) {
			infoListGrantOwnerByOrder.setRepeated(new Boolean(false));
		}
		else {
			infoListGrantOwnerByOrder.setRepeated(new Boolean(true));
		}				
		result.add(infoListGrantOwnerByOrder);
	}
}
else
{
	//TODO.. listam-se os bolseiros sem contractos activos????
}
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
//
//Read All elements, and insert the result at the end of the list
//		Integer countAllGrantOwner = persistentGrantOwner.countAll();
//		infoGrantOwnerList.add(countAllGrantOwner);

}
