/*
 * Created on 13/Nov/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servicos.teacher;

import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoCareer;
import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoCategory;
import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoProfessionalCareer;
import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoTeachingCareer;
import net.sourceforge.fenixedu.applicationTier.Servico.Autenticacao;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servicos.ServiceNeedsAuthenticationTestCase;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class EditCareerTest extends ServiceNeedsAuthenticationTestCase {

    public EditCareerTest(String testName) {
        super(testName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.ServiceTestCase#getNameOfServiceToBeTested()
     */
    protected String getNameOfServiceToBeTested() {
        return "EditCareer";
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.ServiceTestCase#getDataSetFilePath()
     */
    protected String getDataSetFilePath() {
        return "etc/datasets/servicos/teacher/testEditCareerDataSet.xml";
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase#getAuthenticatedAndAuthorizedUser()
     */
    protected String[] getAuthenticatedAndAuthorizedUser() {
        String[] args = { "user", "pass", getApplication() };
        return args;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase#getAuthenticatedAndUnauthorizedUser()
     */
    protected String[] getAuthenticatedAndUnauthorizedUser() {
        String[] args = { "manager", "pass", getApplication() };
        return args;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase#getNotAuthenticatedUser()
     */
    protected String[] getNotAuthenticatedUser() {
        String[] args = { "jccm", "pass", getApplication() };
        return args;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase#getAuthorizeArguments()
     */
    protected Object[] getAuthorizeArguments() {
        Integer careerId = new Integer(1);

        InfoCareer infoCareer = new InfoProfessionalCareer();
        infoCareer.setIdInternal(careerId);
        infoCareer.setBeginYear(new Integer(1999));
        infoCareer.setEndYear(new Integer(2000));
        ((InfoProfessionalCareer) infoCareer).setEntity("empresa");
        ((InfoProfessionalCareer) infoCareer).setFunction("director");

        InfoTeacher infoTeacher = new InfoTeacher();
        infoTeacher.setIdInternal(new Integer(1));

        infoCareer.setInfoTeacher(infoTeacher);

        Object[] args = { careerId, infoCareer };
        return args;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.Servicos.ServiceNeedsAuthenticationTestCase#getApplication()
     */
    protected String getApplication() {
        return Autenticacao.EXTRANET;
    }

    public void testCreateNewProfessionalCareer() {
        try {
            InfoCareer infoCareer = new InfoProfessionalCareer();
            infoCareer.setBeginYear(new Integer(1995));
            infoCareer.setEndYear(new Integer(1999));
            ((InfoProfessionalCareer) infoCareer).setEntity("empresa");
            ((InfoProfessionalCareer) infoCareer).setFunction("presidente");

            InfoTeacher infoTeacher = new InfoTeacher();
            infoTeacher.setIdInternal(new Integer(1));

            infoCareer.setInfoTeacher(infoTeacher);
            Object[] args = { null, infoCareer };

            ServiceManagerServiceFactory.executeService(userView, getNameOfServiceToBeTested(), args);

            compareDataSetUsingExceptedDataSetTablesAndColumns("etc/datasets/servicos/teacher/testExpectedCreateProfessionalCareerDataSet.xml");
        } catch (FenixServiceException ex) {
            fail("Creating a new professional career " + ex);
        } catch (Exception ex) {
            fail("Creating a new professional career " + ex);
        }
    }

    public void testCreateNewTeachingCareer() {
        try {
            InfoCategory infoCategory = new InfoCategory();
            infoCategory.setIdInternal(new Integer(1));

            InfoCareer infoCareer = new InfoTeachingCareer();
            infoCareer.setBeginYear(new Integer(1999));
            infoCareer.setEndYear(new Integer(2000));
            ((InfoTeachingCareer) infoCareer).setCourseOrPosition("PO");
            ((InfoTeachingCareer) infoCareer).setInfoCategory(infoCategory);

            InfoTeacher infoTeacher = new InfoTeacher();
            infoTeacher.setIdInternal(new Integer(1));

            infoCareer.setInfoTeacher(infoTeacher);
            Object[] args = { null, infoCareer };

            ServiceManagerServiceFactory.executeService(userView, getNameOfServiceToBeTested(), args);

            compareDataSetUsingExceptedDataSetTablesAndColumns("etc/datasets/servicos/teacher/testExpectedCreateTeachingCareerDataSet.xml");
        } catch (Exception ex) {
            fail("Creating a new teaching career " + ex);
        }
    }

    public void testEditExistingProfessionalCareer() {
        try {
            Integer careerId = new Integer(1);

            InfoCareer infoCareer = new InfoProfessionalCareer();
            infoCareer.setIdInternal(careerId);
            infoCareer.setBeginYear(new Integer(1999));
            infoCareer.setEndYear(new Integer(2000));
            ((InfoProfessionalCareer) infoCareer).setEntity("empresa");
            ((InfoProfessionalCareer) infoCareer).setFunction("presidente");

            InfoTeacher infoTeacher = new InfoTeacher();
            infoTeacher.setIdInternal(new Integer(1));

            infoCareer.setInfoTeacher(infoTeacher);
            Object[] args = { careerId, infoCareer };

            ServiceManagerServiceFactory.executeService(userView, getNameOfServiceToBeTested(), args);

            compareDataSetUsingExceptedDataSetTablesAndColumns("etc/datasets/servicos/teacher/testExpectedEditProfessionalCareerDataSet.xml");
        } catch (Exception ex) {
            fail("Editing a professional career " + ex);
        }
    }

    public void testEditExistingTeachingCareer() {
        try {
            Integer careerId = new Integer(2);

            InfoCategory infoCategory = new InfoCategory();
            infoCategory.setIdInternal(new Integer(1));

            InfoCareer infoCareer = new InfoTeachingCareer();
            infoCareer.setIdInternal(careerId);
            infoCareer.setBeginYear(new Integer(1999));
            infoCareer.setEndYear(new Integer(2000));
            ((InfoTeachingCareer) infoCareer).setCourseOrPosition("ASA");
            ((InfoTeachingCareer) infoCareer).setInfoCategory(infoCategory);

            InfoTeacher infoTeacher = new InfoTeacher();
            infoTeacher.setIdInternal(new Integer(1));

            infoCareer.setInfoTeacher(infoTeacher);
            Object[] args = { careerId, infoCareer };

            ServiceManagerServiceFactory.executeService(userView, getNameOfServiceToBeTested(), args);

            compareDataSetUsingExceptedDataSetTablesAndColumns("etc/datasets/servicos/teacher/testExpectedEditTeachingCareerDataSet.xml");
        } catch (Exception ex) {
            fail("Editing a teaching career " + ex);
        }
    }
}