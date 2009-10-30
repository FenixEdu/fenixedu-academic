package net.sourceforge.fenixedu.domain.assiduousness;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Servico.assiduousness.ReadAssiduousnessWorkSheet;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.EmployeeWorkSheet;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.WorkDaySheet;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.RootDomainObject;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

public class MissingClocking extends MissingClocking_Base {

    public MissingClocking(Assiduousness assiduousness, DateTime date, JustificationMotive justificationMotive,
	    DateTime lastModifiedDate, Employee modifiedBy, Integer oracleSequence) {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setAssiduousness(assiduousness);
	setDate(date);
	setJustificationMotive(justificationMotive);
	setLastModifiedDate(lastModifiedDate);
	setModifiedBy(modifiedBy);
	setOracleSequence(oracleSequence);
    }

    public MissingClocking(Assiduousness assiduousness, DateTime date, JustificationMotive justificationMotive,
	    Employee modifiedBy) {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setAssiduousness(assiduousness);
	setDate(date);
	setJustificationMotive(justificationMotive);
	setLastModifiedDate(new DateTime());
	setModifiedBy(modifiedBy);
	setOracleSequence(0);
	correctAssiduousnessClosedMonth();
    }

    public void modify(DateTime date, JustificationMotive justificationMotive, Employee modifiedBy) {
	ClosedMonth closedMonth = ClosedMonth.getClosedMonthForBalance(date.toLocalDate());
	if (closedMonth != null && closedMonth.getClosedForBalance()
		&& getLastModifiedDate().isBefore(closedMonth.getClosedForBalanceDate())) {
	    anulate(modifiedBy);
	    MissingClocking missingClocking = new MissingClocking(getAssiduousness(), date, justificationMotive, modifiedBy);
	    missingClocking.correctAssiduousnessClosedMonth();
	} else {
	    setDate(date);
	    setJustificationMotive(justificationMotive);
	    setLastModifiedDate(new DateTime());
	    setModifiedBy(modifiedBy);
	    correctAssiduousnessClosedMonth();
	}
    }

    @Override
    public void anulate(Employee modifiedBy) {
	super.anulate(modifiedBy);
	correctAssiduousnessClosedMonth();
    }

