package middleware.personAndEmployee;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;

import Dominio.IPessoa;
import Dominio.Role;
import ServidorAplicacao.Servico.exceptions.NotExecuteException;
import Util.RoleType;

/**
 * @author  Ivo Brandão
 */
public class ServicoSeguroPessoasInactivas {

	private String _ficheiro = null;
	private String _delimitador = new String(";");

	private Collection _listaPessoasFromFile = null;
	private List _listaPessoasFromDB = null;

	/** Construtor */
	public ServicoSeguroPessoasInactivas(String[] args) {
		_ficheiro = "E:/Projectos/_carregamentos/pessoa-activos.dat"; //args[0];
	}

	/** executa a actualizacao da tabela Pessoa na Base de Dados */
	public static void main(String[] args) throws Exception {
		ServicoSeguroPessoasInactivas servico = new ServicoSeguroPessoasInactivas(args);

		//Read all persons from file
		try {
			System.out.println("Reading persons from File");
			servico._listaPessoasFromFile = LeituraFicheiroPessoa.lerFicheiro(servico._ficheiro, servico._delimitador);
			System.out.println("Finished read persons from File(" + servico._listaPessoasFromFile.size() + ")");
		} catch (Exception nee) {
			throw new NotExecuteException(nee.getMessage());
		}

		desactivarPessoas(servico);
		System.out.println("  Done !");
	}

	private static void desactivarPessoas(ServicoSeguroPessoasInactivas servico)
	{
		if (servico._listaPessoasFromFile != null) {
			//Open databases
			PersistenceBroker broker = PersistenceBrokerFactory.defaultPersistenceBroker();
			broker.clearCache();
			broker.beginTransaction();

			//Read all person from Data Base
			servico._listaPessoasFromDB = PersonUtils.readAllPersonsEmployee(broker);
			if (servico._listaPessoasFromDB != null) {

				Iterator iterador = servico._listaPessoasFromDB.iterator();
				IPessoa pessoaFromDB = null;
				int counter = 0;
				while (iterador.hasNext()) {
					pessoaFromDB = (IPessoa) iterador.next();
					try {
						final IPessoa pessoaPredicate = pessoaFromDB;
						//Find if the person from BD exists in file with active persons.
						IPessoa pessoaFromFile = (IPessoa) CollectionUtils.find(servico._listaPessoasFromFile, new Predicate() {
							public boolean evaluate(Object obj) {
								IPessoa pessoa = (IPessoa) obj;
								return pessoa.getNumeroDocumentoIdentificacao().equals(pessoaPredicate.getNumeroDocumentoIdentificacao())
									&& pessoa.getTipoDocumentoIdentificacao().equals(pessoaPredicate.getTipoDocumentoIdentificacao());
							}
						});
						
						if (pessoaFromFile == null) {
							//person is inactive, then delete roles and put password null
							pessoaFromDB.setPassword(null);
							pessoaFromDB.setPersonRoles(null);
							pessoaFromDB.setUsername(
									"INA" + pessoaFromDB.getNumeroDocumentoIdentificacao());

							pessoaFromDB.getPersonRoles().remove(
									PersonUtils.descobreRole(broker, RoleType.EMPLOYEE));
							pessoaFromDB.getPersonRoles().remove(PersonUtils.descobreRole(broker, RoleType.TEACHER));

							Role rolePerson = PersonUtils.descobreRole(broker, RoleType.PERSON);
							if (pessoaFromDB.getPersonRoles().size() == 1
									&& pessoaFromDB.getPersonRoles().contains(rolePerson))
							{
								pessoaFromDB.getPersonRoles().remove(rolePerson);
							}					
							
							
							
							broker.store(pessoaFromDB);
							counter++;
							//System.out.println("Inactive person: " + pessoaFromDB.getNumeroDocumentoIdentificacao() + "-" + pessoaFromDB.getTipoDocumentoIdentificacao().getTipo());
						}
					} catch (Exception e) {
						e.printStackTrace(System.out);
						System.out.println("Error with person: " + pessoaFromDB);
						continue;
					}
					
				}
				System.out.println("Inactivei "+ counter + " pessoas.");
			}

			//close databases
			broker.commitTransaction();
		}
	}
}