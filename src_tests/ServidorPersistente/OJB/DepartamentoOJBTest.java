/*
 * DepartamentoOJBTest.java
 * JUnit based test
 *
 * Created on 26 de Agosto de 2002, 0:49
 */

package ServidorPersistente.OJB;

import java.util.ArrayList;

import junit.framework.Test;
import junit.framework.TestSuite;
import Dominio.Department;
import Dominio.IDepartment;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentDepartment;
import ServidorPersistente.exceptions.ExistingPersistentException;

public class DepartamentoOJBTest extends TestCaseOJB {
	SuportePersistenteOJB persistentSupport = null; 
	IPersistentDepartment persistentDepartment = null;
    public DepartamentoOJBTest(java.lang.String testName) {
        super(testName);
    }
    
    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite(DepartamentoOJBTest.class);
        
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
		persistentDepartment = persistentSupport.getIDepartamentoPersistente();
    }
    
    protected void tearDown() {
        super.tearDown();
    }
    
// -------------------------------------------------------------------------------------------------------------------------------------------
    public void testEscreverDepartamento() {
        
        // Tentativa de escrita na BD de um objecto existente.
        IDepartment dep = new Department("dep1", "d1");
        
        try {
            persistentSupport.iniciarTransaccao();
            persistentDepartment.escreverDepartamento(dep);
            persistentSupport.confirmarTransaccao();
            fail("testEscreverDepartamento: confirmarTransaccao_1");
        } catch(ExistingPersistentException ex) {
			// All is OK
		} catch(ExcepcaoPersistencia ex2) {
			fail("testEscreverDepartamento: unexpected exception");
		}        
        // Tentativa de escrita na BD de um objecto inexistente.
        dep = new Department("dep2", "d2");
        
        try {
            persistentSupport.iniciarTransaccao();
            persistentDepartment.escreverDepartamento(dep);
            persistentSupport.confirmarTransaccao();
            assertTrue("testEscreverDepartamento: Objecto inexistente", true);
        } catch(ExcepcaoPersistencia ex2) {
            fail("testEscreverDepartamento: confirmarTransaccao_2");
        }
        
        // Agora faz-se uma tentativa de ler a dep que se acabou de escrever.
        IDepartment dep2 = null;

        try {
            persistentSupport.iniciarTransaccao();
            dep2 = persistentDepartment.lerDepartamentoPorNome(dep.getName());
            persistentSupport.confirmarTransaccao();
        } catch(ExcepcaoPersistencia ex) {
            fail("testEscreverDepartamento: confirmarTransaccao_3");
        }
        
        assertNotNull(dep2);
        assertEquals(dep2.getName(), dep.getName());
        assertEquals(dep2.getCode(), dep.getCode());
        
        
        // Agora vai ler por Sigla
        dep2 = null;
        try {
            persistentSupport.iniciarTransaccao();
            dep2 = persistentDepartment.lerDepartamentoPorSigla(dep.getCode());
            persistentSupport.confirmarTransaccao();
            
        } catch(ExcepcaoPersistencia ex) {
            fail("testEscreverDepartamento: lerDepartamentoPorSigla");
        }
        
        assertNotNull(dep2);
        assertEquals(dep2.getName(), dep.getName());
        assertEquals(dep2.getCode(), dep.getCode());
        
    }
