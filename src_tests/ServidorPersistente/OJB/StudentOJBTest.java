package ServidorPersistente.OJB;

import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;
import Dominio.IPessoa;
import Dominio.IStudent;
import Dominio.Pessoa;
import Dominio.Student;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.IPessoaPersistente;
import ServidorPersistente.exceptions.ExistingPersistentException;
import Util.StudentState;
import Util.TipoCurso;

/**
 * @author dcs-rjao
 *
 * 20/Mar/2003
 */

public class StudentOJBTest extends TestCaseOJB {

	SuportePersistenteOJB persistentSupport = null;
	IPersistentStudent persistentStudent = null;
	IPessoaPersistente persistentPerson = null;

	public StudentOJBTest(java.lang.String testName) {
		super(testName);
	}

	public static void main(java.lang.String[] args) {
		System.out.println("Beginning of test from class StudentOJB.\n");
		junit.textui.TestRunner.run(suite());
		System.out.println("End of test from class StudentOJB.\n");
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(StudentOJBTest.class);
		return suite;
	}

	protected void setUp() {
		super.setUp();
		try {
			persistentSupport = SuportePersistenteOJB.getInstance();
		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace();
			fail("Error in SetUp.");
		}
		persistentStudent = persistentSupport.getIPersistentStudent();
		persistentPerson = persistentSupport.getIPessoaPersistente();
	}

	protected void tearDown() {
		super.tearDown();
	}

	// -------------------------------------------------------------------------------------------------------------------------

	public void testReadByUserName() {
		IPessoa person = null;
		IStudent student = null;
		System.out.println("1->read existing student");
		try {
			person= new Pessoa();
			person.setIdInternal(new Integer(3));
			persistentSupport.iniciarTransaccao();
			person = (IPessoa)persistentPerson.readByOId(person, false);
			assertNotNull(person);
			String username = person.getUsername();
			student = new Student();
			student.setIdInternal(new Integer(3));
			student = (IStudent) persistentStudent.readByOId(student, false);
			assertNotNull(student);
			IStudent studentFromDb = persistentStudent.readByUsername(username);
			assertNotNull(studentFromDb);
			assertEquals(student,studentFromDb);
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia e) {
			fail("failed reading by Username");
		}
		System.out.println("read by useername a unexisting student");
		try {
			persistentSupport.iniciarTransaccao();
					IStudent studentFromDb = persistentStudent.readByUsername("istonãoéumusername");
					assertNull(studentFromDb);
				
					persistentSupport.confirmarTransaccao();
				} catch (ExcepcaoPersistencia e) {
					fail("failed reading by Username");
				}
	}

	public void testWriteStudent() {

		IPessoa person = null;
		// read existing Pessoa
		try {
			persistentSupport.iniciarTransaccao();
			person = persistentPerson.lerPessoaPorUsername("Jorge");
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex) {
			fail("Reading Pessoa");
		}

		assertNotNull(person);

		// Student ja existente
		IStudent student =
			new Student(
				new Integer(600),
				new StudentState(567),
				person,
				new TipoCurso(1));

		System.out.println("\n- Test 1.1 : Write Existing Student\n");
		try {
			persistentSupport.iniciarTransaccao();
			persistentStudent.lockWrite(student);
			persistentSupport.confirmarTransaccao();
			fail("Write Existing Student");
		} catch (ExistingPersistentException ex) {
			// All Is OK
			try {
				persistentSupport.cancelarTransaccao();
			} catch (ExcepcaoPersistencia e) {
				e.printStackTrace();
				fail("cancelarTransaccao() in Write Existing Student");
			}
		} catch (ExcepcaoPersistencia ex) {
			fail("Unexpected exception in Write Existing Student");
		}

		// Student inexistente
		student =
			new Student(
				new Integer(123),
				new StudentState(1),
				person,
				new TipoCurso(10));

		System.out.println("\n- Test 1.2 : Write Non Existing Student\n");
		try {
			persistentSupport.iniciarTransaccao();
			persistentStudent.lockWrite(student);
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex2) {
			fail("Write Non Existing Student");
		}

		IStudent st = null;

		try {
			persistentSupport.iniciarTransaccao();
			st =
				persistentStudent.readStudentByNumberAndDegreeType(
					new Integer(123),
					new TipoCurso(10));
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex) {
			fail("Reading Non Existing Student Just Writen Before");
		}

		assertNotNull(st);

		assertTrue(st.equals(student));

	}

