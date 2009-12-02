package net.sourceforge.fenixedu.domain.assiduousness;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.assiduousness.util.AnulationState;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.LocalDate;
import org.joda.time.Partial;
import org.joda.time.TimeOfDay;
import org.joda.time.YearMonthDay;

public class Justification extends Justification_Base {

	public Justification() {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
	}

	public Partial getPartialDate() {
		Partial p = new Partial();
		YearMonthDay y = getDate().toYearMonthDay();
		if (y != null) {
			for (int i = 0; i < y.getFields().length; i++) {
				p = p.with(y.getFieldType(i), y.getValue(i));
			}
		}
		TimeOfDay t = getDate().toTimeOfDay();
		if (t != null && !t.isEqual(new TimeOfDay(0, 0, 0))) {
			for (int i = 0; i < t.getFields().length; i++) {
				p = p.with(t.getFieldType(i), t.getValue(i));
			}
		}
		return p;
	}

	public Partial getPartialEndDate() {
		if (this.isLeave()) {
			return ((Leave) this).getPartialEndDate();
		}
		return null;
	}

	public Integer getModifiedByNumber() {
		if (getModifiedBy() != null) {
			return getModifiedBy().getEmployeeNumber();
		}
		return null;
	}

	@Override
	public void delete() {
		if (getAnulation() != null) {
			Anulation a = getAnulation();
			removeAnulation();
			a.delete();
		}
		removeAssiduousness();
		removeJustificationMotive();
		removeModifiedBy();
		super.delete();
	}

	public void anulate(Employee modifiedBy) {
		if (getAnulation() != null) {
			getAnulation().setState(AnulationState.VALID);
			getAnulation().setModifiedBy(modifiedBy);
		} else {
			new Anulation(this, modifiedBy);
		}
		setLastModifiedDate(new DateTime());
	}

	protected List<JustificationMotive> getNotCorrected(HashMap<JustificationMotive, Duration> map,
			List<ClosedMonthJustification> closedMonthJustifications) {
		List<JustificationMotive> notCorrected = new ArrayList<JustificationMotive>();
		for (JustificationMotive justificationMotive : map.keySet()) {
			boolean alreadyCorrected = false;
			Duration duration = map.get(justificationMotive);
			for (ClosedMonthJustification closedMonthJustification : closedMonthJustifications) {
				if (closedMonthJustification.getJustificationMotive().equals(justificationMotive)) {
					alreadyCorrected = true;
				}
			}
			if (!alreadyCorrected && duration != null && !duration.equals(Duration.ZERO)) {
				notCorrected.add(justificationMotive);
			}
		}
		return notCorrected;
	}

	protected List<WorkScheduleType> getNotCorrectedExtraWork(HashMap<WorkScheduleType, Duration> map,
			List<AssiduousnessExtraWork> assiduousnessExtraWorks) {
		List<WorkScheduleType> notCorrected = new ArrayList<WorkScheduleType>();
		for (WorkScheduleType workScheduleType : map.keySet()) {
			boolean alreadyCorrected = false;
			Duration duration = map.get(workScheduleType);
			for (AssiduousnessExtraWork assiduousnessExtraWork : assiduousnessExtraWorks) {
				if (assiduousnessExtraWork.getWorkScheduleType().equals(workScheduleType)) {
					alreadyCorrected = true;
				}
			}
			if (!alreadyCorrected && duration != null && !duration.equals(Duration.ZERO)) {
				notCorrected.add(workScheduleType);
			}
		}
		return notCorrected;
	}

	protected List<WorkScheduleType> getNotCorrectedExtraWorkDays(HashMap<WorkScheduleType, Integer> map,
			List<AssiduousnessExtraWork> assiduousnessExtraWorks) {
		List<WorkScheduleType> notCorrected = new ArrayList<WorkScheduleType>();
		for (WorkScheduleType workScheduleType : map.keySet()) {
			boolean alreadyCorrected = false;
			Integer value = map.get(workScheduleType);
			for (AssiduousnessExtraWork assiduousnessExtraWork : assiduousnessExtraWorks) {
				if (assiduousnessExtraWork.getWorkScheduleType().equals(workScheduleType)) {
					alreadyCorrected = true;
				}
			}
			if (!alreadyCorrected && value != null && value != 0) {
				notCorrected.add(workScheduleType);
			}
		}
		return notCorrected;
	}

	public boolean getIsCorrection() {
		return getIsCorrection(getDate().toLocalDate());
	}

	public boolean getIsCorrection(LocalDate date) {
		ClosedMonth closedMonth = ClosedMonth.getClosedMonthForBalance(date);
		if (closedMonth != null) {
			DateTime closedForBalanceDate = closedMonth.getClosedForBalanceDate();
			return (closedForBalanceDate != null && getLastModifiedDate() != null && closedForBalanceDate
					.isBefore(getLastModifiedDate()));
		}
		return false;
	}

	public boolean isEqual(DateTime dateTime, JustificationMotive justificationMotive, Boolean anulated) {
		return getDate().equals(dateTime) && getJustificationMotive().equals(justificationMotive) && isAnulated() == anulated;
	}

}
