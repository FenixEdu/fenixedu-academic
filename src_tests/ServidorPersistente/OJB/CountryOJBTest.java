/*
 * CountryOJBTest.java
 * JUnit based test
 *
 * Created on 28 of December 2002, 10:37
 * 
 * 
 */
 
/**
 *
 * @author  Nuno Nunes & Joana Mota
 */ 

package ServidorPersistente.OJB;

import java.util.ArrayList;

import junit.framework.Test;
import junit.framework.TestSuite;

import Dominio.Country;
import Dominio.ICountry;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.exceptions.ExistingPersistentException;

public class CountryOJBTest extends TestCaseOJB {
    public CountryOJBTest(java.lang.String testName) {
        super(testName);
    }
    
    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite(CountryOJBTest.class);
        
        return suite;
    }
    
    protected void setUp() {
        super.setUp();
    }
    
    protected void tearDown() {
        super.tearDown();
    }
    
// -------------------------------------------------------------------------------------------------------------------------------------------
    public void testWriteCountry() {
        
        ICountry country = new Country("Portugal", "Portuguesa", "PT");

        try {
            persistentSupport.iniciarTransaccao();
            persistentCountry.writeCountry(country);
            persistentSupport.confirmarTransaccao();
            fail("testEscreverPais: confirmarTransaccao_1");
		} catch(ExistingPersistentException ex) {
			// All is OK
        } catch(ExcepcaoPersistencia ex) {
			fail("testEscreverPais: unexpected exception");
        }
        
        // write non existing
        country = new Country("Polonia", "Polaco", "PC");
        
        try {
            persistentSupport.iniciarTransaccao();
            persistentCountry.writeCountry(country);
            persistentSupport.confirmarTransaccao();
        } catch(ExcepcaoPersistencia ex2) {
		    fail("testEscreverPais: write non Existing");	
        }
        
        ICountry country2 = null;
        
        try {
            persistentSupport.iniciarTransaccao();
            country2 = persistentCountry.readCountryByName(country.getName());
            persistentSupport.confirmarTransaccao();
        } catch(ExcepcaoPersistencia ex) {
            fail("testEscreverPais: confirmarTransaccao_3");
        }
        
        assertNotNull(country2);
        assertEquals(country2.getName(), country.getName());
        assertEquals(country2.getNationality(), country.getNationality());
        assertEquals(country2.getCode(), country.getCode());
        
    }
// -------------------------------------------------------------------------------------------------------------------------------------------
    public void testDeleteAllCountrys() {
        
        try {
            persistentSupport.iniciarTransaccao();
            persistentCountry.deleteAllCountrys();
            persistentSupport.confirmarTransaccao();
            assertTrue("testApagarTodosOsPaises: Paises apagados", true);
        } catch(ExcepcaoPersistencia ex2) {
            fail("testApagarTodosOsPaises: confirmarTransaccao_1");
        }

        ArrayList result = null;
        
        try {
            persistentSupport.iniciarTransaccao();
            result = persistentCountry.readAllCountrys();
            persistentSupport.confirmarTransaccao();
        } catch(ExcepcaoPersistencia ex) {
            fail("testApagarTodosOsPaises: confirmarTransaccao_2");
        }
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
// -------------------------------------------------------------------------------------------------------------------------------------------
    public void testReadCountry() {
        ICountry country = null;

        
        try {
            persistentSupport.iniciarTransaccao();
            country = persistentCountry.readCountryByName("Portugal");
            persistentSupport.confirmarTransaccao();
        } catch(ExcepcaoPersistencia ex2) {
            fail("testLerPais: confirmarTransaccao_1");
        }
        assertNotNull(country);
        assertTrue(country.getName().equals("Portugal"));
        assertTrue(country.getNationality().equals("Portuguesa"));
        assertTrue(country.getCode().equals("PT"));
        

        country = null;
        try {
            persistentSupport.iniciarTransaccao();
            country = persistentCountry.readCountryByName("Chipre");
            persistentSupport.confirmarTransaccao();
        } catch(ExcepcaoPersistencia ex2) {
            fail("testLerPais: confirmarTransaccao_2");
        }
        assertNull(country);
    }
// -------------------------------------------------------------------------------------------------------------------------------------------
    public void testDeleteCountry() {


        try {
            persistentSupport.iniciarTransaccao();
			persistentCountry.deleteCountryByName("Portugal");
            persistentSupport.confirmarTransaccao();
        } catch(ExcepcaoPersistencia ex3) {
            fail("testApagarPais: confirmarTransaccao_1");
        }
        ICountry country = null;
        
        try {
            persistentSupport.iniciarTransaccao();
            country = persistentCountry.readCountryByName("Portugal");
            persistentSupport.confirmarTransaccao();
        } catch(ExcepcaoPersistencia ex) {
            fail("testApagarPais: lerPaisPorNome");
        }
        assertNull(country);

        try {
            persistentSupport.iniciarTransaccao();
            persistentCountry.deleteCountryByName("Chipre");
            persistentSupport.confirmarTransaccao();
            assertTrue("testApagarPais: Pais apagado", true);
        } catch(ExcepcaoPersistencia ex2) {
            fail("testApagarPais: confirmarTransaccao_2");
        }
    }
// -------------------------------------------------------------------------------------------------------------------------------------------
    public void testReadAllCountrys() {
        ArrayList list = null;


        try {
            persistentSupport.iniciarTransaccao();
            list = persistentCountry.readAllCountrys();
            persistentSupport.confirmarTransaccao();
        } catch(ExcepcaoPersistencia ex2) {
            fail("testLerTodosOsPaises: confirmarTransaccao_1");
        }
        assertNotNull(list);
        assertEquals(list.size(), 2);
    }
}