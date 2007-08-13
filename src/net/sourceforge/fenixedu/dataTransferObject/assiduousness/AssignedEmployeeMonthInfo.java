package net.sourceforge.fenixedu.dataTransferObject.assiduousness;

import java.io.Serializable;

public class AssignedEmployeeMonthInfo implements Serializable {

    private AssignedEmployeeAnualInfo assignedEmployeeAnualInfo;

    private YearMonth yearMonth;

    private Double unjustifiedMiss;

    private Integer totalMedicalLeave;

    private Integer sonMedicalLeave;

    private Integer relativeMedicalLeave;

    private Integer deficiencyLeave;

    private Integer internmentMedicalLeave;

    private Integer sonInternmentMedicalLeave;

    private Double article66;

    private Integer bereavementLeave;

    private Integer marriageLeave;

    private Integer childbirthLeave;

    private Integer leaveWithoutPayment;

    private Integer pointTolerance;

    private Integer article17;

    private Integer acquiredExtraWorkVacations;

    private Integer usedExtraWorkVacations;

    private Integer acquiredDismissal;

    private Integer usedDismissal;

    private Double vacationsInWorkDays;

    private Integer medicalLeaveInWorkDays;

    private Integer bereavementLeaveInWorkDays;

    private Integer marriageInWorkDays;

    private Integer childbirthInWorkDays;

    private Integer leaveWithoutPaymentInWorkDays;

    private Double article52;

    private Double relativeClinicMedicalTreatment;

    private Double childClinicMedicalTreatment;

    private Integer medicalGroupCheckup;

    private Integer AFCT;

    private Integer infectumContagious;

    private Integer accidentInServiceInLocal;

    private Integer accidentInServiceInIter;

    private Integer bloodDonation;

    private Integer childbirthOrPaternity;

    private Integer workStudentExamEve;

    private Integer workStudentExamDay;

    private Integer formationCoursesNotAuthorized;

    private Integer formationCoursesAuthorized;

    private Integer prison;

    private Integer missForFulfilmentOfObligation;

    private Integer unionActivity;

    private Integer missWithLostOfIncome;

    private Double strike;

    public AssignedEmployeeMonthInfo(AssignedEmployeeAnualInfo assignedEmployeeAnualInfo,
	    YearMonth yearMonth) {
	setAssignedEmployeeAnualInfo(assignedEmployeeAnualInfo);
	setYearMonth(yearMonth);
    }

    public Integer getAccidentInServiceInIter() {
	return accidentInServiceInIter;
    }

    public void setAccidentInServiceInIter(Integer accidentInServiceInIter) {
	this.accidentInServiceInIter = accidentInServiceInIter;
    }

    public Integer getAccidentInServiceInLocal() {
	return accidentInServiceInLocal;
    }

    public void setAccidentInServiceInLocal(Integer accidentInServiceInLocal) {
	this.accidentInServiceInLocal = accidentInServiceInLocal;
    }

    public Integer getAcquiredExtraWorkVacations() {
	return acquiredExtraWorkVacations;
    }

    public void setAcquiredExtraWorkVacations(Integer acquiredExtraWorkVacations) {
	this.acquiredExtraWorkVacations = acquiredExtraWorkVacations;
    }

    public Integer getAcquiredDismissal() {
	return acquiredDismissal;
    }

    public void setAcquiredDismissal(Integer acquiredDismissal) {
	this.acquiredDismissal = acquiredDismissal;
    }

    public Integer getAFCT() {
	return AFCT;
    }

    public void setAFCT(Integer afct) {
	AFCT = afct;
    }

    public Integer getArticle17() {
	return article17;
    }

    public void setArticle17(Integer article17) {
	this.article17 = article17;
    }

    public Double getArticle52() {
	return article52;
    }

    public void setArticle52(Double article52) {
	this.article52 = article52;
    }

    public Double getArticle66() {
	return article66;
    }

    public void setArticle66(Double article66) {
	this.article66 = article66;
    }

    public Integer getBloodDonation() {
	return bloodDonation;
    }

    public void setBloodDonation(Integer bloodDonation) {
	this.bloodDonation = bloodDonation;
    }

    public Integer getChildbirthInWorkDays() {
	return childbirthInWorkDays;
    }

