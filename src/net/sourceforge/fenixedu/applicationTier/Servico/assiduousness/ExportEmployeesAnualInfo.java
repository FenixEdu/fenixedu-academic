package net.sourceforge.fenixedu.applicationTier.Servico.assiduousness;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.EmployeeAnualInfo;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.EmployeeMonthInfo;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.YearMonth;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.assiduousness.Assiduousness;
import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessClosedMonth;
import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessStatus;
import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessStatusHistory;
import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessVacations;
import net.sourceforge.fenixedu.domain.assiduousness.ClosedMonth;
import net.sourceforge.fenixedu.domain.assiduousness.ClosedMonthJustification;
import net.sourceforge.fenixedu.domain.assiduousness.ExtraWorkRequest;
import net.sourceforge.fenixedu.domain.assiduousness.JustificationMotive;
import net.sourceforge.fenixedu.domain.assiduousness.Leave;
import net.sourceforge.fenixedu.domain.assiduousness.util.AssiduousnessState;
import net.sourceforge.fenixedu.domain.assiduousness.util.JustificationGroup;
import net.sourceforge.fenixedu.util.NumberUtils;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Duration;
import org.joda.time.Interval;
import org.joda.time.LocalDate;

public class ExportEmployeesAnualInfo extends Service {

    public List<EmployeeAnualInfo> run(YearMonth yearMonth, AssiduousnessStatus assiduousnessStatus) {

	ClosedMonth closedMonth;
	List<Assiduousness> employeesAssiduousness = getEmployeesAssiduousnessByStatus(assiduousnessStatus, yearMonth);

	List<EmployeeAnualInfo> employeeAnualInfoList = new ArrayList<EmployeeAnualInfo>();
	for (Assiduousness assiduousness : employeesAssiduousness) {
	    YearMonth thisYearMonth = new YearMonth(yearMonth.getYear(), yearMonth.getMonth());
	    EmployeeAnualInfo employeeAnualInfo = new EmployeeAnualInfo(assiduousness.getEmployee(), yearMonth);
	    for (; thisYearMonth.getYear().intValue() == yearMonth.getYear(); thisYearMonth.subtractMonth()) {
		closedMonth = ClosedMonth.getClosedMonth(thisYearMonth);
		employeeAnualInfo.setCurrentYearMonth(new YearMonth(thisYearMonth.getYear(), thisYearMonth.getMonth()));
		LocalDate beginDate = new LocalDate(thisYearMonth.getYear(), thisYearMonth.getNumberOfMonth(), 01);
		int endDay = beginDate.dayOfMonth().getMaximumValue();
		LocalDate endDate = new LocalDate(thisYearMonth.getYear(), thisYearMonth.getNumberOfMonth(), endDay);
		fillIn(closedMonth, employeeAnualInfo, assiduousnessStatus, beginDate, endDate);
	    }
	    AssiduousnessVacations assiduousnessVacations = assiduousness.getAssiduousnessVacationsByYear(employeeAnualInfo
		    .getCurrentYearMonth().getYear());
	    if (assiduousnessVacations != null) {
		employeeAnualInfo.setTotalVacations(assiduousnessVacations.getTotalDays());
	    }
	    employeeAnualInfoList.add(employeeAnualInfo);
	}
	return employeeAnualInfoList;
    }

