/*
 * Created on 28/10/2003
 */

package net.sourceforge.fenixedu.applicationTier.Servico.person.base;

import java.util.ArrayList;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.security.PasswordEncryptor;
import net.sourceforge.fenixedu.applicationTier.utils.GeneratePassword;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.ICountry;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCountry;
import net.sourceforge.fenixedu.persistenceTier.IPersistentPersonRole;
import net.sourceforge.fenixedu.persistenceTier.IPessoaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.exceptions.ExistingPersistentException;

/**
 * @author Barbosa
 * @author Pica
 */

/*
 * This class is the base class to create a new person. Each class that needs to
 * create a new object of class Person (person) should extend THIS class.
 *  
 */

public class CreatePersonBaseClass {
    public static IPerson setPersonAttributes(IPerson personToLock, InfoPerson newPerson,
            IPersistentCountry pCountry) throws ExcepcaoPersistencia {
        personToLock.setNome(newPerson.getNome());
        if (newPerson.getNumeroDocumentoIdentificacao() != null)
            personToLock.setNumeroDocumentoIdentificacao(newPerson.getNumeroDocumentoIdentificacao());
        if (newPerson.getTipoDocumentoIdentificacao() != null)
            personToLock.setIdDocumentType(newPerson.getTipoDocumentoIdentificacao());

        personToLock.setCodigoFiscal(newPerson.getCodigoFiscal());
        personToLock.setCodigoPostal(newPerson.getCodigoPostal());
        personToLock.setConcelhoMorada(newPerson.getConcelhoMorada());
        personToLock.setConcelhoNaturalidade(newPerson.getConcelhoNaturalidade());
        personToLock.setDataEmissaoDocumentoIdentificacao(newPerson
                .getDataEmissaoDocumentoIdentificacao());
        personToLock.setDataValidadeDocumentoIdentificacao(newPerson
                .getDataValidadeDocumentoIdentificacao());
        personToLock.setDistritoMorada(newPerson.getDistritoMorada());
        personToLock.setDistritoNaturalidade(newPerson.getDistritoNaturalidade());
        personToLock.setEmail(newPerson.getEmail());
        personToLock.setEnderecoWeb(newPerson.getEnderecoWeb());
        personToLock.setMaritalStatus(newPerson.getMaritalStatus());
        personToLock.setFreguesiaMorada(newPerson.getFreguesiaMorada());
        personToLock.setFreguesiaNaturalidade(newPerson.getFreguesiaNaturalidade());
        personToLock.setLocalEmissaoDocumentoIdentificacao(newPerson
                .getLocalEmissaoDocumentoIdentificacao());
        personToLock.setLocalidade(newPerson.getLocalidade());
        personToLock.setLocalidadeCodigoPostal(newPerson.getLocalidadeCodigoPostal());
        personToLock.setMorada(newPerson.getMorada());
        personToLock.setNacionalidade(newPerson.getNacionalidade());
        personToLock.setNascimento(newPerson.getNascimento());
        personToLock.setNomeMae(newPerson.getNomeMae());
        personToLock.setNomePai(newPerson.getNomePai());
        personToLock.setNumContribuinte(newPerson.getNumContribuinte());

        personToLock.setProfissao(newPerson.getProfissao());
        personToLock.setGender(newPerson.getSexo());
        personToLock.setTelefone(newPerson.getTelefone());
        personToLock.setTelemovel(newPerson.getTelemovel());

        if (newPerson.getInfoPais() != null) {
            ICountry country = (ICountry) pCountry.readByOID(Country.class, newPerson.getInfoPais()
                    .getIdInternal());
            personToLock.setPais(country);
        }

        //Generate person's Password
        if (personToLock.getPassword() == null)
            personToLock.setPassword(PasswordEncryptor.encryptPassword(GeneratePassword
                    .generatePassword()));

        return personToLock;
    }

    public static IPerson createPersonBase(IPerson personToLock, InfoPerson newPerson,
            ISuportePersistente sp, IPessoaPersistente pPerson, IPersistentPersonRole pPersonRole)
            throws FenixServiceException {
        IPersistentCountry pCountry = null;

        //IPerson person = null;
        try {
            pCountry = sp.getIPersistentCountry();

            //Check if the person Exists
            if (newPerson.getIdInternal() != null && !newPerson.getIdInternal().equals(new Integer(0)))
                personToLock = (IPerson) pPerson.readByOID(Person.class, newPerson.getIdInternal());

            boolean islocked = false;
            //Create the new Person if it does not exist
            if (personToLock == null) {
                personToLock = new Person();
                pPerson.simpleLockWrite(personToLock);

                personToLock.getPersonRoles().add(
                        sp.getIPersistentRole().readByRoleType(RoleType.PERSON));
                islocked = true;
            }
            if (!islocked) {
                //lock person for WRITE
                pPerson.simpleLockWrite(personToLock);
            }

            personToLock = setPersonAttributes(personToLock, newPerson, pCountry);

        } catch (ExistingPersistentException ex) {
            throw new ExistingServiceException(ex);
        } catch (ExcepcaoPersistencia ex) {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx;
        }
        return personToLock;
    }
}
