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

import Dominio.Departamento;
import Dominio.IDepartamento;
import ServidorPersistente.ExcepcaoPersistencia;

public class DepartamentoOJBTest extends TestCaseOJB {
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
    }
    
    protected void tearDown() {
        super.tearDown();
    }
    
// -------------------------------------------------------------------------------------------------------------------------------------------
    public void testEscreverDepartamento() {
        
        // Tentativa de escrita na BD de um objecto existente.
        IDepartamento dep = new Departamento("dep1", "d1");
        
        try {
            _suportePersistente.iniciarTransaccao();
            departamentoPersistente.escreverDepartamento(dep);
            _suportePersistente.confirmarTransaccao();
            fail("testEscreverDepartamento: confirmarTransaccao_1");
        } catch(ExcepcaoPersistencia ex2) {
			// All is OK
        }
        
        // Tentativa de escrita na BD de um objecto inexistente.
        dep = new Departamento("dep2", "d2");
        
        try {
            _suportePersistente.iniciarTransaccao();
            departamentoPersistente.escreverDepartamento(dep);
            _suportePersistente.confirmarTransaccao();
            assertTrue("testEscreverDepartamento: Objecto inexistente", true);
        } catch(ExcepcaoPersistencia ex2) {
            fail("testEscreverDepartamento: confirmarTransaccao_2");
        }
        
        // Agora faz-se uma tentativa de ler a dep que se acabou de escrever.
        IDepartamento dep2 = null;

        try {
            _suportePersistente.iniciarTransaccao();
            dep2 = departamentoPersistente.lerDepartamentoPorNome(dep.getNome());
            _suportePersistente.confirmarTransaccao();
        } catch(ExcepcaoPersistencia ex) {
            fail("testEscreverDepartamento: confirmarTransaccao_3");
        }
        
        assertNotNull(dep2);
        assertEquals(dep2.getNome(), dep.getNome());
        assertEquals(dep2.getSigla(), dep.getSigla());
        
        
        // Agora vai ler por Sigla
        dep2 = null;
        try {
            _suportePersistente.iniciarTransaccao();
            dep2 = departamentoPersistente.lerDepartamentoPorSigla(dep.getSigla());
            _suportePersistente.confirmarTransaccao();
            
        } catch(ExcepcaoPersistencia ex) {
            fail("testEscreverDepartamento: lerDepartamentoPorSigla");
        }
        
        assertNotNull(dep2);
        assertEquals(dep2.getNome(), dep.getNome());
        assertEquals(dep2.getSigla(), dep.getSigla());
        
    }