    private void fillIn(ClosedMonth closedMonth, EmployeeAnualInfo employeeAnualInfo, AssiduousnessStatus assiduousnessStatus,
	    LocalDate beginDate, LocalDate endDate) {
	List<Leave> leaves = employeeAnualInfo.getEmployee().getAssiduousness().getLeaves(beginDate, endDate);
	EmployeeMonthInfo employeeMonthInfo = employeeAnualInfo.getCurrentMonthInfo();
	// categoria - não temos
	AssiduousnessClosedMonth assiduousnessClosedMonth = getAssiduousnessClosedMonth(closedMonth, employeeAnualInfo
		.getEmployee().getAssiduousness());
	if (assiduousnessClosedMonth != null
		&& isActiveAndHasStatus(assiduousnessClosedMonth.getAssiduousnessStatusHistory(), assiduousnessStatus, beginDate,
			endDate)) {
	    AssiduousnessClosedMonth previousAssiduousnessClosedMonth = assiduousnessClosedMonth
		    .getPreviousAssiduousnessClosedMonth();

	    setUnjustifiedMiss(employeeMonthInfo, assiduousnessClosedMonth, previousAssiduousnessClosedMonth);
	    setMedicalLeaves(employeeMonthInfo, leaves, beginDate, endDate);

	    setArticle66(employeeMonthInfo, assiduousnessClosedMonth, previousAssiduousnessClosedMonth);
	    employeeMonthInfo.setBereavementLeave(countLeaveNumberOfDays(leaves, "NOJO", beginDate, endDate));
	    employeeMonthInfo.setMarriageLeave(countLeaveNumberOfDays(leaves, "LPC", beginDate, endDate));
	    employeeMonthInfo.setChildbirthLeave(countLeaveNumberOfDays(leaves, "LP", beginDate, endDate));
	    employeeMonthInfo.setLeaveWithoutPayment(countLeaveNumberOfDays(leaves, "LS/V", beginDate, endDate));
	    List<JustificationMotive> justificationMotives = JustificationMotive
		    .getJustificationMotivesByGroup(JustificationGroup.TOLERANCES);
	    employeeMonthInfo.setPointTolerance(getJustificationWorkingDays(leaves, beginDate, endDate, justificationMotives));
	    employeeMonthInfo.setArticle17(getJustificationDays(leaves, beginDate, endDate, "A17", "A1306"));
	    setAcquiredVacations(employeeMonthInfo, beginDate);
	    employeeMonthInfo.setUsedVacations(getJustificationWorkingDays(leaves, beginDate, endDate, "FER", "F1306", "FA42"));
	    employeeMonthInfo.setUsedTransferedVacations(getJustificationWorkingDays(leaves, beginDate, endDate, "FTRANS",
		    "FT1306", "FA42T"));
	    employeeMonthInfo.setUsedPastHourVacations(countLeaveNumberOfWorkDays(leaves, "FHEA", beginDate, endDate));
	    employeeMonthInfo.setUsedA17Vacations(getJustificationWorkingDays(leaves, beginDate, endDate, "FA17", "FA1306"));
	    employeeMonthInfo.setUsedLowSeasonVacations(countLeaveNumberOfWorkDays(leaves, "FEB", beginDate, endDate));
	    employeeMonthInfo.setUsedExtraWorkVacations(getJustificationWorkingDays(leaves, beginDate, endDate, "FHE", "FH1306"));
	    employeeMonthInfo.setUsedHalfDaysVacations(getJustificationWorkingDays(leaves, beginDate, endDate, "1/2 FÉRIAS",
		    "1/2 FER"));

	    // TODO not supported yet, dispensas adquiridas no mes por work
	    // extra
	    setAcquiredDismissal(employeeAnualInfo.getEmployee().getAssiduousness());
	    employeeMonthInfo.setUsedDismissal(countLeaveNumberOfDays(leaves, "DHE", beginDate, endDate));
	    setVacationsInWorkDays(employeeMonthInfo, leaves, beginDate, endDate);
	    employeeMonthInfo.setBereavementLeaveWorkDays(countLeaveNumberOfWorkDays(leaves, "NOJO", beginDate, endDate));
	    employeeMonthInfo.setMarriageInWorkDays(countLeaveNumberOfWorkDays(leaves, "LPC", beginDate, endDate));
	    employeeMonthInfo.setChildbirthInWorkDays(countLeaveNumberOfWorkDays(leaves, "LP", beginDate, endDate));
	    employeeMonthInfo.setLeaveWithoutPaymentInWorkDays(countLeaveNumberOfWorkDays(leaves, "LS/V", beginDate, endDate));
	    setArticle52(employeeMonthInfo, assiduousnessClosedMonth, beginDate, endDate);
	    setChildClinicTreatment(employeeMonthInfo, assiduousnessClosedMonth, beginDate, endDate);
	    setRelativeClinicTreatment(employeeMonthInfo, assiduousnessClosedMonth, beginDate, endDate);
	    employeeMonthInfo.setAFCT(countLeaveNumberOfDays(leaves, "AFCT", beginDate, endDate));
	    employeeMonthInfo.setInfectumContagious(countLeaveNumberOfDays(leaves, "IFC", beginDate, endDate));
	    employeeMonthInfo.setAccidentInServiceInLocal(countLeaveNumberOfDays(leaves, "ACINLO", beginDate, endDate));
	    employeeMonthInfo.setAccidentInServiceInIter(countLeaveNumberOfDays(leaves, "ACINTE", beginDate, endDate));
	    employeeMonthInfo.setBloodDonation(countLeaveNumberOfDays(leaves, "DSANG", beginDate, endDate));
	    employeeMonthInfo.setChildbirthOrPaternity(countLeaveNumberOfDays(leaves, "PATERNID", beginDate, endDate));
	    employeeMonthInfo.setWorkStudentExamEve(countLeaveNumberOfDays(leaves, "TEVESP", beginDate, endDate));
	    employeeMonthInfo.setWorkStudentExamDay(countLeaveNumberOfDays(leaves, "TRAEST", beginDate, endDate));
	    employeeMonthInfo.setFormationCoursesNotAuthorized(countLeaveNumberOfDays(leaves, "CURSO", beginDate, endDate));
	    employeeMonthInfo.setFormationCoursesAuthorized(countLeaveNumberOfDays(leaves, "CURCD", beginDate, endDate));
	    employeeMonthInfo.setPrison(countLeaveNumberOfDays(leaves, "PRISÃO", beginDate, endDate));
	    employeeMonthInfo.setMissForFulfilmentOfObligation(countLeaveNumberOfDays(leaves, "A62", beginDate, endDate));
	    employeeMonthInfo.setUnionActivity(countLeaveNumberOfDays(leaves, "A.SINDIC", beginDate, endDate));
	    employeeMonthInfo.setMissWithLostOfIncome(countLeaveNumberOfDays(leaves, "A68", beginDate, endDate));

	    Integer strikeDays = countLeaveNumberOfDays(leaves, "GREVE", beginDate, endDate);
	    if (strikeDays == null) {
		strikeDays = 0;
	    }
	    Double totalStrike = countLeaveNumberOfHalfDays(leaves, "1/2GREVE") + strikeDays;
	    employeeMonthInfo.setStrike(totalStrike != 0 ? totalStrike : null);
	}
    }

