package net.sourceforge.fenixedu.applicationTier.Servico.assiduousness;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.AssignedEmployeeAnualInfo;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.AssignedEmployeeMonthInfo;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.YearMonth;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.assiduousness.Assiduousness;
import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessClosedMonth;
import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessStatusHistory;
import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessVacations;
import net.sourceforge.fenixedu.domain.assiduousness.ClosedMonthJustification;
import net.sourceforge.fenixedu.domain.assiduousness.ExtraWorkRequest;
import net.sourceforge.fenixedu.domain.assiduousness.Leave;
import net.sourceforge.fenixedu.domain.assiduousness.util.AssiduousnessState;
import net.sourceforge.fenixedu.domain.assiduousness.util.JustificationGroup;
import net.sourceforge.fenixedu.util.NumberUtils;

import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;
import org.joda.time.Days;
import org.joda.time.Duration;
import org.joda.time.Interval;
import org.joda.time.YearMonthDay;

public class ExportAssignedEmployeesInfo extends Service {

    private YearMonthDay beginDate;

    private YearMonthDay endDate;

    public List<AssignedEmployeeAnualInfo> run(YearMonth yearMonth) {

	setDates(yearMonth);
	List<Assiduousness> assignedEmployeesAssiduousness = getAssignedEmployeesAssiduousness();

	List<AssignedEmployeeAnualInfo> assignedEmployeeInfoList = new ArrayList<AssignedEmployeeAnualInfo>();
	for (Assiduousness assiduousness : assignedEmployeesAssiduousness) {
	    AssignedEmployeeAnualInfo assignedEmployeeAnualInfo = new AssignedEmployeeAnualInfo(
		    assiduousness.getEmployee(), yearMonth);
	    fillIn(assignedEmployeeAnualInfo, assiduousness.getLeaves(beginDate, endDate));
	    for (int iter = yearMonth.getNumberOfMonth() - 1; iter > 0; iter--) {
		updateCurrentDates(assignedEmployeeAnualInfo, iter);
		fillIn(assignedEmployeeAnualInfo, assiduousness.getLeaves(beginDate, endDate));
	    }

	    AssiduousnessVacations assiduousnessVacations = assiduousness
		    .getAssiduousnessVacationsByYear(assignedEmployeeAnualInfo.getCurrentYearMonth()
			    .getYear());
	    if (assiduousnessVacations != null) {
		assignedEmployeeAnualInfo.setTotalVacations(assiduousnessVacations.getTotalDays());
	    }
	    setDates(yearMonth);
	    assignedEmployeeInfoList.add(assignedEmployeeAnualInfo);
	}
	return assignedEmployeeInfoList;
    }

    private void setDates(YearMonth yearMonth) {
	beginDate = new YearMonthDay(yearMonth.getYear(), yearMonth.getMonth().ordinal() + 1, 01);
	int endDay = beginDate.dayOfMonth().getMaximumValue();
	endDate = new YearMonthDay(yearMonth.getYear(), yearMonth.getMonth().ordinal() + 1, endDay);
    }

    private void updateCurrentDates(AssignedEmployeeAnualInfo assignedEmployeeAnualInfo, int iter) {
	assignedEmployeeAnualInfo.decreaseCurrentMonth();
	beginDate = new YearMonthDay(assignedEmployeeAnualInfo.getCurrentYearMonth().getYear(),
		assignedEmployeeAnualInfo.getCurrentYearMonth().getMonth().ordinal() + 1, 01);
	int endDay = beginDate.dayOfMonth().getMaximumValue();
	endDate = new YearMonthDay(assignedEmployeeAnualInfo.getCurrentYearMonth().getYear(),
		assignedEmployeeAnualInfo.getCurrentYearMonth().getMonth().ordinal() + 1, endDay);
    }

