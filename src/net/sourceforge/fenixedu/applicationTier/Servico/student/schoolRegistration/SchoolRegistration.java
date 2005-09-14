/*
 * Created on Jul 19, 2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student.schoolRegistration;

import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.dataTransferObject.student.schoolRegistration.InfoResidenceCandidacy;
import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.ICountry;
import net.sourceforge.fenixedu.domain.IExecutionYear;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.IRole;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.IResidenceCandidacies;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCountry;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.IPersistentRole;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudent;
import net.sourceforge.fenixedu.persistenceTier.IPessoaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.PeriodState;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 */
public class SchoolRegistration implements IService {

    public SchoolRegistration() {
        super();
    }

    public Boolean run(IUserView userView, InfoPerson infoPerson,
            InfoResidenceCandidacy infoResidenceCandidacy) throws ExcepcaoPersistencia {

        ISuportePersistente suportePersistente = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentStudent persistentStudent = suportePersistente.getIPersistentStudent();
        String username = userView.getUtilizador();
        IStudent student = persistentStudent.readByUsername(username);
        IPessoaPersistente pessoaPersistente = suportePersistente.getIPessoaPersistente();

        IPerson pessoa = (IPerson) pessoaPersistente.readByOID(Person.class, infoPerson.getIdInternal());

        if (isStudentRegistered(pessoa)) {
            return Boolean.FALSE;
        }
        updatePersonalInfo(suportePersistente, infoPerson, pessoa);
        writeResidenceCandidacy(student, infoResidenceCandidacy);
        updateStudentInfo(suportePersistente, student);

        return Boolean.TRUE;
    }

    private boolean isStudentRegistered(IPerson pessoa) {

        boolean result;
        result = CollectionUtils.exists(pessoa.getPersonRoles(), new Predicate() {

            public boolean evaluate(Object arg0) {
                IRole role = (IRole) arg0;
                return role.getRoleType().equals(RoleType.FIRST_TIME_STUDENT);
            }
        });

        return !result;
    }

    private void updatePersonalInfo(ISuportePersistente sp, final InfoPerson infoPerson, IPerson person)
            throws ExcepcaoPersistencia {

        IPersistentRole pRole = sp.getIPersistentRole();
        
        List<IRole> roles = pRole.readAll();
        IPersistentCountry pCountry = sp.getIPersistentCountry();
        List<ICountry> countries = (List<ICountry>) pCountry.readAll(Country.class);
        ICountry country = (ICountry) CollectionUtils.find(countries, new Predicate(){

            public boolean evaluate(Object arg0) {
                ICountry country = (ICountry) arg0;
                return country.getNationality().equals(infoPerson.getNacionalidade());
            }});         

        person.edit(infoPerson,country);

        IRole roleToAdd = null;
        IRole roleToRemove = null;
        
        for (IRole role : roles) {
            if(role.getRoleType().equals(RoleType.FIRST_TIME_STUDENT)){
                roleToRemove = role;
            } else {
                if(role.getRoleType().equals(RoleType.STUDENT)){
                    roleToAdd = role;
                }
            }
        }
        
        person.addPersonRoles(roleToAdd);        
        person.removePersonRoles(roleToRemove);
    }
    
    private void writeResidenceCandidacy(IStudent student,
            InfoResidenceCandidacy infoResidenceCandidacy) throws ExcepcaoPersistencia {

        if (infoResidenceCandidacy != null) {
            IResidenceCandidacies residenceCandidacy = DomainFactory.makeResidenceCandidacies();

            residenceCandidacy.setStudent(student);
            residenceCandidacy.setCreationDate(new Date());
            residenceCandidacy.setCandidate(infoResidenceCandidacy.getCandidate());
            residenceCandidacy.setObservations(infoResidenceCandidacy.getObservations());
        }
    }

    private void updateStudentInfo(ISuportePersistente sp, IStudent student)
            throws ExcepcaoPersistencia {

        IPersistentExecutionYear pExecutionYear = sp.getIPersistentExecutionYear();
        List<IExecutionYear> executionYears = (List<IExecutionYear>) pExecutionYear.readAll(ExecutionYear.class);
        IExecutionYear executionYear = (IExecutionYear) CollectionUtils.find(executionYears,new Predicate(){

            public boolean evaluate(Object arg0) {
                IExecutionYear executionYear = (IExecutionYear) arg0;
                return executionYear.getState().equals(PeriodState.CURRENT);
            }});
        
        student.setRegistrationYear(executionYear);
    }

}