package ServidorAplicacao.Servico.teacher;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.ISiteComponent;
import DataBeans.InfoSiteCommon;
import DataBeans.InfoSiteStudents;
import DataBeans.InfoStudent;
import DataBeans.TeacherAdministrationSiteView;
import DataBeans.util.Cloner;
import Dominio.CurricularCourse;
import Dominio.ExecutionCourse;
import Dominio.ICurricularCourse;
import Dominio.IEnrolment;
import Dominio.IExecutionCourse;
import Dominio.IFrequenta;
import Dominio.ISite;
import Dominio.IStudent;
import ServidorAplicacao.Factory.TeacherAdministrationSiteComponentBuilder;
import ServidorAplicacao.Servico.ExcepcaoInexistente;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IFrequentaPersistente;
import ServidorPersistente.IPersistentCurricularCourse;
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
public class ReadStudentsByCurricularCourse implements IService
{
   
    public ReadStudentsByCurricularCourse()
    {

    }

    

    
    public Object run( Integer executionCourseCode, Integer courseCode ) throws ExcepcaoInexistente,
                    FenixServiceException
    {
        List infoStudentList = null;
        ISite site = null;
//        ICurricularCourseScope curricularCourseScope = null;
        ICurricularCourse curricularCourse = null;
        try
        {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

            IExecutionCourse executionCourse = new ExecutionCourse();
            executionCourse.setIdInternal(executionCourseCode);

            IPersistentSite persistentSite = sp.getIPersistentSite();
            site = persistentSite.readByExecutionCourse(executionCourse);

            if (courseCode == null)
            {
                infoStudentList = getAllAttendingStudents(sp, executionCourse);
            }
            else
            {
//                curricularCourseScope = new CurricularCourseScope();
//                curricularCourseScope.setIdInternal(scopeCode);
                
                curricularCourse = new CurricularCourse();
                curricularCourse.setIdInternal(courseCode);
                
//                infoStudentList = getCurricularCourseScopeStudents(curricularCourseScope, sp);
                infoStudentList = getCurricularCourseStudents(curricularCourse, sp);
                
//                IPersistentCurricularCourseScope persistentCurricularCourseScope = sp
//                                .getIPersistentCurricularCourseScope();
//                curricularCourseScope = (ICurricularCourseScope) persistentCurricularCourseScope
//                                .readByOId(curricularCourseScope, false);

                IPersistentCurricularCourse persistentCurricularCourse = sp
                .getIPersistentCurricularCourse();
                curricularCourse = (ICurricularCourse) persistentCurricularCourse
                .readByOId(curricularCourse, false);
            }

//            TeacherAdministrationSiteView siteView = createSiteView(infoStudentList, site, curricularCourseScope);
            TeacherAdministrationSiteView siteView = createSiteView(infoStudentList, site, curricularCourse);
            return siteView;
            
        }
        catch (ExcepcaoPersistencia ex)
        {
            ex.printStackTrace();
            FenixServiceException newEx = new FenixServiceException("");
            newEx.fillInStackTrace();
            throw newEx;
        }

    }

//    private List getCurricularCourseScopeStudents( ICurricularCourseScope curricularCourseScope, ISuportePersistente sp ) throws ExcepcaoPersistencia
    private List getCurricularCourseStudents( ICurricularCourse curricularCourse, ISuportePersistente sp ) throws ExcepcaoPersistencia
    {
        List infoStudentList;
        IPersistentEnrolment persistentEnrolment = sp.getIPersistentEnrolment();

//        List enrolments = persistentEnrolment.readByCurricularCourseScope(curricularCourseScope);
        List enrolments = persistentEnrolment.readByCurricularCourse(curricularCourse);

        infoStudentList = (List) CollectionUtils.collect(enrolments, new Transformer()
        {
            public Object transform( Object input )
            {
                IEnrolment enrolment = (IEnrolment) input;
                IStudent student = enrolment.getStudentCurricularPlan().getStudent();
                InfoStudent infoStudent = Cloner.copyIStudent2InfoStudent(student);
                return infoStudent;
            }
        });
        return infoStudentList;
    }

    private List getAllAttendingStudents( ISuportePersistente sp, IExecutionCourse executionCourse ) throws ExcepcaoPersistencia
    {
        List infoStudentList;
        //	all students that attend this execution course
        IFrequentaPersistente frequentaPersistente = sp.getIFrequentaPersistente();
        List attendList = frequentaPersistente.readByExecutionCourse(executionCourse);

        infoStudentList = (List) CollectionUtils.collect(attendList, new Transformer()
        {

            public Object transform( Object input )
            {
                IFrequenta attend = (IFrequenta) input;
                IStudent student = attend.getAluno();
                InfoStudent infoStudent = Cloner.copyIStudent2InfoStudent(student);
                return infoStudent;
            }
        });
        return infoStudentList;
    }

//    private TeacherAdministrationSiteView createSiteView( List infoStudentList, ISite site, ICurricularCourseScope curricularCourseScope ) throws FenixServiceException
    private TeacherAdministrationSiteView createSiteView( List infoStudentList, ISite site, ICurricularCourse curricularCourse) throws FenixServiceException
    {
        InfoSiteStudents infoSiteStudents = new InfoSiteStudents();
        infoSiteStudents.setStudents(infoStudentList);

        if (curricularCourse != null)
        {
//            infoSiteStudents.setInfoCurricularCourseScope(Cloner.copyICurricularCourseScope2InfoCurricularCourseScope(curricularCourseScope));
            infoSiteStudents.setInfoCurricularCourse(Cloner.copyCurricularCourse2InfoCurricularCourse(curricularCourse));
        }
        TeacherAdministrationSiteComponentBuilder componentBuilder = new TeacherAdministrationSiteComponentBuilder();
        ISiteComponent commonComponent = componentBuilder.getComponent(new InfoSiteCommon(), site,
                        null, null, null);

        TeacherAdministrationSiteView siteView = new TeacherAdministrationSiteView(commonComponent,
                        infoSiteStudents);
        return siteView;
    }
}
