/*
 * Created on Jul 19, 2004
 *
 */
package ServidorAplicacao.Servico.student.schoolRegistration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.Predicate;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoPerson;
import Dominio.ExecutionPeriod;
import Dominio.ICountry;
import Dominio.ICurricularCourse;
import Dominio.IDegreeCurricularPlan;
import Dominio.IEnrollment;
import Dominio.IExecutionCourse;
import Dominio.IExecutionPeriod;
import Dominio.IFrequenta;
import Dominio.IPessoa;
import Dominio.IRole;
import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;
import Dominio.ITurma;
import Dominio.ITurno;
import Dominio.Pessoa;
import Dominio.Role;
import Dominio.Student;
import Dominio.student.ISchoolRegistrationInquiryAnswer;
import Dominio.student.SchoolRegistrationInquiryAnswer;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.enrollment.WriteEnrollment;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.security.PasswordEncryptor;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IFrequentaPersistente;
import ServidorPersistente.IPersistentCountry;
import ServidorPersistente.IPersistentEnrollment;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentRole;
import ServidorPersistente.IPersistentSchoolRegistrationInquiryAnswer;
import ServidorPersistente.IPessoaPersistente;
import ServidorPersistente.IStudentCurricularPlanPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurmaPersistente;
import ServidorPersistente.ITurnoPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.RoleType;
import Util.TipoCurso;
import Util.enrollment.CurricularCourseEnrollmentType;

import commons.CollectionUtils;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 */
public class SchoolRegistration implements IService {

    public SchoolRegistration() {
        super();
    }

    public void run(UserView userView, HashMap answers, InfoPerson infoPerson) 
    	throws ExcepcaoPersistencia, FenixServiceException {

        ISuportePersistente suportePersistente = SuportePersistenteOJB.getInstance();
        String user = userView.getUtilizador();
        Integer studentNumber = new Integer(user.substring(1));
        
        writeInquiryAnswers(suportePersistente,studentNumber,answers);
        enrollStudent1stTime1stYear(suportePersistente,studentNumber);
        updatePersonalInfo(suportePersistente,infoPerson);
        System.out.println("Já estaaaaaaaaa!");        

    }    

	private void writeInquiryAnswers(ISuportePersistente sp, Integer studentNumber, HashMap answers) 
		throws ExcepcaoPersistencia, FenixServiceException {

        IPersistentSchoolRegistrationInquiryAnswer persistentSRIA = sp.getIPersistentSchoolRegistrationInquiryAnswer();
        ISchoolRegistrationInquiryAnswer schoolRegistrationInquiryAnswer = persistentSRIA.readAnswersByStudentNumber(studentNumber);

        if (schoolRegistrationInquiryAnswer == null) {
            schoolRegistrationInquiryAnswer = new SchoolRegistrationInquiryAnswer();
        }
        persistentSRIA.simpleLockWrite(schoolRegistrationInquiryAnswer);
        schoolRegistrationInquiryAnswer.setKeyStudent(studentNumber);

        Iterator iterator = answers.keySet().iterator();
        while (iterator.hasNext()) {
            String key = (String) iterator.next();
            schoolRegistrationInquiryAnswer.setAnswer(new Integer(key), new Boolean((String) answers.get(key)));
        }
    }

