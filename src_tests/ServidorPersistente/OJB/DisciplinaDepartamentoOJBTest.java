/*
 * DisciplinaDepartamentoOJBTest.java
 * JUnit based test
 *
 * Created on 26 de Agosto de 2002, 0:49
 */

package ServidorPersistente.OJB;

import java.util.ArrayList;

import junit.framework.Test;
import junit.framework.TestSuite;

import Dominio.DisciplinaDepartamento;
import Dominio.IDepartamento;
import Dominio.IDisciplinaDepartamento;
import ServidorPersistente.ExcepcaoPersistencia;

public class DisciplinaDepartamentoOJBTest extends TestCaseOJB {
    public DisciplinaDepartamentoOJBTest(java.lang.String testName) {
        super(testName);
    }
    
    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite(DisciplinaDepartamentoOJBTest.class);
        
        return suite;
    }
    
    protected void setUp() {
        super.setUp();
    }
    
    protected void tearDown() {
        super.tearDown();
    }
    
// -------------------------------------------------------------------------------------------------------------------------------------------
    public void testEscreverDisciplina() {
        
        IDepartamento depTemp = null;
        try {
        	_suportePersistente.iniciarTransaccao();
        	depTemp = departamentoPersistente.lerDepartamentoPorSigla("d1");
        	_suportePersistente.iniciarTransaccao();
        } catch(ExcepcaoPersistencia ex) {
            fail("testEscreverDisciplina: lerDepartamento");
        }
        
        assertNotNull(depTemp);
        
        // Tentativa de escrita na BD de um objecto existente.
        IDisciplinaDepartamento disciplina = new DisciplinaDepartamento("Engenharia da Programacao", "ep", depTemp);
        
        try {
            _suportePersistente.iniciarTransaccao();
        } catch(ExcepcaoPersistencia ex) {
            fail("testEscreverDisciplina: iniciarTransaccao_1");
        }
        
        // A escrita na base de dados e' efectuada apenas no 'confirmarTransaccao'
        // por isso o metodo 'escreverDisciplina' nao e' suposto dar erro.
        // Assim o verdadeiro teste a' escrita e' controlado na chamada ao metodo
        // 'confirmarTransaccao'.
        try {
            disciplinaDepartamentoPersistente.escreverDisciplinaDepartamento(disciplina);
        } catch(ExcepcaoPersistencia ex1) {
            fail("testEscreverDisciplina: escreverDisciplina_1");
        }
        try {
            _suportePersistente.confirmarTransaccao();
            // Neste teste especifico o 'confirmarTransaccao' tem que dar errado
            // porque estamos a tentar escrever na BD um objecto que ja' existe.
            // Por isso, se passar o 'confirmarTransaccao' sem problemas, algo de
            // muito errado aconteceu.
            try {
                _suportePersistente.cancelarTransaccao();
            } catch(ExcepcaoPersistencia ex3) {
                fail("testEscreverDisciplina: cancelarTransaccao_1");
            }
            fail("testEscreverDisciplina: confirmarTransaccao_1");
        } catch(ExcepcaoPersistencia ex2) {
            assertTrue("testEscreverDisciplina: Objecto existente", true);
        }
        
        // Tentativa de escrita na BD de um objecto inexistente.
        disciplina = new DisciplinaDepartamento("Prog por Obj", "PO", depTemp);
        
        try {
            _suportePersistente.iniciarTransaccao();
        } catch(ExcepcaoPersistencia ex) {
            fail("testEscreverDisciplina: iniciarTransaccao_2");
        }
        
        try {
            disciplinaDepartamentoPersistente.escreverDisciplinaDepartamento(disciplina);
        } catch(ExcepcaoPersistencia ex1) {
            fail("testEscreverDisciplina: escreverDisciplina_2");
        }
        try {
            _suportePersistente.confirmarTransaccao();
            assertTrue("testEscreverDisciplina: Objecto inexistente", true);
        } catch(ExcepcaoPersistencia ex2) {
            try {
                _suportePersistente.cancelarTransaccao();
            } catch(ExcepcaoPersistencia ex3) {
                fail("testEscreverDisciplina: cancelarTransaccao_2");
            }
            fail("testEscreverDisciplina: confirmarTransaccao_2");
        }
        
        // Agora faz-se uma tentativa de ler a disciplina que se acabou de escrever.
        try {
            _suportePersistente.iniciarTransaccao();
        } catch(ExcepcaoPersistencia ex) {
            fail("testEscreverDisciplina: iniciarTransaccao_3");
        }
        
        IDisciplinaDepartamento disciplinaDepartamento2 = null;
        try {
            disciplinaDepartamento2 = disciplinaDepartamentoPersistente.lerDisciplinaDepartamentoPorNomeESigla(disciplina.getNome(), disciplina.getSigla());
        } catch(ExcepcaoPersistencia ex) {
            fail("testEscreverDisciplina: lerDisciplinaPorNomeESigla");
        }
        
        try {
            _suportePersistente.confirmarTransaccao();
        } catch(ExcepcaoPersistencia ex) {
            try {
                _suportePersistente.cancelarTransaccao();
            } catch(ExcepcaoPersistencia ex3) {
                fail("testEscreverDisciplina: cancelarTransaccao_3");
            }
            fail("testEscreverDisciplina: confirmarTransaccao_3");
        }
        
        assertNotNull(disciplinaDepartamento2);
        assertEquals(disciplinaDepartamento2.getNome(), disciplina.getNome());
        assertEquals(disciplinaDepartamento2.getSigla(), disciplina.getSigla());
        assertNotNull(disciplinaDepartamento2.getDepartamento());
        assertTrue(disciplinaDepartamento2.getDepartamento().getNome().equals("dep1"));
        assertTrue(disciplinaDepartamento2.getDepartamento().getSigla().equals("d1"));
        
    }