    private Integer getJustificationWorkingDays(List<Leave> leaves, LocalDate beginDate, LocalDate endDate,
	    List<JustificationMotive> justificationMotives) {
	int total = 0;
	for (JustificationMotive justificationMotive : justificationMotives) {
	    Integer days = countLeaveNumberOfDays(leaves, justificationMotive.getAcronym(), beginDate, endDate);
	    if (days != null) {
		total += days.intValue();
	    }
	}
	return total == 0 ? null : Integer.valueOf(total);
    }

    private Integer getJustificationDays(List<Leave> leaves, LocalDate beginDate, LocalDate endDate, String... justifications) {
	int total = 0;
	for (String justification : justifications) {
	    Integer days = countLeaveNumberOfDays(leaves, justification, beginDate, endDate);
	    if (days != null) {
		total += days.intValue();
	    }
	}
	return total == 0 ? null : Integer.valueOf(total);
    }

    private Integer getJustificationWorkingDays(List<Leave> leaves, LocalDate beginDate, LocalDate endDate,
	    String... justifications) {
	int total = 0;
	for (String justification : justifications) {
	    Integer days = countLeaveNumberOfWorkDays(leaves, justification, beginDate, endDate);
	    if (days != null) {
		total += days.intValue();
	    }
	}
	return total == 0 ? null : Integer.valueOf(total);
    }

