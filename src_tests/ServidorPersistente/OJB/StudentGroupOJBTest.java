/*
 * Created on 12/Mai/2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ServidorPersistente.OJB;

import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;
import Dominio.IDisciplinaExecucao;
import Dominio.IGroupProperties;
import Dominio.IStudentGroup;
import Dominio.StudentGroup;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.IPersistentGroupProperties;
import ServidorPersistente.IPersistentStudentGroup;
import ServidorPersistente.ISuportePersistente;

/**
 * @author asnr and scpo
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class StudentGroupOJBTest extends TestCaseOJB {
	
	ISuportePersistente persistentSupport=null;
	IDisciplinaExecucaoPersistente persistentExecutionCourse=null;
	IPersistentGroupProperties persistentGroupProperties =null;
	IPersistentStudentGroup persistentStudentGroup =null;
	
	IGroupProperties groupProperties1=null;
	
  public StudentGroupOJBTest(java.lang.String testName) {
	super(testName);
  }
    
  public static void main(java.lang.String[] args) {
	junit.textui.TestRunner.run(suite());
  }
    
  public static Test suite() {
	TestSuite suite = new TestSuite(StudentGroupOJBTest.class);
        
	return suite;
  }
    
  protected void setUp() {
	try {
		
		
		
		persistentSupport = SuportePersistenteOJB.getInstance();
			
		persistentExecutionCourse = persistentSupport.getIDisciplinaExecucaoPersistente();		
		persistentGroupProperties = persistentSupport.getIPersistentGroupProperties();
		persistentStudentGroup = persistentSupport.getIPersistentStudentGroup();
		persistentSupport.iniciarTransaccao();
		
		IDisciplinaExecucao executionCourse1 = persistentExecutionCourse.readBySiglaAndAnoLectivoAndSiglaLicenciatura("PO","2002/2003","MEEC");
		
		
		groupProperties1 = persistentGroupProperties.readGroupPropertiesByExecutionCourseAndProjectName(executionCourse1,"projectB");
		
		
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
   
   
  
    
  /** Test of lockWrite method, of class ServidorPersistente.OJB.StudentGroupOJB. */
  public void testReadBy() {
		
	  	
		  //read existing StudentGroup  
		  try {
				  persistentSupport.iniciarTransaccao();	
				  IStudentGroup existingStudentGroup = persistentStudentGroup.readBy(groupProperties1,new Integer(1));
				  assertNotNull(existingStudentGroup);			
				  persistentSupport.confirmarTransaccao();

			} catch (ExcepcaoPersistencia ex) {
				fail("testReadBy:fail read existing StudentGroup");
			}

			// read unexisting StudentGroup
			try {
				  persistentSupport.iniciarTransaccao();	
				  IStudentGroup unexistingStudentGroup = persistentStudentGroup.readBy(groupProperties1,new Integer(99));
				  assertNull(unexistingStudentGroup);			
				  persistentSupport.confirmarTransaccao();

			} catch (ExcepcaoPersistencia ex) {
				fail("testReadBy:fail read non - existing StudentGroup");
			}
		}	
  
	/** Test of delete method, of class ServidorPersistente.OJB.StudentGroupOJB. */
			public void testDeleteStudentGroup() {
			
				try {
				//	read existing StudentGroup
					persistentSupport.iniciarTransaccao();	
			  		IStudentGroup existingStudentGroup = persistentStudentGroup.readBy(groupProperties1,new Integer(1));
			  		assertNotNull(existingStudentGroup);			
			  		persistentStudentGroup.delete(existingStudentGroup);
					persistentSupport.confirmarTransaccao();
			
				//	trying to read deleted StudentGroup
					persistentSupport.iniciarTransaccao();	
					IStudentGroup studentGroupDeleted = persistentStudentGroup.readBy(groupProperties1,new Integer(1));
			  		assertNull(studentGroupDeleted);	
					persistentSupport.confirmarTransaccao();
				} catch (ExcepcaoPersistencia ex) {
					fail("testDeleteStudentGroup");
				}
			}
	/** Test of Write method, of class ServidorPersistente.OJB.StudentGroupOJB. */
			public void testLockWrite() {
				try{
					//write existing StudentGroup
					persistentSupport.iniciarTransaccao();
					IStudentGroup existingStudentGroup = persistentStudentGroup.readBy(groupProperties1,new Integer(1));
					persistentStudentGroup.lockWrite(existingStudentGroup);
					persistentSupport.confirmarTransaccao();
				}catch(ExcepcaoPersistencia excepcaoPersistencia) 
				{
					fail("testLockWrite: write the same StudentGroup");
				
				}
			
				//	write existing StudentGroup changed
				try{
					persistentSupport.iniciarTransaccao();
					IStudentGroup existingStudentGroupToChange = persistentStudentGroup.readBy(groupProperties1,new Integer(1));
					Integer idAntigo = existingStudentGroupToChange.getIdInternal();
					existingStudentGroupToChange.setIdInternal(new Integer(idAntigo.intValue()+1));
					persistentStudentGroup.lockWrite(existingStudentGroupToChange);
					persistentSupport.confirmarTransaccao();
			
				}catch(ExcepcaoPersistencia excepcaoPersistencia) {
						System.out.println("testLockWrite: write Existing changed");
				
				}
			
				StudentGroup newStudentGroup =  new StudentGroup(new Integer(10),groupProperties1);
			
				try {
					persistentSupport.iniciarTransaccao();
					persistentStudentGroup.lockWrite(newStudentGroup);
					persistentSupport.confirmarTransaccao();
				} catch (ExcepcaoPersistencia excepcaoPersistencia) {
					fail("testLockWrite: write an invalid StudentGroup");
				}
//
				// read StudentGroup written
				try {
					persistentSupport.iniciarTransaccao();
					IStudentGroup newStudentGroupRead = persistentStudentGroup.readBy(groupProperties1,new Integer(10));
					assertNotNull(newStudentGroupRead);
					persistentSupport.confirmarTransaccao();
				} catch (ExcepcaoPersistencia excepcaoPersistencia) {
					fail("testEscreverNota: unexpected exception reading");
				}
			
	
				}
	/** Test of deleteAll method, of class ServidorPersistente.OJB.StudentGroupOJB. */
			public void testDeleteAll() {
				List allStudentGroup=null;
				
				try {
					
				//	read all StudentGroup	
					persistentSupport.iniciarTransaccao();	
					allStudentGroup = persistentStudentGroup.readAll();
					assertEquals(allStudentGroup.size(),6);
					persistentSupport.confirmarTransaccao();
					
				//	deleteAll Method
					persistentSupport.iniciarTransaccao();	
					persistentStudentGroup.deleteAll();
					persistentSupport.confirmarTransaccao();
					
				
				//	read all deleted StudentGroup	
					persistentSupport.iniciarTransaccao();	
					allStudentGroup = persistentStudentGroup.readAll();
					assertEquals(allStudentGroup.size(),0);
					persistentSupport.confirmarTransaccao();
					
			
				} catch (ExcepcaoPersistencia ex) {
					fail("testDeleteAllStudentGroup");
				}
			}

	/** Test of readAll method, of class ServidorPersistente.OJB.StudentGroupOJB. */
			public void testReadAll() {
				List allStudentGroup=null;
				
				try {
					
					//	read all StudentGroup	
						persistentSupport.iniciarTransaccao();	
						allStudentGroup = persistentStudentGroup.readAll();
						assertEquals(allStudentGroup.size(),6);
						persistentSupport.confirmarTransaccao();
					
					
						} catch (ExcepcaoPersistencia ex) {
							fail("testReadAllStudentGroup");
						}
					}
		
}