    public void setChildbirthInWorkDays(Integer childbirthInWorkDays) {
	this.childbirthInWorkDays = childbirthInWorkDays;
    }

    public Integer getChildbirthLeave() {
	return childbirthLeave;
    }

    public void setChildbirthLeave(Integer childbirthLeave) {
	this.childbirthLeave = childbirthLeave;
    }

    public Integer getChildbirthOrPaternity() {
	return childbirthOrPaternity;
    }

    public void setChildbirthOrPaternity(Integer childbirthOrPaternity) {
	this.childbirthOrPaternity = childbirthOrPaternity;
    }

    public Integer getDeficiencyLeave() {
	return deficiencyLeave;
    }

    public void setDeficiencyLeave(Integer deficiencyLeave) {
	this.deficiencyLeave = deficiencyLeave;
    }

    public Integer getFormationCoursesAuthorized() {
	return formationCoursesAuthorized;
    }

    public void setFormationCoursesAuthorized(Integer formationCoursesAuthorized) {
	this.formationCoursesAuthorized = formationCoursesAuthorized;
    }

    public Integer getFormationCoursesNotAuthorized() {
	return formationCoursesNotAuthorized;
    }

    public void setFormationCoursesNotAuthorized(Integer formationCoursesNotAuthorized) {
	this.formationCoursesNotAuthorized = formationCoursesNotAuthorized;
    }

    public Integer getInfectumContagious() {
	return infectumContagious;
    }

    public void setInfectumContagious(Integer infectumContagious) {
	this.infectumContagious = infectumContagious;
    }

    public Integer getInternmentMedicalLeave() {
	return internmentMedicalLeave;
    }

    public void setInternmentMedicalLeave(Integer internmentMedicalLeave) {
	this.internmentMedicalLeave = internmentMedicalLeave;
    }

    public Integer getLeaveWithoutPayment() {
	return leaveWithoutPayment;
    }

    public void setLeaveWithoutPayment(Integer leaveWithoutPayment) {
	this.leaveWithoutPayment = leaveWithoutPayment;
    }

    public Integer getLeaveWithoutPaymentInWorkDays() {
	return leaveWithoutPaymentInWorkDays;
    }

    public void setLeaveWithoutPaymentInWorkDays(Integer leaveWithoutPaymentInWorkDays) {
	this.leaveWithoutPaymentInWorkDays = leaveWithoutPaymentInWorkDays;
    }

    public Integer getMarriageInWorkDays() {
	return marriageInWorkDays;
    }

    public void setMarriageInWorkDays(Integer marriageInWorkDays) {
	this.marriageInWorkDays = marriageInWorkDays;
    }

    public Integer getMarriageLeave() {
	return marriageLeave;
    }

    public void setMarriageLeave(Integer marriageLeave) {
	this.marriageLeave = marriageLeave;
    }

    public Integer getMedicalGroupCheckup() {
	return medicalGroupCheckup;
    }

    public void setMedicalGroupCheckup(Integer medicalGroupCheckup) {
	this.medicalGroupCheckup = medicalGroupCheckup;
    }

    public Integer getMedicalLeaveInWorkDays() {
	return medicalLeaveInWorkDays;
    }

    public void setMedicalLeaveInWorkDays(Integer medicalLeaveInWorkDays) {
	this.medicalLeaveInWorkDays = medicalLeaveInWorkDays;
    }

    public Integer getMissForFulfilmentOfObligation() {
	return missForFulfilmentOfObligation;
    }

    public void setMissForFulfilmentOfObligation(Integer missForFulfilmentOfObligation) {
	this.missForFulfilmentOfObligation = missForFulfilmentOfObligation;
    }

    public Integer getMissWithLostOfIncome() {
	return missWithLostOfIncome;
    }

    public void setMissWithLostOfIncome(Integer missWithLostOfIncome) {
	this.missWithLostOfIncome = missWithLostOfIncome;
    }

    public Integer getBereavementLeave() {
	return bereavementLeave;
    }

    public void setBereavementLeave(Integer bereavementLeave) {
	this.bereavementLeave = bereavementLeave;
    }

    public Integer getBereavementLeaveInWorkDays() {
	return bereavementLeaveInWorkDays;
    }

