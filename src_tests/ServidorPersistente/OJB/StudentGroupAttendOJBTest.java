/*
 * Created on 22/Jul/2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ServidorPersistente.OJB;

import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;
import Dominio.IDisciplinaExecucao;
import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;
import Dominio.IFrequenta;
import Dominio.IGroupProperties;
import Dominio.IStudent;
import Dominio.IStudentGroup;
import Dominio.IStudentGroupAttend;
import Dominio.StudentGroup;
import Dominio.StudentGroupAttend;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.IFrequentaPersistente;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.IPersistentGroupProperties;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.IPersistentStudentGroup;
import ServidorPersistente.IPersistentStudentGroupAttend;
import ServidorPersistente.ISuportePersistente;

/**
 * @author asnr and scpo
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class StudentGroupAttendOJBTest extends TestCaseOJB{
		
		ISuportePersistente persistentSupport=null;
		IDisciplinaExecucaoPersistente persistentExecutionCourse=null;
		IPersistentExecutionYear persistentExecutionYear=null;
		IPersistentExecutionPeriod persistentExecutionPeriod=null;
		IPersistentGroupProperties persistentGroupProperties =null;
		IPersistentStudentGroup persistentStudentGroup =null;
		IPersistentStudent persistentStudent=null;
		IFrequentaPersistente persistentAttend =null;
		IPersistentStudentGroupAttend persistentStudentGroupAttend =null;
		
		IStudentGroup studentGroup1=null;
		IStudentGroup studentGroup2=null;
		IFrequenta attend = null;
	IGroupProperties groupProperties1=null;
	
	  public StudentGroupAttendOJBTest(java.lang.String testName) {
		super(testName);
	  }
    
	  public static void main(java.lang.String[] args) {
		junit.textui.TestRunner.run(suite());
	  }
    
	  public static Test suite() {
		TestSuite suite = new TestSuite(StudentGroupAttendOJBTest.class);
        
		return suite;
	  }
    
	  protected void setUp() {
		try {
		
			
			persistentSupport = SuportePersistenteOJB.getInstance();
			
			persistentExecutionCourse = persistentSupport.getIDisciplinaExecucaoPersistente();		
			persistentExecutionYear = persistentSupport.getIPersistentExecutionYear();
			persistentExecutionPeriod = persistentSupport.getIPersistentExecutionPeriod();		
			persistentGroupProperties = persistentSupport.getIPersistentGroupProperties();
			persistentAttend = persistentSupport.getIFrequentaPersistente();
			persistentStudent = persistentSupport.getIPersistentStudent();
			persistentStudentGroup = persistentSupport.getIPersistentStudentGroup();
			persistentStudentGroupAttend = persistentSupport.getIPersistentStudentGroupAttend();
			persistentSupport.iniciarTransaccao();
		
			
			IExecutionYear executionYear = persistentExecutionYear.readExecutionYearByName("2002/2003");

			IExecutionPeriod executionPeriod =persistentExecutionPeriod.readByNameAndExecutionYear("2º Semestre", executionYear);

			IDisciplinaExecucao executionCourse1 =persistentExecutionCourse.readByExecutionCourseInitialsAndExecutionPeriod("TFCII",executionPeriod);
			IDisciplinaExecucao executionCourse2 = persistentExecutionCourse.readBySiglaAndAnoLectivoAndSiglaLicenciatura("PO","2002/2003","MEEC");
		
		
					
			groupProperties1 = persistentGroupProperties.readBy(executionCourse1);
			
			IGroupProperties groupProperties2 = persistentGroupProperties.readBy(executionCourse2);
			
			studentGroup1 = persistentStudentGroup.readBy(groupProperties1,new Integer(1));
			studentGroup2 = persistentStudentGroup.readBy(groupProperties2,new Integer(1));
			
			
			IStudent student = persistentStudent.readByUsername("user");
			
			attend = persistentAttend.readByAlunoAndDisciplinaExecucao(student,executionCourse1);
			
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
   
   
  
    
	  /** Test of lockWrite method, of class ServidorPersistente.OJB.StudentGroupAttendOJB. */
	  public void testReadBy() {
		
	  	
			  //read existing StudentGroupAttend  
			  try {
					  persistentSupport.iniciarTransaccao();	
					  IStudentGroupAttend existingStudentGroupAttend = persistentStudentGroupAttend.readBy(studentGroup1,attend);
					  assertNotNull(existingStudentGroupAttend);			
					  persistentSupport.confirmarTransaccao();

				} catch (ExcepcaoPersistencia ex) {
					fail("testReadBy:fail read existing StudentGroupAttend");
				}

				// read unexisting StudentGroupAttend
				try {
					  persistentSupport.iniciarTransaccao();	
					  IStudentGroupAttend unexistingStudentGroupAttend = persistentStudentGroupAttend.readBy(studentGroup2,attend);
					  assertNull(unexistingStudentGroupAttend);			
					  persistentSupport.confirmarTransaccao();

				} catch (ExcepcaoPersistencia ex) {
					fail("testReadBy:fail read non - existing StudentGroupAttend");
				}
			}	
  
	/** Test of delete method, of class ServidorPersistente.OJB.StudentGroupAttendOJB. */
			public void testDeleteStudentGroupAttend() {
			
				try {
				//	read existing StudentGroupAttend
					persistentSupport.iniciarTransaccao();	
					IStudentGroupAttend existingStudentGroupAttend = persistentStudentGroupAttend.readBy(studentGroup1,attend);
					assertNotNull(existingStudentGroupAttend);			
					persistentStudentGroupAttend.delete(existingStudentGroupAttend);
					persistentSupport.confirmarTransaccao();
			
				//	trying to read deleted StudentGroupAttend
					persistentSupport.iniciarTransaccao();	
					IStudentGroupAttend studentGroupAttendDeleted = persistentStudentGroupAttend.readBy(studentGroup1,attend);
					assertNull(studentGroupAttendDeleted);	
					persistentSupport.confirmarTransaccao();
				} catch (ExcepcaoPersistencia ex) {
					fail("testDeleteStudentGroupAttend");
				}
			}
	/** Test of Write method, of class ServidorPersistente.OJB.StudentGroupAttendOJB. */
			public void testLockWrite() {
				try{
					//write existing StudentGroupAttend
					persistentSupport.iniciarTransaccao();
					IStudentGroupAttend existingStudentGroupAttend = persistentStudentGroupAttend.readBy(studentGroup1,attend);
					persistentStudentGroupAttend.lockWrite(existingStudentGroupAttend);
					persistentSupport.confirmarTransaccao();
				}catch(ExcepcaoPersistencia excepcaoPersistencia) 
				{
					fail("testLockWrite: write the same StudentGroupAttend");
				
				}
			
				//	write existing StudentGroupAttend changed
				try{
					persistentSupport.iniciarTransaccao();
					IStudentGroupAttend existingStudentGroupAttendToChange = persistentStudentGroupAttend.readBy(studentGroup1,attend);
					Integer idAntigo = existingStudentGroupAttendToChange.getIdInternal();
					existingStudentGroupAttendToChange.setIdInternal(new Integer(idAntigo.intValue()+10));
					persistentStudentGroupAttend.lockWrite(existingStudentGroupAttendToChange);
					persistentSupport.confirmarTransaccao();
			
				}catch(ExcepcaoPersistencia excepcaoPersistencia) {
						System.out.println("testLockWrite: write Existing changed");
				
				}
			
				StudentGroup newStudentGroup = new StudentGroup(new Integer(18),groupProperties1);
				StudentGroupAttend newStudentGroupAttend =  new StudentGroupAttend(newStudentGroup,attend);
			
				try {
					persistentSupport.iniciarTransaccao();
					persistentStudentGroupAttend.lockWrite(newStudentGroupAttend);
					persistentSupport.confirmarTransaccao();
				} catch (ExcepcaoPersistencia excepcaoPersistencia) {
					fail("testLockWrite: write an invalid StudentGroupAttend");
				}
//
				// read StudentGroupAttend written
				try {
					persistentSupport.iniciarTransaccao();
					IStudentGroupAttend newStudentGroupAttendRead = persistentStudentGroupAttend.readBy(newStudentGroup,attend);
					assertNotNull(newStudentGroupAttendRead);
					persistentSupport.confirmarTransaccao();
				} catch (ExcepcaoPersistencia excepcaoPersistencia) {
					fail("testEscreverNota: unexpected exception reading");
				}
			
	
				}
	/** Test of deleteAll method, of class ServidorPersistente.OJB.StudentGroupAttendOJB. */
			public void testDeleteAll() {
				List allStudentGroupAttend=null;
				
				try {
					
				//	read all StudentGroupAttend	
					persistentSupport.iniciarTransaccao();	
					allStudentGroupAttend = persistentStudentGroupAttend.readAll();
					assertEquals(allStudentGroupAttend.size(),4);
					persistentSupport.confirmarTransaccao();
					
				//	deleteAll Method
					persistentSupport.iniciarTransaccao();	
					persistentStudentGroupAttend.deleteAll();
					persistentSupport.confirmarTransaccao();
					
				
				//	read all deleted StudentGroupAttend	
					persistentSupport.iniciarTransaccao();	
					allStudentGroupAttend = persistentStudentGroupAttend.readAll();
					assertEquals(allStudentGroupAttend.size(),0);
					persistentSupport.confirmarTransaccao();
					
			
				} catch (ExcepcaoPersistencia ex) {
					fail("testDeleteAllStudentGroupAttend");
				}
			}

	/** Test of readAll method, of class ServidorPersistente.OJB.StudentGroupAttendOJB. */
			public void testReadAll() {
				List allStudentGroupAttend=null;
				
				try {
					
					//	read all StudentGroupAttend	
						persistentSupport.iniciarTransaccao();	
						allStudentGroupAttend = persistentStudentGroupAttend.readAll();
						assertEquals(allStudentGroupAttend.size(),4);
						persistentSupport.confirmarTransaccao();
					
					
						} catch (ExcepcaoPersistencia ex) {
							fail("testReadAllStudentGroupAttend");
						}
					}
		
}