    private void enrollStudent1stTime1stYear(ISuportePersistente sp, Integer studentNumber) 
    	throws ExcepcaoPersistencia, FenixServiceException {

        IStudentCurricularPlanPersistente scpPersistent = sp.getIStudentCurricularPlanPersistente();
        IPersistentEnrollment persistentEnrollment = sp.getIPersistentEnrolment();
        IPersistentExecutionPeriod persistentEP = sp.getIPersistentExecutionPeriod();
        IFrequentaPersistente frequentaPersistente = sp.getIFrequentaPersistente();
        ITurnoPersistente turnoPersistente = sp.getITurnoPersistente();

        IStudentCurricularPlan scp = scpPersistent.readActiveStudentCurricularPlan(studentNumber, TipoCurso.LICENCIATURA_OBJ);
        IDegreeCurricularPlan dcp = scp.getDegreeCurricularPlan();
        List curricularCourses = dcp.getCurricularCoursesByYearAndSemesterAndBranch(1, new Integer(1), scp.getBranch());

        WriteEnrollment we = new WriteEnrollment();
        for (int iter = 0; iter < curricularCourses.size(); iter++) {
            ICurricularCourse cc = (ICurricularCourse) curricularCourses.get(iter);
            Integer executionPeriodId = persistentEP.readActualExecutionPeriod().getIdInternal();
            we.run(null, scp.getIdInternal(), cc.getIdInternal(), executionPeriodId, CurricularCourseEnrollmentType.DEFINITIVE, "false");
        }
        
        sp.confirmarTransaccao();
        sp.iniciarTransaccao();
        ITurmaPersistente turmaPersistente = sp.getITurmaPersistente();
        IStudent student = new Student();
        student.setIdInternal(new Integer(1570));
        IExecutionPeriod executionPeriod = new ExecutionPeriod();
        executionPeriod.setIdInternal(new Integer(80));        

        scp = scpPersistent.readActiveStudentCurricularPlan(studentNumber, TipoCurso.LICENCIATURA_OBJ);
        List studentEnrollments = scp.getEnrolments();        
        List executionCourses = new ArrayList();
        for (int iter = 0; iter < studentEnrollments.size(); iter++) {
            IEnrollment enrollment = (IEnrollment) studentEnrollments.get(iter);
            IFrequenta attend = frequentaPersistente.readByEnrolment(enrollment);
            List classes = turmaPersistente.readByExecutionCourse(attend.getDisciplinaExecucao());
            ITurma chosenClass = selectClass(classes);
            
            List filteredShifts = filterShifts(chosenClass.getAssociatedShifts(),attend.getDisciplinaExecucao());
            List shiftsToEnroll = selectShitfsToEnroll(filteredShifts);
            enrollInShifts(shiftsToEnroll);            
        }        
            
    }  

    private void updatePersonalInfo(ISuportePersistente sp, InfoPerson infoPerson) 
    	throws ExcepcaoPersistencia, FenixServiceException {

        IPessoaPersistente pessoaPersistente = sp.getIPessoaPersistente();
        IPessoa pessoa = (IPessoa) pessoaPersistente.readByOID(Pessoa.class, infoPerson.getIdInternal());
        IPersistentRole pRole = sp.getIPersistentRole();
        IRole newRole = pRole.readByRoleType(RoleType.STUDENT);
        IPersistentCountry pCountry = sp.getIPersistentCountry();
        ICountry country = pCountry.readCountryByNationality(infoPerson.getNacionalidade());

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
        pessoa.setPassword(PasswordEncryptor.encryptPassword("pass"/*infoPerson.getPassword()*/));
        pessoa.setProfissao(infoPerson.getProfissao());
        pessoa.setTelefone(infoPerson.getTelefone());
        pessoa.setTelemovel(infoPerson.getTelemovel());
        pessoa.setPais(country);

        //remove firstTimeStudentRole and add studentRole
        CollectionUtils.filter(pessoa.getPersonRoles(), new Predicate() {

            public boolean evaluate(Object arg0) {
                IRole role = (Role) arg0;

                IRole newRole = new Role();
                newRole.setRoleType(RoleType.FIRST_TIME_STUDENT);

                return !role.equals(newRole);
            }
        });

        Object[] obj = { newRole};

        CollectionUtils.addAll(pessoa.getPersonRoles(), obj);

    }
    
    private ITurma selectClass(List classes) {
        
        return (ITurma) classes.get(0);
    }

    private List filterShifts(List shifts,IExecutionCourse executionCourse){
        
        List filteredShifts = new ArrayList();
        for(int iter=0; iter < shifts.size(); iter++){
            ITurno shift = (ITurno) shifts.get(iter);
            if(shift.getDisciplinaExecucao().equals(executionCourse)){
                filteredShifts.add(shift);
                System.out.println("Nome: " + shift.getNome());
                System.out.println("Tipo: " + shift.getTipo());
                System.out.println("IdInternal: " + shift.getIdInternal());   
            }            
        }
        System.out.println("####################################################");
        return filteredShifts;
    }
    
    private List selectShitfsToEnroll(List filteredShifts) {
        return null;
    }
    
    private void enrollInShifts(List shiftsToEnroll) {       
        
    }

}