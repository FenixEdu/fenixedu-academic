package net.sourceforge.fenixedu.domain.cardGeneration;

import java.util.List;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class SantanderSequenceNumberGenerator extends SantanderSequenceNumberGenerator_Base {
    
    private  SantanderSequenceNumberGenerator() {
        super();
        setSequenceNumber(0);
    }
    
    private static SantanderSequenceNumberGenerator getInstance() {
	SantanderSequenceNumberGenerator instance;
	List<SantanderSequenceNumberGenerator> instances = RootDomainObject.getInstance().getSantanderSequenceNumberGenerators();
	if (instances.size() > 1) {
	    throw new DomainException("santanderSequenceNumberGenerator.more.than.one.instances.exist");
	}
	if (instances.size() == 0) {
	    instance = new SantanderSequenceNumberGenerator();
	} else {
	    instance = instances.get(0);
	}
	return instance;
    }
    
    public static int getNewSequenceNumber() {
	Integer seqNumber = getInstance().getSequenceNumber();
	getInstance().setSequenceNumber(++seqNumber);
	return seqNumber;
    }
    
}
