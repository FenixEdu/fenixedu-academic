/*
 * Created on Jul 19, 2004
 *
 */
package ServidorAplicacao.Servico.student.schoolRegistration;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.commons.collections.Predicate;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoPerson;
import DataBeans.student.schoolRegistration.InfoResidenceCandidacy;
import Dominio.ICountry;
import Dominio.IPessoa;
import Dominio.IRole;
import Dominio.IStudent;
import Dominio.Pessoa;
import Dominio.Role;
import Dominio.student.IPersonalDataUseInquiryAnswers;
import Dominio.student.IResidenceCandidancies;
import Dominio.student.PersonalDataUseInquiryAnswers;
import Dominio.student.ResidenceCandidacies;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.security.PasswordEncryptor;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCountry;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.IPersistentPersonalDataUseInquiryAnswers;
import ServidorPersistente.IPersistentResidenceCandidacies;
import ServidorPersistente.IPersistentRole;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.IPessoaPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.RoleType;

import commons.CollectionUtils;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 */
public class SchoolRegistration implements IService {

    public SchoolRegistration() {
        super();
    }

    public Boolean run(UserView userView, HashMap answers, InfoPerson infoPerson,
            InfoResidenceCandidacy infoResidenceCandidacy) throws ExcepcaoPersistencia,
            FenixServiceException {

        ISuportePersistente suportePersistente = SuportePersistenteOJB.getInstance();
        IPersistentStudent persistentStudent = suportePersistente.getIPersistentStudent();
        String username = userView.getUtilizador();
        Integer studentNumber = new Integer(username.substring(1));
        IStudent student = persistentStudent.readByUsername(username);
        IPessoaPersistente pessoaPersistente = suportePersistente.getIPessoaPersistente();

        IPessoa pessoa = (IPessoa) pessoaPersistente.readByOID(Pessoa.class, infoPerson.getIdInternal());

        if (isStudentRegistered(pessoa)) {
            return Boolean.FALSE;
        }
        updatePersonalInfo(suportePersistente, infoPerson, pessoa);
        writeInquiryAnswers(suportePersistente, student, answers);
        writeResidenceCandidacy(suportePersistente, student, infoResidenceCandidacy);
        updateStudentInfo(suportePersistente, student);

        return Boolean.TRUE;
    }

    private boolean isStudentRegistered(IPessoa pessoa) {

        boolean result;
        result = CollectionUtils.exists(pessoa.getPersonRoles(), new Predicate() {

            public boolean evaluate(Object arg0) {
                IRole role = (Role) arg0;

                IRole newRole = new Role();
                newRole.setRoleType(RoleType.FIRST_TIME_STUDENT);

                return role.equals(newRole);
            }
        });

        return !result;
    }

    private void writeInquiryAnswers(ISuportePersistente sp, IStudent student, HashMap answers)
            throws ExcepcaoPersistencia, FenixServiceException {

        IPersistentPersonalDataUseInquiryAnswers persistentPDUIA = sp
                .getIPersistentPersonalDataUseInquiryAnswers();
        IPersonalDataUseInquiryAnswers personalDataUseInquiryAnswers = persistentPDUIA
                .readAnswersByStudent(student);

        if (personalDataUseInquiryAnswers == null) {
            personalDataUseInquiryAnswers = new PersonalDataUseInquiryAnswers();
        }

        persistentPDUIA.simpleLockWrite(personalDataUseInquiryAnswers);

        personalDataUseInquiryAnswers.setStudent(student);

        Iterator iterator = answers.keySet().iterator();
        while (iterator.hasNext()) {
            String key = (String) iterator.next();
            personalDataUseInquiryAnswers.setAnswer(new Integer(key), new Boolean((String) answers
                    .get(key)));
        }
    }

