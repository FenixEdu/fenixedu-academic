/*
 * Created on 10/11/2003
 * 
 */
package ServidorAplicacao.Servico.grant.owner;

import java.util.ArrayList;
import java.util.List;

import DataBeans.grant.owner.InfoGrantOwner;
import DataBeans.util.Cloner;
import Dominio.IPessoa;
import Dominio.grant.owner.IGrantOwner;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPessoaPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.grant.IPersistentGrantOwner;
import Util.TipoDocumentoIdentificacao;

/**
 * @author  Barbosa
 * @author  Pica
 *  
 */
public class SearchGrantOwner implements IServico {

	private static SearchGrantOwner service = new SearchGrantOwner();
	/**
	 * The singleton access method of this class.
	 */
	public static SearchGrantOwner getService() {
		return service;
	}
	/**
	 * The constructor of this class.
	 */
	private SearchGrantOwner() {
	}
	/**
	 * The name of the service
	 */
	public final String getNome() {
		return "SearchGrantOwner";
	}

	/**
	 * Executes the service.
	 */
	public List run(String name, String IdNumber, Integer IdType)
		throws FenixServiceException {
		ISuportePersistente persistentSupport = null;
		IPessoaPersistente persistentPerson = null;
		IPersistentGrantOwner persistentGrantOwner = null;

		TipoDocumentoIdentificacao type = null;
		List grantOwnerList = new ArrayList();
		List personList = new ArrayList();
		IGrantOwner grantOwner = null;
		IPessoa person = null;

		try {
			persistentSupport = SuportePersistenteOJB.getInstance();
			persistentPerson = persistentSupport.getIPessoaPersistente();
			persistentGrantOwner = persistentSupport.getIPersistentGrantOwner();

			// Search by ID number and ID type
			if ((IdNumber != null) && (IdType != null)) {
				type = new TipoDocumentoIdentificacao(IdType);
				person =
					persistentPerson.lerPessoaPorNumDocIdETipoDocId(
						IdNumber,
						type);
			}
			//Search by name IF search by ID has failed
			if (person == null) {
				if (name != null)
					personList = persistentPerson.findPersonByName(name);
			} else
				personList.add(person);

			if (personList.size() > 0) {
				//Get all the grantOwners associated with each person in list
				for (int i = 0; i < personList.size(); i++) {
					InfoGrantOwner newInfoGrantOwner = new InfoGrantOwner();
					IPessoa newPerson = (IPessoa) personList.get(i);
					grantOwner =
						persistentGrantOwner.readGrantOwnerByPerson(
							newPerson.getIdInternal());

					if (grantOwner != null)
						//The person is a GrantOwner 
						newInfoGrantOwner =
							Cloner.copyIGrantOwner2InfoGrantOwner(grantOwner);
					else {
						//The person is NOT a GrantOwner
						newInfoGrantOwner.setPersonInfo(
							Cloner.copyIPerson2InfoPerson(newPerson));
					}
					grantOwnerList.add(newInfoGrantOwner);
				}
			}
		} catch (ExcepcaoPersistencia excepcaoPersistencia) {
			throw new FenixServiceException(excepcaoPersistencia.getMessage());
		}
		return grantOwnerList;
	}
}
