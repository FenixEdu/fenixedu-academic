package ServidorAplicacao.Servico.teacher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.ISiteComponent;
import DataBeans.InfoSiteCommon;
import DataBeans.InfoSiteStudents;
import DataBeans.InfoStudent;
import DataBeans.TeacherAdministrationSiteView;
import DataBeans.util.Cloner;
import Dominio.CurricularCourseScope;
import Dominio.ExecutionCourse;
import Dominio.Enrolment;
import Dominio.Frequenta;
import Dominio.ICurricularCourseScope;
import Dominio.IExecutionCourse;
import Dominio.IEnrolment;
import Dominio.IFrequenta;
import Dominio.ISite;
import Dominio.IStudent;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Factory.TeacherAdministrationSiteComponentBuilder;
import ServidorAplicacao.Servico.ExcepcaoInexistente;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IFrequentaPersistente;
import ServidorPersistente.IPersistentCurricularCourseScope;
import ServidorPersistente.IPersistentEnrolment;
import ServidorPersistente.IPersistentSite;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Fernanda Quitério
 * @author Tânia Pousão 
 * @author Ângela
 *
 */
public class ReadStudentsByCurricularCourse implements IServico
{
    private static ReadStudentsByCurricularCourse _servico = new ReadStudentsByCurricularCourse();

    /**
    	* The actor of this class.
    	**/
    private ReadStudentsByCurricularCourse()
    {

    }

    /**
     * Returns Service Name
     */
    public String getNome()
    {
        return "ReadStudentsByCurricularCourse";
    }

    /**
     * Returns the _servico.
     * @return ReadExecutionCourse
     */
    public static ReadStudentsByCurricularCourse getService()
    {
        return _servico;
    }

    public Object run(Integer executionCourseCode, Integer scopeCode)
        throws ExcepcaoInexistente, FenixServiceException
    {

        List infoStudentList = new ArrayList();
        ISite site = null;
        IExecutionCourse executionCourse = null;
        ICurricularCourseScope curricularCourseScope = null;
        try
        {
            executionCourse = new ExecutionCourse();
            executionCourse.setIdInternal(executionCourseCode);

            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentExecutionCourse executionCourseDAO = sp.getIDisciplinaExecucaoPersistente();
            executionCourse = (IExecutionCourse) executionCourseDAO.readByOId(executionCourse, false);

            IPersistentSite persistentSite = sp.getIPersistentSite();
            site = persistentSite.readByExecutionCourse(executionCourse);

            List studentsList = new ArrayList();
            if (scopeCode == null)
            {

                //				all students that attend this execution course
                IFrequentaPersistente frequentaPersistente = sp.getIFrequentaPersistente();
                List attendList = frequentaPersistente.readByExecutionCourse(executionCourse);

                Iterator iterStudent = attendList.listIterator();
                while (iterStudent.hasNext())
                {
                    IFrequenta attend = (Frequenta) iterStudent.next();
                    IStudent student = attend.getAluno();
                    studentsList.add(student);
                }
            } else
            {

                curricularCourseScope = new CurricularCourseScope();
                curricularCourseScope.setIdInternal(scopeCode);
                IEnrolment enrolmentForCriteria = new Enrolment();
                enrolmentForCriteria.setCurricularCourseScope(curricularCourseScope);
                IPersistentEnrolment persistentEnrolment = sp.getIPersistentEnrolment();
                List enrolments = persistentEnrolment.readByCriteria(enrolmentForCriteria);

                Iterator iterEnrolments = enrolments.listIterator();
                while (iterEnrolments.hasNext())
                {
                    IEnrolment enrolment = (IEnrolment) iterEnrolments.next();
                    IStudent student = enrolment.getStudentCurricularPlan().getStudent();
                    studentsList.add(student);
                }

                IPersistentCurricularCourseScope persistentCurricularCourseScope =
                    sp.getIPersistentCurricularCourseScope();
                curricularCourseScope =
                    (ICurricularCourseScope) persistentCurricularCourseScope.readByOId(
                        curricularCourseScope,
                        false);

            }

            Iterator iterStudents = studentsList.listIterator();

            while (iterStudents.hasNext())
            {

                IStudent student = (IStudent) iterStudents.next();
                InfoStudent infoStudent = Cloner.copyIStudent2InfoStudent(student);
                infoStudentList.add(infoStudent);
            }
        } catch (ExcepcaoPersistencia ex)
        {
            ex.printStackTrace();
            FenixServiceException newEx = new FenixServiceException("");
            newEx.fillInStackTrace();
            throw newEx;
        }

        InfoSiteStudents infoSiteStudents = new InfoSiteStudents();
        infoSiteStudents.setStudents(infoStudentList);
        if (curricularCourseScope != null)
        {
            infoSiteStudents.setInfoCurricularCourseScope(
                Cloner.copyICurricularCourseScope2InfoCurricularCourseScope(curricularCourseScope));
        }
        TeacherAdministrationSiteComponentBuilder componentBuilder =
            new TeacherAdministrationSiteComponentBuilder();
        ISiteComponent commonComponent =
            componentBuilder.getComponent(new InfoSiteCommon(), site, null, null, null);

        TeacherAdministrationSiteView siteView =
            new TeacherAdministrationSiteView(commonComponent, infoSiteStudents);
        return siteView;
    }
}
