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
import DataBeans.InfoSiteSections;
import DataBeans.TeacherAdministrationSiteView;
import DataBeans.util.Cloner;
import Dominio.ExecutionCourse;
import Dominio.IExecutionCourse;
import Dominio.ISection;
import Dominio.ISite;
import Dominio.Section;
import ServidorAplicacao.Servicos.TestCaseReadServices;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentSection;
import ServidorPersistente.IPersistentSite;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Fernanda Quitério
 * 
 */
public class TeacherAdministrationSiteComponentServiceSectionsTest extends TestCaseReadServices
{

    /**
     * @param testName
     */
    public TeacherAdministrationSiteComponentServiceSectionsTest(String testName)
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
                new Integer(27),
                new InfoSiteCommon(),
                new InfoSiteSections(),
                null,
                new Integer(4),
                null };
        return args;
    }

    protected Object getObjectToCompare()
    {
        ISuportePersistente sp = null;
        InfoExecutionCourse infoExecutionCourse = null;
        ISite site = null;
        List sections = null;
        List allSectionsList = null;
        ISection iSection = null;

        try
        {
            sp = SuportePersistenteOJB.getInstance();
            sp.iniciarTransaccao();

            IPersistentExecutionCourse persistentExecutionCourse =
                sp.getIPersistentExecutionCourse();
            IPersistentSite persistentSite = sp.getIPersistentSite();
            IPersistentSection persistentSection = sp.getIPersistentSection();

            IExecutionCourse executionCourse =
                (IExecutionCourse) persistentExecutionCourse.readByOId(
                    new ExecutionCourse(new Integer(27)),
                    false);
            infoExecutionCourse = (InfoExecutionCourse) Cloner.get(executionCourse);

            site = persistentSite.readByExecutionCourse(executionCourse);

            sections = persistentSection.readBySite(site);

            allSectionsList = persistentSection.readBySite(site);

            iSection = (ISection) persistentSection.readByOId(new Section(new Integer(4)), false);

            sp.confirmarTransaccao();
        } catch (ExcepcaoPersistencia e)
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

        List infoSectionsList = new ArrayList();

        if (allSectionsList != null && allSectionsList.isEmpty() == false)
        {
            Iterator iterSections = allSectionsList.iterator();
            if (iSection.getSuperiorSection() == null)
            {
                while (iterSections.hasNext())
                {
                    ISection section = (ISection) iterSections.next();
                    if ((section.getSuperiorSection() == null)
                        && !section.getName().equals(iSection.getName()))
                    {
                        infoSectionsList.add(Cloner.copyISection2InfoSection(section));
                    }
                }
            } else
            {
                while (iterSections.hasNext())
                {
                    ISection section = (ISection) iterSections.next();
                    if ((section.getSuperiorSection() != null
                        && section.getSuperiorSection().getIdInternal().equals(
                            iSection.getSuperiorSection().getIdInternal()))
                        && !section.getName().equals(iSection.getName()))
                    {
                        infoSectionsList.add(Cloner.copyISection2InfoSection(section));
                    }
                }
            }
        }

        InfoSection infoSection = Cloner.copyISection2InfoSection(iSection);
        Collections.sort(infoSectionsList);
        InfoSiteSections infoSiteSections = new InfoSiteSections();
        infoSiteSections.setSection(infoSection);
        infoSiteSections.setSections(infoSectionsList);

        InfoSite infoSite = Cloner.copyISite2InfoSite(site);
        Collections.sort(infoSections);
        InfoSiteCommon infoSiteCommon = new InfoSiteCommon();
        infoSiteCommon.setExecutionCourse(infoExecutionCourse);
        infoSiteCommon.setMail(infoSite.getMail());
        infoSiteCommon.setSections(infoSections);
        infoSiteCommon.setTitle(infoExecutionCourse.getNome());

        TeacherAdministrationSiteView siteView =
            new TeacherAdministrationSiteView(infoSiteCommon, infoSiteSections);

        return siteView;
    }

    /**
     * This method must return 'true' if the service needs authorization to be runned and 'false' otherwise.
     */
    protected boolean needsAuthorization()
    {
        return true;
    }

    /* (non-Javadoc)
     * @see ServidorAplicacao.Servicos.TestCaseCreateServices#getArgumentListOfServiceToBeTestedUnsuccessfuly()
     */
    protected HashMap getArgumentListOfServiceToBeTestedUnsuccessfuly()
    {
        return null;
    }

    /* (non-Javadoc)
     * @see ServidorAplicacao.Servicos.TestCaseReadServices#getNumberOfItemsToRetrieve()
     */
    protected int getNumberOfItemsToRetrieve()
    {
        return 0;
    }

}