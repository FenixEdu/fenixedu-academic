package net.sourceforge.fenixedu.dataTransferObject.residenceManagement;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.fenixedu.domain.accounting.ResidenceEvent;
import net.sourceforge.fenixedu.domain.residence.ResidenceMonth;
import net.sourceforge.fenixedu.util.Money;

import org.joda.time.YearMonthDay;

public class ResidenceDebtEventBean extends ResidenceEventBean {

	private Money roomValuePaid;
	private String paidDate;

	private YearMonthDay paidDateObject;
	private ResidenceEvent eventObject;
	private ResidenceMonth month;

	public ResidenceDebtEventBean(String userName, String fiscalNumber, String name, Double roomValue, String room,
			String paidDate, Double roomValuePaid) {
		super(userName, fiscalNumber, name, roomValue, room);
		this.setPaidDate(paidDate);
		this.setRoomValuePaid(new Money(roomValuePaid));
	}

	public void setRoomValuePaid(Money roomValuePaid) {
		this.roomValuePaid = roomValuePaid;
	}

	public Money getRoomValuePaid() {
		return roomValuePaid;
	}

	public void setPaidDate(String paidDate) {
		this.paidDate = paidDate;
	}

	public String getPaidDate() {
		return paidDate;
	}

	@Override
	public boolean getStatus() {
		if (!super.getStatus()) {
			return false;
		}

		String date = getPaidDate();
		Pattern p = Pattern.compile("^(\\d\\d).(\\d\\d).((\\d\\d){1,2})$");
		Matcher m = p.matcher(date);

		if (!m.matches()) {
			setStatusMessage("label.error.invalid.date");
			return false;
		}

		int day = Integer.valueOf(m.group(1));
		int month = Integer.valueOf(m.group(2));
		int year = m.group(3).length() == 2 ? 2000 + Integer.valueOf(m.group(3)) : Integer.valueOf(m.group(3));
		this.setPaidDateObject(new YearMonthDay(year, month, day));

		if (getPaidDate() == null) {
			setStatusMessage("label.error.invalid.date");
			return false;
		}

		if (getRoomValuePaid() == null) {
			setStatusMessage("label.error.invalid.payment.amount");
			return false;
		}

		for (ResidenceEvent residenceEvent : getMonth().getEventsSet()) {
			if (residenceEvent.getPerson() != getStudent().getPerson()) {
				continue;
			}

			if (!residenceEvent.isOpen()) {
				setStatusMessage("label.error.already.paid");
				return false;
			}

			if (getPaidDateObject().isAfter(new YearMonthDay())) {
				setStatusMessage("label.error.invalid.date");
				return false;
			}

			if (!residenceEvent.getAmountToPay().equals(getRoomValuePaid())) {
				setStatusMessage("label.error.invalid.payment.amount");
				return false;
			}

			setEventObject(residenceEvent);
			setPaidDateObject(new YearMonthDay(year, month, day));
			break;
		}
		if (getEventObject() == null) {
			setStatusMessage("label.error.invalid.payment.amount");
			return false;
		}
		return true;
	}

	public YearMonthDay getPaidDateObject() {
		return paidDateObject;
	}

	public void setPaidDateObject(YearMonthDay paidDateObject) {
		this.paidDateObject = paidDateObject;
	}

	public ResidenceEvent getEventObject() {
		return eventObject;
	}

	public void setEventObject(ResidenceEvent eventObject) {
		this.eventObject = eventObject;
	}

	public ResidenceMonth getMonth() {
		return month;
	}

	public void setMonth(ResidenceMonth month) {
		this.month = month;
	}

}