    private void correctAssiduousnessClosedMonth() {
	ClosedMonth correctionClosedMonth = ClosedMonth.getNextClosedMonth();
	Boolean correctNext = false;
	LocalDate date = getDate().toLocalDate();
	AssiduousnessStatusHistory assiduousnessStatusHistory = getAssiduousness().getLastAssiduousnessStatusHistoryBetween(date,
		date);

	do {
	    correctNext = false;
	    ClosedMonth closedMonth = ClosedMonth.getClosedMonthForBalance(date);
	    if (closedMonth != null && closedMonth.getClosedForBalance()) {
		AssiduousnessClosedMonth oldAssiduousnessClosedMonth = closedMonth
			.getAssiduousnessClosedMonth(assiduousnessStatusHistory);
		if (oldAssiduousnessClosedMonth != null) {

		    EmployeeWorkSheet employeeWorkSheet = ReadAssiduousnessWorkSheet.run(getAssiduousness().getEmployee(),
			    oldAssiduousnessClosedMonth.getBeginDate(), oldAssiduousnessClosedMonth.getEndDate());

		    AssiduousnessClosedMonth newAssiduousnessClosedMonth = oldAssiduousnessClosedMonth;

		    if (!oldAssiduousnessClosedMonth.hasEqualValues(employeeWorkSheet)) {
			correctNext = true;
			if (oldAssiduousnessClosedMonth.getIsCorrection()
				&& oldAssiduousnessClosedMonth.getCorrectedOnClosedMonth().equals(correctionClosedMonth)) {
			    oldAssiduousnessClosedMonth.correct(employeeWorkSheet);
			} else {
			    newAssiduousnessClosedMonth = new AssiduousnessClosedMonth(employeeWorkSheet, correctionClosedMonth,
				    oldAssiduousnessClosedMonth);
			}
		    }

		    List<ClosedMonthJustification> closedMonthJustifications = oldAssiduousnessClosedMonth
			    .getClosedMonthJustifications();
		    for (ClosedMonthJustification closedMonthJustification : closedMonthJustifications) {
			if (!closedMonthJustification.hasEqualValues(employeeWorkSheet)) {
			    correctNext = true;
			    if (closedMonthJustification.getIsCorrection()
				    && closedMonthJustification.getCorrectedOnClosedMonth().equals(correctionClosedMonth)) {
				closedMonthJustification.correct(employeeWorkSheet);
			    } else {
				new ClosedMonthJustification(employeeWorkSheet, correctionClosedMonth,
					oldAssiduousnessClosedMonth, closedMonthJustification.getJustificationMotive());
			    }
			}
		    }

		    Set<JustificationMotive> notCorrectedJustifications = new HashSet<JustificationMotive>();
		    notCorrectedJustifications.addAll(getNotCorrected(employeeWorkSheet.getJustificationsDuration(),
			    closedMonthJustifications));
		    for (JustificationMotive justificationMotive : notCorrectedJustifications) {
			new ClosedMonthJustification(employeeWorkSheet, correctionClosedMonth, oldAssiduousnessClosedMonth,
				justificationMotive);
		    }

		    if (getDate().toLocalDate().equals(date)) {
			List<AssiduousnessExtraWork> assiduousnessExtraWorks = oldAssiduousnessClosedMonth
				.getAssiduousnessExtraWorks();
			for (AssiduousnessExtraWork assiduousnessExtraWork : assiduousnessExtraWorks) {
			    if (!assiduousnessExtraWork.hasEqualValues(employeeWorkSheet)) {
				if (assiduousnessExtraWork.getIsCorrection()
					&& assiduousnessExtraWork.getCorrectedOnClosedMonth().equals(correctionClosedMonth)) {
				    assiduousnessExtraWork.correct(employeeWorkSheet);
				} else {
				    new AssiduousnessExtraWork(employeeWorkSheet, correctionClosedMonth,
					    oldAssiduousnessClosedMonth, assiduousnessExtraWork.getWorkScheduleType());
				}
			    }
			}

			Set<WorkScheduleType> notCorrected = new HashSet<WorkScheduleType>();
			notCorrected
				.addAll(getNotCorrectedExtraWork(employeeWorkSheet.getExtra125Map(), assiduousnessExtraWorks));
			notCorrected
				.addAll(getNotCorrectedExtraWork(employeeWorkSheet.getExtra150Map(), assiduousnessExtraWorks));
			notCorrected.addAll(getNotCorrectedExtraWork(employeeWorkSheet.getExtra150WithLimitsMap(),
				assiduousnessExtraWorks));
			notCorrected.addAll(getNotCorrectedExtraWork(employeeWorkSheet.getExtraNight160Map(),
				assiduousnessExtraWorks));
			notCorrected.addAll(getNotCorrectedExtraWork(employeeWorkSheet.getExtraNight190Map(),
				assiduousnessExtraWorks));
			notCorrected.addAll(getNotCorrectedExtraWork(employeeWorkSheet.getExtraNight190WithLimitsMap(),
				assiduousnessExtraWorks));
			notCorrected.addAll(getNotCorrectedExtraWork(employeeWorkSheet.getExtra25Map(), assiduousnessExtraWorks));
			notCorrected.addAll(getNotCorrectedExtraWork(employeeWorkSheet.getUnjustifiedMap(),
				assiduousnessExtraWorks));
			notCorrected.addAll(getNotCorrectedExtraWorkDays(employeeWorkSheet.getExtraWorkNightsMap(),
				assiduousnessExtraWorks));
			for (WorkScheduleType workScheduleType : notCorrected) {
			    new AssiduousnessExtraWork(employeeWorkSheet, correctionClosedMonth, oldAssiduousnessClosedMonth,
				    workScheduleType);
			}

			AssiduousnessClosedDay assiduousnessClosedDay = oldAssiduousnessClosedMonth
				.getAssiduousnessClosedDay(getDate().toLocalDate());
			WorkDaySheet workDaySheet = employeeWorkSheet.getWorkDaySheet(getDate().toLocalDate());
			if (assiduousnessClosedDay != null && assiduousnessClosedDay.getIsCorrection()
				&& assiduousnessClosedDay.getCorrectedOnClosedMonth().equals(correctionClosedMonth)) {
			    assiduousnessClosedDay.correct(workDaySheet);
			} else {
			    new AssiduousnessClosedDay(newAssiduousnessClosedMonth, workDaySheet, correctionClosedMonth);
			}
		    }
		}
	    }
	    date = date.plusMonths(1);
	} while (correctNext);
    }

    @Override
    public boolean isMissingClocking() {
	return true;
    }

}
