package ServidorAplicacao.Servicos.MasterDegree.administrativeOffice.gratuity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import DataBeans.InfoCurriculum;
import DataBeans.InfoEmployee;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoGratuityValues;
import DataBeans.InfoPaymentPhase;
import DataBeans.InfoPerson;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servicos.MasterDegree.administrativeOffice.AdministrativeOfficeBaseTest;
import ServidorApresentacao.Action.masterDegree.utils.SessionConstants;
import ServidorPersistente.IPersistentGratuityValues;
import framework.factory.ServiceManagerServiceFactory;

/**
 * 
 * @author Fernanda Quitério
 * 19/Jan/2004
 *
 */
public class InsertGratuityDataTest extends AdministrativeOfficeBaseTest
{

	/**
	 * @param name
	 */
	public InsertGratuityDataTest(String name)
	{
		super(name);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.ServiceTestCase#getNameOfServiceToBeTested()
	 */
	protected String getNameOfServiceToBeTested()
	{
		return new String("InsertGratuityData");
	}

	protected String getDataSetFilePath() {
		return "etc/datasets_templates/servicos/MasterDegree/administrativeOffice/gratuity/testInsertGratuityDataDataSet.xml";
	}
	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.MasterDegree.administrativeOffice.AdministrativeOfficeBaseTest#getServiceArgumentsForNotAuthenticatedUser()
	 */
	protected Object[] getServiceArgumentsForNotAuthenticatedUser() throws FenixServiceException
	{
		Calendar now = Calendar.getInstance();
		
		InfoPerson infoPerson = new InfoPerson();
		infoPerson.setUsername("naoAutenticado");
		
		InfoEmployee infoEmployee = new InfoEmployee();
		infoEmployee.setPerson(infoPerson);
		
		InfoExecutionDegree infoExecutionDegree = new InfoExecutionDegree();
		infoExecutionDegree.setIdInternal(new Integer(76));
		
		InfoGratuityValues infoGratuityValues = new InfoGratuityValues();
		infoGratuityValues.setIdInternal(new Integer(111));
		infoGratuityValues.setAnualValue(new Double(650));
		infoGratuityValues.setCreditValue(new Double(50));
		infoGratuityValues.setRegistrationPayment(Boolean.TRUE);
		infoGratuityValues.setWhen(now.getTime());
		infoGratuityValues.setInfoEmployee(infoEmployee);
		infoGratuityValues.setInfoExecutionDegree(infoExecutionDegree);
		
		Object args[] = { infoGratuityValues };
		return args;
	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.MasterDegree.administrativeOffice.AdministrativeOfficeBaseTest#getServiceArgumentsForNotAuthorizedUser()
	 */
	protected Object[] getServiceArgumentsForNotAuthorizedUser() throws FenixServiceException
	{
		Calendar now = Calendar.getInstance();
		
		InfoPerson infoPerson = new InfoPerson();
		infoPerson.setUsername(getAuthenticatedAndNotAuthorizedUser()[0]);
		
		InfoEmployee infoEmployee = new InfoEmployee();
		infoEmployee.setPerson(infoPerson);
		
		InfoExecutionDegree infoExecutionDegree = new InfoExecutionDegree();
		infoExecutionDegree.setIdInternal(new Integer(76));
		
		InfoGratuityValues infoGratuityValues = new InfoGratuityValues();
		infoGratuityValues.setIdInternal(new Integer(111));
		infoGratuityValues.setAnualValue(new Double(650));
		infoGratuityValues.setCreditValue(new Double(50));
		infoGratuityValues.setRegistrationPayment(Boolean.TRUE);
		infoGratuityValues.setWhen(now.getTime());
		infoGratuityValues.setInfoEmployee(infoEmployee);
		infoGratuityValues.setInfoExecutionDegree(infoExecutionDegree);
		
		Object args[] = { infoGratuityValues };
		return args;
	}

	
	/* (non-Javadoc)
	 * @see ServidorAplicacao.Servicos.MasterDegree.administrativeOffice.AdministrativeOfficeBaseTest#getServiceArgumentsForNotAuthorizedUser()
	 */
	protected Object[] getServiceArgumentForSuccess() throws FenixServiceException
	{
		Calendar now = Calendar.getInstance();
		
		InfoPerson infoPerson = new InfoPerson();
		infoPerson.setUsername(getAuthenticatedAndAuthorizedUser()[0]);
		
		InfoEmployee infoEmployee = new InfoEmployee();
		infoEmployee.setPerson(infoPerson);
		
		InfoExecutionDegree infoExecutionDegree = new InfoExecutionDegree();
		infoExecutionDegree.setIdInternal(new Integer(76));
		
		List infoPaymentPhases = new ArrayList();
		InfoPaymentPhase infoPaymentPhase = new InfoPaymentPhase();
		infoPaymentPhase.setDescription(SessionConstants.REGISTRATION_PAYMENT);
		now.set(2004, 1, 20);
		infoPaymentPhase.setEndDate(now.getTime());
		infoPaymentPhase.setValue(new Double(150));
		
		infoPaymentPhases.add(infoPaymentPhase);
		
		infoPaymentPhase = new InfoPaymentPhase();
		now.set(2004, 5, 21);
		infoPaymentPhase.setEndDate(now.getTime());
		now.set(2004, 1, 21);
		infoPaymentPhase.setStartDate(now.getTime());
		infoPaymentPhase.setValue(new Double(200));
		
		infoPaymentPhases.add(infoPaymentPhase);
		
		infoPaymentPhase = new InfoPaymentPhase();
		now.set(2004, 7, 22);
		infoPaymentPhase.setEndDate(now.getTime());
		now.set(2004, 5, 22);
		infoPaymentPhase.setStartDate(now.getTime());
		infoPaymentPhase.setValue(new Double(300));
		
		infoPaymentPhases.add(infoPaymentPhase);
		
		InfoGratuityValues infoGratuityValues = new InfoGratuityValues();
		infoGratuityValues.setIdInternal(new Integer(111));
		infoGratuityValues.setAnualValue(new Double(650));
		infoGratuityValues.setCreditValue(new Double(50));
		infoGratuityValues.setRegistrationPayment(Boolean.TRUE);
		infoGratuityValues.setWhen(now.getTime());
		infoGratuityValues.setInfoEmployee(infoEmployee);
		infoGratuityValues.setInfoExecutionDegree(infoExecutionDegree);
		infoGratuityValues.setInfoPaymentPhases(infoPaymentPhases);
		
		Object args[] = { infoGratuityValues };
		return args;
	}
	
	public void testInsertGratuityDataSuccessfull() {
		try {

			Object[] args = getServiceArgumentForSuccess();

			//Valid user
			String[] argsUser = getAuthenticatedAndAuthorizedUser();
			IUserView id = (IUserView) ServiceManagerServiceFactory.executeService(null, "Autenticacao", argsUser);

			ServiceManagerServiceFactory.executeService(id, getNameOfServiceToBeTested(), args);

			IPersistentGratuityValues persistentGratuityValues = S
			
			//Check information read is correct
			assertEquals(new String("objectivos gerais em portugues"), infoCurriculum.getGeneralObjectives());
			assertEquals(new String("objectivos gerais em ingles"), infoCurriculum.getGeneralObjectivesEn());
			assertEquals(new String("objectivos operacionais em portugues"), infoCurriculum.getOperacionalObjectives());
			assertEquals(new String("objectivos operacionais em ingles"), infoCurriculum.getOperacionalObjectivesEn());
			assertEquals(new String("programa"), infoCurriculum.getProgram());
			assertEquals(new String("programa em ingles"), infoCurriculum.getProgramEn());
			Calendar calendar = Calendar.getInstance();
			calendar.clear();
			calendar.set(2003, 10-1, 20, 0, 0, 0);
			assertEquals(calendar.getTime().getTime(), infoCurriculum.getLastModificationDate().getTime());

			assertEquals(infoCurriculum.getInfoCurricularCourse().getIdInternal(), new Integer(1));
			assertEquals(new Integer(infoCurriculum.getInfoCurricularCourse().getInfoScopes().size()), new Integer(1));
			assertEquals(new Integer(infoCurriculum.getInfoCurricularCourse().getInfoAssociatedExecutionCourses().size()), new Integer(0));

			System.out.println(
					"testReadCurrentCurriculumByCurricularCourseCodeSuccessfull was SUCCESSFULY runned by service: " + getNameOfServiceToBeTested());

		} catch (FenixServiceException e) {
			e.printStackTrace();
			fail("Reading a Curriculum from database " + e);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Reading a Curriculum from database " + e);
		}
	}
	
}