    private void setArticle52(EmployeeMonthInfo employeeMonthInfo, AssiduousnessClosedMonth assiduousnessClosedMonth,
	    LocalDate beginDate, LocalDate endDate) {
	double clinicTreatmentPercentage = getClinicTreatmentPercentage(assiduousnessClosedMonth, "T.AMB", beginDate, endDate);
	double medicExams = getClinicTreatmentPercentage(assiduousnessClosedMonth, "IDMED", beginDate, endDate);
	BigDecimal totalPercentage = new BigDecimal(clinicTreatmentPercentage).add(new BigDecimal(medicExams));
	if (totalPercentage.doubleValue() != 0.0) {
	    employeeMonthInfo.setArticle52(NumberUtils.formatDoubleWithoutRound(totalPercentage.doubleValue(), 1));
	}
    }

    private void setChildClinicTreatment(EmployeeMonthInfo employeeMonthInfo, AssiduousnessClosedMonth assiduousnessClosedMonth,
	    LocalDate beginDate, LocalDate endDate) {
	double percentage = getClinicTreatmentPercentage(assiduousnessClosedMonth, "TAMBFM", beginDate, endDate);
	if (percentage != 0.0) {
	    employeeMonthInfo.setChildClinicMedicalTreatment(NumberUtils.formatDoubleWithoutRound(percentage, 1));
	}
    }

    private void setRelativeClinicTreatment(EmployeeMonthInfo employeeMonthInfo,
	    AssiduousnessClosedMonth assiduousnessClosedMonth, LocalDate beginDate, LocalDate endDate) {
	double percentage = getClinicTreatmentPercentage(assiduousnessClosedMonth, "TAMBF", beginDate, endDate);
	if (percentage != 0.0) {
	    employeeMonthInfo.setRelativeClinicMedicalTreatment(NumberUtils.formatDoubleWithoutRound(percentage, 1));
	}
    }

    private double getClinicTreatmentPercentage(AssiduousnessClosedMonth assiduousnessClosedMonth, String justificationAcronym,
	    LocalDate beginDate, LocalDate endDate) {
	Duration duration = Duration.ZERO;
	for (ClosedMonthJustification closedMonthJustification : assiduousnessClosedMonth.getClosedMonthJustifications()) {
	    if (closedMonthJustification.getJustificationMotive().getAcronym().equalsIgnoreCase(justificationAcronym)) {
		duration = duration.plus(closedMonthJustification.getJustificationDuration());
	    }
	}
	double percentage = 0.0;
	if (!duration.equals(Duration.ZERO)) {
	    percentage = (double) duration.getMillis()
		    / (double) assiduousnessClosedMonth.getAssiduousnessStatusHistory().getAssiduousness()
			    .getAverageWorkTimeDuration(beginDate, endDate).getMillis();
	}
	return percentage;
    }

    private void setVacationsInWorkDays(EmployeeMonthInfo employeeMonthInfo, List<Leave> leaves, LocalDate beginDate,
	    LocalDate endDate) {
	Interval dateInterval = new Interval(beginDate.toDateTimeAtMidnight(), endDate.toDateTimeAtMidnight());
	double counter = 0;
	for (Leave leave : leaves) {
	    if (leave.getJustificationMotive().getJustificationGroup() != null
		    && (leave.getJustificationMotive().getJustificationGroup().equals(JustificationGroup.CURRENT_YEAR_HOLIDAYS)
			    || leave.getJustificationMotive().getJustificationGroup().equals(
				    JustificationGroup.LAST_YEAR_HOLIDAYS) || leave.getJustificationMotive()
			    .getJustificationGroup().equals(JustificationGroup.NEXT_YEAR_HOLIDAYS))) {
		if (!leave.getJustificationMotive().getAcronym().equalsIgnoreCase("FA17")
			&& !leave.getJustificationMotive().getAcronym().startsWith("1/2")) {
		    counter += leave.getWorkDaysBetween(dateInterval);
		} else if (leave.getJustificationMotive().getAcronym().startsWith("1/2")) {
		    counter += 0.5;
		}
	    }
	}
	employeeMonthInfo.setVacationsInWorkDays(counter != 0 ? counter : null);
    }

    private void setAcquiredDismissal(Assiduousness assiduousness) {
	// TODO not supported yet

    }

