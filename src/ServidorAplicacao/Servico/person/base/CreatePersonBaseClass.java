/*
 * Created on 28/10/2003
 */

package ServidorAplicacao.Servico.person.base;

import DataBeans.InfoPerson;
import Dominio.ICountry;
import Dominio.IPersonRole;
import Dominio.IPessoa;
import Dominio.PersonRole;
import Dominio.Pessoa;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.security.PasswordEncryptor;
import ServidorAplicacao.utils.GeneratePassword;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCountry;
import ServidorPersistente.IPersistentPersonRole;
import ServidorPersistente.IPessoaPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.exceptions.ExistingPersistentException;
import Util.RoleType;

/**
 * @author  Barbosa
 * @author  Pica
 */

/*
 * This class is the base class to create a new person. 
 * Each class that needs to create a new object of class Pessoa (person) 
 *  should extend THIS class.
 * 
 */

public class CreatePersonBaseClass {

	public IPessoa createPersonBase(InfoPerson newPerson, ISuportePersistente persistentSupport, IPessoaPersistente persistentPerson,IPersistentPersonRole persistentPersonRole)
		throws FenixServiceException {
		
		IPersistentCountry persistentCountry = null;

		IPessoa person = null;

		try {
			persistentCountry = persistentSupport.getIPersistentCountry();

			//Check if the person Exists
			person =
				persistentPerson.lerPessoaPorNumDocIdETipoDocId(
					newPerson.getNumeroDocumentoIdentificacao(),
					newPerson.getTipoDocumentoIdentificacao());

			// Create the new Person if it does not exist
			if (person == null) {
				person = new Pessoa();
				
				IPersonRole personRole = new PersonRole();
				persistentPersonRole.simpleLockWrite(personRole);
				personRole.setPerson(person);
				personRole.setRole(
					persistentSupport.getIPersistentRole().readByRoleType(RoleType.PERSON));
			} 
			//lock person for WRITE
			persistentPerson.simpleLockWrite(person);

			if (newPerson.getNome() != null)
				person.setNome(newPerson.getNome());
			if (newPerson.getNumeroDocumentoIdentificacao() != null)
				person.setNumeroDocumentoIdentificacao(
					newPerson.getNumeroDocumentoIdentificacao());
			if (newPerson.getTipoDocumentoIdentificacao() != null)
				person.setTipoDocumentoIdentificacao(
					newPerson.getTipoDocumentoIdentificacao());

			if (newPerson.getCodigoFiscal() != null)
				person.setCodigoFiscal(newPerson.getCodigoFiscal());
			if (newPerson.getCodigoPostal() != null)
				person.setCodigoPostal(newPerson.getCodigoPostal());
			if (newPerson.getConcelhoMorada() != null)
				person.setConcelhoMorada(newPerson.getConcelhoMorada());
			if (newPerson.getConcelhoNaturalidade() != null)
				person.setConcelhoNaturalidade(
					newPerson.getConcelhoNaturalidade());
			if (newPerson.getDataEmissaoDocumentoIdentificacao() != null)
				person.setDataEmissaoDocumentoIdentificacao(
					newPerson.getDataEmissaoDocumentoIdentificacao());
			if (newPerson.getDataValidadeDocumentoIdentificacao() != null)
				person.setDataValidadeDocumentoIdentificacao(
					newPerson.getDataValidadeDocumentoIdentificacao());
			if (newPerson.getDistritoMorada() != null)
				person.setDistritoMorada(newPerson.getDistritoMorada());
			if (newPerson.getDistritoNaturalidade() != null)
				person.setDistritoNaturalidade(
					newPerson.getDistritoNaturalidade());
			if (newPerson.getEmail() != null)
				person.setEmail(newPerson.getEmail());
			if (newPerson.getEnderecoWeb() != null)
				person.setEnderecoWeb(newPerson.getEnderecoWeb());
			if (newPerson.getEstadoCivil() != null)
				person.setEstadoCivil(newPerson.getEstadoCivil());
			if (newPerson.getFreguesiaMorada() != null)
				person.setFreguesiaMorada(newPerson.getFreguesiaMorada());
			if (newPerson.getFreguesiaNaturalidade() != null)
				person.setFreguesiaNaturalidade(
					newPerson.getFreguesiaNaturalidade());
			if (newPerson.getLocalEmissaoDocumentoIdentificacao() != null)
				person.setLocalEmissaoDocumentoIdentificacao(
					newPerson.getLocalEmissaoDocumentoIdentificacao());
			if (newPerson.getLocalidade() != null)
				person.setLocalidade(newPerson.getLocalidade());
			if (newPerson.getLocalidadeCodigoPostal() != null)
				person.setLocalidadeCodigoPostal(
					newPerson.getLocalidadeCodigoPostal());
			if (newPerson.getMorada() != null)
				person.setMorada(newPerson.getMorada());
			if (newPerson.getNacionalidade() != null)
				person.setNacionalidade(newPerson.getNacionalidade());
			if (newPerson.getNascimento() != null)
				person.setNascimento(newPerson.getNascimento());
			if (newPerson.getNomeMae() != null)
				person.setNomeMae(newPerson.getNomeMae());
			if (newPerson.getNomePai() != null)
				person.setNomePai(newPerson.getNomePai());
			if (newPerson.getNumContribuinte() != null)
				person.setNumContribuinte(newPerson.getNumContribuinte());

			if (newPerson.getProfissao() != null)
				person.setProfissao(newPerson.getProfissao());
			if (newPerson.getSexo() != null)
				person.setSexo(newPerson.getSexo());
			if (newPerson.getTelefone() != null)
				person.setTelefone(newPerson.getTelefone());
			if (newPerson.getTelemovel() != null)
				person.setTelemovel(newPerson.getTelemovel());

			if (newPerson.getInfoPais() != null) {
				ICountry country = persistentCountry.readCountryByCode(newPerson.getInfoPais().getCode());
				person.setPais(country);
			}				
			
			//Generate person's Password
			if(person.getPassword() == null)
				person.setPassword(PasswordEncryptor.encryptPassword(GeneratePassword.generatePassword()));

		} catch (ExistingPersistentException ex) {
			throw new ExistingServiceException(ex);
		} catch (ExcepcaoPersistencia ex) {
			FenixServiceException newEx =
				new FenixServiceException("Persistence layer error");
			newEx.fillInStackTrace();
			throw newEx;
		}
		return person;
	}
}