    private void updatePersonalInfo(ISuportePersistente sp, InfoPerson infoPerson, IPessoa pessoa)
            throws ExcepcaoPersistencia, FenixServiceException {

        IPersistentRole pRole = sp.getIPersistentRole();
        IRole newRole = pRole.readByRoleType(RoleType.STUDENT);
        IPersistentCountry pCountry = sp.getIPersistentCountry();
        ICountry country = pCountry.readCountryByNationality(infoPerson.getNacionalidade());

        IPessoaPersistente pessoaPersistente = sp.getIPessoaPersistente();

        pessoaPersistente.simpleLockWrite(pessoa);

        pessoa.setCodigoPostal(infoPerson.getCodigoPostal());
        pessoa.setConcelhoMorada(infoPerson.getConcelhoMorada());
        pessoa.setConcelhoNaturalidade(infoPerson.getConcelhoNaturalidade());
        pessoa.setDataEmissaoDocumentoIdentificacao(infoPerson.getDataEmissaoDocumentoIdentificacao());
        pessoa.setDataValidadeDocumentoIdentificacao(infoPerson.getDataValidadeDocumentoIdentificacao());
        pessoa.setDistritoMorada(infoPerson.getDistritoMorada());
        pessoa.setDistritoNaturalidade(infoPerson.getDistritoNaturalidade());
        pessoa.setEmail(infoPerson.getEmail());
        pessoa.setAvailableEmail(infoPerson.getAvailableEmail());
        pessoa.setEnderecoWeb(infoPerson.getEnderecoWeb());
        pessoa.setAvailableWebSite(infoPerson.getAvailableWebSite());
        pessoa.setEstadoCivil(infoPerson.getEstadoCivil());
        pessoa.setFreguesiaMorada(infoPerson.getFreguesiaMorada());
        pessoa.setFreguesiaNaturalidade(infoPerson.getFreguesiaNaturalidade());
        pessoa.setLocalidade(infoPerson.getLocalidade());
        pessoa.setLocalidadeCodigoPostal(infoPerson.getLocalidadeCodigoPostal());
        pessoa.setMorada(infoPerson.getMorada());
        pessoa.setNacionalidade(infoPerson.getNacionalidade());
        pessoa.setNomeMae(infoPerson.getNomeMae());
        pessoa.setNomePai(infoPerson.getNomePai());
        pessoa.setNumContribuinte(infoPerson.getNumContribuinte());
        pessoa.setPassword(PasswordEncryptor.encryptPassword(infoPerson.getPassword()));
        pessoa.setProfissao(infoPerson.getProfissao());
        pessoa.setTelefone(infoPerson.getTelefone());
        pessoa.setTelemovel(infoPerson.getTelemovel());
        pessoa.setPais(country);
        pessoa.setAvailablePhoto(infoPerson.getAvailablePhoto());

        //remove firstTimeStudentRole and add studentRole
        CollectionUtils.filter(pessoa.getPersonRoles(), new Predicate() {

            public boolean evaluate(Object arg0) {
                IRole role = (Role) arg0;

                IRole newRole = new Role();
                newRole.setRoleType(RoleType.FIRST_TIME_STUDENT);

                return !role.equals(newRole);
            }
        });

        Object[] obj = { newRole };

        CollectionUtils.addAll(pessoa.getPersonRoles(), obj);

    }

    private void writeResidenceCandidacy(ISuportePersistente sp, IStudent student,
            InfoResidenceCandidacy infoResidenceCandidacy) throws ExcepcaoPersistencia,
            FenixServiceException {

        if (infoResidenceCandidacy != null) {
            IPersistentResidenceCandidacies pResidenceCandidacies = sp
                    .getIPersistentResidenceCandidacies();

            IResidenceCandidancies residenceCandidacy = new ResidenceCandidacies();
            pResidenceCandidacies.simpleLockWrite(residenceCandidacy);

            residenceCandidacy.setStudent(student);
            residenceCandidacy.setCreationDate(new Date());
            residenceCandidacy.setDislocated(infoResidenceCandidacy.getDislocated());
            residenceCandidacy.setObservations(infoResidenceCandidacy.getObservations());
        }
    }

    private void updateStudentInfo(ISuportePersistente sp, IStudent student)
            throws ExcepcaoPersistencia, FenixServiceException {

        IPersistentExecutionYear pExecutionYear = sp.getIPersistentExecutionYear();
        IPersistentStudent persistentStudent = sp.getIPersistentStudent();

        persistentStudent.simpleLockWrite(student);
        student.setRegistrationYear(pExecutionYear.readCurrentExecutionYear());
    }

}