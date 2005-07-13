package net.sourceforge.fenixedu.domain;


public class DomainObjectTest extends DomainTestBase {

    private class SomeDomainObject extends DomainObject {
    }

    private class SomeOtherDomainObject extends DomainObject {
    }

    private class SomeOtherObject {
    }


    private SomeDomainObject someDomainObject1;
    private SomeDomainObject someDomainObject2;
    private SomeOtherDomainObject someOtherDomainObject;
    private SomeOtherObject someOtherObject;
    private SomeDomainObject someBadDomainObject;

    protected void setUp() throws Exception {
        super.setUp();

        someDomainObject1 = new SomeDomainObject();
        someDomainObject1.setIdInternal(1);

        someDomainObject2 = new SomeDomainObject();
        someDomainObject2.setIdInternal(2);

        someOtherDomainObject = new SomeOtherDomainObject();
        someOtherDomainObject.setIdInternal(1);

        someOtherObject = new SomeOtherObject();

        someBadDomainObject = new SomeDomainObject();
    }


    public void testHashCode() {
    	assertEquals("Unexpected hashCode for domain object.", 1, someDomainObject1.hashCode());
    	assertEquals("Unexpected hashCode for domain object.", 2, someDomainObject2.hashCode());
    	assertEquals("Unexpected hashCode for domain object.", 1, someOtherDomainObject.hashCode());

    	try {
    		someBadDomainObject.hashCode();
    		fail("HashCode for imporoperly created object should cause an exception.");
    	} catch (RuntimeException ex) {
    		assertNull("IdInternal was not initialized.", someBadDomainObject.getIdInternal());
    	}
    }

    public void testEquals() {
    	assertFalse("Domain object is not equals to null.", someDomainObject1.equals(null));

    	assertTrue("Domain object is always equals to itself.",
    			someDomainObject1.equals(someDomainObject1));

    	assertFalse("Domain object is not equals to any other domain object of sane class.",
    			someDomainObject1.equals(someDomainObject2));
    	assertFalse("Domain object is not equals to any other domain object of sane class.",
    			someDomainObject2.equals(someDomainObject1));

    	assertFalse("Domain object is not equals to any other domain object.",
    			someDomainObject1.equals(someOtherDomainObject));
    	assertFalse("Domain object is not equals to any other domain object.",
    			someOtherDomainObject.equals(someDomainObject1));

    	assertFalse("Domain object is not equals to any other object.",
    			someDomainObject1.equals(someOtherObject));
    	assertFalse("Domain object is not equals to any other object.",
    			someOtherObject.equals(someDomainObject1));
    }

}