	// -------------------------------------------------------------------------------------------------------------------------

	public void testDeleteAllStudents() {

		System.out.println("\n- Test 2 : Delete All Students");
		try {
			persistentSupport.iniciarTransaccao();
			persistentStudent.deleteAll();
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex2) {
			fail("Delete All Students");
		}

		List result = null;

		try {
			persistentSupport.iniciarTransaccao();
			result = persistentStudent.readAll();
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex) {
			fail("Reading Result Of Deleting All Students");
		}

		assertNotNull(result);
		assertTrue(result.isEmpty());
	}

	// -------------------------------------------------------------------------------------------------------------------------

	public void testReadStudent() {

		IPessoa person = null;
		// read existing Pessoa
		try {
			persistentSupport.iniciarTransaccao();
			person = persistentPerson.lerPessoaPorUsername("Jorge");
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex) {
			fail("Reading Pessoa");
		}

		assertNotNull(person);

		IStudent student = null;

		// Student ja existente
		System.out.println("\n- Test 3.1 : Read Existing Student\n");
		try {
			persistentSupport.iniciarTransaccao();
			student =
				persistentStudent.readStudentByDegreeTypeAndPerson(
					new TipoCurso(1),
					person);
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex2) {
			fail("Read Existing Student");
		}
		assertNotNull(student);
		assertTrue(student.getNumber().intValue() == 700);

		// Student inexistente
		student = null;
		System.out.println("\n- Test 3.2 : Read Non Existing Student");
		try {
			persistentSupport.iniciarTransaccao();
			student =
				persistentStudent.readStudentByNumberAndDegreeType(
					new Integer(123),
					new TipoCurso(10));
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex2) {
			fail("Read Non Existing Student");
		}
		assertNull(student);
	}

	// -------------------------------------------------------------------------------------------------------------------------

	public void testDeleteStudent() {

		IStudent student = null;

		// Student ja existente
		System.out.println("\n- Test 4.1 : Delete Existing Student\n");
		try {
			persistentSupport.iniciarTransaccao();
			student =
				persistentStudent.readStudentByNumberAndDegreeType(
					new Integer(600),
					new TipoCurso(1));
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex) {
			fail("Reading Existing Student To Delete");
		}
		assertNotNull(student);

		try {
			persistentSupport.iniciarTransaccao();
			persistentStudent.delete(student);
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex3) {
			fail("Delete Existing Student");
		}

		IStudent st = null;
		try {
			persistentSupport.iniciarTransaccao();
			st =
				persistentStudent.readStudentByNumberAndDegreeType(
					new Integer(600),
					new TipoCurso(1));
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex) {
			fail("Reading Just Deleted Student");
		}
		assertNull(st);

		// Student inexistente
		System.out.println("\n- Test 4.2 : Delete Non Existing Student\n");
		try {
			persistentSupport.iniciarTransaccao();
			persistentStudent.delete(new Student());
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex2) {
			fail("Delete Existing Student");
		}
	}

	// -------------------------------------------------------------------------------------------------------------------------

	public void testReadAllStudents() {

		List list = null;

		System.out.println("\n- Test 5 : Read All Existing Student\n");
		try {
			persistentSupport.iniciarTransaccao();
			list = persistentStudent.readAll();
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex2) {
			fail("Read All Students");
		}
		assertNotNull(list);
		assertEquals(list.size(), 5);
	}

}