// -------------------------------------------------------------------------------------------------------------------------------------------
    public void testApagarTodasAsDisciplinas() {
        
        try {
            _suportePersistente.iniciarTransaccao();
        } catch(ExcepcaoPersistencia ex) {
            fail("testApagarTodasAsDisciplinas: iniciarTransaccao_1");
        }
        
        try {
            disciplinaDepartamentoPersistente.apagarTodasAsDisciplinasDepartamento();
        } catch(ExcepcaoPersistencia ex1) {
            fail("testApagarTodasAsDisciplinas: apagarTodasAsDisciplinas");
        }
        try {
            _suportePersistente.confirmarTransaccao();
            assertTrue("testApagarTodasAsDisciplinas: Disciplinas apagadas", true);
        } catch(ExcepcaoPersistencia ex2) {
            try {
                _suportePersistente.cancelarTransaccao();
            } catch(ExcepcaoPersistencia ex3) {
                fail("testApagarTodasAsDisciplinas: cancelarTransaccao_1");
            }
            fail("testApagarTodasAsDisciplinas: confirmarTransaccao_1");
        }
        
        // Agora faz-se uma tentativa de ler a disciplina que se acabou de escrever.
        try {
            _suportePersistente.iniciarTransaccao();
        } catch(ExcepcaoPersistencia ex) {
            fail("testApagarTodasAsDisciplinas: iniciarTransaccao_2");
        }
        
        ArrayList result = null;
        try {
            result = disciplinaDepartamentoPersistente.lerTodasAsDisciplinasDepartamento();
        } catch(ExcepcaoPersistencia ex1) {
            fail("testApagarTodasAsDisciplinas: lerTodasAsDisciplinas");
        }
        
        try {
            _suportePersistente.confirmarTransaccao();
        } catch(ExcepcaoPersistencia ex) {
            try {
                _suportePersistente.cancelarTransaccao();
            } catch(ExcepcaoPersistencia ex3) {
                fail("testApagarTodasAsDisciplinas: cancelarTransaccao_2");
            }
            fail("testApagarTodasAsDisciplinas: confirmarTransaccao_2");
        }
        // Porque ao tentar ler uma tabela vazia ele nao se queixa logo e'
        // devolvido um ArrayList vazio.
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
// -------------------------------------------------------------------------------------------------------------------------------------------
    public void testLerDisciplina() {
        
        // Tentativa de leitura da BD de um objecto existente.
        try {
            _suportePersistente.iniciarTransaccao();
        } catch(ExcepcaoPersistencia ex) {
            fail("testLerDisciplina: iniciarTransaccao_1");
        }
        
        IDisciplinaDepartamento disciplina = null;
        try {
            disciplina = disciplinaDepartamentoPersistente.lerDisciplinaDepartamentoPorNomeESigla("Engenharia da Programacao", "ep");
        } catch(ExcepcaoPersistencia ex) {
            fail("testLerDisciplina: lerDisciplinaPorNomeESigla");
        }
        
        try {
            _suportePersistente.confirmarTransaccao();
        } catch(ExcepcaoPersistencia ex2) {
            try {
                _suportePersistente.cancelarTransaccao();
            } catch(ExcepcaoPersistencia ex3) {
                fail("testLerDisciplina: cancelarTransaccao_1");
            }
            fail("testLerDisciplina: confirmarTransaccao_1");
        }
        assertNotNull(disciplina);
        assertTrue(disciplina.getNome().equals("Engenharia da Programacao"));
        assertTrue(disciplina.getSigla().equals("ep"));
        
        // Tentativa de leitura da BD de um objecto inexistente.
        disciplina = null;
        try {
            _suportePersistente.iniciarTransaccao();
        } catch(ExcepcaoPersistencia ex) {
            fail("testLerDisciplina: iniciarTransaccao_2");
        }
        
        try {
            disciplina = disciplinaDepartamentoPersistente.lerDisciplinaDepartamentoPorNomeESigla("nao existe", "ne");
        } catch(ExcepcaoPersistencia ex) {
            fail("testLerDisciplina: lerDisciplinaPorNomeESigla");
        }
        
        try {
            _suportePersistente.confirmarTransaccao();
        } catch(ExcepcaoPersistencia ex2) {
            try {
                _suportePersistente.cancelarTransaccao();
            } catch(ExcepcaoPersistencia ex3) {
                fail("testLerDisciplina: cancelarTransaccao_2");
            }
            fail("testLerDisciplina: confirmarTransaccao_2");
        }
        assertNull(disciplina);
    }
// -------------------------------------------------------------------------------------------------------------------------------------------
    public void testApagarDisciplina() {

        // Tentativa de apagar da BD um objecto existente.
        try {
            _suportePersistente.iniciarTransaccao();
        } catch(ExcepcaoPersistencia ex) {
            fail("testApagarDisciplina: iniciarTransaccao_1");
        }
 
        try {
            disciplinaDepartamentoPersistente.apagarDisciplinaDepartamentoPorNomeESigla("Engenharia da Programacao", "ep");
        } catch(ExcepcaoPersistencia ex1) {
            fail("testApagarDisciplina: apagarDisciplinaPorNomeESigla_1");
        }
        try {
            _suportePersistente.confirmarTransaccao();
        } catch(ExcepcaoPersistencia ex3) {
            try {
                _suportePersistente.cancelarTransaccao();
            } catch(ExcepcaoPersistencia ex4) {
                fail("testApagarDisciplina: cancelarTransaccao_1");
            }
            fail("testApagarDisciplina: confirmarTransaccao_1");
        }
        IDisciplinaDepartamento disciplina = null;
        try {
            _suportePersistente.iniciarTransaccao();
            disciplina = disciplinaDepartamentoPersistente.lerDisciplinaDepartamentoPorNomeESigla("Engenharia da Programacao", "ep");
            _suportePersistente.confirmarTransaccao();
        } catch(ExcepcaoPersistencia ex) {
            try {
                _suportePersistente.cancelarTransaccao();
            } catch(ExcepcaoPersistencia ex4) {
                fail("testApagarDisciplina: cancelarTransaccao para lerDisciplinaPorNomeESigla");
            }
            fail("testApagarDisciplina: lerDisciplinaPorNomeESigla");
        }
        assertNull(disciplina);

        // Tentativa de apagar da BD um objecto inexistente.
        try {
            _suportePersistente.iniciarTransaccao();
        } catch(ExcepcaoPersistencia ex) {
            fail("testApagarDisciplina: iniciarTransaccao_2");
        }
 
        try {
            disciplinaDepartamentoPersistente.apagarDisciplinaDepartamento(new DisciplinaDepartamento());
        } catch(ExcepcaoPersistencia ex1) {
            fail("testApagarDisciplina: apagarDisciplina_2");
        }
        try {
            _suportePersistente.confirmarTransaccao();
            assertTrue("testApagarDisciplina: Disciplina apagada", true);
        } catch(ExcepcaoPersistencia ex2) {
            try {
                _suportePersistente.cancelarTransaccao();
            } catch(ExcepcaoPersistencia ex3) {
                fail("testApagarDisciplina: cancelarTransaccao_2");
            }
            fail("testApagarDisciplina: confirmarTransaccao_2");
        }
    }
// -------------------------------------------------------------------------------------------------------------------------------------------
    public void testLerTodasAsDisciplinas() {

        try {
            _suportePersistente.iniciarTransaccao();
        } catch(ExcepcaoPersistencia ex) {
            fail("testLerTodasDisciplinas: iniciarTransaccao_1");
        }
 
        ArrayList listap = null;
        try {
            listap = disciplinaDepartamentoPersistente.lerTodasAsDisciplinasDepartamento();
        } catch(ExcepcaoPersistencia ex) {
            fail("testLerTodasDisciplinas: lerTodasAsDisciplinas");
        }
 
        try {
            _suportePersistente.confirmarTransaccao();
        } catch(ExcepcaoPersistencia ex2) {
            try {
                _suportePersistente.cancelarTransaccao();
            } catch(ExcepcaoPersistencia ex3) {
                fail("testLerTodasDisciplinas: cancelarTransaccao_1");
            }
            fail("testLerTodasDisciplinas: confirmarTransaccao_1");
        }
        assertNotNull(listap);
        assertEquals(listap.size(), 2);
    }
}