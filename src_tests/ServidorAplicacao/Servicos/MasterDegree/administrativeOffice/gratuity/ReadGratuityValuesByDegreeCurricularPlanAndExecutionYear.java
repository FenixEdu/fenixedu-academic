/*
 * Created on 13/Jan/2004
 *  
 */
package ServidorAplicacao.Servicos.MasterDegree.administrativeOffice.gratuity;

import DataBeans.InfoGratuityValues;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servicos.MasterDegree.administrativeOffice.AdministrativeOfficeBaseTest;
import framework.factory.ServiceManagerServiceFactory;

/**
 * @author Tânia Pousão
 *  
 */
public class ReadGratuityValuesByDegreeCurricularPlanAndExecutionYear
	extends AdministrativeOfficeBaseTest
{

	public ReadGratuityValuesByDegreeCurricularPlanAndExecutionYear(String name)
	{
		super(name);
		this.dataSetFilePath =
			"etc/datasets/servicos/MasterDegree/administrativeOffice/gratuity/testReadGratuityValuesByDegreeCurricularPlanAndExecutionYearDataSet.xml";
	}

	protected String getNameOfServiceToBeTested()
	{
		return "ReadGratuityValuesByDegreeCurricularPlanAndExecutionYear";
	}

	protected Object[] getServiceArgumentsForNotAuthenticatedUser()
	{
		Object[] args = { new Integer(33), new String("2003/2004")};

		return args;
	}

	protected Object[] getServiceArgumentsForNotAuthorizedUser()
	{
		Object[] args = { new Integer(33), new String("2003/2004")};

		return args;
	}

	public void testSucessReadGratuityValues()
	{
		Object[] args = { new Integer(33), new String("2003/2004") };
		
		InfoGratuityValues infoGratuityValues = null;
		try
		{
			infoGratuityValues = (InfoGratuityValues) ServiceManagerServiceFactory.executeService(userView, getNameOfServiceToBeTested(), args);
		}
		catch (FenixServiceException e)
		{
			e.printStackTrace();
			fail("testSucessReadGratuityValues " + e.getMessage());
		}
		
		//<GRATUITY_VALUES ID_INTERNAL='1' ANUAL_VALUE='1000' SCHOLARSHIP_VALUE='NULL' FINAL_PROOF_VALUE='NULL' 
		//COURSE_VALUE='50' CREDIT_VALUE='NULL' PROOF_REQUEST_PAYMENT='1' START_PAYMENT='2003-01-01' END_PAYMENT='2003-12-31' KEY_EXECUTION_DEGREE='10' />
		assertNotNull(infoGratuityValues);
		assertEquals(infoGratuityValues.getIdInternal(), new Integer(1));
		assertEquals(infoGratuityValues.getAnualValue(), new Double(1000));
		assertNull(infoGratuityValues.getScholarShipValue());
		assertNull(infoGratuityValues.getFinalProofValue());
		assertEquals(infoGratuityValues.getCourseValue(), new Double(50));
		assertNull(infoGratuityValues.getCreditValue());
		assertEquals(infoGratuityValues.getProofRequestPayment(), Boolean.TRUE);
		assertNotNull(infoGratuityValues.getInfoExecutionDegree());
		assertEquals(infoGratuityValues.getInfoExecutionDegree().getIdInternal(), new Integer(10));
	}
	
	public void testReadGratuityValuesThatDontExists()
	{
		Object[] args = { new Integer(35), new String("2003/2004") };
		
		InfoGratuityValues infoGratuityValues = null;
		try
		{
			infoGratuityValues = (InfoGratuityValues) ServiceManagerServiceFactory.executeService(userView, getNameOfServiceToBeTested(), args);
		}
		catch (FenixServiceException e)
		{
			e.printStackTrace();
			fail("testSucessReadGratuityValues " + e.getMessage());
		}
		
		assertNull((infoGratuityValues));		
	}
	
	public void testReadGratuityValuesWithArg1NULL()
	{
		Object[] args = { null, new String("2003/2004") };
		
		
		try
		{
			ServiceManagerServiceFactory.executeService(userView, getNameOfServiceToBeTested(), args);
		}
		catch (FenixServiceException e)
		{
			e.printStackTrace();
			String msg =
			e.getMessage().substring(
					e.getMessage().lastIndexOf(".") + 1,
					e.getMessage().lastIndexOf(".") + 16);
			assertEquals(new String("noGratuityValues"), msg);
		}
	}
	
	public void testReadGratuityValuesWithArg2NULL()
	{
		Object[] args = { new Integer(35), null };
		
		try
		{
		    ServiceManagerServiceFactory.executeService(userView, getNameOfServiceToBeTested(), args);
		}
		catch (FenixServiceException e)
		{
			e.printStackTrace();
			String msg =
			e.getMessage().substring(
					e.getMessage().lastIndexOf(".") + 1,
					e.getMessage().lastIndexOf(".") + 16);
			assertEquals(new String("noGratuityValues"), msg);
		}
	}
	
	public void testReadGratuityValuesWithDegreeCurricularPlanUnExist()
	{
		Object[] args = { new Integer(1), new String("2003/2004") };
		
		try
		{
		    ServiceManagerServiceFactory.executeService(userView, getNameOfServiceToBeTested(), args);
		}
		catch (FenixServiceException e)
		{
			e.printStackTrace();
			String msg =
			e.getMessage().substring(
					e.getMessage().lastIndexOf(".") + 1,
					e.getMessage().lastIndexOf(".") + 16);
			assertEquals(new String("noGratuityValues"), msg);
		}
	}
	
	public void testReadGratuityValuesWithExecutionYearUnExist()
	{
		Object[] args = { new Integer(1), new String("2006/2007") };
		
		try
		{
		    ServiceManagerServiceFactory.executeService(userView, getNameOfServiceToBeTested(), args);
		}
		catch (FenixServiceException e)
		{
			e.printStackTrace();
			String msg =
			e.getMessage().substring(
					e.getMessage().lastIndexOf(".") + 1,
					e.getMessage().lastIndexOf(".") + 16);
			assertEquals(new String("noGratuityValues"), msg);
		}
	}
	
	public void testReadGratuityValuesWithExecutionDegreeUnExist()
	{
		Object[] args = { new Integer(37), new String("2003/2004") };
		
		try
		{
		    ServiceManagerServiceFactory.executeService(userView, getNameOfServiceToBeTested(), args);
		}
		catch (FenixServiceException e)
		{
			e.printStackTrace();
			String msg =
			e.getMessage().substring(
					e.getMessage().lastIndexOf(".") + 1,
					e.getMessage().lastIndexOf(".") + 16);
			assertEquals(new String("noGratuityValues"), msg);
		}
	}
	
}
