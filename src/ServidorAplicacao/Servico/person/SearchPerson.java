/*
 * Created on 22/Dez/2003
 *  
 */
package ServidorAplicacao.Servico.person;

import java.util.HashMap;
import java.util.List;

import DataBeans.InfoObject;
import DataBeans.util.Cloner;
import Dominio.IDomainObject;
import Dominio.IPessoa;
import ServidorAplicacao.Servico.framework.SearchService;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPessoaPersistente;
import ServidorPersistente.ISuportePersistente;

/**
 * @author Tânia Pousão
 *  
 */
public class SearchPerson extends SearchService
{
	static private SearchPerson service = new SearchPerson();

	private SearchPerson()
	{

	}

	public static SearchPerson getService()
	{
		return service;
	}

	public String getNome()
	{
		return "SearchPerson";
	}

	protected InfoObject cloneDomainObject(IDomainObject object)
	{
		return Cloner.copyIPerson2InfoPerson((IPessoa) object);
	}

	protected List doSearch(HashMap parametersSearch, ISuportePersistente sp)
		throws ExcepcaoPersistencia
	{
		System.out.println("SearchPerson Service: doSearch");
		IPessoaPersistente persistentPerson = sp.getIPessoaPersistente();

		String name = (String) parametersSearch.get(new String("name"));
		String email = (String) parametersSearch.get(new String("email"));
		String username = (String) parametersSearch.get(new String("username"));

		return persistentPerson.findPersonByNameAndEmailAndUsername(name, email, username);
	}

}
