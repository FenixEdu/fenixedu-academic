package ServidorAplicacao.Servicos.teacher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import DataBeans.InfoExecutionCourse;
import DataBeans.InfoSection;
import DataBeans.InfoSite;
import DataBeans.InfoSiteCommon;
import DataBeans.InfoSiteTeachers;
import DataBeans.InfoTeacher;
import DataBeans.TeacherAdministrationSiteView;
import DataBeans.util.Cloner;
import Dominio.ExecutionCourse;
import Dominio.IExecutionCourse;
import Dominio.IProfessorship;
import Dominio.IResponsibleFor;
import Dominio.ISection;
import Dominio.ISite;
import Dominio.ITeacher;
import ServidorAplicacao.Servicos.TestCaseReadServices;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentResponsibleFor;
import ServidorPersistente.IPersistentSection;
import ServidorPersistente.IPersistentSite;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Fernanda Quitério
 *  
 */
public class TeacherAdministrationSiteComponentServiceTeachersTest extends TestCaseReadServices
{

    /**
	 * @param testName
	 */
    public TeacherAdministrationSiteComponentServiceTeachersTest(String testName)
    {
        super(testName);
    }

    /**
	 * @see ServidorAplicacao.Servicos.TestCaseNeedAuthorizationServices#getNameOfServiceToBeTested()
	 */
    protected String getNameOfServiceToBeTested()
    {
        return "TeacherAdministrationSiteComponentService";
    }

    /**
	 * @see ServidorAplicacao.Servicos.TestCaseDeleteAndEditServices#getArgumentsOfServiceToBeTestedUnsuccessfuly()
	 */
    protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly()
    {
        return null;
    }

    /**
	 * @see ServidorAplicacao.Servicos.TestCaseDeleteAndEditServices#getArgumentsOfServiceToBeTestedSuccessfuly()
	 */
    protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly()
    {

        Object[] args =
            {
                new Integer(26),
                new InfoSiteCommon(),
                new InfoSiteTeachers(),
                null,
                null,
                new String("user")};
        return args;
    }

    protected Object getObjectToCompare()
    {
        ISuportePersistente sp = null;
        InfoExecutionCourse infoExecutionCourse = null;
        ISite site = null;
        List sections = null;
        List teachersList = null;
        List responsibleTeachers = null;
        ITeacher teacher = null;
        IResponsibleFor responsibleFor = null;

        try
        {
            sp = SuportePersistenteOJB.getInstance();
            IPersistentExecutionCourse persistentExecutionCourse = sp.getIPersistentExecutionCourse();
            IPersistentSite persistentSite = sp.getIPersistentSite();
            IPersistentSection persistentSection = sp.getIPersistentSection();
            IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();
            IPersistentResponsibleFor persistentResponsibleFor = sp.getIPersistentResponsibleFor();

            sp.iniciarTransaccao();
            IExecutionCourse executionCourse =
                (IExecutionCourse) persistentExecutionCourse.readByOId(
                    new ExecutionCourse(new Integer(26)),
                    false);
            infoExecutionCourse = (InfoExecutionCourse) Cloner.get(executionCourse);

            site = persistentSite.readByExecutionCourse(executionCourse);

            sections = persistentSection.readBySite(site);

            teachersList = sp.getIPersistentProfessorship().readByExecutionCourse(executionCourse);

            responsibleTeachers = persistentResponsibleFor.readByExecutionCourse(executionCourse);

            teacher = persistentTeacher.readTeacherByUsername("user");
            responsibleFor =
                persistentResponsibleFor.readByTeacherAndExecutionCourse(teacher, executionCourse);

            sp.confirmarTransaccao();
        }
        catch (ExcepcaoPersistencia e)
        {
            System.out.println("failed setting up the test data");
            e.printStackTrace();
        }

        List infoSections = new ArrayList();
        ListIterator iter = sections.listIterator();

        while (iter.hasNext())
        {
            InfoSection infoSection = Cloner.copyISection2InfoSection((ISection) iter.next());
            infoSections.add(infoSection);
        }

        boolean isResponsible = false;
        List infoTeachers = new ArrayList();
        if (teachersList != null)
        {
            Iterator teachersIterator = teachersList.iterator();
            while (teachersIterator.hasNext())
            {
                IProfessorship professorship = (IProfessorship) teachersIterator.next();
                ITeacher iTeacher = professorship.getTeacher();
                InfoTeacher infoTeacher = Cloner.copyITeacher2InfoTeacher(iTeacher);
                infoTeachers.add(infoTeacher);
            }

            List infoResponsibleTeachers = new ArrayList();

            if (responsibleTeachers != null)
            {
                Iterator iter2 = responsibleTeachers.iterator();
                while (iter2.hasNext())
                {
                    IResponsibleFor responsible = (IResponsibleFor) iter2.next();
                    ITeacher iTeacher = responsible.getTeacher();
                    InfoTeacher infoTeacher = Cloner.copyITeacher2InfoTeacher(iTeacher);
                    infoResponsibleTeachers.add(infoTeacher);
                }

                if (teacher != null)
                {
                    if (responsibleTeachers != null
                        && !responsibleTeachers.isEmpty()
                        && responsibleTeachers.contains(responsibleFor))
                    {
                        isResponsible = true;
                    }
                }
            }
        }
        InfoSiteTeachers infoSiteTeachers = new InfoSiteTeachers();
        infoSiteTeachers.setInfoTeachers(infoTeachers);
        infoSiteTeachers.setIsResponsible(new Boolean(isResponsible));

        InfoSite infoSite = Cloner.copyISite2InfoSite(site);
        Collections.sort(infoSections);
        InfoSiteCommon infoSiteCommon = new InfoSiteCommon();
        infoSiteCommon.setExecutionCourse(infoExecutionCourse);
        infoSiteCommon.setMail(infoSite.getMail());
        infoSiteCommon.setSections(infoSections);
        infoSiteCommon.setTitle(infoExecutionCourse.getNome());

        TeacherAdministrationSiteView siteView =
            new TeacherAdministrationSiteView(infoSiteCommon, infoSiteTeachers);

        return siteView;
    }

    /**
	 * This method must return 'true' if the service needs authorization to be
	 * runned and 'false' otherwise.
	 */
    protected boolean needsAuthorization()
    {
        return true;
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.Servicos.TestCaseCreateServices#getArgumentListOfServiceToBeTestedUnsuccessfuly()
	 */
    protected HashMap getArgumentListOfServiceToBeTestedUnsuccessfuly()
    {
        return null;
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.Servicos.TestCaseReadServices#getNumberOfItemsToRetrieve()
	 */
    protected int getNumberOfItemsToRetrieve()
    {
        return 0;
    }

}