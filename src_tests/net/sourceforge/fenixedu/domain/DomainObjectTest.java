package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

public class DomainObjectTest extends DomainTestBase {

    private class SomeDomainObject extends Site {
    }

    private ISuportePersistente persistentSupport;

    private void beginTransaction() {
        try {
            persistentSupport.iniciarTransaccao();
        } catch (ExcepcaoPersistencia e) {
            fail("Unexpected excpetion beginning transaction.");
        }        
    }

    private void abortTransaction() {
        try {
            persistentSupport.cancelarTransaccao();
        } catch (ExcepcaoPersistencia e) {
            fail("Unexpected excpetion beginning transaction.");
        }        
    }

    protected void setUp() throws Exception {
        super.setUp();

        persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
    }

    public void testHashCode() {
        SomeDomainObject domainObject = new SomeDomainObject();
        try {
            domainObject.hashCode();
            fail("Expected RuntimeException because idInternal was not set.");
        } catch (final RuntimeException rex) {
            assertNull("Domain object idInternal was set outside of a transaction.",
                    domainObject.getIdInternal());
        }

        DomainObject.turnOnLockMode();
        beginTransaction();

        domainObject = new SomeDomainObject();
        final int hashCode = domainObject.hashCode();
        assertTrue("Domain object idInternal was not properly set.", hashCode > 0);

        final SomeDomainObject anotherDomainObject = new SomeDomainObject();
        final int anotherHashCode = anotherDomainObject.hashCode();
        assertTrue("Domain object idInternal was not properly set.", hashCode > 0);

        assertTrue("Two domain objects cannot have the same hashCode.", hashCode != anotherHashCode);
        assertTrue("A domain objects hashCode does not change.", hashCode == domainObject.hashCode());

        abortTransaction();
        DomainObject.turnOffLockMode();
    }

    public void testEquals() {
        DomainObject.turnOnLockMode();
        beginTransaction();

        final SomeDomainObject domainObject = new SomeDomainObject();
        final SomeDomainObject anotherDomainObject = new SomeDomainObject();

        assertFalse("A domain object is not equals to null.", domainObject.equals(null));
        assertTrue("A domain object is always equals to itself.", domainObject.equals(domainObject));
        assertFalse("A domain object is only equals to itself.", domainObject.equals(anotherDomainObject));

        abortTransaction();
        DomainObject.turnOffLockMode();
    }

}
