/*
 * PessoaOJBTest.java
 * JUnit based test
 */

package ServidorPersistente.OJB;

import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.ojb.odmg.OJB;
import org.odmg.Database;
import org.odmg.Implementation;
import org.odmg.ODMGException;
import org.odmg.OQLQuery;
import org.odmg.QueryException;

import Dominio.IPessoa;
import Dominio.Pessoa;
import ServidorAplicacao.security.PasswordEncryptor;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPessoaPersistente;
import ServidorPersistente.exceptions.ExistingPersistentException;
import Util.TipoDocumentoIdentificacao;


/**
 *
 * @author jorge
 */
public class PessoaOJBTest extends TestCaseOJB {
	
  SuportePersistenteOJB persistentSupport = null;
  IPessoaPersistente persistentPerson = null;
   
	
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
	try {
		persistentSupport = SuportePersistenteOJB.getInstance();
	} catch (ExcepcaoPersistencia e) {
		e.printStackTrace();
		fail("Error");
	}
	persistentPerson = persistentSupport.getIPessoaPersistente();
  }
    
  protected void tearDown() {
    super.tearDown();
  }
    
  /** Test of readBySeccaoAndNome method, of class ServidorPersistente.OJB.ItemOJB. */
  public void testReadByUtilizador() {
    IPessoa pessoa = null;
    // read existing Pessoa
    try {
      persistentSupport.iniciarTransaccao();
      //pessoa = persistentPerson.readByUtilizador("jorge");
      pessoa = persistentPerson.lerPessoaPorUsername("jorge");
      persistentSupport.confirmarTransaccao();
    } catch (ExcepcaoPersistencia ex) {
      fail("testReadByUtilizador:fail read existing pessoa");
    }

	assertNotNull(pessoa);
	assertTrue(pessoa.getUsername().equals("jorge"));
        
    // read unexisting Pessoa
    try {
      persistentSupport.iniciarTransaccao();
      //pessoa = persistentPerson.readByUtilizador("ars");
      pessoa = persistentPerson.lerPessoaPorUsername("ars");
      assertNull(pessoa);
      persistentSupport.confirmarTransaccao();
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
    pessoa.setPassword(PasswordEncryptor.encryptPassword("pass"));
    pessoa.setPrivilegios(null);
    try {
      persistentSupport.iniciarTransaccao();
      //persistentPerson.lockWrite(pessoa);
      persistentPerson.escreverPessoa(pessoa);
      persistentSupport.confirmarTransaccao();
      fail("testCreateExistingPessoa: expected an exception");
    } catch (ExistingPersistentException ex) {
	    assertNotNull("Write Existing", ex);
	} catch (ExcepcaoPersistencia ex) {
		fail("Write Existing: unexpected exception");
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
    pessoa.setPassword(PasswordEncryptor.encryptPassword("pass2"));
    pessoa.setPrivilegios(null);
    try {
      persistentSupport.iniciarTransaccao();
      //persistentPerson.lockWrite(pessoa);
      persistentPerson.escreverPessoa(pessoa);
      persistentSupport.confirmarTransaccao();
    } catch (ExcepcaoPersistencia ex) {
      fail("testCreateNonExistingPessoa"); 
    }
  }

  /** Test of write method, of class ServidorPersistente.OJB.PessoaOJB. */
  public void testWriteExistingUnchangedObject() {
    // write pessoa already mapped into memory
    IPessoa pessoa = null;
    try {
      persistentSupport.iniciarTransaccao();
	  pessoa = persistentPerson.lerPessoaPorUsername("jorge");
      persistentPerson.escreverPessoa(pessoa);
      persistentSupport.confirmarTransaccao();
    } catch (ExcepcaoPersistencia ex) {
      fail("testWriteExistingUnchangedObject");
    }
  }

  /** Test of write method, of class ServidorPersistente.OJB.PessoaOJB. */
  public void testWriteExistingChangedObject() {
    // write pessoa already mapped into memory
    try {
      persistentSupport.iniciarTransaccao();
      //persistentPerson.lockWrite(_pessoa1);
      IPessoa pessoa = persistentPerson.lerPessoaPorUsername("jorge");
      pessoa.setPassword(PasswordEncryptor.encryptPassword("xxxxxxxx"));
      persistentPerson.escreverPessoa(pessoa);
      persistentSupport.confirmarTransaccao();

	  pessoa = null;

      persistentSupport.iniciarTransaccao();
      //IPessoa pessoa = persistentPerson.readByUtilizador(_pessoa1.getUtilizador());
      pessoa = persistentPerson.lerPessoaPorUsername("jorge");
      persistentSupport.confirmarTransaccao();
      assertTrue(PasswordEncryptor.areEquals(pessoa.getPassword(),"xxxxxxxx"));
    } catch (ExcepcaoPersistencia ex) {
      fail("testWriteExistingChangedObject");
    }
  }

  /** Test of delete method, of class ServidorPersistente.OJB.PessoaOJB. */
  public void testDeletePessoa() {
    try {
      persistentSupport.iniciarTransaccao();
      IPessoa pessoa = persistentPerson.lerPessoaPorUsername("jorge");
	  assertNotNull(pessoa);
      persistentPerson.apagarPessoa(pessoa);
      persistentSupport.confirmarTransaccao();

      persistentSupport.iniciarTransaccao();
      pessoa = persistentPerson.lerPessoaPorUsername("jorge");
      persistentSupport.confirmarTransaccao();

      assertEquals(pessoa, null);
    } catch (ExcepcaoPersistencia ex) {
      fail("testDeletePessoa");
    }
  }

  /** Test of deleteAll method, of class ServidorPersistente.OJB.PessoaOJB. */
  public void testDeleteAll() {
    try {
      persistentSupport.iniciarTransaccao();
      //persistentPerson.deleteAll();
      persistentPerson.apagarTodasAsPessoas();
      persistentSupport.confirmarTransaccao();

      persistentSupport.iniciarTransaccao();
      List result = null;
      try {
        Implementation odmg = OJB.getInstance();

		///////////////////////////////////////////////////////////////////
		// Added Code due to Upgrade from OJB 0.9.5 to OJB rc1
		///////////////////////////////////////////////////////////////////
		Database db = odmg.newDatabase();

		try {
			db.open("OJB/repository.xml", Database.OPEN_READ_WRITE);
		} catch (ODMGException e) {
			e.printStackTrace();
		}
		///////////////////////////////////////////////////////////////////
		// End of Added Code
		///////////////////////////////////////////////////////////////////

        OQLQuery query = odmg.newOQLQuery();;
        String oqlQuery = "select pessoa from " + Pessoa.class.getName();
        query.create(oqlQuery);
        result = (List) query.execute();
      } catch (QueryException ex) {
        throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
      }
      persistentSupport.confirmarTransaccao();
      assertNotNull(result);
      assertTrue(result.isEmpty());
    } catch (ExcepcaoPersistencia ex) {
      fail("testDeleteAll");
    }
  }
}