    public void setBereavementLeaveWorkDays(Integer bereavementLeaveInWorkDays) {
	this.bereavementLeaveInWorkDays = bereavementLeaveInWorkDays;
    }

    public Integer getPointTolerance() {
	return pointTolerance;
    }

    public void setPointTolerance(Integer pointTolerance) {
	this.pointTolerance = pointTolerance;
    }

    public Integer getPrison() {
	return prison;
    }

    public void setPrison(Integer prison) {
	this.prison = prison;
    }

    public Integer getRelativeMedicalLeave() {
	return relativeMedicalLeave;
    }

    public void setRelativeMedicalLeave(Integer relativeMedicalLeave) {
	this.relativeMedicalLeave = relativeMedicalLeave;
    }

    public Integer getSonInternmentMedicalLeave() {
	return sonInternmentMedicalLeave;
    }

    public void setSonInternmentMedicalLeave(Integer sonInternmentMedicalLeave) {
	this.sonInternmentMedicalLeave = sonInternmentMedicalLeave;
    }

    public Integer getSonMedicalLeave() {
	return sonMedicalLeave;
    }

    public void setSonMedicalLeave(Integer sonMedicalLeave) {
	this.sonMedicalLeave = sonMedicalLeave;
    }

    public Double getStrike() {
	return strike;
    }

    public void setStrike(Double strike) {
	this.strike = strike;
    }

    public Integer getTotalMedicalLeave() {
	return totalMedicalLeave;
    }

    public void setTotalMedicalLeave(Integer totalMedicalLeave) {
	this.totalMedicalLeave = totalMedicalLeave;
    }

    public Integer getUnionActivity() {
	return unionActivity;
    }

    public void setUnionActivity(Integer unionActivity) {
	this.unionActivity = unionActivity;
    }

    public Double getUnjustifiedMiss() {
	return unjustifiedMiss;
    }

    public void setUnjustifiedMiss(Double unjustifiedMiss) {
	this.unjustifiedMiss = unjustifiedMiss;
    }

    public Integer getUsedDismissal() {
	return usedDismissal;
    }

    public void setUsedDismissal(Integer usedDismissal) {
	this.usedDismissal = usedDismissal;
    }

    public Integer getUsedExtraWorkVacations() {
	return usedExtraWorkVacations;
    }

    public void setUsedExtraWorkVacations(Integer usedExtraWorkVacations) {
	this.usedExtraWorkVacations = usedExtraWorkVacations;
    }

    public Double getVacationsInWorkDays() {
	return vacationsInWorkDays;
    }

    public void setVacationsInWorkDays(Double vacationsInWorkDays) {
	this.vacationsInWorkDays = vacationsInWorkDays;
    }

    public Integer getWorkStudentExamDay() {
	return workStudentExamDay;
    }

    public void setWorkStudentExamDay(Integer workStudentExamDay) {
	this.workStudentExamDay = workStudentExamDay;
    }

    public Integer getWorkStudentExamEve() {
	return workStudentExamEve;
    }

    public void setWorkStudentExamEve(Integer workStudentExamEve) {
	this.workStudentExamEve = workStudentExamEve;
    }

    public YearMonth getYearMonth() {
	return yearMonth;
    }

    public void setYearMonth(YearMonth yearMonth) {
	this.yearMonth = yearMonth;
    }

    public AssignedEmployeeAnualInfo getAssignedEmployeeAnualInfo() {
	return assignedEmployeeAnualInfo;
    }

    public void setAssignedEmployeeAnualInfo(AssignedEmployeeAnualInfo assignedEmployeeAnualInfo) {
	this.assignedEmployeeAnualInfo = assignedEmployeeAnualInfo;
    }

    public Double getChildClinicMedicalTreatment() {
	return childClinicMedicalTreatment;
    }

    public void setChildClinicMedicalTreatment(Double childClinicMedicalTreatment) {
	this.childClinicMedicalTreatment = childClinicMedicalTreatment;
    }

    public Double getRelativeClinicMedicalTreatment() {
	return relativeClinicMedicalTreatment;
    }

    public void setRelativeClinicMedicalTreatment(Double relativeClinicMedicalTreatment) {
	this.relativeClinicMedicalTreatment = relativeClinicMedicalTreatment;
    }
}
