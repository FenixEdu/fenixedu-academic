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

public class CreatePersonBaseClass
{
    public IPessoa createPersonBase(
        InfoPerson newPerson,
        ISuportePersistente persistentSupport,
        IPessoaPersistente persistentPerson,
        IPersistentPersonRole persistentPersonRole)
        throws FenixServiceException
    {
        IPersistentCountry persistentCountry = null;

        IPessoa person = null;
        try
        {
            persistentCountry = persistentSupport.getIPersistentCountry();

            //Check if the person Exists
            person =
                persistentPerson.lerPessoaPorNumDocIdETipoDocId(
                    newPerson.getNumeroDocumentoIdentificacao(),
                    newPerson.getTipoDocumentoIdentificacao());

            // Create the new Person if it does not exist
            if (person == null)
            {
                person = new Pessoa();

                IPersonRole personRole = new PersonRole();
                persistentPersonRole.simpleLockWrite(personRole);
                personRole.setPerson(person);
                personRole.setRole(
                    persistentSupport.getIPersistentRole().readByRoleType(RoleType.PERSON));
            }
            //lock person for WRITE
            persistentPerson.simpleLockWrite(person);

            //if (newPerson.getNome() != null)
            person.setNome(newPerson.getNome());
            if (newPerson.getNumeroDocumentoIdentificacao() != null)
                person.setNumeroDocumentoIdentificacao(newPerson.getNumeroDocumentoIdentificacao());
            if (newPerson.getTipoDocumentoIdentificacao() != null)
                person.setTipoDocumentoIdentificacao(newPerson.getTipoDocumentoIdentificacao());

            person.setCodigoFiscal(newPerson.getCodigoFiscal());
            person.setCodigoPostal(newPerson.getCodigoPostal());
            person.setConcelhoMorada(newPerson.getConcelhoMorada());
            person.setConcelhoNaturalidade(newPerson.getConcelhoNaturalidade());
            person.setDataEmissaoDocumentoIdentificacao(
                newPerson.getDataEmissaoDocumentoIdentificacao());
            person.setDataValidadeDocumentoIdentificacao(
                newPerson.getDataValidadeDocumentoIdentificacao());
            person.setDistritoMorada(newPerson.getDistritoMorada());
            person.setDistritoNaturalidade(newPerson.getDistritoNaturalidade());
            person.setEmail(newPerson.getEmail());
            person.setEnderecoWeb(newPerson.getEnderecoWeb());
            person.setEstadoCivil(newPerson.getEstadoCivil());
            person.setFreguesiaMorada(newPerson.getFreguesiaMorada());
            person.setFreguesiaNaturalidade(newPerson.getFreguesiaNaturalidade());
            person.setLocalEmissaoDocumentoIdentificacao(
                newPerson.getLocalEmissaoDocumentoIdentificacao());
            person.setLocalidade(newPerson.getLocalidade());
            person.setLocalidadeCodigoPostal(newPerson.getLocalidadeCodigoPostal());
            person.setMorada(newPerson.getMorada());
            person.setNacionalidade(newPerson.getNacionalidade());
            person.setNascimento(newPerson.getNascimento());
            person.setNomeMae(newPerson.getNomeMae());
            person.setNomePai(newPerson.getNomePai());
            person.setNumContribuinte(newPerson.getNumContribuinte());

            person.setProfissao(newPerson.getProfissao());
            person.setSexo(newPerson.getSexo());
            person.setTelefone(newPerson.getTelefone());
            person.setTelemovel(newPerson.getTelemovel());

            if (newPerson.getInfoPais() != null)
            {
                ICountry country =
                    persistentCountry.readCountryByCode(newPerson.getInfoPais().getCode());
                person.setPais(country);
            }

            //Generate person's Password
            if (person.getPassword() == null)
                person.setPassword(
                    PasswordEncryptor.encryptPassword(GeneratePassword.generatePassword()));

        } catch (ExistingPersistentException ex)
        {
            throw new ExistingServiceException(ex);
        } catch (ExcepcaoPersistencia ex)
        {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx;
        }
        return person;
    }
}