// -------------------------------------------------------------------------------------------------------------------------------------------
    public void testApagarTodosOsDepartamentos() {
        
        try {
            _suportePersistente.iniciarTransaccao();
            departamentoPersistente.apagarTodosOsDepartamentos();
            _suportePersistente.confirmarTransaccao();
            assertTrue("testApagarTodosOsDepartamentos: Departamentos apagados", true);
        } catch(ExcepcaoPersistencia ex2) {
            fail("testApagarTodosOsDepartamentos: confirmarTransaccao_1");
        }
        
        // Agora faz-se uma tentativa de ler a dep que se acabou de escrever.
        ArrayList result = null;

        try {
            _suportePersistente.iniciarTransaccao();
            result = departamentoPersistente.lerTodosOsDepartamentos();
            _suportePersistente.confirmarTransaccao();
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
        IDepartamento dep = null;

        try {
            _suportePersistente.iniciarTransaccao();
            dep = departamentoPersistente.lerDepartamentoPorNome("dep1");
            _suportePersistente.confirmarTransaccao();
        } catch(ExcepcaoPersistencia ex2) {
            fail("testLerDepartamento: confirmarTransaccao_1");
        }
        assertNotNull(dep);
        assertTrue(dep.getNome().equals("dep1"));
        assertTrue(dep.getSigla().equals("d1"));
        assertNotNull(dep.getDisciplinasAssociadas());
        assertTrue(dep.getDisciplinasAssociadas().size() == 2);
        
        // Tentativa de leitura da BD de um objecto inexistente.
        dep = null;
        try {
            _suportePersistente.iniciarTransaccao();
            dep = departamentoPersistente.lerDepartamentoPorNome("dep3");
            _suportePersistente.confirmarTransaccao();
        } catch(ExcepcaoPersistencia ex2) {
            fail("testLerDepartamento: confirmarTransaccao_2");
        }
        assertNull(dep);

        try {
        	_suportePersistente.iniciarTransaccao();
            dep = departamentoPersistente.lerDepartamentoPorSigla("d3");
            _suportePersistente.confirmarTransaccao();
        } catch(ExcepcaoPersistencia ex) {
            fail("testLerDepartamento: lerDepartamentoPorSigla");
        }
        
        assertNull(dep);

    }
// -------------------------------------------------------------------------------------------------------------------------------------------
    public void testApagarDepartamento() {

        // Tentativa de apagar da BD um objecto existente.
        try {
            _suportePersistente.iniciarTransaccao();
        } catch(ExcepcaoPersistencia ex) {
            fail("testApagarDepartamento: iniciarTransaccao_1");
        }
 
        try {
            departamentoPersistente.apagarDepartamentoPorNome("dep1");
        } catch(ExcepcaoPersistencia ex1) {
            fail("testApagarDepartamento: apagarDepartamentoPorNome_1");
        }
        try {
            _suportePersistente.confirmarTransaccao();
        } catch(ExcepcaoPersistencia ex3) {
            try {
                _suportePersistente.cancelarTransaccao();
            } catch(ExcepcaoPersistencia ex4) {
                fail("testApagarDepartamento: cancelarTransaccao_1");
            }
            fail("testApagarDepartamento: confirmarTransaccao_1");
        }
        
        IDepartamento dep = null;
        try {
            _suportePersistente.iniciarTransaccao();
            dep = departamentoPersistente.lerDepartamentoPorNome("dep1");
            _suportePersistente.confirmarTransaccao();
        } catch(ExcepcaoPersistencia ex) {
            try {
                _suportePersistente.cancelarTransaccao();
            } catch(ExcepcaoPersistencia ex4) {
                fail("testApagarDepartamento: cancelarTransaccao para lerDepartamentoPorNomeESigla");
            }
            fail("testApagarDepartamento: lerDepartamentoPorNomeESigla");
        }
        assertNull(dep);

        // Tentativa de apagar da BD um objecto inexistente.
        try {
            _suportePersistente.iniciarTransaccao();
        } catch(ExcepcaoPersistencia ex) {
            fail("testApagarDepartamento: iniciarTransaccao_2");
        }
 
        try {
            departamentoPersistente.apagarDepartamento(new Departamento());
        } catch(ExcepcaoPersistencia ex1) {
            fail("testApagarDepartamento: apagarDepartamento_2");
        }
        try {
            _suportePersistente.confirmarTransaccao();
            assertTrue("testApagarDepartamento: Departamento apagado", true);
        } catch(ExcepcaoPersistencia ex2) {
            try {
                _suportePersistente.cancelarTransaccao();
            } catch(ExcepcaoPersistencia ex3) {
                fail("testApagarDepartamento: cancelarTransaccao_2");
            }
            fail("testApagarDepartamento: confirmarTransaccao_2");
        }
    }
// -------------------------------------------------------------------------------------------------------------------------------------------
    public void testLerTodosOsDepartamentos() {
        try {
            _suportePersistente.iniciarTransaccao();
        } catch(ExcepcaoPersistencia ex) {
            fail("testLerTodasDepartamentos: iniciarTransaccao_1");
        }
 
        ArrayList listap = null;
        try {
            listap = departamentoPersistente.lerTodosOsDepartamentos();
        } catch(ExcepcaoPersistencia ex) {
            fail("testLerTodosOsDepartamentos: lerTodosOsDepartamentos");
        }
 
        try {
            _suportePersistente.confirmarTransaccao();
        } catch(ExcepcaoPersistencia ex2) {
            try {
                _suportePersistente.cancelarTransaccao();
            } catch(ExcepcaoPersistencia ex3) {
                fail("testLerTodosOsDepartamentos: cancelarTransaccao_1");
            }
            fail("testLerTodosOsDepartamentos: confirmarTransaccao_1");
        }
        assertNotNull(listap);
        assertEquals(listap.size(), 2);
    }
}