package net.sourceforge.fenixedu.domain.phd.seminar;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class PublicPresentationSeminarState extends PublicPresentationSeminarState_Base {

    private PublicPresentationSeminarState() {
	super();
    }

    PublicPresentationSeminarState(final PublicPresentationSeminarProcess process,
	    final PublicPresentationSeminarProcessStateType type, final Person person) {
	this(process, type, person, null);
    }

    PublicPresentationSeminarState(final PublicPresentationSeminarProcess process,
	    final PublicPresentationSeminarProcessStateType type, final Person person, final String remarks) {

	this();

	super.init(person, remarks);

	check(process, "error.PublicPresentationSeminarState.invalid.process");
	check(type, "error.PublicPresentationSeminarState.invalid.type");

	checkType(process, type);

	setProcess(process);
	setType(type);
    }

    private void checkType(final PublicPresentationSeminarProcess process, final PublicPresentationSeminarProcessStateType type) {
	final PublicPresentationSeminarProcessStateType currentType = process.getActiveStateType();
	if (currentType != null && currentType.equals(type)) {
	    throw new DomainException("error.PublicPresentationSeminarState.equals.previous.state");
	}
    }

    @Override
    protected void disconnect() {
	removeProcess();
	super.disconnect();
    }
}