    private void fillIn(AssignedEmployeeAnualInfo assignedEmployeeAnualInfo, List<Leave> leaves) {
	AssignedEmployeeMonthInfo assignedEmployeeInfo = assignedEmployeeAnualInfo.getCurrentMonthInfo();
	//categoria - não temos
	AssiduousnessClosedMonth assiduousnessClosedMonth = getAssiduousnessClosedMonth(assignedEmployeeAnualInfo
		.getEmployee().getAssiduousness());
	AssiduousnessClosedMonth previousAssiduousnessClosedMonth = assiduousnessClosedMonth
		.getPreviousAssiduousnessClosedMonth();

	setUnjustifiedMiss(assignedEmployeeInfo, assiduousnessClosedMonth,
		previousAssiduousnessClosedMonth);
	setMedicalLeaves(assignedEmployeeInfo, leaves);

	setArticle66(assignedEmployeeInfo, assiduousnessClosedMonth, previousAssiduousnessClosedMonth);
	assignedEmployeeInfo.setBereavementLeave(countLeaveNumberOfDays(leaves, "NOJO"));
	assignedEmployeeInfo.setMarriageLeave(countLeaveNumberOfDays(leaves, "LPC"));
	assignedEmployeeInfo.setChildbirthLeave(countLeaveNumberOfDays(leaves, "LP"));
	assignedEmployeeInfo.setLeaveWithoutPayment(countLeaveNumberOfDays(leaves, "LS/V"));
	assignedEmployeeInfo.setPointTolerance(countLeaveNumberOfWorkDays(leaves, "TOL"));
	assignedEmployeeInfo.setArticle17(countLeaveNumberOfDays(leaves, "A17"));
	setAcquiredVacations(assignedEmployeeInfo);
	assignedEmployeeInfo.setUsedExtraWorkVacations(countLeaveNumberOfDays(leaves, "FHE"));
	//TODO not supported yet, dispensas adquiridas no mes por work extra
	setAcquiredDismissal(assignedEmployeeAnualInfo.getEmployee().getAssiduousness());
	assignedEmployeeInfo.setUsedDismissal(countLeaveNumberOfDays(leaves, "DHE"));
	setVacationsInWorkDays(assignedEmployeeInfo, leaves);
	assignedEmployeeInfo.setBereavementLeaveWorkDays(countLeaveNumberOfWorkDays(leaves, "NOJO"));
	assignedEmployeeInfo.setMarriageInWorkDays(countLeaveNumberOfWorkDays(leaves, "LPC"));
	assignedEmployeeInfo.setChildbirthInWorkDays(countLeaveNumberOfWorkDays(leaves, "LP"));
	assignedEmployeeInfo
		.setLeaveWithoutPaymentInWorkDays(countLeaveNumberOfWorkDays(leaves, "LS/V"));
	setArticle52(assignedEmployeeInfo, assiduousnessClosedMonth);
	setChildClinicTreatment(assignedEmployeeInfo, assiduousnessClosedMonth);
	setRelativeClinicTreatment(assignedEmployeeInfo, assiduousnessClosedMonth);
	assignedEmployeeInfo.setAFCT(countLeaveNumberOfDays(leaves, "AFCT"));
	assignedEmployeeInfo.setInfectumContagious(countLeaveNumberOfDays(leaves, "IFC"));
	assignedEmployeeInfo.setAccidentInServiceInLocal(countLeaveNumberOfDays(leaves, "ACINLO"));
	assignedEmployeeInfo.setAccidentInServiceInIter(countLeaveNumberOfDays(leaves, "ACINTE"));
	assignedEmployeeInfo.setBloodDonation(countLeaveNumberOfDays(leaves, "DSANG"));
	assignedEmployeeInfo.setChildbirthOrPaternity(countLeaveNumberOfDays(leaves, "PATERNID"));
	assignedEmployeeInfo.setWorkStudentExamEve(countLeaveNumberOfDays(leaves, "TEVESP"));
	assignedEmployeeInfo.setWorkStudentExamDay(countLeaveNumberOfDays(leaves, "TRAEST"));
	assignedEmployeeInfo.setFormationCoursesNotAuthorized(countLeaveNumberOfDays(leaves, "CURSO"));
	assignedEmployeeInfo.setFormationCoursesAuthorized(countLeaveNumberOfDays(leaves, "CURCD"));
	assignedEmployeeInfo.setPrison(countLeaveNumberOfDays(leaves, "PRISÃO"));
	assignedEmployeeInfo.setMissForFulfilmentOfObligation(countLeaveNumberOfDays(leaves, "A62"));
	assignedEmployeeInfo.setUnionActivity(countLeaveNumberOfDays(leaves, "A.SINDIC"));
	assignedEmployeeInfo.setMissWithLostOfIncome(countLeaveNumberOfDays(leaves, "A68"));

	Integer strikeDays = countLeaveNumberOfDays(leaves, "GREVE");
	if (strikeDays == null) {
	    strikeDays = 0;
	}
	Double totalStrike = countLeaveNumberOfHalfDays(leaves, "1/2GREVE") + strikeDays;
	assignedEmployeeInfo.setStrike(totalStrike != 0 ? totalStrike : null);
    }

