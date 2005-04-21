/*
 * ChangeMasterDegreeCandidate.java O Servico ChangeMasterDegreeCandidate altera
 * a informacao de um candidato de Mestrado Nota : E suposto os campos
 * (numeroCandidato, anoCandidatura, chaveCursoMestrado, username) nao se
 * puderem alterar Created on 02 de Dezembro de 2002, 16:25
 */

/**
 * Autores : - Nuno Nunes (nmsn@rnl.ist.utl.pt) - Joana Mota
 * (jccm@rnl.ist.utl.pt)
 */

package net.sourceforge.fenixedu.applicationTier.Servico.commons.student;

import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.ICountry;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.exceptions.ExistingPersistentException;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class ChangePersonalStudentInfo implements IService {

    /**
     * The actor of this class.
     */
    public ChangePersonalStudentInfo() {
    }

    public InfoPerson run(InfoPerson newInfoPerson) throws ExcepcaoInexistente, FenixServiceException,
            ExistingPersistentException, ExcepcaoPersistencia {

        ISuportePersistente sp = null;
        IPerson person = null;

        try {
            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            person = (IPerson) sp.getIPessoaPersistente().readByOID(Person.class,
                    newInfoPerson.getIdInternal());
        } catch (ExcepcaoPersistencia ex) {

            throw new FenixServiceException("Persistence layer error", ex);
        }

        if (person == null) {
            throw new ExcepcaoInexistente("Unknown Person !!");
        }
        sp.getIPessoaPersistente().simpleLockWrite(person);

        // Get new Country
        ICountry nationality = null;
        if ((newInfoPerson.getInfoPais() == null)
                || (newInfoPerson.getInfoPais().getNationality().length() == 0)) {
            person.setPais(null);
        } else {
            try {
                if ((person.getPais() == null)
                        || (!newInfoPerson.getInfoPais().getNationality().equals(
                                person.getPais().getNationality()))) {
                    nationality = sp.getIPersistentCountry().readCountryByNationality(
                            newInfoPerson.getInfoPais().getNationality());
                    person.setPais(nationality);
                }
            } catch (ExcepcaoPersistencia ex) {
                FenixServiceException newEx = new FenixServiceException("Persistence layer error");
                newEx.fillInStackTrace();
                throw newEx;
            }
        }

        // Change personal Information

        person.setNascimento(newInfoPerson.getNascimento());
        person
                .setDataEmissaoDocumentoIdentificacao(newInfoPerson
                        .getDataEmissaoDocumentoIdentificacao());
        person.setDataValidadeDocumentoIdentificacao(newInfoPerson
                .getDataValidadeDocumentoIdentificacao());
        person.setIdDocumentType(newInfoPerson.getTipoDocumentoIdentificacao());
        person.setNumeroDocumentoIdentificacao(newInfoPerson.getNumeroDocumentoIdentificacao());
        person.setLocalEmissaoDocumentoIdentificacao(newInfoPerson
                .getLocalEmissaoDocumentoIdentificacao());
        person.setNome(newInfoPerson.getNome());

        person.setGender(newInfoPerson.getSexo());
        person.setMaritalStatus(newInfoPerson.getMaritalStatus());

        person.setNomePai(newInfoPerson.getNomePai());
        person.setNomeMae(newInfoPerson.getNomeMae());
        person.setFreguesiaNaturalidade(newInfoPerson.getFreguesiaNaturalidade());
        person.setConcelhoNaturalidade(newInfoPerson.getConcelhoNaturalidade());
        person.setDistritoNaturalidade(newInfoPerson.getDistritoNaturalidade());
        person.setLocalidadeCodigoPostal(newInfoPerson.getLocalidadeCodigoPostal());
        person.setMorada(newInfoPerson.getMorada());
        person.setLocalidade(newInfoPerson.getLocalidade());
        person.setCodigoPostal(newInfoPerson.getCodigoPostal());
        person.setFreguesiaMorada(newInfoPerson.getFreguesiaMorada());
        person.setConcelhoMorada(newInfoPerson.getConcelhoMorada());
        person.setDistritoMorada(newInfoPerson.getDistritoMorada());
        person.setTelefone(newInfoPerson.getTelefone());
        person.setTelemovel(newInfoPerson.getTelemovel());
        person.setEmail(newInfoPerson.getEmail());
        person.setEnderecoWeb(newInfoPerson.getEnderecoWeb());
        person.setNumContribuinte(newInfoPerson.getNumContribuinte());
        person.setProfissao(newInfoPerson.getProfissao());
        person.setNacionalidade(newInfoPerson.getNacionalidade());

        InfoPerson infoPerson = Cloner.copyIPerson2InfoPerson(person);

        return infoPerson;
    }
}