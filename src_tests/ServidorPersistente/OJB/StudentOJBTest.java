/*
 * StudentOJBTest.java
 * JUnit based test
 *
 * Created on 29 de Outubro de 2002, 19:40
 */

package ServidorPersistente.OJB;

import java.util.ArrayList;
import junit.framework.*;
import java.util.List;
import org.apache.ojb.odmg.OJB;
import org.odmg.QueryException;
import org.odmg.OQLQuery;
import org.odmg.Implementation;
import ServidorPersistente.*;
import Dominio.*;
import Util.*;
import java.util.Date;

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
       //super.tearDown();
    }
    
    /** Test of readByNumero method, of class ServidorPersistente.OJB.StudentOJB. */
   
    public void testReadByNumero() {
    IStudent aluno = null;
    // read existing aluno
    try {
      _suportePersistente.iniciarTransaccao();
      aluno = persistentStudent.readByNumero(new Integer(600), licenciatura);
      _suportePersistente.confirmarTransaccao();
    } catch (ExcepcaoPersistencia ex) {
      fail("testReadByNumero:fail read existing aluno");
    }
    
    assertEquals("testReadByNumero:read existing aluno",aluno.getNumber(),alunojaexistente.getNumber());
        
    // read unexisting aluno
    try {
      _suportePersistente.iniciarTransaccao();
      aluno = persistentStudent.readByNumero(new Integer(2005), licenciatura);
      assertNull(aluno);
      _suportePersistente.confirmarTransaccao();
    } catch (ExcepcaoPersistencia ex) {
      fail("testReadBySeccaoAndNome:fail read unexisting aluno");
    }
  }
    

	public void testReadByUsername() {
		IStudent aluno = null;
		// read existing aluno
		try {
			_suportePersistente.iniciarTransaccao();
			aluno = persistentStudent.readByUsername("user");
			_suportePersistente.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex) {
			fail("testReadByUsername:fail read existing aluno");
		}

		assertNotNull("testReadByUsername:read existing aluno",aluno);
		assertEquals(
			"testReadByUsername:read existing aluno",
			aluno.getNumber(),
			alunojaexistente.getNumber());

		// read unexisting aluno
		try {
			_suportePersistente.iniciarTransaccao();
			aluno = persistentStudent.readByUsername("No has this string for a username");
			assertNull(aluno);
			_suportePersistente.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex) {
			fail("testReadByUsername:fail read unexisting aluno");
		}
	}


    /** Test of readByNumeroAndEstado method, of class ServidorPersistente.OJB.StudentOJB. */
   
    public void testReadByNumeroAndEstado() {
    IStudent aluno = null;
    // read existing aluno
    try {
      _suportePersistente.iniciarTransaccao();
      aluno = persistentStudent.readByNumeroAndEstado(new Integer(600),new Integer(567), licenciatura);
      _suportePersistente.confirmarTransaccao();
    } catch (ExcepcaoPersistencia ex) {
      fail("testReadByNumeroAndEstado:fail read existing aluno");
    }
    assertEquals("testReadByNumeroAndEstado:read existing aluno",aluno.getNumber(),alunojaexistente.getNumber());
    assertEquals("testReadByNumeroAndEstado:read existing aluno",aluno.getState(),alunojaexistente.getState());
        
    // read unexisting aluno
    try {
      _suportePersistente.iniciarTransaccao();
      aluno = persistentStudent.readByNumeroAndEstado(new Integer(2005),new Integer(567), licenciatura);
      assertNull(aluno);
      _suportePersistente.confirmarTransaccao();
    } catch (ExcepcaoPersistencia ex) {
      fail("testReadByNumeroAndEstado:fail read unexisting aluno");
    }
  }

    public void testReadByNumeroAndEstadoAndPessoa() {
    IStudent aluno = null;
    // read existing aluno
    try {
      _suportePersistente.iniciarTransaccao();
      aluno = persistentStudent.readByNumeroAndEstadoAndPessoa(new Integer(600),new Integer(567),_pessoa1, licenciatura);
      _suportePersistente.confirmarTransaccao();
    } catch (ExcepcaoPersistencia ex) {
      fail("testReadByNumero:fail read existing aluno");
    }
    assertNotNull("testReadByNumero:read existing aluno", aluno);
    assertEquals("testReadByNumero:read existing aluno",aluno.getNumber(),alunojaexistente.getNumber());
    assertEquals("testReadByNumero:read existing aluno",aluno.getState(),alunojaexistente.getState());
    assertEquals("testReadByNumero:read existing aluno",aluno.getPerson().getNumeroDocumentoIdentificacao(),alunojaexistente.getPerson().getNumeroDocumentoIdentificacao());
        
    // read unexisting aluno
    try {
      _suportePersistente.iniciarTransaccao();
      aluno = persistentStudent.readByNumeroAndEstadoAndPessoa(new Integer(2005),new Integer(567),_pessoa1, licenciatura);
      assertNull(aluno);
      _suportePersistente.confirmarTransaccao();
    } catch (ExcepcaoPersistencia ex) {
      fail("testReadByNumeroAndEstadoAndPessoa:fail read unexisting aluno");
    }
  }

    
    public void readAllAlunos() {
        
        ArrayList alunos = null;
        System.out.println("**** READ_ALL_ALUNOS 1");        
        try {
            _suportePersistente.iniciarTransaccao();
            System.out.println("**** READ_ALL_ALUNOS 2");            
            alunos = persistentStudent.readAllAlunos();
            System.out.println("**** READ_ALL_ALUNOS 3");
            _suportePersistente.confirmarTransaccao();
        } catch(ExcepcaoPersistencia ex) {
        	System.out.println("**** READ_ALL_ALUNOS 4");
            fail("testReadAllAlunos: readAllAlunos");
        }
        System.out.println("**** READ_ALL_ALUNOS 5");
        System.out.println("**** Alunos na BD = " + alunos.size());        
        assertNotNull(alunos);
        assertEquals(alunos.size(), 1);
    }
    
    /** Test of lockWrite method, of class ServidorPersistente.OJB.StudentOJB. */
 
   public void testCreateExistingAluno() {
    IPessoa pessoa3 = new Pessoa("10000679", new TipoDocumentoIdentificacao(TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE), "Lisboa", new Date(), new Date(), "Ricardo Nortadas", new Sexo(Sexo.MASCULINO), new EstadoCivil(EstadoCivil.DIVORCIADO), new Date(), "Pai", "Mae", "Portugues", "Portugal", "Portugal", "Portugal", "Rua", "LindaAVelha", "2780", "Oeiras", "LindaAVelha", "Oeiras", "Lisboa", "214192888", "936344277", "rfan@mega.ist.utl.pt", "URL", "123231232132", "estudante", "rfan", "eu", null, "123123213123123123");
    try{
        _suportePersistente.iniciarTransaccao();
        _pessoaPersistente.escreverPessoa(pessoa3);
        _suportePersistente.confirmarTransaccao();
    }catch (ExcepcaoPersistencia ex) {
      fail("testCreateNonExistingPessoa");
    }
    IStudent aluno = new Student(new Integer(600),new Integer(67),pessoa3, licenciatura);
    try {
      _suportePersistente.iniciarTransaccao();
      persistentStudent.lockWrite(aluno);
      _suportePersistente.confirmarTransaccao();
      fail("testCreateExistingAluno");
    } catch (ExcepcaoPersistencia ex) {
      //all is ok
    }
  }
 
    public void testCreateNonExistingAluno() {      
        IPessoa pessoa3 = new Pessoa("10000677", new TipoDocumentoIdentificacao(TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE), "Lisboa", new Date(), new Date(), "Ricardo Nortadas", new Sexo(Sexo.MASCULINO), new EstadoCivil(EstadoCivil.DIVORCIADO), new Date(), "Pai", "Mae", "Portugues", "Portugal", "Portugal", "Portugal", "Rua", "LindaAVelha", "2780", "Oeiras", "LindaAVelha", "Oeiras", "Lisboa", "214192888", "936344277", "rfan@mega.ist.utl.pt", "URL", "123231232132", "estudante", "rfan", "eu", null, "123123213123123123");
        try{
        _suportePersistente.iniciarTransaccao();
        _pessoaPersistente.escreverPessoa(pessoa3);
        _suportePersistente.confirmarTransaccao();
        }catch (ExcepcaoPersistencia ex) {
            fail("testCreateNonExistingPessoa");
        }
        IStudent aluno = new Student(new Integer(2004),new Integer(345),pessoa3, licenciatura);
        try {
            _suportePersistente.iniciarTransaccao();
            persistentStudent.lockWrite(aluno);
            _suportePersistente.confirmarTransaccao();
          } catch (ExcepcaoPersistencia ex) {
            fail("testCreateNonExistingaluno");
        }
    }
  
    public void testWriteExistingUnchangedObject() {
    // write aluno already mapped into memory
    try {
    	_suportePersistente.iniciarTransaccao();
    	IStudent aluno = persistentStudent.readByNumero(alunojaexistente.getNumber(), licenciatura);
    	_suportePersistente.confirmarTransaccao();

      _suportePersistente.iniciarTransaccao();
      //persistentStudent.lockWrite(aluno2Sitio1Topo1Sub1Sub1);
      persistentStudent.lockWrite(aluno);
      _suportePersistente.confirmarTransaccao();
    } catch (ExcepcaoPersistencia ex) {
      fail("testWriteExistingUnchangedObject");
    }
  }

  // Test of write method, of class ServidorPersistente.OJB.alunoOJB. 
  public void testWriteExistingChangedObject() {

    // write aluno already mapped into memory
    try {
      _suportePersistente.iniciarTransaccao();
      //persistentStudent.lockWrite(aluno2Sitio1Topo1);
      //aluno2Sitio1Topo1.setOrdem(5);
      IStudent aluno = persistentStudent.readByNumero(alunojaexistente.getNumber(), licenciatura);
      //alunojaexistente.setEstado("Prescrito");
      aluno.setState(new Integer(3));
      _suportePersistente.confirmarTransaccao();
      _suportePersistente.iniciarTransaccao();
      //Ialuno aluno = persistentStudent.readBySeccaoAndNome(aluno2Sitio1Topo1.getSeccao(),
        //                                                aluno2Sitio1Topo1.getNome());
      aluno = persistentStudent.readByNumero(alunojaexistente.getNumber(), licenciatura);
      _suportePersistente.confirmarTransaccao();
      if(aluno.getState().compareTo(new Integer (3))!=0)
        {
            fail("testWriteExistingChangedObject");
        }
      //System.out.println(aluno.getEstado());
      //System.out.println(new Integer(3));
      //assertTrue(aluno.getEstado().toString()=="3");
    } catch (ExcepcaoPersistencia ex) {
      fail("testWriteExistingChangedObject");
    }
  }


  // Test of delete method, of class ServidorPersistente.OJB.alunoOJB. 
  public void testDeleteAluno() {
    try {
    	_suportePersistente.iniciarTransaccao();
    	IStudent aluno = persistentStudent.readByNumero(alunoapagado.getNumber(), licenciatura);
    	_suportePersistente.confirmarTransaccao();

      _suportePersistente.iniciarTransaccao();
      //System.out.println(alunoapagado.getNumero());
      persistentStudent.delete(aluno);
      _suportePersistente.confirmarTransaccao();

      _suportePersistente.iniciarTransaccao();
      //IStudent aluno = persistentStudent.readBySeccaoAndNome(alunoapagado.getSeccao(),
      //                                                  alunoapagado.getNome());
      aluno = persistentStudent.readByNumero(alunoapagado.getNumber(), licenciatura);
      _suportePersistente.confirmarTransaccao();

      assertEquals(aluno, null);
    } catch (ExcepcaoPersistencia ex) {
      fail("testDeleteAluno");
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
    
 