    private void setArticle52(AssignedEmployeeMonthInfo assignedEmployeeInfo,
	    AssiduousnessClosedMonth assiduousnessClosedMonth) {
	double clinicTreatmentPercentage = getClinicTreatmentPercentage(assiduousnessClosedMonth,
		"T.AMB");
	double medicExams = getClinicTreatmentPercentage(assiduousnessClosedMonth, "IDMED");
	BigDecimal totalPercentage = new BigDecimal(clinicTreatmentPercentage).add(new BigDecimal(
		medicExams));
	if (totalPercentage.doubleValue() != 0.0) {
	    assignedEmployeeInfo.setArticle52(NumberUtils.formatDoubleWithoutRound(totalPercentage
		    .doubleValue(), 1));
	}
    }

    private void setChildClinicTreatment(AssignedEmployeeMonthInfo assignedEmployeeInfo,
	    AssiduousnessClosedMonth assiduousnessClosedMonth) {
	double percentage = getClinicTreatmentPercentage(assiduousnessClosedMonth, "TAMBFM");
	if (percentage != 0.0) {
	    assignedEmployeeInfo.setChildClinicMedicalTreatment(NumberUtils.formatDoubleWithoutRound(
		    percentage, 1));
	}
    }

    private void setRelativeClinicTreatment(AssignedEmployeeMonthInfo assignedEmployeeInfo,
	    AssiduousnessClosedMonth assiduousnessClosedMonth) {
	double percentage = getClinicTreatmentPercentage(assiduousnessClosedMonth, "TAMBF");
	if (percentage != 0.0) {
	    assignedEmployeeInfo.setRelativeClinicMedicalTreatment(NumberUtils.formatDoubleWithoutRound(
		    percentage, 1));
	}
    }

    private double getClinicTreatmentPercentage(AssiduousnessClosedMonth assiduousnessClosedMonth,
	    String justificationAcronym) {
	Duration duration = Duration.ZERO;
	for (ClosedMonthJustification closedMonthJustification : assiduousnessClosedMonth
		.getClosedMonthJustifications()) {
	    if (closedMonthJustification.getJustificationMotive().getAcronym().equalsIgnoreCase(
		    justificationAcronym)) {
		duration = duration.plus(closedMonthJustification.getJustificationDuration());
	    }
	}
	double percentage = 0.0;
	if (!duration.equals(Duration.ZERO)) {
	    percentage = (double) duration.getMillis()
		    / (double) assiduousnessClosedMonth.getAssiduousness().getAverageWorkTimeDuration(
			    beginDate, endDate).getMillis();
	}
	return percentage;
    }

    private void setVacationsInWorkDays(AssignedEmployeeMonthInfo assignedEmployeeInfo,
	    List<Leave> leaves) {
	Interval dateInterval = new Interval(beginDate.toDateTimeAtMidnight(), endDate
		.toDateTimeAtMidnight());
	double counter = 0;
	for (Leave leave : leaves) {
	    if (leave.getJustificationMotive().getJustificationGroup() != null
		    && (leave.getJustificationMotive().getJustificationGroup().equals(
			    JustificationGroup.CURRENT_YEAR_HOLIDAYS)
			    || leave.getJustificationMotive().getJustificationGroup().equals(
				    JustificationGroup.LAST_YEAR_HOLIDAYS) || leave
			    .getJustificationMotive().getJustificationGroup().equals(
				    JustificationGroup.NEXT_YEAR_HOLIDAYS))) {
		if (!leave.getJustificationMotive().getAcronym().equalsIgnoreCase("FA17")
			&& !leave.getJustificationMotive().getAcronym().startsWith("1/2")) {
		    counter += leave.getWorkDaysBetween(dateInterval);
		} else if (leave.getJustificationMotive().getAcronym().startsWith("1/2")) {
		    counter += 0.5;
		}
	    }
	}
	assignedEmployeeInfo.setVacationsInWorkDays(counter != 0 ? counter : null);
    }

