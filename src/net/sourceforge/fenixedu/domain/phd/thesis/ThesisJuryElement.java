package net.sourceforge.fenixedu.domain.phd.thesis;

import java.util.Collections;
import java.util.Comparator;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.phd.PhdParticipant;

import org.joda.time.DateTime;

public class ThesisJuryElement extends ThesisJuryElement_Base {

    static public final Comparator<ThesisJuryElement> COMPARATOR_BY_ELEMENT_ORDER = new Comparator<ThesisJuryElement>() {
	@Override
	public int compare(ThesisJuryElement o1, ThesisJuryElement o2) {
	    return o1.getElementOrder().compareTo(o2.getElementOrder());
	}
    };

    protected ThesisJuryElement() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setCreationDate(new DateTime());
    }

    protected ThesisJuryElement init(final PhdThesisProcess process, PhdParticipant participant, PhdThesisJuryElementBean bean) {
	check(process, "error.ThesisJuryElement.invalid.process");
	check(participant, "error.ThesisJuryElement.participant.cannot.be.null");
	setElementOrder(generateNextElementOrder(process));
	setProcess(process);
	setParticipant(participant);
	setReporter(bean.isReporter());

	return this;
    }

    private Integer generateNextElementOrder(final PhdThesisProcess process) {
	if (!process.hasAnyThesisJuryElements()) {
	    return Integer.valueOf(1);
	}
	return Integer.valueOf(Collections.max(process.getThesisJuryElements(), ThesisJuryElement.COMPARATOR_BY_ELEMENT_ORDER)
		.getElementOrder().intValue() + 1);
    }

    public void delete() {
	disconnect();
	deleteDomainObject();
    }

    protected void disconnect() {
	removeProcess();
	removeRootDomainObject();
    }

    public boolean isInternal() {
	return getParticipant().isInternal();
    }

    public String getName() {
	return getParticipant().getName();
    }

    public String getQualification() {
	return getParticipant().getQualification();
    }

    public String getCategory() {
	return getParticipant().getCategory();
    }

    public String getWorkLocation() {
	return getParticipant().getWorkLocation();
    }

    public String getAddress() {
	return getParticipant().getAddress();
    }

    public String getPhone() {
	return getParticipant().getPhone();
    }

    public String getEmail() {
	return getParticipant().getEmail();
    }

    static public ThesisJuryElement create(final PhdThesisProcess process, final PhdThesisJuryElementBean bean) {
	final PhdParticipant participant = bean.getParticipant() == null ? PhdParticipant.create(process
		.getIndividualProgramProcess(), bean) : bean.getParticipant();

	return new ThesisJuryElement().init(process, participant, bean);
    }

}