    private void setAcquiredVacations(EmployeeMonthInfo employeeMonthInfo, LocalDate beginDate) {
	int counter = 0;
	for (ExtraWorkRequest extraWorkRequest : employeeMonthInfo.getEmployeeAnualInfo().getEmployee().getAssiduousness()
		.getExtraWorkRequests(beginDate)) {
	    counter += extraWorkRequest.getNormalVacationsDays() + extraWorkRequest.getNightVacationsDays();
	}
	employeeMonthInfo.setAcquiredExtraWorkVacations(counter != 0 ? counter : null);
    }

    private void setMedicalLeaves(EmployeeMonthInfo employeeMonthInfo, List<Leave> leaves, LocalDate beginDate, LocalDate endDate) {
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
	Interval dateInterval = new Interval(beginDate.toDateTimeAtMidnight(), endDate.toDateTimeAtMidnight());
	for (Leave leave : leaves) {
	    if (leave.getJustificationMotive().getAcronym().equalsIgnoreCase("AMFM")) {
		sonMedicalLeave += countLeaveNumberOfDays(sonMedicalLeave, leave, beginDate, endDate);
		totalMedicalLeaveWorkDays += leave.getWorkDaysBetween(dateInterval);
	    } else if (leave.getJustificationMotive().getAcronym().equalsIgnoreCase("AMF")) {
		relativeMedicalLeave += countLeaveNumberOfDays(relativeMedicalLeave, leave, beginDate, endDate);
		totalMedicalLeaveWorkDays += leave.getWorkDaysBetween(dateInterval);
	    } else if (leave.getJustificationMotive().getAcronym().equalsIgnoreCase("AMD")) {
		deficiencyLeave += countLeaveNumberOfDays(deficiencyLeave, leave, beginDate, endDate);
		totalMedicalLeaveWorkDays += leave.getWorkDaysBetween(dateInterval);
	    } else if (leave.getJustificationMotive().getAcronym().equalsIgnoreCase("AMI")) {
		internmentMedicalLeave += countLeaveNumberOfDays(internmentMedicalLeave, leave, beginDate, endDate);
		totalMedicalLeaveWorkDays += leave.getWorkDaysBetween(dateInterval);
	    } else if (leave.getJustificationMotive().getAcronym().equalsIgnoreCase("AMIFM")) {
		sonInternmentMedicalLeave += countLeaveNumberOfDays(sonInternmentMedicalLeave, leave, beginDate, endDate);
		totalMedicalLeaveWorkDays += leave.getWorkDaysBetween(dateInterval);
	    } else if (leave.getJustificationMotive().getAcronym().equalsIgnoreCase("J.MEDICA")) {
		medicalGroupCheckup += countLeaveNumberOfDays(medicalGroupCheckup, leave, beginDate, endDate);
		totalMedicalLeaveWorkDays += leave.getWorkDaysBetween(dateInterval);
	    } else if (leave.getJustificationMotive().getAcronym().equalsIgnoreCase("FAM")) {
		medicalLeave += countLeaveNumberOfDays(medicalLeave, leave, beginDate, endDate);
		totalMedicalLeaveWorkDays += leave.getWorkDaysBetween(dateInterval);
	    } else if (leave.getJustificationMotive().getAcronym().equalsIgnoreCase("DOINC")) {
		incapacitantLongEalness += countLeaveNumberOfDays(incapacitantLongEalness, leave, beginDate, endDate);
		totalMedicalLeaveWorkDays += leave.getWorkDaysBetween(dateInterval);
	    } else if (leave.getJustificationMotive().getAcronym().equalsIgnoreCase("J.M.-DOINC")) {
		checkupIncapacitantLongEalness += countLeaveNumberOfDays(checkupIncapacitantLongEalness, leave, beginDate,
			endDate);
		totalMedicalLeaveWorkDays += leave.getWorkDaysBetween(dateInterval);
	    }
	}

	employeeMonthInfo.setSonMedicalLeave(sonMedicalLeave != 0 ? sonMedicalLeave : null);
	employeeMonthInfo.setRelativeMedicalLeave(relativeMedicalLeave != 0 ? relativeMedicalLeave : null);
	employeeMonthInfo.setDeficiencyLeave(deficiencyLeave != 0 ? deficiencyLeave : null);
	employeeMonthInfo.setInternmentMedicalLeave(internmentMedicalLeave != 0 ? internmentMedicalLeave : null);
	employeeMonthInfo.setSonInternmentMedicalLeave(sonInternmentMedicalLeave != 0 ? sonInternmentMedicalLeave : null);
	employeeMonthInfo.setMedicalGroupCheckup(medicalGroupCheckup != 0 ? medicalGroupCheckup : null);

	int totalMedicalLeave = sonMedicalLeave + relativeMedicalLeave + deficiencyLeave + internmentMedicalLeave
		+ sonInternmentMedicalLeave + medicalGroupCheckup + medicalLeave + incapacitantLongEalness
		+ checkupIncapacitantLongEalness;
	employeeMonthInfo.setTotalMedicalLeave(totalMedicalLeave != 0 ? totalMedicalLeave : null);
	employeeMonthInfo.setMedicalLeaveInWorkDays(totalMedicalLeaveWorkDays != 0 ? totalMedicalLeaveWorkDays : null);

    }