    private void setAcquiredDismissal(Assiduousness assiduousness) {
	//TODO not supported yet

    }

    private void setAcquiredVacations(AssignedEmployeeMonthInfo assignedEmployeeInfo) {
	int counter = 0;
	for (ExtraWorkRequest extraWorkRequest : assignedEmployeeInfo.getAssignedEmployeeAnualInfo()
		.getEmployee().getAssiduousness().getExtraWorkRequests(beginDate)) {
	    counter += extraWorkRequest.getNormalVacationsDays()
		    + extraWorkRequest.getNightVacationsDays();
	}
	assignedEmployeeInfo.setAcquiredExtraWorkVacations(counter != 0 ? counter : null);
    }

    private void setMedicalLeaves(AssignedEmployeeMonthInfo assignedEmployeeInfo, List<Leave> leaves) {
	int sonMedicalLeave = 0;
	int relativeMedicalLeave = 0;
	int deficiencyLeave = 0;
	int internmentMedicalLeave = 0;
	int sonInternmentMedicalLeave = 0;
	int medicalGroupCheckup = 0;
	int medicalLeave = 0;
	int incapacitantLongEalness = 0;
	int checkupIncapacitantLongEalness = 0;

	int totalMedicalLeaveWorkDays = 0;
	Interval dateInterval = new Interval(beginDate.toDateTimeAtMidnight(), endDate
		.toDateTimeAtMidnight());
	for (Leave leave : leaves) {
	    if (leave.getJustificationMotive().getAcronym().equalsIgnoreCase("AMFM")) {
		sonMedicalLeave += countLeaveNumberOfDays(sonMedicalLeave, leave);
		totalMedicalLeaveWorkDays += leave.getWorkDaysBetween(dateInterval);
	    } else if (leave.getJustificationMotive().getAcronym().equalsIgnoreCase("AMF")) {
		relativeMedicalLeave += countLeaveNumberOfDays(relativeMedicalLeave, leave);
		totalMedicalLeaveWorkDays += leave.getWorkDaysBetween(dateInterval);
	    } else if (leave.getJustificationMotive().getAcronym().equalsIgnoreCase("AMD")) {
		deficiencyLeave += countLeaveNumberOfDays(deficiencyLeave, leave);
		totalMedicalLeaveWorkDays += leave.getWorkDaysBetween(dateInterval);
	    } else if (leave.getJustificationMotive().getAcronym().equalsIgnoreCase("AMI")) {
		internmentMedicalLeave += countLeaveNumberOfDays(internmentMedicalLeave, leave);
		totalMedicalLeaveWorkDays += leave.getWorkDaysBetween(dateInterval);
	    } else if (leave.getJustificationMotive().getAcronym().equalsIgnoreCase("AMIFM")) {
		sonInternmentMedicalLeave += countLeaveNumberOfDays(sonInternmentMedicalLeave, leave);
		totalMedicalLeaveWorkDays += leave.getWorkDaysBetween(dateInterval);
	    } else if (leave.getJustificationMotive().getAcronym().equalsIgnoreCase("J.MEDICA")) {
		medicalGroupCheckup += countLeaveNumberOfDays(medicalGroupCheckup, leave);
		totalMedicalLeaveWorkDays += leave.getWorkDaysBetween(dateInterval);
	    } else if (leave.getJustificationMotive().getAcronym().equalsIgnoreCase("FAM")) {
		medicalLeave += countLeaveNumberOfDays(medicalLeave, leave);
		totalMedicalLeaveWorkDays += leave.getWorkDaysBetween(dateInterval);
	    } else if (leave.getJustificationMotive().getAcronym().equalsIgnoreCase("DOINC")) {
		incapacitantLongEalness += countLeaveNumberOfDays(incapacitantLongEalness, leave);
		totalMedicalLeaveWorkDays += leave.getWorkDaysBetween(dateInterval);
	    } else if (leave.getJustificationMotive().getAcronym().equalsIgnoreCase("J.M.-DOINC")) {
		checkupIncapacitantLongEalness += countLeaveNumberOfDays(checkupIncapacitantLongEalness,
			leave);
		totalMedicalLeaveWorkDays += leave.getWorkDaysBetween(dateInterval);
	    }
	}

	assignedEmployeeInfo.setSonMedicalLeave(sonMedicalLeave != 0 ? sonMedicalLeave : null);
	assignedEmployeeInfo.setRelativeMedicalLeave(relativeMedicalLeave != 0 ? relativeMedicalLeave
		: null);
	assignedEmployeeInfo.setDeficiencyLeave(deficiencyLeave != 0 ? deficiencyLeave : null);
	assignedEmployeeInfo
		.setInternmentMedicalLeave(internmentMedicalLeave != 0 ? internmentMedicalLeave : null);
	assignedEmployeeInfo
		.setSonInternmentMedicalLeave(sonInternmentMedicalLeave != 0 ? sonInternmentMedicalLeave
			: null);
	assignedEmployeeInfo.setMedicalGroupCheckup(medicalGroupCheckup != 0 ? medicalGroupCheckup
		: null);

	int totalMedicalLeave = sonMedicalLeave + relativeMedicalLeave + deficiencyLeave
		+ internmentMedicalLeave + sonInternmentMedicalLeave + medicalGroupCheckup
		+ medicalLeave + incapacitantLongEalness + checkupIncapacitantLongEalness;
	assignedEmployeeInfo.setTotalMedicalLeave(totalMedicalLeave != 0 ? totalMedicalLeave : null);
	assignedEmployeeInfo
		.setMedicalLeaveInWorkDays(totalMedicalLeaveWorkDays != 0 ? totalMedicalLeaveWorkDays
			: null);

    }

