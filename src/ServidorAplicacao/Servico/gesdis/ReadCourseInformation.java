/*
 * Created on 12/Nov/2003
 *  
 */
package ServidorAplicacao.Servico.gesdis;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.SiteView;
import DataBeans.gesdis.InfoSiteCourseInformation;
import DataBeans.util.Cloner;
import Dominio.DisciplinaExecucao;
import Dominio.IBibliographicReference;
import Dominio.ICurricularCourse;
import Dominio.ICurriculum;
import Dominio.IDisciplinaExecucao;
import Dominio.IProfessorship;
import Dominio.ITeacher;
import Dominio.ResponsibleFor;
import Dominio.gesdis.ICourseReport;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.IPersistentBibliographicReference;
import ServidorPersistente.IPersistentCurriculum;
import ServidorPersistente.IPersistentProfessorship;
import ServidorPersistente.IPersistentResponsibleFor;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.gesdis.IPersistentCourseReport;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class ReadCourseInformation implements IServico
{

    private static ReadCourseInformation service = new ReadCourseInformation();

    /**
	 * The singleton access method of this class.
	 */
    public static ReadCourseInformation getService()
    {
        return service;
    }

    /**
	 *  
	 */
    private ReadCourseInformation()
    {
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.IServico#getNome()
	 */
    public final String getNome()
    {
        return "ReadCourseInformation";
    }

    /**
	 * Executes the service.
	 */
    public SiteView run(Integer executionCourseId) throws FenixServiceException
    {
        try
        {
            SiteView siteView = new SiteView();

            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IDisciplinaExecucaoPersistente persistentExecutionCourse =
                sp.getIDisciplinaExecucaoPersistente();

            IDisciplinaExecucao executionCourse =
                (IDisciplinaExecucao) persistentExecutionCourse.readByOId(
                    new DisciplinaExecucao(executionCourseId),
                    false);

            InfoSiteCourseInformation infoSiteCourseReport = new InfoSiteCourseInformation();

            infoSiteCourseReport.setInfoExecutionCourse(
                Cloner.copyIExecutionCourse2InfoExecutionCourse(executionCourse));

            List infoResponsibleTeachers = getInfoResponsibleTeachers(executionCourse, sp);
            infoSiteCourseReport.setInfoResponsibleTeachers(infoResponsibleTeachers);

            List curricularCourses = executionCourse.getAssociatedCurricularCourses();
            List infoCurricularCourses = getInfoCurricularCourses(curricularCourses);
            infoSiteCourseReport.setInfoCurricularCourses(infoCurricularCourses);

            List infoCurriculums = getInfoCurriculums(curricularCourses, sp);
            infoSiteCourseReport.setInfoCurriculums(infoCurriculums);

            List infoLecturingTeachers = getInfoLecturingTeachers(executionCourse, sp);
            infoSiteCourseReport.setInfoLecturingTeacher(infoLecturingTeachers);

            List infoBibliographicReferences = getInfoBibliographicReferences(executionCourse, sp);
            infoSiteCourseReport.setInfoBibliographicReferences(infoBibliographicReferences);

            IPersistentCourseReport persistentCourseReport = sp.getIPersistentCourseReport();
            ICourseReport courseReport =
                persistentCourseReport.readCourseReportByExecutionCourse(executionCourse);
            infoSiteCourseReport.setInfoCourseReport(
                Cloner.copyICourseReport2InfoCourseReport(courseReport));

            siteView.setComponent(infoSiteCourseReport);

            return siteView;
        } catch (ExcepcaoPersistencia e)
        {
            throw new FenixServiceException(e);
        }
    }

    /**
	 * @param executionCourse
	 * @param sp
	 * @return
	 */
    private List getInfoBibliographicReferences(
        IDisciplinaExecucao executionCourse,
        ISuportePersistente sp)
        throws ExcepcaoPersistencia
    {
        IPersistentBibliographicReference persistentBibliographicReference =
            sp.getIPersistentBibliographicReference();
        List bibliographicReferences =
            persistentBibliographicReference.readBibliographicReference(executionCourse);

        List infoBibliographicReferences = new ArrayList();
        Iterator iter = bibliographicReferences.iterator();
        while (iter.hasNext())
        {
            IBibliographicReference bibliographicReference = (IBibliographicReference) iter.next();
            infoBibliographicReferences.add(
                Cloner.copyIBibliographicReference2InfoBibliographicReference(bibliographicReference));
        }
        return infoBibliographicReferences;
    }

    private List getInfoLecturingTeachers(IDisciplinaExecucao executionCourse, ISuportePersistente sp)
        throws ExcepcaoPersistencia
    {
        IPersistentProfessorship persistentProfessorship = sp.getIPersistentProfessorship();
        List professorShips = persistentProfessorship.readByExecutionCourse(executionCourse);

        List infoLecturingTeachers = new ArrayList();
        Iterator iter = professorShips.iterator();
        while (iter.hasNext())
        {
            IProfessorship professorship = (IProfessorship) iter.next();
            ITeacher teacher = professorship.getTeacher();
            infoLecturingTeachers.add(Cloner.copyITeacher2InfoTeacher(teacher));
        }
        return infoLecturingTeachers;
    }

    private List getInfoCurriculums(List curricularCourses, ISuportePersistente sp)
        throws ExcepcaoPersistencia
    {
        IPersistentCurriculum persistentCurriculum = sp.getIPersistentCurriculum();

        List infoCurriculums = new ArrayList();
        Iterator iter = curricularCourses.iterator();
        while (iter.hasNext())
        {
            ICurricularCourse curricularCourse = (ICurricularCourse) iter.next();
            ICurriculum curriculum =
                persistentCurriculum.readCurriculumByCurricularCourse(curricularCourse);
            infoCurriculums.add(Cloner.copyICurriculum2InfoCurriculum(curriculum));
        }
        return infoCurriculums;
    }

    private List getInfoResponsibleTeachers(IDisciplinaExecucao executionCourse, ISuportePersistente sp)
        throws ExcepcaoPersistencia
    {
        IPersistentResponsibleFor persistentResponsibleFor = sp.getIPersistentResponsibleFor();
        List responsiblesFor = persistentResponsibleFor.readByExecutionCourse(executionCourse);

        List infoResponsibleTeachers = new ArrayList();
        Iterator iter = responsiblesFor.iterator();
        while (iter.hasNext())
        {
            ResponsibleFor responsibleFor = (ResponsibleFor) iter.next();
            ITeacher teacher = responsibleFor.getTeacher();
            infoResponsibleTeachers.add(Cloner.copyITeacher2InfoTeacher(teacher));
        }
        return infoResponsibleTeachers;
    }

    private List getInfoCurricularCourses(List curricularCourses)
    {
        Iterator iter;
        List infoCurricularCourses = new ArrayList();
        iter = curricularCourses.iterator();
        while (iter.hasNext())
        {
            ICurricularCourse curricularCourse = (ICurricularCourse) iter.next();
            infoCurricularCourses.add(
                Cloner.copyCurricularCourse2InfoCurricularCourse(curricularCourse));
        }
        return infoCurricularCourses;
    }
}
