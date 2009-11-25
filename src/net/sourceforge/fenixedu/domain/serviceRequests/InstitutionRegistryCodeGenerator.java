package net.sourceforge.fenixedu.domain.serviceRequests;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.RegistryDiplomaRequest;

import org.joda.time.DateTime;

public class InstitutionRegistryCodeGenerator extends InstitutionRegistryCodeGenerator_Base {
    public InstitutionRegistryCodeGenerator() {
	super();
	new RectorateSubmissionBatch(this);
	setRootDomainObject(RootDomainObject.getInstance());
    }

    protected Integer getNextNumber() {
	super.setCurrent((super.getCurrent() != null ? super.getCurrent() : 0) + 1);
	return super.getCurrent();
    }

    @Override
    public Integer getCurrent() {
	throw new UnsupportedOperationException();
    }

    @Override
    public void setCurrent(Integer current) {
	throw new UnsupportedOperationException();
    }

    public RectorateSubmissionBatch getCurrentRectorateSubmissionBatch() {
	DateTime last = null;
	RectorateSubmissionBatch current = null;
	for (RectorateSubmissionBatch bag : getRectorateSubmissionBatchSet()) {
	    if (last == null || bag.getCreation().isAfter(last)) {
		last = bag.getCreation();
		current = bag;
	    }
	}
	return current;
    }

    public RegistryCode createRegistryFor(RegistryDiplomaRequest request) {
	return new RegistryCode(this, request);
    }

    public Object getRectorateSubmissionBatchesByState(RectorateSubmissionState state) {
	Set<RectorateSubmissionBatch> result = new HashSet<RectorateSubmissionBatch>();
	for (RectorateSubmissionBatch batch : getRectorateSubmissionBatchSet()) {
	    if (batch.getState().equals(state)) {
		result.add(batch);
	    }
	}
	return result;
    }
}