// -------------------------------------------------------------------------------------------------------------------------------------------
    public void testApagarTodosOsDepartamentos() {
        
        try {
            persistentSupport.iniciarTransaccao();
            persistentDepartment.apagarTodosOsDepartamentos();
            persistentSupport.confirmarTransaccao();
            assertTrue("testApagarTodosOsDepartamentos: Departamentos apagados", true);
        } catch(ExcepcaoPersistencia ex2) {
            fail("testApagarTodosOsDepartamentos: confirmarTransaccao_1");
        }
        
        // Agora faz-se uma tentativa de ler a dep que se acabou de escrever.
        ArrayList result = null;

        try {
            persistentSupport.iniciarTransaccao();
            result = persistentDepartment.lerTodosOsDepartamentos();
            persistentSupport.confirmarTransaccao();
        } catch(ExcepcaoPersistencia ex) {
            fail("testApagarTodosOsDepartamentos: confirmarTransaccao_2");
        }
        // Porque ao tentar ler uma tabela vazia ele nao se queixa logo e'
        // devolvido um ArrayList vazio.
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
// -------------------------------------------------------------------------------------------------------------------------------------------
    public void testLerDepartamento() {
        
        // Tentativa de leitura da BD de um objecto existente.
        IDepartment dep = null;

        try {
            persistentSupport.iniciarTransaccao();
            dep = persistentDepartment.lerDepartamentoPorNome("dep1");
            persistentSupport.confirmarTransaccao();
        } catch(ExcepcaoPersistencia ex2) {
            fail("testLerDepartamento: confirmarTransaccao_1");
        }
        assertNotNull(dep);
        assertTrue(dep.getName().equals("dep1"));
        assertTrue(dep.getCode().equals("d1"));
        assertNotNull(dep.getDisciplinasAssociadas());
        assertTrue(dep.getDisciplinasAssociadas().size() == 2);
        
        // Tentativa de leitura da BD de um objecto inexistente.
        dep = null;
        try {
            persistentSupport.iniciarTransaccao();
            dep = persistentDepartment.lerDepartamentoPorNome("dep3");
            persistentSupport.confirmarTransaccao();
        } catch(ExcepcaoPersistencia ex2) {
            fail("testLerDepartamento: confirmarTransaccao_2");
        }
        assertNull(dep);

        try {
        	persistentSupport.iniciarTransaccao();
            dep = persistentDepartment.lerDepartamentoPorSigla("d3");
            persistentSupport.confirmarTransaccao();
        } catch(ExcepcaoPersistencia ex) {
            fail("testLerDepartamento: lerDepartamentoPorSigla");
        }
        
        assertNull(dep);

    }
// -------------------------------------------------------------------------------------------------------------------------------------------
    public void testApagarDepartamento() {

        // Tentativa de apagar da BD um objecto existente.
        try {
            persistentSupport.iniciarTransaccao();
        } catch(ExcepcaoPersistencia ex) {
            fail("testApagarDepartamento: iniciarTransaccao_1");
        }
 
        try {
            persistentDepartment.apagarDepartamentoPorNome("dep1");
        } catch(ExcepcaoPersistencia ex1) {
            fail("testApagarDepartamento: apagarDepartamentoPorNome_1");
        }
        try {
            persistentSupport.confirmarTransaccao();
        } catch(ExcepcaoPersistencia ex3) {
            try {
                persistentSupport.cancelarTransaccao();
            } catch(ExcepcaoPersistencia ex4) {
                fail("testApagarDepartamento: cancelarTransaccao_1");
            }
            fail("testApagarDepartamento: confirmarTransaccao_1");
        }
        
        IDepartment dep = null;
        try {
            persistentSupport.iniciarTransaccao();
            dep = persistentDepartment.lerDepartamentoPorNome("dep1");
            persistentSupport.confirmarTransaccao();
        } catch(ExcepcaoPersistencia ex) {
            try {
                persistentSupport.cancelarTransaccao();
            } catch(ExcepcaoPersistencia ex4) {
                fail("testApagarDepartamento: cancelarTransaccao para lerDepartamentoPorNomeESigla");
            }
            fail("testApagarDepartamento: lerDepartamentoPorNomeESigla");
        }
        assertNull(dep);

        // Tentativa de apagar da BD um objecto inexistente.
        try {
            persistentSupport.iniciarTransaccao();
        } catch(ExcepcaoPersistencia ex) {
            fail("testApagarDepartamento: iniciarTransaccao_2");
        }
 
        try {
            persistentDepartment.apagarDepartamento(new Department());
        } catch(ExcepcaoPersistencia ex1) {
            fail("testApagarDepartamento: apagarDepartamento_2");
        }
        try {
            persistentSupport.confirmarTransaccao();
            assertTrue("testApagarDepartamento: Department apagado", true);
        } catch(ExcepcaoPersistencia ex2) {
            try {
                persistentSupport.cancelarTransaccao();
            } catch(ExcepcaoPersistencia ex3) {
                fail("testApagarDepartamento: cancelarTransaccao_2");
            }
            fail("testApagarDepartamento: confirmarTransaccao_2");
        }
    }
// -------------------------------------------------------------------------------------------------------------------------------------------
    public void testLerTodosOsDepartamentos() {
        try {
            persistentSupport.iniciarTransaccao();
        } catch(ExcepcaoPersistencia ex) {
            fail("testLerTodasDepartamentos: iniciarTransaccao_1");
        }
 
        ArrayList listap = null;
        try {
            listap = persistentDepartment.lerTodosOsDepartamentos();
        } catch(ExcepcaoPersistencia ex) {
            fail("testLerTodosOsDepartamentos: lerTodosOsDepartamentos");
        }
 
        try {
            persistentSupport.confirmarTransaccao();
        } catch(ExcepcaoPersistencia ex2) {
            try {
                persistentSupport.cancelarTransaccao();
            } catch(ExcepcaoPersistencia ex3) {
                fail("testLerTodosOsDepartamentos: cancelarTransaccao_1");
            }
            fail("testLerTodosOsDepartamentos: confirmarTransaccao_1");
        }
        assertNotNull(listap);
        assertEquals(listap.size(), 2);
    }
}