    private void setUnjustifiedMiss(EmployeeMonthInfo employeeMonthInfo, AssiduousnessClosedMonth assiduousnessClosedMonth,
	    AssiduousnessClosedMonth previousAssiduousnessClosedMonth) {
	BigDecimal unjustified = BigDecimal
		.valueOf(assiduousnessClosedMonth.getAccumulatedUnjustified() != null ? assiduousnessClosedMonth
			.getAccumulatedUnjustified() : 0.0);
	unjustified = unjustified.add(BigDecimal
		.valueOf(assiduousnessClosedMonth.getUnjustifiedDays() != null ? (double) assiduousnessClosedMonth
			.getUnjustifiedDays() : 0.0));
	if (previousAssiduousnessClosedMonth != null) {
	    unjustified = unjustified
		    .subtract(BigDecimal
			    .valueOf(previousAssiduousnessClosedMonth.getAccumulatedUnjustified() != null ? previousAssiduousnessClosedMonth
				    .getAccumulatedUnjustified()
				    : 0.0));
	}
	employeeMonthInfo.setUnjustifiedMiss(unjustified.doubleValue() != 0.0 ? unjustified.doubleValue() : null);
    }

    private void setArticle66(EmployeeMonthInfo employeeMonthInfo, AssiduousnessClosedMonth assiduousnessClosedMonth,
	    AssiduousnessClosedMonth previousAssiduousnessClosedMonth) {
	BigDecimal a66 = BigDecimal.valueOf(assiduousnessClosedMonth.getAccumulatedArticle66() != null ? assiduousnessClosedMonth
		.getAccumulatedArticle66() : 0.0);
	if (previousAssiduousnessClosedMonth != null) {
	    a66 = a66
		    .subtract(BigDecimal
			    .valueOf(previousAssiduousnessClosedMonth.getAccumulatedArticle66() != null ? previousAssiduousnessClosedMonth
				    .getAccumulatedArticle66()
				    : 0.0));
	}
	employeeMonthInfo.setArticle66(a66.doubleValue() != 0.0 ? a66.doubleValue() : null);
    }

