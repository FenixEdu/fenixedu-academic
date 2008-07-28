package net.sourceforge.fenixedu.domain.thesis;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;

import org.joda.time.DateTime;

/**
 * Generic library operation over a thesis.
 * 
 * @author Pedro Santos (pmrsa)
 */
public abstract class ThesisLibraryOperation extends ThesisLibraryOperation_Base {

    public ThesisLibraryOperation() {
	super();
    }

    protected void init(Thesis thesis, Person performer) {
	if (thesis.hasLastLibraryOperation())
	    setPrevious(thesis.getLastLibraryOperation());
	super.setThesis(thesis);
	super.setPerformer(performer);
	super.setOperation(new DateTime());
    }

    public Integer getThesisId() {
	return getThesis().getIdInternal();
    }

    public Integer getPerformerId() {
	return getPerformer().getIdInternal();
    }

    public abstract ThesisLibraryState getState();

    public abstract String getLibraryReference();

    public abstract String getPendingComment();

    @Override
    protected RootDomainObject getRootDomainObject() {
	return getThesis().getRootDomainObject();
    }

    @Override
    public void setThesis(Thesis thesis) {
	throw new UnsupportedOperationException();
    }

    @Override
    public void setPerformer(Person performer) {
	throw new UnsupportedOperationException();
    }

    @Override
    public void setOperation(DateTime operation) {
	throw new UnsupportedOperationException();
    }
}
