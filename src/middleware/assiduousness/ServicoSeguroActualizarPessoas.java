package middleware.assiduousness;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import middleware.RoleFunctions;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.Query;
import org.apache.ojb.broker.query.QueryByCriteria;

import Dominio.IPersonRole;
import Dominio.IPessoa;
import Dominio.Pessoa;
import Dominio.Role;
import ServidorAplicacao.Servico.exceptions.NotExecuteException;
import Util.LeituraFicheiroPessoa;
import Util.RoleType;

/**
 * @author  Ivo Brandão
 */
public class ServicoSeguroActualizarPessoas {

	private String _ficheiro = null;
	private Collection _listaPessoas = null;
	private Pessoa _pessoa = null;
	private String _delimitador = new String(";");

	/** Construtor */
	public ServicoSeguroActualizarPessoas(String[] args) {
		_ficheiro = args[0];
	}

	/** executa a actualizacao da tabela Pessoa na Base de Dados */
	public static void main(String[] args) throws Exception {
		ServicoSeguroActualizarPessoas servico = new ServicoSeguroActualizarPessoas(args);
		Role role = null;
		int newPersons = 0;
		int newRoles = 0;

		try {
			servico._listaPessoas =
				LeituraFicheiroPessoa.lerFicheiro(servico._ficheiro, servico._delimitador);
		} catch (NotExecuteException nee) {
			throw new NotExecuteException(nee.getMessage());
		}

		System.out.println("Converting Persons " + servico._listaPessoas.size() + " ... ");


		PersistenceBroker broker = PersistenceBrokerFactory.defaultPersistenceBroker();
		broker.clearCache();
		broker.beginTransaction();
		
		
		try {

			if (servico._listaPessoas != null) {
				Iterator iterador = servico._listaPessoas.iterator();
				Pessoa pessoaBD = null;
	
				/* ciclo que percorre a Collection de Pessoas */
				while (iterador.hasNext()) {
					
					servico._pessoa = (Pessoa)iterador.next();

					Criteria criteria = new Criteria();
					Query query = null;

					criteria.addEqualTo("numeroDocumentoIdentificacao", servico._pessoa.getNumeroDocumentoIdentificacao());
					criteria.addEqualTo("tipoDocumentoIdentificacao", servico._pessoa.getTipoDocumentoIdentificacao());
					query = new QueryByCriteria(Pessoa.class,criteria);
					List result = (List) broker.getCollectionByQuery(query);	
		
					IPessoa person2Write = null;
					// A Pessoa nao Existe
					if (result.size() == 0){
						person2Write = (IPessoa) servico._pessoa;
						newPersons++;
					} else {
						// A Pessoa Existe
						person2Write = (IPessoa) result.get(0);
						updatePerson((Pessoa) person2Write, (Pessoa) servico._pessoa);
					}
					
					IPersonRole personRole = RoleFunctions.readPersonRole((Pessoa) person2Write, RoleType.PERSON, broker);
					if (personRole == null){
						criteria = new Criteria();
						query = null;
						criteria.addEqualTo("roleType", RoleType.PERSON);
						query = new QueryByCriteria(Role.class, criteria);

						result = (List) broker.getCollectionByQuery(query);
		
						if (result.size() == 0){
							throw new Exception("Role Desconhecido !!!");
						} else {
							if (person2Write.getPersonRoles() == null){
								person2Write.setPersonRoles(new ArrayList()); 
							}
							person2Write.getPersonRoles().add((Role) result.get(0)); 
							newRoles++;
						}
							  
					}
					
					broker.store(person2Write);
				}
			}
		} catch(Exception e){
			throw new Exception("Erro a converter a pessoa " + servico._pessoa + "\n" + e);							
		}
		broker.commitTransaction();
		System.out.println("New persons added : " + newPersons);
		System.out.println("New Roles added : " + newRoles);
		System.out.println("  Done !");
	}

	/** retorna a lista de pessoas */
	public Collection getListaPessoas() {
		return _listaPessoas;
	}
	
	
	
	private static void updatePerson(Pessoa person2Write, Pessoa person2Convert) throws Exception{

		try{
			// Password Backup
			
			String password = new String(person2Write.getPassword());
			Integer internalCode = new Integer(person2Write.getIdInternal().intValue());
			
			BeanUtils.copyProperties(person2Write, person2Convert);

			person2Write.setIdInternal(internalCode);
			person2Write.setPassword(password);
		} catch(Exception e){
			System.out.println("Erro a converter a Pessoa " + person2Convert.getNome());
			throw new Exception(e);
		}
	}


}