package ServidorAplicacao.Servicos.teacher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

import DataBeans.InfoExecutionCourse;
import DataBeans.InfoSection;
import DataBeans.InfoSite;
import DataBeans.InfoSiteCommon;
import DataBeans.InfoSiteItems;
import DataBeans.TeacherAdministrationSiteView;
import DataBeans.util.Cloner;
import Dominio.ExecutionCourse;
import Dominio.IExecutionCourse;
import Dominio.IItem;
import Dominio.ISection;
import Dominio.ISite;
import Dominio.Item;
import ServidorAplicacao.Servicos.TestCaseReadServices;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentItem;
import ServidorPersistente.IPersistentSection;
import ServidorPersistente.IPersistentSite;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Fernanda Quitério
 * 
 */
public class TeacherAdministrationSiteComponentServiceItemsTest extends TestCaseReadServices
{

    /**
     * @param testName
     */
    public TeacherAdministrationSiteComponentServiceItemsTest(String testName)
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
            { new Integer(24), new InfoSiteCommon(), new InfoSiteItems(), null, new Integer(2), null };
        return args;
    }

    protected Object getObjectToCompare()
    {
        ISuportePersistente sp = null;
        InfoExecutionCourse infoExecutionCourse = null;
        ISite site = null;
        List sections = null;
        List allItemsList = null;
        IItem iItem = null;

        try
        {
            sp = SuportePersistenteOJB.getInstance();
            sp.iniciarTransaccao();

            IPersistentExecutionCourse persistentExecutionCourse =
                sp.getIPersistentExecutionCourse();
            IPersistentSite persistentSite = sp.getIPersistentSite();
            IPersistentSection persistentSection = sp.getIPersistentSection();
            IPersistentItem persistentItem = sp.getIPersistentItem();

            IExecutionCourse executionCourse =
                (IExecutionCourse) persistentExecutionCourse.readByOId(
                    new ExecutionCourse(new Integer(24)),
                    false);
            infoExecutionCourse = (InfoExecutionCourse) Cloner.get(executionCourse);

            site = persistentSite.readByExecutionCourse(executionCourse);

            sections = persistentSection.readBySite(site);

            iItem = (IItem) persistentItem.readByOId(new Item(new Integer(2)), false);

            allItemsList = persistentItem.readAllItemsBySection(iItem.getSection());

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

        List infoItemsList = new ArrayList();
        ListIterator iterItems = allItemsList.listIterator();

        while (iterItems.hasNext())
        {
            IItem item = (IItem) iterItems.next();
            if (!item.getIdInternal().equals(iItem.getIdInternal()))
            {
                infoItemsList.add(Cloner.copyIItem2InfoItem(item));
            }
        }

        InfoSiteItems infoSiteItems = new InfoSiteItems();
        Collections.sort(infoItemsList);
        infoSiteItems.setItem(Cloner.copyIItem2InfoItem(iItem));
        infoSiteItems.setItems(infoItemsList);

        InfoSite infoSite = Cloner.copyISite2InfoSite(site);
        Collections.sort(infoSections);
        InfoSiteCommon infoSiteCommon = new InfoSiteCommon();
        infoSiteCommon.setExecutionCourse(infoExecutionCourse);
        infoSiteCommon.setMail(infoSite.getMail());
        infoSiteCommon.setSections(infoSections);
        infoSiteCommon.setTitle(infoExecutionCourse.getNome());

        TeacherAdministrationSiteView siteView =
            new TeacherAdministrationSiteView(infoSiteCommon, infoSiteItems);

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