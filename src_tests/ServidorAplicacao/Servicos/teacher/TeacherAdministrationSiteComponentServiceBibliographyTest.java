package ServidorAplicacao.Servicos.teacher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

import DataBeans.InfoBibliographicReference;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoSection;
import DataBeans.InfoSite;
import DataBeans.InfoSiteCommon;
import DataBeans.TeacherAdministrationSiteView;
import DataBeans.util.Cloner;
import Dominio.BibliographicReference;
import Dominio.DisciplinaExecucao;
import Dominio.IBibliographicReference;
import Dominio.IDisciplinaExecucao;
import Dominio.ISection;
import Dominio.ISite;
import ServidorAplicacao.Servicos.TestCaseReadServices;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.IPersistentSection;
import ServidorPersistente.IPersistentSite;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Fernanda Quitério
 * 
 */
public class TeacherAdministrationSiteComponentServiceBibliographyTest extends TestCaseReadServices
{

    /**
     * @param testName
     */
    public TeacherAdministrationSiteComponentServiceBibliographyTest(String testName)
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
                new Integer(24),
                new InfoSiteCommon(),
                new InfoBibliographicReference(),
                null,
                new Integer(1),
                null };
        return args;
    }

    protected Object getObjectToCompare()
    {
        ISuportePersistente sp = null;
        InfoExecutionCourse infoExecutionCourse = null;
        ISite site = null;
        List sections = null;
        IBibliographicReference iBibliographicReference = null;

        try
        {
            sp = SuportePersistenteOJB.getInstance();
            sp.iniciarTransaccao();

            IDisciplinaExecucaoPersistente persistentExecutionCourse =
                sp.getIDisciplinaExecucaoPersistente();
            IPersistentSite persistentSite = sp.getIPersistentSite();
            IPersistentSection persistentSection = sp.getIPersistentSection();

            IDisciplinaExecucao executionCourse =
                (IDisciplinaExecucao) persistentExecutionCourse.readByOId(
                    new DisciplinaExecucao(new Integer(24)),
                    false);
            infoExecutionCourse = Cloner.copyIExecutionCourse2InfoExecutionCourse(executionCourse);

            site = persistentSite.readByExecutionCourse(executionCourse);

            sections = persistentSection.readBySite(site);

            iBibliographicReference =
                (IBibliographicReference) sp.getIPersistentBibliographicReference().readByOId(
                    new BibliographicReference(new Integer(1)),
                    false);

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

        InfoBibliographicReference infoBibliographicReference =
            Cloner.copyIBibliographicReference2InfoBibliographicReference(iBibliographicReference);

        InfoSite infoSite = Cloner.copyISite2InfoSite(site);

        InfoSiteCommon infoSiteCommon = new InfoSiteCommon();
        infoSiteCommon.setExecutionCourse(infoExecutionCourse);
        infoSiteCommon.setMail(infoSite.getMail());
        infoSiteCommon.setSections(infoSections);
        infoSiteCommon.setTitle(infoExecutionCourse.getNome());

        TeacherAdministrationSiteView siteView =
            new TeacherAdministrationSiteView(infoSiteCommon, infoBibliographicReference);

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