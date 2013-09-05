package net.sourceforge.fenixedu.domain.cardGeneration;

import java.util.List;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class SantanderSequenceNumberGenerator extends SantanderSequenceNumberGenerator_Base {

    private SantanderSequenceNumberGenerator() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setSequenceNumber(0);
        setPhotoSequenceNumber(0);
    }

    private static SantanderSequenceNumberGenerator getInstance() {
        final List<SantanderSequenceNumberGenerator> instances = RootDomainObject.getInstance().getSantanderSequenceNumberGenerators();
        if (instances.size() > 1) {
            throw new DomainException("santanderSequenceNumberGenerator.more.than.one.instances.exist");
        }
        return instances.size() == 0 ? new SantanderSequenceNumberGenerator() : instances.iterator().next();
    }

    public static int getNewSequenceNumber() {
        Integer seqNumber = getInstance().getSequenceNumber();
        getInstance().setSequenceNumber(++seqNumber);
        return seqNumber;
    }

    public static int getNewPhotoSequenceNumber() {
        Integer seqNumber = getInstance().getPhotoSequenceNumber();
        getInstance().setPhotoSequenceNumber(++seqNumber);
        return seqNumber;
    }

}
