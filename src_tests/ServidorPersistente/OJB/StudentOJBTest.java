/*
 * StudentOJBTest.java
 * JUnit based test
 *
 * Created on 29 de Outubro de 2002, 19:40
 */

package ServidorPersistente.OJB;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.ojb.odmg.OJB;
import org.odmg.Implementation;
import org.odmg.OQLQuery;
import org.odmg.QueryException;

import Dominio.Frequenta;
import Dominio.IPessoa;
import Dominio.IStudent;
import Dominio.Pessoa;
import Dominio.Student;
import Dominio.StudentCurricularPlan;
import ServidorPersistente.ExcepcaoPersistencia;
import Util.EstadoCivil;
import Util.Sexo;
import Util.TipoCurso;
import Util.TipoDocumentoIdentificacao;

/**
 *
 * @author ciapl-nortadas
 */



public class StudentOJBTest extends TestCaseOJB {
	
    public StudentOJBTest(java.lang.String testName) {
        super(testName);
    }
    
    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite(StudentOJBTest.class);
        
        return suite;
    }
    
    protected void setUp() {
        super.setUp();
    }
    
    protected void tearDown() {
       super.tearDown();
    }
    
    /** Test of readByNumero method, of class ServidorPersistente.OJB.StudentOJB. */
   
    public void testReadByNumero() {
    IStudent student = null;
    // read existing aluno
    try {
      _suportePersistente.iniciarTransaccao();
      student = persistentStudent.readByNumero(new Integer(600), new TipoCurso(TipoCurso.LICENCIATURA));
	  assertNotNull(student);
      _suportePersistente.confirmarTransaccao();
    } catch (ExcepcaoPersistencia ex) {
      fail("testReadByNumero:fail read existing aluno");
    }
    
    assertEquals("testReadByNumero:read existing aluno",student.getNumber(), new Integer(600));
        
    // read unexisting aluno
    try {
      _suportePersistente.iniciarTransaccao();
      student = persistentStudent.readByNumero(new Integer(2005), new TipoCurso(TipoCurso.LICENCIATURA));
      assertNull(student);
      _suportePersistente.confirmarTransaccao();
    } catch (ExcepcaoPersistencia ex) {
      fail("testReadBySeccaoAndNome:fail read unexisting aluno");
    }
  }
    

	public void testReadByUsername() {
		IStudent student = null;
		try {
			_suportePersistente.iniciarTransaccao();
			student = persistentStudent.readByUsername("user");
			assertNotNull(student);
			_suportePersistente.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex) {
			fail("testReadByUsername:fail read existing aluno");
		}

		assertNotNull("testReadByUsername:read existing aluno",student);
		assertEquals("testReadByUsername:read existing aluno", student.getNumber(), new Integer(600)); 

		try {
			_suportePersistente.iniciarTransaccao();
			student = persistentStudent.readByUsername("No has this string for a username");
			assertNull(student);
			_suportePersistente.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex) {
			fail("testReadByUsername:fail read unexisting aluno");
		}
	}


    /** Test of readByNumeroAndEstado method, of class ServidorPersistente.OJB.StudentOJB. */
   
    public void testReadByNumeroAndEstado() {
    IStudent student = null;
    try {
      _suportePersistente.iniciarTransaccao();
      student = persistentStudent.readByNumeroAndEstado(new Integer(600),new Integer(567), new TipoCurso(TipoCurso.LICENCIATURA));
      assertNotNull(student);
      _suportePersistente.confirmarTransaccao();
    } catch (ExcepcaoPersistencia ex) {
      fail("testReadByNumeroAndEstado:fail read existing aluno");
    }
    assertEquals("testReadByNumeroAndEstado:read existing aluno",student.getNumber(), new Integer(600));
    assertEquals("testReadByNumeroAndEstado:read existing aluno",student.getState(), new Integer(567));
        
    try {
      _suportePersistente.iniciarTransaccao();
      student = persistentStudent.readByNumeroAndEstado(new Integer(2005),new Integer(567), new TipoCurso(TipoCurso.LICENCIATURA));
      assertNull(student);
      _suportePersistente.confirmarTransaccao();
    } catch (ExcepcaoPersistencia ex) {
      fail("testReadByNumeroAndEstado:fail read unexisting aluno");
    }
  }

    public void testReadByNumeroAndEstadoAndPessoa() {
    IStudent student = null;
    IPessoa person = null;
    try {
      _suportePersistente.iniciarTransaccao();
      
      person = _pessoaPersistente.lerPessoaPorUsername("user");
      assertNotNull(person);
      
      student = persistentStudent.readByNumeroAndEstadoAndPessoa(new Integer(600),new Integer(567), person, new TipoCurso(TipoCurso.LICENCIATURA));
      assertNotNull(student);
      _suportePersistente.confirmarTransaccao();
    } catch (ExcepcaoPersistencia ex) {
      fail("testReadByNumero:fail read existing aluno");
    }
    assertNotNull("testReadByNumero:read existing aluno", student);
    assertEquals("testReadByNumero:read existing aluno",student.getNumber(), new Integer(600));
    assertEquals("testReadByNumero:read existing aluno",student.getState(), new Integer(567));
    
    assertEquals("testReadByNumero:read existing aluno",student.getPerson().getNumeroDocumentoIdentificacao(), "123456789");
    
    
    try {
      _suportePersistente.iniciarTransaccao();
      student = persistentStudent.readByNumeroAndEstadoAndPessoa(new Integer(2005),new Integer(567),person, new TipoCurso(TipoCurso.LICENCIATURA));
      assertNull(student);
      _suportePersistente.confirmarTransaccao();
    } catch (ExcepcaoPersistencia ex) {
      fail("testReadByNumeroAndEstadoAndPessoa:fail read unexisting aluno");
    }
  }

    
    public void testReadAllAlunos() {
        ArrayList students = null;
        try {
            _suportePersistente.iniciarTransaccao();
            students = persistentStudent.readAllAlunos();
            _suportePersistente.confirmarTransaccao();
        } catch(ExcepcaoPersistencia ex) {
            fail("testReadAllAlunos: readAllAlunos");
        }
        assertNotNull(students);
        assertEquals(students.size(), 5);
    }
    
    /** Test of lockWrite method, of class ServidorPersistente.OJB.StudentOJB. */
 
   public void testCreateExistingAluno() {
    IPessoa person = new Pessoa("10000679", new TipoDocumentoIdentificacao(TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE), "Lisboa", new Date(), new Date(), "Ricardo Nortadas", new Sexo(Sexo.MASCULINO), new EstadoCivil(EstadoCivil.DIVORCIADO), new Date(), "Pai", "Mae", "Portugues", "Portugal", "Portugal", "Portugal", "Rua", "LindaAVelha", "2780", "Oeiras", "LindaAVelha", "Oeiras", "Lisboa", "214192888", "936344277", "rfan@mega.ist.utl.pt", "URL", "123231232132", "estudante", "rfan", "eu", null, "123123213123123123");
    try{
        _suportePersistente.iniciarTransaccao();
        _pessoaPersistente.escreverPessoa(person);
        _suportePersistente.confirmarTransaccao();
    }catch (ExcepcaoPersistencia ex) {
      fail("testCreateNonExistingPessoa");
    }
    IStudent student = new Student(new Integer(600),new Integer(67),person, new TipoCurso(TipoCurso.LICENCIATURA));
    try {
      _suportePersistente.iniciarTransaccao();
      persistentStudent.lockWrite(student);
      _suportePersistente.confirmarTransaccao();
      fail("testCreateExistingAluno");
    } catch (ExcepcaoPersistencia ex) {
      // all is ok
    }
  }
 
    public void testCreateNonExistingAluno() {      
        IPessoa person = new Pessoa("10000677", new TipoDocumentoIdentificacao(TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE), "Lisboa", new Date(), new Date(), "Ricardo Nortadas", new Sexo(Sexo.MASCULINO), new EstadoCivil(EstadoCivil.DIVORCIADO), new Date(), "Pai", "Mae", "Portugues", "Portugal", "Portugal", "Portugal", "Rua", "LindaAVelha", "2780", "Oeiras", "LindaAVelha", "Oeiras", "Lisboa", "214192888", "936344277", "rfan@mega.ist.utl.pt", "URL", "123231232132", "estudante", "rfan", "eu", null, "123123213123123123");
        try{
        _suportePersistente.iniciarTransaccao();
        _pessoaPersistente.escreverPessoa(person);
        _suportePersistente.confirmarTransaccao();
        }catch (ExcepcaoPersistencia ex) {
            fail("testCreateNonExistingPessoa");
        }
        IStudent student = new Student(new Integer(2004),new Integer(345),person, new TipoCurso(TipoCurso.LICENCIATURA));
        try {
            _suportePersistente.iniciarTransaccao();
            persistentStudent.lockWrite(student);
            _suportePersistente.confirmarTransaccao();
          } catch (ExcepcaoPersistencia ex) {
            fail("testCreateNonExistingaluno");
        }
    }
  
    public void testWriteExistingUnchangedObject() {
    try {
    	_suportePersistente.iniciarTransaccao();
    	IStudent student = persistentStudent.readByNumero(new Integer(600), new TipoCurso(TipoCurso.LICENCIATURA));
    	_suportePersistente.confirmarTransaccao();

        _suportePersistente.iniciarTransaccao();
        persistentStudent.lockWrite(student);
        _suportePersistente.confirmarTransaccao();
    } catch (ExcepcaoPersistencia ex) {
      fail("testWriteExistingUnchangedObject");
    }
  }

  // Test of write method, of class ServidorPersistente.OJB.alunoOJB. 
  public void testWriteExistingChangedObject() {
	IStudent student = null;
	// read existing aluno
	try {
	  _suportePersistente.iniciarTransaccao();
	  student = persistentStudent.readByNumero(new Integer(600), new TipoCurso(TipoCurso.LICENCIATURA));
	  assertNotNull(student);
	  student.setDegreeType(new TipoCurso(TipoCurso.DOUTORAMENTO));
	  _suportePersistente.confirmarTransaccao();
	} catch (ExcepcaoPersistencia ex) {
	  fail("testReadByNumero:fail read existing aluno");
	}

	// Check Change
	try {
	  _suportePersistente.iniciarTransaccao();
	  student = null;
	  student = persistentStudent.readByNumero(new Integer(600), new TipoCurso(TipoCurso.DOUTORAMENTO));
	  assertNotNull(student);
	  assertEquals(student.getDegreeType(), new TipoCurso(TipoCurso.DOUTORAMENTO));
	  _suportePersistente.confirmarTransaccao();
	} catch (ExcepcaoPersistencia ex) {
	  fail("testReadByNumero:fail read existing aluno");
	}
  }


  // Test of delete method, of class ServidorPersistente.OJB.alunoOJB. 
  public void testDeleteAluno() {
	IStudent student = null;
	// read existing aluno
	try {
	  _suportePersistente.iniciarTransaccao();
	  student = persistentStudent.readByNumero(new Integer(45498), new TipoCurso(TipoCurso.LICENCIATURA));
	  assertNotNull(student);
	  _suportePersistente.confirmarTransaccao();
	} catch (ExcepcaoPersistencia ex) {
	  fail("testReadByNumero:fail read existing aluno");
	}

	// Check Attends
	try {
		_suportePersistente.iniciarTransaccao();
		try {
			Implementation odmg = OJB.getInstance();
			OQLQuery query = odmg.newOQLQuery();
			String oqlQuery = "select all from " + Frequenta.class.getName();
			oqlQuery += " where aluno.number = $1"
			+ " and aluno.degreeType = $2";
			query.create(oqlQuery);
			query.bind(student.getNumber());
			query.bind(student.getDegreeType());
			List result = (List) query.execute();
			assertEquals(result.size(), 3);
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		} 
	} catch (ExcepcaoPersistencia ex) {
	  fail("testReadByNumero:fail read existing aluno");
	}
	
	// Check StudentCurricularPlans
	try {
	    _suportePersistente.iniciarTransaccao();
		try {
			Implementation odmg = OJB.getInstance();
			OQLQuery query = odmg.newOQLQuery();;
	
			String oqlQuery = "select all from " + StudentCurricularPlan.class.getName();
			oqlQuery += " where student.number = $1"
			+ " and student.degreeType = $2";
			query.create(oqlQuery);
			query.bind(student.getNumber());
			query.bind(student.getDegreeType());
			List result = (List) query.execute();
			assertEquals(result.size(), 1);
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		} 
	} catch (ExcepcaoPersistencia ex) {
	  fail("testReadByNumero:fail read existing aluno");
	}

	// Delete Student

	try {
	  _suportePersistente.iniciarTransaccao();
	  persistentStudent.delete(student);
	  _suportePersistente.confirmarTransaccao();
	} catch (ExcepcaoPersistencia ex) {
	  fail("testReadByNumero:fail read existing aluno");
	}

	// Check Attends
	try {
		_suportePersistente.iniciarTransaccao();
		try {
			Implementation odmg = OJB.getInstance();
			OQLQuery query = odmg.newOQLQuery();
			String oqlQuery = "select all from " + Frequenta.class.getName();
			oqlQuery += " where aluno.number = $1"
			+ " and aluno.degreeType = $2";
			query.create(oqlQuery);
			query.bind(student.getNumber());
			query.bind(student.getDegreeType());
			List result = (List) query.execute();
			assertEquals(result.size(), 0);
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		} 
	} catch (ExcepcaoPersistencia ex) {
	  fail("testReadByNumero:fail read existing aluno");
	}
	
	// Check StudentCurricularPlans
	try {
		_suportePersistente.iniciarTransaccao();
		try {
			Implementation odmg = OJB.getInstance();
			OQLQuery query = odmg.newOQLQuery();;
	
			String oqlQuery = "select all from " + StudentCurricularPlan.class.getName();
			oqlQuery += " where student.number = $1"
			+ " and student.degreeType = $2";
			query.create(oqlQuery);
			query.bind(student.getNumber());
			query.bind(student.getDegreeType());
			List result = (List) query.execute();
			assertEquals(result.size(), 0);
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		} 
	} catch (ExcepcaoPersistencia ex) {
	  fail("testReadByNumero:fail read existing aluno");
	}
  }

  // Test of deleteAll method, of class ServidorPersistente.OJB.alunoOJB. 
  public void testDeleteAll() {
    try {
      _suportePersistente.iniciarTransaccao();
      persistentStudent.deleteAll();
      _suportePersistente.confirmarTransaccao();
      _suportePersistente.iniciarTransaccao();
      List result = null;
      try {
        Implementation odmg = OJB.getInstance();
        OQLQuery query = odmg.newOQLQuery();;
        String oqlQuery = "select aluno from " + Student.class.getName();
        query.create(oqlQuery);
        result = (List) query.execute();
      } catch (QueryException ex) {
        throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
      }
      _suportePersistente.confirmarTransaccao();
      assertNotNull(result);
      assertTrue(result.isEmpty());
    } catch (ExcepcaoPersistencia ex) {
      fail("testDeleteAluno");
    }
  }   
 
}
    
 