    private void setUnjustifiedMiss(AssignedEmployeeMonthInfo assignedEmployeeInfo,
	    AssiduousnessClosedMonth assiduousnessClosedMonth,
	    AssiduousnessClosedMonth previousAssiduousnessClosedMonth) {
	BigDecimal unjustified = BigDecimal
		.valueOf(assiduousnessClosedMonth.getAccumulatedUnjustified() != null ? assiduousnessClosedMonth
			.getAccumulatedUnjustified()
			: 0.0);
	unjustified = unjustified
		.add(BigDecimal
			.valueOf(assiduousnessClosedMonth.getUnjustifiedDays() != null ? (double) assiduousnessClosedMonth
				.getUnjustifiedDays()
				: 0.0));
	if (previousAssiduousnessClosedMonth != null) {
	    unjustified = unjustified.subtract(BigDecimal.valueOf(previousAssiduousnessClosedMonth
		    .getAccumulatedUnjustified() != null ? previousAssiduousnessClosedMonth
		    .getAccumulatedUnjustified() : 0.0));
	}
	assignedEmployeeInfo.setUnjustifiedMiss(unjustified.doubleValue() != 0.0 ? unjustified
		.doubleValue() : null);
    }

    private void setArticle66(AssignedEmployeeMonthInfo assignedEmployeeInfo,
	    AssiduousnessClosedMonth assiduousnessClosedMonth,
	    AssiduousnessClosedMonth previousAssiduousnessClosedMonth) {
	BigDecimal a66 = BigDecimal
		.valueOf(assiduousnessClosedMonth.getAccumulatedArticle66() != null ? assiduousnessClosedMonth
			.getAccumulatedArticle66()
			: 0.0);
	if (previousAssiduousnessClosedMonth != null) {
	    a66 = a66.subtract(BigDecimal.valueOf(previousAssiduousnessClosedMonth
		    .getAccumulatedArticle66() != null ? previousAssiduousnessClosedMonth
		    .getAccumulatedArticle66() : 0.0));
	}
	assignedEmployeeInfo.setArticle66(a66.doubleValue() != 0.0 ? a66.doubleValue() : null);
    }