    private AssiduousnessClosedMonth getAssiduousnessClosedMonth(ClosedMonth closedMonth, Assiduousness assiduousness) {
	for (AssiduousnessClosedMonth assiduousnessClosedMonth : closedMonth.getAssiduousnessClosedMonths()) {
	    if (assiduousnessClosedMonth.getAssiduousnessStatusHistory().getAssiduousness().equals(assiduousness)) {
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

    private Integer countLeaveNumberOfDays(List<Leave> leaves, String justificationAcronym, LocalDate beginDate, LocalDate endDate) {
	int counter = 0;
	for (Leave leave : leaves) {
	    if (leave.getJustificationMotive().getAcronym().equalsIgnoreCase(justificationAcronym)) {
		counter += countLeaveNumberOfDays(counter, leave, beginDate, endDate);
	    }
	}
	return counter != 0 ? counter : null;
    }

    private int countLeaveNumberOfDays(int counter, Leave leave, LocalDate beginDate, LocalDate endDate) {
	DateTime beginDateInPeriod = leave.getDate().isBefore(beginDate.toDateTimeAtMidnight()) ? beginDate
		.toDateTimeAtMidnight() : leave.getDate();
	DateTime endDateInPeriod = leave.getEndDate().isAfter(endDate.toDateTimeAtMidnight()) ? endDate.toDateTimeAtMidnight()
		: leave.getEndDate();
	return Days.daysBetween(beginDateInPeriod, endDateInPeriod.plusDays(1)).getDays();
    }

    private Integer countLeaveNumberOfWorkDays(List<Leave> leaves, String justificationAcronym, LocalDate beginDate,
	    LocalDate endDate) {
	int countWorkDays = 0;
	Interval dateInterval = new Interval(beginDate.toDateTimeAtMidnight(), endDate.toDateTimeAtMidnight());
	for (Leave leave : leaves) {
	    if (leave.getJustificationMotive().getAcronym().equalsIgnoreCase(justificationAcronym)) {
		countWorkDays += leave.getWorkDaysBetween(dateInterval);
	    }
	}
	return countWorkDays != 0 ? countWorkDays : null;
    }

    private List<Assiduousness> getEmployeesAssiduousnessByStatus(AssiduousnessStatus assiduousnessStatus, YearMonth yearMonth) {
	LocalDate beginDate = new LocalDate(yearMonth.getYear(), yearMonth.getNumberOfMonth(), 01);
	LocalDate endDate = new LocalDate(yearMonth.getYear(), yearMonth.getNumberOfMonth(), beginDate.dayOfMonth()
		.getMaximumValue());
	List<Assiduousness> employeesAssiduousness = new ArrayList<Assiduousness>();
	for (Assiduousness assiduousness : RootDomainObject.getInstance().getAssiduousnesss()) {
	    if (isAnyStatusHistoryActiveAndHasStatus(assiduousnessStatus, assiduousness.getAssiduousnessStatusHistories(),
		    beginDate, endDate)) {
		employeesAssiduousness.add(assiduousness);
	    }
	}
	return employeesAssiduousness;
    }

    private boolean isAnyStatusHistoryActiveAndHasStatus(AssiduousnessStatus assiduousnessStatus,
	    List<AssiduousnessStatusHistory> assiduousnessStatusHistories, LocalDate beginDate, LocalDate endDate) {
	for (AssiduousnessStatusHistory assiduousnessStatusHistory : assiduousnessStatusHistories) {
	    if (isActiveAndHasStatus(assiduousnessStatusHistory, assiduousnessStatus, beginDate, endDate)) {
		return true;
	    }
	}
	return false;
    }

    private boolean isActiveAndHasStatus(AssiduousnessStatusHistory assiduousnessStatusHistory,
	    AssiduousnessStatus assiduousnessStatus, LocalDate beginDate, LocalDate endDate) {
	if (assiduousnessStatusHistory.getEndDate() != null) {
	    Interval statusInterval = new Interval(assiduousnessStatusHistory.getBeginDate().toDateMidnight(),
		    assiduousnessStatusHistory.getEndDate().toDateMidnight().plusDays(1));
	    Interval interval = new Interval(beginDate.toDateMidnight(), endDate.toDateMidnight().plusDays(1));
	    if (interval.overlaps(statusInterval)
		    && assiduousnessStatusHistory.getAssiduousnessStatus().getState() == AssiduousnessState.ACTIVE
		    && assiduousnessStatusHistory.getAssiduousnessStatus().getDescription().equalsIgnoreCase(
			    assiduousnessStatus.getDescription())) {
		return true;
	    }
	} else {
	    if ((assiduousnessStatusHistory.getBeginDate().isBefore(endDate) || assiduousnessStatusHistory.getBeginDate()
		    .isEqual(endDate))
		    && assiduousnessStatusHistory.getAssiduousnessStatus().getState() == AssiduousnessState.ACTIVE
		    && assiduousnessStatusHistory.getAssiduousnessStatus().getDescription().equalsIgnoreCase(
			    assiduousnessStatus.getDescription())) {
		return true;
	    }
	}
	return false;
    }
}