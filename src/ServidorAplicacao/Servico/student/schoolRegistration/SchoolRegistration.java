/*
 * Created on Jul 19, 2004
 *
 */
package ServidorAplicacao.Servico.student.schoolRegistration;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.ICurricularCourse;
import Dominio.IDegreeCurricularPlan;
import Dominio.IStudentCurricularPlan;
import Dominio.student.ISchoolRegistrationInquiryAnswer;
import Dominio.student.SchoolRegistrationInquiryAnswer;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.enrollment.WriteEnrollment;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentEnrollment;
import ServidorPersistente.IPersistentEnrolmentEvaluation;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentSchoolRegistrationInquiryAnswer;
import ServidorPersistente.IStudentCurricularPlanPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.TipoCurso;
import Util.enrollment.CurricularCourseEnrollmentType;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 */
public class SchoolRegistration implements IService {

    public SchoolRegistration() {
        super();
    }

    public void run(UserView userView, HashMap answers) throws ExcepcaoPersistencia, FenixServiceException {

        ISuportePersistente suportePersistente = SuportePersistenteOJB.getInstance();
        String user = userView.getUtilizador();
        Integer studentNumber = new Integer(user.substring(1));
        
        writeInquiryAnswers(suportePersistente,studentNumber,answers);
        enrollStudent1stTime1stYear(suportePersistente,studentNumber);

    }

    private void enrollStudent1stTime1stYear(ISuportePersistente sp, Integer studentNumber) throws ExcepcaoPersistencia,
    		FenixServiceException {
        
        IStudentCurricularPlanPersistente scpPersistent = sp.getIStudentCurricularPlanPersistente();
        IPersistentEnrollment persistentEnrollment = sp.getIPersistentEnrolment();
        IPersistentExecutionPeriod persistentEP = sp.getIPersistentExecutionPeriod();
        IPersistentEnrolmentEvaluation persistentEnrolmentEvaluation = sp.getIPersistentEnrolmentEvaluation();
        
        IStudentCurricularPlan scp = scpPersistent.readActiveStudentCurricularPlan(studentNumber,TipoCurso.LICENCIATURA_OBJ);        
        IDegreeCurricularPlan dcp = scp.getDegreeCurricularPlan();
        List curricularCourses = dcp.getCurricularCoursesByYearAndSemesterAndBranch(1,new Integer(1),scp.getBranch());

        WriteEnrollment we = new WriteEnrollment();
        for(int iter=0; iter<curricularCourses.size(); iter++)
        {         
            ICurricularCourse cc = (ICurricularCourse) curricularCourses.get(iter);
            Integer executionPeriodId = persistentEP.readActualExecutionPeriod().getIdInternal();
            we.run(null,scp.getIdInternal(),cc.getIdInternal(),executionPeriodId,CurricularCourseEnrollmentType.DEFINITIVE);
        }
    }

    private void writeInquiryAnswers(ISuportePersistente sp, Integer studentNumber, HashMap answers) throws ExcepcaoPersistencia,
            FenixServiceException {

        IPersistentSchoolRegistrationInquiryAnswer persistentSRIA = sp.getIPersistentSchoolRegistrationInquiryAnswer();
        ISchoolRegistrationInquiryAnswer schoolRegistrationInquiryAnswer = persistentSRIA.readAnswersByStudentNumber(studentNumber);

        if (schoolRegistrationInquiryAnswer == null)
        {
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
}