    private AssiduousnessClosedMonth getAssiduousnessClosedMonth(Assiduousness assiduousness) {
	for (AssiduousnessClosedMonth assiduousnessClosedMonth : assiduousness
		.getAssiduousnessClosedMonths()) {
	    if (assiduousnessClosedMonth.getClosedMonth().getClosedYearMonth().get(
		    DateTimeFieldType.monthOfYear()) == beginDate.getMonthOfYear()) {
		return assiduousnessClosedMonth;
	    }
	}
	return null;
    }

    private Double countLeaveNumberOfHalfDays(List<Leave> leaves, String justificationAcronym) {
	double counter = 0;
	for (Leave leave : leaves) {
	    if (leave.getJustificationMotive().getAcronym().equalsIgnoreCase(justificationAcronym)) {
		counter += 0.5;
	    }
	}
	return counter;
    }

    private Integer countLeaveNumberOfDays(List<Leave> leaves, String justificationAcronym) {
	int counter = 0;
	for (Leave leave : leaves) {
	    if (leave.getJustificationMotive().getAcronym().equalsIgnoreCase(justificationAcronym)) {
		counter += countLeaveNumberOfDays(counter, leave);
	    }
	}
	return counter != 0 ? counter : null;
    }

    private int countLeaveNumberOfDays(int counter, Leave leave) {
	DateTime beginDateInPeriod = leave.getDate().isBefore(beginDate.toDateTimeAtMidnight()) ? beginDate
		.toDateTimeAtMidnight()
		: leave.getDate();
	DateTime endDateInPeriod = leave.getEndDate().isAfter(endDate.toDateTimeAtMidnight()) ? endDate
		.toDateTimeAtMidnight() : leave.getEndDate();
	return Days.daysBetween(beginDateInPeriod, endDateInPeriod.plusDays(1)).getDays();
    }

    private Integer countLeaveNumberOfWorkDays(List<Leave> leaves, String justificationAcronym) {
	int countWorkDays = 0;
	Interval dateInterval = new Interval(beginDate.toDateTimeAtMidnight(), endDate
		.toDateTimeAtMidnight());
	for (Leave leave : leaves) {
	    if (leave.getJustificationMotive().getAcronym().equalsIgnoreCase(justificationAcronym)) {
		countWorkDays += leave.getWorkDaysBetween(dateInterval);
	    }
	}
	return countWorkDays != 0 ? countWorkDays : null;
    }

    private List<Assiduousness> getAssignedEmployeesAssiduousness() {
	List<Assiduousness> assignedEmployeesAssiduousness = new ArrayList<Assiduousness>();
	for (Assiduousness assiduousness : RootDomainObject.getInstance().getAssiduousnesss()) {
	    if (isAssigned(assiduousness.getAssiduousnessStatusHistories())) {
		assignedEmployeesAssiduousness.add(assiduousness);
	    }
	}
	return assignedEmployeesAssiduousness;
    }

    private boolean isAssigned(List<AssiduousnessStatusHistory> assiduousnessStatusHistories) {
	for (AssiduousnessStatusHistory assiduousnessStatusHistory : assiduousnessStatusHistories) {
	    if (assiduousnessStatusHistory.getEndDate() != null) {
		Interval statusInterval = new Interval(assiduousnessStatusHistory.getBeginDate()
			.toDateMidnight(), assiduousnessStatusHistory.getEndDate().toDateMidnight()
			.plusDays(1));
		Interval interval = new Interval(beginDate.toDateMidnight(), endDate.toDateMidnight()
			.plusDays(1));
		if (interval.overlaps(statusInterval)
			&& assiduousnessStatusHistory.getAssiduousnessStatus().getState() == AssiduousnessState.ACTIVE
			&& assiduousnessStatusHistory.getAssiduousnessStatus().getDescription()
				.equalsIgnoreCase("Destacado no IST")) {
		    return true;
		}
	    } else {
		if ((assiduousnessStatusHistory.getBeginDate().isBefore(endDate) || assiduousnessStatusHistory
			.getBeginDate().isEqual(endDate))
			&& assiduousnessStatusHistory.getAssiduousnessStatus().getState() == AssiduousnessState.ACTIVE
			&& assiduousnessStatusHistory.getAssiduousnessStatus().getDescription()
				.equalsIgnoreCase("Destacado no IST")) {
		    return true;
		}
	    }
	}
	return false;
    }
}