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
    public static IPessoa setPersonAttributes(
        IPessoa personToLock,
        InfoPerson newPerson,
        IPersistentCountry pCountry)
        throws ExcepcaoPersistencia
    {
        personToLock.setNome(newPerson.getNome());
        if (newPerson.getNumeroDocumentoIdentificacao() != null)
            personToLock.setNumeroDocumentoIdentificacao(newPerson.getNumeroDocumentoIdentificacao());
        if (newPerson.getTipoDocumentoIdentificacao() != null)
            personToLock.setTipoDocumentoIdentificacao(newPerson.getTipoDocumentoIdentificacao());

        personToLock.setCodigoFiscal(newPerson.getCodigoFiscal());
        personToLock.setCodigoPostal(newPerson.getCodigoPostal());
        personToLock.setConcelhoMorada(newPerson.getConcelhoMorada());
        personToLock.setConcelhoNaturalidade(newPerson.getConcelhoNaturalidade());
        personToLock.setDataEmissaoDocumentoIdentificacao(
            newPerson.getDataEmissaoDocumentoIdentificacao());
        personToLock.setDataValidadeDocumentoIdentificacao(
            newPerson.getDataValidadeDocumentoIdentificacao());
        personToLock.setDistritoMorada(newPerson.getDistritoMorada());
        personToLock.setDistritoNaturalidade(newPerson.getDistritoNaturalidade());
        personToLock.setEmail(newPerson.getEmail());
        personToLock.setEnderecoWeb(newPerson.getEnderecoWeb());
        personToLock.setEstadoCivil(newPerson.getEstadoCivil());
        personToLock.setFreguesiaMorada(newPerson.getFreguesiaMorada());
        personToLock.setFreguesiaNaturalidade(newPerson.getFreguesiaNaturalidade());
        personToLock.setLocalEmissaoDocumentoIdentificacao(
            newPerson.getLocalEmissaoDocumentoIdentificacao());
        personToLock.setLocalidade(newPerson.getLocalidade());
        personToLock.setLocalidadeCodigoPostal(newPerson.getLocalidadeCodigoPostal());
        personToLock.setMorada(newPerson.getMorada());
        personToLock.setNacionalidade(newPerson.getNacionalidade());
        personToLock.setNascimento(newPerson.getNascimento());
        personToLock.setNomeMae(newPerson.getNomeMae());
        personToLock.setNomePai(newPerson.getNomePai());
        personToLock.setNumContribuinte(newPerson.getNumContribuinte());

        personToLock.setProfissao(newPerson.getProfissao());
        personToLock.setSexo(newPerson.getSexo());
        personToLock.setTelefone(newPerson.getTelefone());
        personToLock.setTelemovel(newPerson.getTelemovel());

        if (newPerson.getInfoPais() != null)
        {
            ICountry country = pCountry.readCountryByCode(newPerson.getInfoPais().getCode());
            personToLock.setPais(country);
        }

        //Generate person's Password
        if (personToLock.getPassword() == null)
            personToLock.setPassword(
                PasswordEncryptor.encryptPassword(GeneratePassword.generatePassword()));

        return personToLock;
    }

    public static IPessoa createPersonBase(
        IPessoa personToLock,
        InfoPerson newPerson,
        ISuportePersistente sp,
        IPessoaPersistente pPerson,
        IPersistentPersonRole pPersonRole)
        throws FenixServiceException
    {
        IPersistentCountry pCountry = null;

        //IPessoa person = null;
        try
        {
            pCountry = sp.getIPersistentCountry();

            //Check if the person Exists
            personToLock =
                pPerson.lerPessoaPorNumDocIdETipoDocId(
                    newPerson.getNumeroDocumentoIdentificacao(),
                    newPerson.getTipoDocumentoIdentificacao());

            // Create the new Person if it does not exist
            if (personToLock == null)
            {
                personToLock = new Pessoa();

                IPersonRole personRole = new PersonRole();
                pPersonRole.simpleLockWrite(personRole);
                personRole.setPerson(personToLock);
                personRole.setRole(sp.getIPersistentRole().readByRoleType(RoleType.PERSON));
            }
            //lock person for WRITE
            pPerson.simpleLockWrite(personToLock);
            
            personToLock = setPersonAttributes(personToLock,newPerson,pCountry);

        } catch (ExistingPersistentException ex)
        {
            throw new ExistingServiceException(ex);
        } catch (ExcepcaoPersistencia ex)
        {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx;
        }
        return personToLock;
    }
}
