package ServidorAplicacao.Servicos.teacher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import DataBeans.InfoExecutionCourse;
import DataBeans.InfoSection;
import DataBeans.InfoSite;
import DataBeans.InfoSiteBibliography;
import DataBeans.InfoSiteCommon;
import DataBeans.TeacherAdministrationSiteView;
import DataBeans.util.Cloner;
import Dominio.ExecutionCourse;
import Dominio.IBibliographicReference;
import Dominio.IExecutionCourse;
import Dominio.ISection;
import Dominio.ISite;
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
public class TeacherAdministrationSiteComponentServiceBibliographiesTest extends
        TestCaseReadServices {

    /**
     * @param testName
     */
    public TeacherAdministrationSiteComponentServiceBibliographiesTest(
            String testName) {
        super(testName);
    }

    /**
     * @see ServidorAplicacao.Servicos.TestCaseNeedAuthorizationServices#getNameOfServiceToBeTested()
     */
    protected String getNameOfServiceToBeTested() {
        return "TeacherAdministrationSiteComponentService";
    }

    /**
     * @see ServidorAplicacao.Servicos.TestCaseDeleteAndEditServices#getArgumentsOfServiceToBeTestedUnsuccessfuly()
     */
    protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {
        return null;
    }

    /**
     * @see ServidorAplicacao.Servicos.TestCaseDeleteAndEditServices#getArgumentsOfServiceToBeTestedSuccessfuly()
     */
    protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {

        Object[] args = { new Integer(24), new InfoSiteCommon(),
                new InfoSiteBibliography(), null, null, null };
        return args;
    }

    protected Object getObjectToCompare() {
        ISuportePersistente sp = null;
        InfoExecutionCourse infoExecutionCourse = null;
        ISite site = null;
        List sections = null;
        List bibliographicReferenceList = null;

        try {
            sp = SuportePersistenteOJB.getInstance();
            sp.iniciarTransaccao();

            IPersistentExecutionCourse persistentExecutionCourse = sp
                    .getIPersistentExecutionCourse();
            IPersistentSite persistentSite = sp.getIPersistentSite();
            IPersistentSection persistentSection = sp.getIPersistentSection();

            IExecutionCourse executionCourse = (IExecutionCourse) persistentExecutionCourse
                    .readByOID(ExecutionCourse.class, new Integer(24));
            infoExecutionCourse = (InfoExecutionCourse) Cloner
                    .get(executionCourse);

            site = persistentSite.readByExecutionCourse(executionCourse);

            sections = persistentSection.readBySite(site);

            bibliographicReferenceList = sp
                    .getIPersistentBibliographicReference()
                    .readBibliographicReference(executionCourse);

            sp.confirmarTransaccao();
        } catch (ExcepcaoPersistencia e) {
            System.out.println("failed setting up the test data");
            e.printStackTrace();
        }

        List infoSections = new ArrayList();
        ListIterator iter = sections.listIterator();

        while (iter.hasNext()) {
            InfoSection infoSection = Cloner
                    .copyISection2InfoSection((ISection) iter.next());
            infoSections.add(infoSection);
        }

        List infobibliographicReferenceList = new ArrayList();

        if (bibliographicReferenceList != null
                && bibliographicReferenceList.isEmpty() == false) {
            Iterator iterBibliography = bibliographicReferenceList.iterator();
            while (iterBibliography.hasNext()) {
                IBibliographicReference bibliographicRerefence = (IBibliographicReference) iterBibliography
                        .next();
                infobibliographicReferenceList
                        .add(Cloner
                                .copyIBibliographicReference2InfoBibliographicReference(bibliographicRerefence));
            }
        }
        InfoSiteBibliography infoSiteBibliography = new InfoSiteBibliography();
        infoSiteBibliography
                .setBibliographicReferences(infobibliographicReferenceList);

        InfoSite infoSite = Cloner.copyISite2InfoSite(site);

        InfoSiteCommon infoSiteCommon = new InfoSiteCommon();
        infoSiteCommon.setExecutionCourse(infoExecutionCourse);
        infoSiteCommon.setMail(infoSite.getMail());
        infoSiteCommon.setSections(infoSections);
        infoSiteCommon.setTitle(infoExecutionCourse.getNome());

        TeacherAdministrationSiteView siteView = new TeacherAdministrationSiteView(
                infoSiteCommon, infoSiteBibliography);

        return siteView;
    }

    /**
     * This method must return 'true' if the service needs authorization to be
     * runned and 'false' otherwise.
     */
    protected boolean needsAuthorization() {
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.TestCaseCreateServices#getArgumentListOfServiceToBeTestedUnsuccessfuly()
     */
    protected HashMap getArgumentListOfServiceToBeTestedUnsuccessfuly() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.TestCaseReadServices#getNumberOfItemsToRetrieve()
     */
    protected int getNumberOfItemsToRetrieve() {
        return 0;
    }

}