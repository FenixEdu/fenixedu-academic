/*
 * PessoaOJBTest.java
 * JUnit based test
 */

package ServidorPersistente.OJB;

import java.util.List;


import junit.framework.*;
import java.util.List;
import org.apache.ojb.odmg.OJB;
import org.odmg.QueryException;
import org.odmg.OQLQuery;
import org.odmg.Implementation;
import ServidorPersistente.*;
import Util.TipoDocumentoIdentificacao;
import Dominio.*;


/**
 *
 * @author jorge
 */
public class PessoaOJBTest extends TestCaseOJB {
  public PessoaOJBTest(java.lang.String testName) {
    super(testName);
  }
    
  public static void main(java.lang.String[] args) {
    junit.textui.TestRunner.run(suite());
  }
    
  public static Test suite() {
    TestSuite suite = new TestSuite(PessoaOJBTest.class);
        
    return suite;
  }
    
  protected void setUp() {
    super.setUp();
  }
    
  protected void tearDown() {
    super.tearDown();
  }
    
  /** Test of readBySeccaoAndNome method, of class ServidorPersistente.OJB.ItemOJB. */
  public void testReadByUtilizador() {
    IPessoa pessoa = null;
    // read existing Pessoa
    try {
      _suportePersistente.iniciarTransaccao();
      //pessoa = _pessoaPersistente.readByUtilizador("jorge");
      pessoa = _pessoaPersistente.lerPessoaPorUsername("jorge");
      _suportePersistente.confirmarTransaccao();
    } catch (ExcepcaoPersistencia ex) {
      fail("testReadByUtilizador:fail read existing pessoa");
    }

	assertNotNull(pessoa);
	assertTrue(pessoa.getUsername().equals("jorge"));
        
    // read unexisting Pessoa
    try {
      _suportePersistente.iniciarTransaccao();
      //pessoa = _pessoaPersistente.readByUtilizador("ars");
      pessoa = _pessoaPersistente.lerPessoaPorUsername("ars");
      assertNull(pessoa);
      _suportePersistente.confirmarTransaccao();
    } catch (ExcepcaoPersistencia ex) {
      fail("testReadByUtilizador:fail read unexisting pessoa");
    }
  }

  // write new existing Pessoa
  public void testCreateExistingItem() {
    //IPessoa pessoa = new Pessoa("jorge","xxxxxxxx", null);
    IPessoa pessoa = new Pessoa();
    pessoa.setNumeroDocumentoIdentificacao("0123456789");
    pessoa.setCodigoFiscal("9876543210");
    pessoa.setTipoDocumentoIdentificacao(new TipoDocumentoIdentificacao(
    	         TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE));
    pessoa.setUsername("jorge");
    pessoa.setPassword("pass");
    pessoa.setPrivilegios(null);
    try {
      _suportePersistente.iniciarTransaccao();
      //_pessoaPersistente.lockWrite(pessoa);
      _pessoaPersistente.escreverPessoa(pessoa);
      _suportePersistente.confirmarTransaccao();
      fail("testCreateExistingPessoa");
    } catch (ExcepcaoPersistencia ex) {
      //all is ok
    }
  }

  // write new non-existing item
  public void testCreateNonExistingPessoa() {
    //IPessoa pessoa = new Pessoa("ars","xxxxxxxx", null);
    IPessoa pessoa = new Pessoa();
    pessoa.setNumeroDocumentoIdentificacao("9786541230");
    pessoa.setCodigoFiscal("0312645978");
    pessoa.setTipoDocumentoIdentificacao(new TipoDocumentoIdentificacao(
    			 TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE));
    pessoa.setUsername("ars");
    pessoa.setPassword("pass2");
    pessoa.setPrivilegios(null);
    try {
      _suportePersistente.iniciarTransaccao();
      //_pessoaPersistente.lockWrite(pessoa);
      _pessoaPersistente.escreverPessoa(pessoa);
      _suportePersistente.confirmarTransaccao();
    } catch (ExcepcaoPersistencia ex) {
      fail("testCreateNonExistingPessoa"); 
    }
  }

  /** Test of write method, of class ServidorPersistente.OJB.PessoaOJB. */
  public void testWriteExistingUnchangedObject() {
    // write pessoa already mapped into memory
    IPessoa pessoa = null;
    try {
      _suportePersistente.iniciarTransaccao();
	  pessoa = _pessoaPersistente.lerPessoaPorUsername("jorge");
      _pessoaPersistente.escreverPessoa(pessoa);
      _suportePersistente.confirmarTransaccao();
    } catch (ExcepcaoPersistencia ex) {
      fail("testWriteExistingUnchangedObject");
    }
  }

  /** Test of write method, of class ServidorPersistente.OJB.PessoaOJB. */
  public void testWriteExistingChangedObject() {
    // write pessoa already mapped into memory
    try {
      _suportePersistente.iniciarTransaccao();
      //_pessoaPersistente.lockWrite(_pessoa1);
      IPessoa pessoa = _pessoaPersistente.lerPessoaPorUsername("jorge");
      pessoa.setPassword("xxxxxxxx");
      _pessoaPersistente.escreverPessoa(pessoa);
      _suportePersistente.confirmarTransaccao();

	  pessoa = null;

      _suportePersistente.iniciarTransaccao();
      //IPessoa pessoa = _pessoaPersistente.readByUtilizador(_pessoa1.getUtilizador());
      pessoa = _pessoaPersistente.lerPessoaPorUsername("jorge");
      _suportePersistente.confirmarTransaccao();
      assertTrue(pessoa.getPassword().equals("xxxxxxxx"));
    } catch (ExcepcaoPersistencia ex) {
      fail("testWriteExistingChangedObject");
    }
  }

  /** Test of delete method, of class ServidorPersistente.OJB.PessoaOJB. */
  public void testDeletePessoa() {
    try {
      _suportePersistente.iniciarTransaccao();
      IPessoa pessoa = _pessoaPersistente.lerPessoaPorUsername("jorge");
	  assertNotNull(pessoa);
      _pessoaPersistente.apagarPessoa(pessoa);
      _suportePersistente.confirmarTransaccao();

      _suportePersistente.iniciarTransaccao();
      pessoa = _pessoaPersistente.lerPessoaPorUsername("jorge");
      _suportePersistente.confirmarTransaccao();

      assertEquals(pessoa, null);
    } catch (ExcepcaoPersistencia ex) {
      fail("testDeletePessoa");
    }
  }

  /** Test of deleteAll method, of class ServidorPersistente.OJB.PessoaOJB. */
  public void testDeleteAll() {
    try {
      _suportePersistente.iniciarTransaccao();
      //_pessoaPersistente.deleteAll();
      _pessoaPersistente.apagarTodasAsPessoas();
      _suportePersistente.confirmarTransaccao();

      _suportePersistente.iniciarTransaccao();
      List result = null;
      try {
        Implementation odmg = OJB.getInstance();
        OQLQuery query = odmg.newOQLQuery();;
        String oqlQuery = "select pessoa from " + Pessoa.class.getName();
        query.create(oqlQuery);
        result = (List) query.execute();
      } catch (QueryException ex) {
        throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
      }
      _suportePersistente.confirmarTransaccao();
      assertNotNull(result);
      assertTrue(result.isEmpty());
    } catch (ExcepcaoPersistencia ex) {
      fail("testDeleteAll");
    }
  }
}
