/*
 * Created on 21/Jul/2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ServidorPersistente.OJB;

import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;
import Dominio.GroupProperties;
import Dominio.IDisciplinaExecucao;
import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;
import Dominio.IGroupProperties;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.IPersistentGroupProperties;
import ServidorPersistente.ISuportePersistente;
import Util.EnrolmentPolicyType;

/**
 * @author asnr and scpo
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class GroupPropertiesOJBTest extends TestCaseOJB {

	ISuportePersistente persistentSupport=null;
	IDisciplinaExecucaoPersistente persistentExecutionCourse=null;
	IPersistentGroupProperties persistentGroupProperties =null;
	IPersistentExecutionYear persistentExecutionYear=null;
	IPersistentExecutionPeriod persistentExecutionPeriod=null;
	IDisciplinaExecucao executionCourse1=null;
	IDisciplinaExecucao executionCourse2=null;
	
	
	
  public GroupPropertiesOJBTest(java.lang.String testName) {
	super(testName);
  }
    
  public static void main(java.lang.String[] args) {
	junit.textui.TestRunner.run(suite());
  }
    
  public static Test suite() {
	TestSuite suite = new TestSuite(GroupPropertiesOJBTest.class);
        
	return suite;
  }
    
  protected void setUp() {
	try {
		
		persistentSupport = SuportePersistenteOJB.getInstance();
			
		persistentExecutionCourse = persistentSupport.getIDisciplinaExecucaoPersistente();		
		persistentGroupProperties = persistentSupport.getIPersistentGroupProperties();
		persistentExecutionYear =persistentSupport.getIPersistentExecutionYear();
		persistentExecutionPeriod =persistentSupport.getIPersistentExecutionPeriod();
		persistentSupport.iniciarTransaccao();
		
		executionCourse1 = persistentExecutionCourse.readBySiglaAndAnoLectivoAndSiglaLicenciatura("PO","2002/2003","MEEC");
	
		
		IExecutionYear executionYear = persistentExecutionYear.readExecutionYearByName("2002/2003");

		IExecutionPeriod executionPeriod =persistentExecutionPeriod.readByNameAndExecutionYear("2º Semestre", executionYear);

		executionCourse2 =persistentExecutionCourse.readByExecutionCourseInitialsAndExecutionPeriod("TFCI",executionPeriod);
		
		
		persistentSupport.confirmarTransaccao();
	  	
	
	} catch (ExcepcaoPersistencia e) {
		e.printStackTrace();
		fail("Error");
	}	
	
	super.setUp();

  }
    
  protected void tearDown() {
	super.tearDown();
  }
  
  
  /** Test of readByExam method, of class ServidorPersistente.OJB.MarkOJB. */
	public void testReadGroupPropertiesByExecutionCourseAndProjectName() {
		
	  	
		//read existing GroupProperties  
		try {
				persistentSupport.iniciarTransaccao();	
				IGroupProperties existingGroupProperties = persistentGroupProperties.readGroupPropertiesByExecutionCourseAndProjectName(executionCourse1,"projectB");
			  	assertNotNull(existingGroupProperties);			
			  	persistentSupport.confirmarTransaccao();

		  } catch (ExcepcaoPersistencia ex) {
			  fail("testReadBy:fail read existing GroupProperties");
		  }

		  // read unexisting GroupProperties
		  try {
				persistentSupport.iniciarTransaccao();	
				IGroupProperties unexistingGroupProperties = persistentGroupProperties.readGroupPropertiesByExecutionCourseAndProjectName(executionCourse2,"nonExistingProject");
				assertNull(unexistingGroupProperties);			
				persistentSupport.confirmarTransaccao();

		  } catch (ExcepcaoPersistencia ex) {
			  fail("testReadBy:fail read non - existing GroupProperties");
		  }
	  }	
  
	/** Test of readByExam method, of class ServidorPersistente.OJB.MarkOJB. */
		public void testReadAllGroupPropertiesByExecutionCourse() {
		
	  	
			//read existing GroupProperties  
			try {
					persistentSupport.iniciarTransaccao();	
					List existingGroupProperties = persistentGroupProperties.readAllGroupPropertiesByExecutionCourse(executionCourse1);
					assertEquals(existingGroupProperties.size(),2);			
					persistentSupport.confirmarTransaccao();

			  } catch (ExcepcaoPersistencia ex) {
				  fail("testReadBy:fail read existing all executionCourse's GroupProperties ");
			  }

			  // read unexisting GroupProperties
			  try {
					persistentSupport.iniciarTransaccao();	
					List unexistingGroupProperties = persistentGroupProperties.readAllGroupPropertiesByExecutionCourse(executionCourse2);
					assertEquals(unexistingGroupProperties.size(),0);			
					persistentSupport.confirmarTransaccao();

			  } catch (ExcepcaoPersistencia ex) {
				  fail("testReadBy:fail read non - existing executionCourse's GroupProperties");
			  }
		  }	
  
  
	/** Test of delete method, of class ServidorPersistente.OJB.GroupPropertiesOJB. */
		public void testDeleteGroupProperties() {
			
			try {
			//	read existing GroupProperties
				persistentSupport.iniciarTransaccao();	
				IGroupProperties groupProperties =  persistentGroupProperties.readGroupPropertiesByExecutionCourseAndProjectName(executionCourse1,"projectB");
				assertNotNull(groupProperties);	
				persistentGroupProperties.delete(groupProperties);
				persistentSupport.confirmarTransaccao();
			
			//	trying to read deleted GroupProperties
				persistentSupport.iniciarTransaccao();	
				
				IGroupProperties groupPropertiesDeleted =  persistentGroupProperties.readGroupPropertiesByExecutionCourseAndProjectName(executionCourse1,"projectB");
				assertNull(groupPropertiesDeleted);	
				persistentSupport.confirmarTransaccao();
			} catch (ExcepcaoPersistencia ex) {
				fail("testDeleteGroupProperties");
			}
		}
		
		
		
	/** Test of Write method, of class ServidorPersistente.OJB.GroupPropertiesOJB. */
		public void testLockWrite() {
			try{
				//write existing GroupProperties
				persistentSupport.iniciarTransaccao();
				IGroupProperties existingGroupProperties = persistentGroupProperties.readGroupPropertiesByExecutionCourseAndProjectName(executionCourse1,"projectB");
				persistentGroupProperties.lockWrite(existingGroupProperties);
				persistentSupport.confirmarTransaccao();
			}catch(ExcepcaoPersistencia excepcaoPersistencia) 
			{
				fail("testLockWrite: write the same groupProperties");
				
			}
			
			//	write existing GroupProperties
			try{
				persistentSupport.iniciarTransaccao();
				IGroupProperties existingGroupPropertiesToChange = persistentGroupProperties.readGroupPropertiesByExecutionCourseAndProjectName(executionCourse1,"projectB");
				Integer idAntigo = existingGroupPropertiesToChange.getIdInternal();
				existingGroupPropertiesToChange.setIdInternal(new Integer(idAntigo.intValue()+1));
				persistentGroupProperties.lockWrite(existingGroupPropertiesToChange);
				persistentSupport.confirmarTransaccao();
			
			}catch(ExcepcaoPersistencia excepcaoPersistencia) {
					System.out.println("testLockWrite: write Existing changed");
				
			}
			
			GroupProperties newGroupProperties =  new GroupProperties(new Integer(4),new Integer (1),
			new Integer(3),new EnrolmentPolicyType(2),
			new Integer(10),"projectD",executionCourse2);
			
			try {
				persistentSupport.iniciarTransaccao();
				persistentGroupProperties.lockWrite(newGroupProperties);
				persistentSupport.confirmarTransaccao();
			} catch (ExcepcaoPersistencia excepcaoPersistencia) {
				fail("testLockWrite: write an invalid groupProperties");
			}

			
			try {
				persistentSupport.iniciarTransaccao();
				IGroupProperties newGroupPropertiesRead = persistentGroupProperties.readGroupPropertiesByExecutionCourseAndProjectName(executionCourse2,"projectD");;
				assertNotNull(newGroupPropertiesRead);
				persistentSupport.confirmarTransaccao();
			} catch (ExcepcaoPersistencia excepcaoPersistencia) {
				fail("testEscreverNota: unexpected exception reading");
			}
			
	
			}
		
	/** Test of deleteAll method, of class ServidorPersistente.OJB.GroupPropertiesOJB. */
			public void testDeleteAll() {
				List allGroupProperties=null;
				
				try {
					
				//	read all groupProperties	
					persistentSupport.iniciarTransaccao();	
					allGroupProperties = persistentGroupProperties.readAll();
					assertEquals(allGroupProperties.size(),4);
					persistentSupport.confirmarTransaccao();
					
				//	deleteAll Method
					persistentSupport.iniciarTransaccao();	
					persistentGroupProperties.deleteAll();
					persistentSupport.confirmarTransaccao();
					
				
				//	read all deleted groupProperties	
					persistentSupport.iniciarTransaccao();	
					allGroupProperties = persistentGroupProperties.readAll();
					assertEquals(allGroupProperties.size(),0);
					persistentSupport.confirmarTransaccao();
					
			
				} catch (ExcepcaoPersistencia ex) {
					fail("testDeleteAllGroupProperties");
				}
			}
		
	/** Test of readAll method, of class ServidorPersistente.OJB.GroupPropertiesOJB. */
		public void testReadAll() {
			List allGroupProperties=null;
				
			try {
					
				//	read all groupProperties	
					persistentSupport.iniciarTransaccao();	
					allGroupProperties = persistentGroupProperties.readAll();
					assertEquals(allGroupProperties.size(),4);
					persistentSupport.confirmarTransaccao();
					
					
					} catch (ExcepcaoPersistencia ex) {
						fail("testReadAllGroupProperties");
					}
				}
		
  
}
