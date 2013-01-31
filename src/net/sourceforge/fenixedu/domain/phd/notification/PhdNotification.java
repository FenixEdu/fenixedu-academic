package net.sourceforge.fenixedu.domain.phd.notification;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcess;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import pt.ist.fenixWebFramework.services.Service;

public class PhdNotification extends PhdNotification_Base {

	public static final Comparator<PhdNotification> COMPARATOR_BY_NUMBER = new Comparator<PhdNotification>() {
		@Override
		public int compare(PhdNotification left, PhdNotification right) {

			int result = left.getNumber().compareTo(right.getNumber());

			return result == 0 ? left.getIdInternal().compareTo(right.getIdInternal()) : result;

		};
	};

	public PhdNotification() {
		super();
		setWhenCreated(new DateTime());
		if (AccessControl.getPerson() != null) {
			setCreatedBy(AccessControl.getPerson().getUsername());
		}
	}

	public PhdNotification(PhdNotificationType type, PhdProgramCandidacyProcess candidacyProcess) {
		this();
		init(type, candidacyProcess);
	}

	public PhdNotification(PhdNotificationBean bean) {
		this(bean.getType(), bean.getCandidacyProcess());
	}

	protected void init(PhdNotificationType type, PhdProgramCandidacyProcess candidacyProcess) {
		check(type, "error.net.sourceforge.fenixedu.domain.phd.notification.PhdNotification.type.cannot.be.null");
		check(candidacyProcess,
				"error.net.sourceforge.fenixedu.domain.phd.notification.PhdNotification.candidacyProcess.cannot.be.null");

		super.setType(type);
		super.setYear(new LocalDate().getYear());
		super.setNumber(generateNumber(getYear()));
		super.setRootDomainObject(RootDomainObject.getInstance());
		super.setCandidacyProcess(candidacyProcess);
		super.setState(PhdNotificationState.EMITTED);

	}

	@Override
	public void setType(PhdNotificationType type) {
		throw new DomainException("error.net.sourceforge.fenixedu.domain.phd.notification.PhdNotification.cannot.modify.type");
	}

	@Override
	public void setCandidacyProcess(PhdProgramCandidacyProcess candidacyProcess) {
		throw new DomainException(
				"error.net.sourceforge.fenixedu.domain.phd.notification.PhdNotification.cannot.modify.candidacyProcess");
	}

	@Override
	public void setState(PhdNotificationState state) {
		throw new DomainException("error.net.sourceforge.fenixedu.domain.phd.notification.PhdNotification.cannot.modify.state");
	}

	private Integer generateNumber(int year) {
		final List<PhdNotification> notifications = getNotificationsForYear(year);
		return notifications.isEmpty() ? 1 : Collections.max(notifications, PhdNotification.COMPARATOR_BY_NUMBER).getNumber() + 1;
	}

	private List<PhdNotification> getNotificationsForYear(int year) {
		final List<PhdNotification> result = new ArrayList<PhdNotification>();

		for (final PhdNotification each : RootDomainObject.getInstance().getPhdNotifications()) {
			if (each.isFor(year)) {
				result.add(each);
			}
		}

		return result;
	}

	private boolean isFor(int year) {
		return getYear().intValue() == year;
	}

	public String getNotificationNumber() {
		return getNumber() + "/" + getYear();
	}

	@Service
	public void markAsSent() {
		super.setState(PhdNotificationState.SENT);
	}

	public boolean isSent() {
		return getState() == PhdNotificationState.SENT;
	}

}
