package net.sourceforge.fenixedu.dataTransferObject;

import java.util.List;

public class InfoExecutionDegreeEditor extends InfoObject {

    private InfoExecutionYear infoExecutionYear;

    private InfoDegreeCurricularPlan infoDegreeCurricularPlan;

    private List coordinatorsList;

    private Boolean temporaryExamMap;

    private InfoCampus infoCampus;

    private InfoPeriod infoPeriodLessonsFirstSemester;

    private InfoPeriod infoPeriodExamsFirstSemester;

    private InfoPeriod infoPeriodLessonsSecondSemester;

    private InfoPeriod infoPeriodExamsSecondSemester;

    private String qualifiedName;

    public boolean bolonha;
    
    public InfoExecutionDegreeEditor() {
    }

    public InfoExecutionYear getInfoExecutionYear() {
        return infoExecutionYear;
    }

    public void setInfoExecutionYear(InfoExecutionYear infoExecutionYear) {
        this.infoExecutionYear = infoExecutionYear;
    }

    public InfoDegreeCurricularPlan getInfoDegreeCurricularPlan() {
        return infoDegreeCurricularPlan;
    }

    public void setInfoDegreeCurricularPlan(InfoDegreeCurricularPlan infoDegreeCurricularPlan) {
        this.infoDegreeCurricularPlan = infoDegreeCurricularPlan;
    }

    public Boolean getTemporaryExamMap() {
        return temporaryExamMap;
    }

    public void setTemporaryExamMap(Boolean temporary) {
        temporaryExamMap = temporary;
    }

    public InfoCampus getInfoCampus() {
        return infoCampus;
    }

    public void setInfoCampus(InfoCampus infoCampus) {
        this.infoCampus = infoCampus;
    }

    public List getCoordinatorsList() {
        return coordinatorsList;
    }

    public void setCoordinatorsList(List coordinatorsList) {
        this.coordinatorsList = coordinatorsList;
    }

    public InfoPeriod getInfoPeriodExamsFirstSemester() {
        return infoPeriodExamsFirstSemester;
    }

    public void setInfoPeriodExamsFirstSemester(InfoPeriod infoPeriodExamsFirstSemester) {
        this.infoPeriodExamsFirstSemester = infoPeriodExamsFirstSemester;
    }

    public InfoPeriod getInfoPeriodExamsSecondSemester() {
        return infoPeriodExamsSecondSemester;
    }

    public void setInfoPeriodExamsSecondSemester(InfoPeriod infoPeriodExamsSecondSemester) {
        this.infoPeriodExamsSecondSemester = infoPeriodExamsSecondSemester;
    }

    public InfoPeriod getInfoPeriodLessonsFirstSemester() {
        return infoPeriodLessonsFirstSemester;
    }

    public void setInfoPeriodLessonsFirstSemester(InfoPeriod infoPeriodLessonsFirstSemester) {
        this.infoPeriodLessonsFirstSemester = infoPeriodLessonsFirstSemester;
    }

    public InfoPeriod getInfoPeriodLessonsSecondSemester() {
        return infoPeriodLessonsSecondSemester;
    }

    public void setInfoPeriodLessonsSecondSemester(InfoPeriod infoPeriodLessonsSecondSemester) {
        this.infoPeriodLessonsSecondSemester = infoPeriodLessonsSecondSemester;
    }
    
    public String getQualifiedName() {
        return qualifiedName;
    }

    public void setQualifiedName(String qualifiedName) {
        this.qualifiedName = qualifiedName;
    }

    public boolean isBolonha() {
        return bolonha;
    }

    public void setBolonha(boolean bolonha) {
        this.bolonha = bolonha;
    }

}
