/*
 * ExecutionDegree.java
 *
 * Created on 2 de Novembro de 2002, 20:53
 */

package Dominio;

import java.util.List;

/**
 * 
 * @author rpfi
 */

public class ExecutionDegree extends DomainObject implements IExecutionDegree {
    private Integer keyCurricularPlan;

    private Integer keyCoordinator;

    private IExecutionYear executionYear;

    private Integer academicYear;

    private IDegreeCurricularPlan degreeCurricularPlan;

    //added by Tânia Pousão
    private List coordinatorsList;

    private Boolean temporaryExamMap;

    //added by Tânia Pousão
    private Integer keyCampus;

    private ICampus campus;

    // added by amsg 4 Jun 2004
    private IPeriod periodLessonsFirstSemester;

    private IPeriod periodExamsFirstSemester;

    private IPeriod periodLessonsSecondSemester;

    private IPeriod periodExamsSecondSemester;

    private Integer keyPeriodLessonsFirstSemester;

    private Integer keyPeriodExamsFirstSemester;

    private Integer keyPeriodLessonsSecondSemester;

    private Integer keyPeriodExamsSecondSemester;

    /** Construtor sem argumentos publico requerido pela moldura de objectos OJB */
    public ExecutionDegree() {
    }

    public ExecutionDegree(IExecutionYear executionYear, IDegreeCurricularPlan curricularPlan) {
        setExecutionYear(executionYear);
        setCurricularPlan(curricularPlan);
    }

    /**
     * @param executionDegreeId
     */
    public ExecutionDegree(Integer executionDegreeId) {
        setIdInternal(executionDegreeId);
    }

    public boolean equals(Object obj) {
        boolean resultado = false;
        if (obj instanceof IExecutionDegree) {
            IExecutionDegree cursoExecucao = (IExecutionDegree) obj;
            resultado = getIdInternal().equals(cursoExecucao.getIdInternal());
        }
        return resultado;
    }

    public String toString() {
        String result = "[CURSO_EXECUCAO";
        result += ", codInt=" + getIdInternal();
        result += ", executionYear=" + executionYear;
        result += ", keyExecutionYear=" + academicYear;
        result += ", degreeCurricularPlan=" + degreeCurricularPlan;
        if (coordinatorsList != null) {
            result += ", coordinatorsList=" + coordinatorsList.size();
        } else {
            result += ", coordinatorsList is NULL";
        }
        result += ", campus=" + campus;
        result += "]";
        return result;
    }

    /**
     * 
     * @see Dominio.IExecutionDegree#getExecutionYear()
     */
    public IExecutionYear getExecutionYear() {
        return executionYear;
    }

    /**
     * 
     * @see Dominio.IExecutionDegree#setExecutionYear(IExecutionYear)
     */
    public void setExecutionYear(IExecutionYear executionYear) {
        this.executionYear = executionYear;
    }

    /**
     * Returns the academicYear.
     * 
     * @return Integer
     */
    public Integer getAcademicYear() {
        return academicYear;
    }

    /**
     * Sets the academicYear.
     * 
     * @param academicYear
     *            The academicYear to set
     */
    public void setAcademicYear(Integer academicYear) {
        this.academicYear = academicYear;
    }

    /**
     * Returns the curricularPlan.
     * 
     * @return IDegreeCurricularPlan
     */
    public IDegreeCurricularPlan getCurricularPlan() {
        return degreeCurricularPlan;
    }

    /**
     * Returns the keyCurricularPlan.
     * 
     * @return Integer
     */
    public Integer getKeyCurricularPlan() {
        return keyCurricularPlan;
    }

    /**
     * Sets the curricularPlan.
     * 
     * @param curricularPlan
     *            The curricularPlan to set
     */
    public void setCurricularPlan(IDegreeCurricularPlan curricularPlan) {
        this.degreeCurricularPlan = curricularPlan;
    }

    /**
     * Sets the keyCurricularPlan.
     * 
     * @param keyCurricularPlan
     *            The keyCurricularPlan to set
     */
    public void setKeyCurricularPlan(Integer keyCurricularPlan) {
        this.keyCurricularPlan = keyCurricularPlan;
    }

    /**
     * @return
     */
    public IDegreeCurricularPlan getDegreeCurricularPlan() {
        return degreeCurricularPlan;
    }

    /**
     * @return
     */
    public Integer getKeyCoordinator() {
        return keyCoordinator;
    }

    /**
     * @param plan
     */
    public void setDegreeCurricularPlan(IDegreeCurricularPlan plan) {
        degreeCurricularPlan = plan;
    }

    /**
     * @param integer
     */
    public void setKeyCoordinator(Integer integer) {
        keyCoordinator = integer;
    }

    /**
     * @return
     */
    public Boolean getTemporaryExamMap() {
        return temporaryExamMap;
    }

    /**
     * @param boolean1
     */
    public void setTemporaryExamMap(Boolean temporary) {
        temporaryExamMap = temporary;
    }

    /**
     * @return Returns the campus.
     */
    public ICampus getCampus() {
        return campus;
    }

    /**
     * @param campus
     *            The campus to set.
     */
    public void setCampus(ICampus campus) {
        this.campus = campus;
    }

    /**
     * @return Returns the keyCampus.
     */
    public Integer getKeyCampus() {
        return keyCampus;
    }

    /**
     * @param keyCampus
     *            The keyCampus to set.
     */
    public void setKeyCampus(Integer keyCampus) {
        this.keyCampus = keyCampus;
    }

    /**
     * @return Returns the coordinatorsList.
     */
    public List getCoordinatorsList() {
        return coordinatorsList;
    }

    /**
     * @param coordinatorsList
     *            The coordinatorsList to set.
     */
    public void setCoordinatorsList(List coordinatorsList) {
        this.coordinatorsList = coordinatorsList;
    }

    public IPeriod getPeriodExamsFirstSemester() {
        return periodExamsFirstSemester;
    }

    public void setPeriodExamsFirstSemester(IPeriod periodExamsFirstSemester) {
        this.periodExamsFirstSemester = periodExamsFirstSemester;
    }

    public IPeriod getPeriodExamsSecondSemester() {
        return periodExamsSecondSemester;
    }

    public void setPeriodExamsSecondSemester(IPeriod periodExamsSecondSemester) {
        this.periodExamsSecondSemester = periodExamsSecondSemester;
    }

    public IPeriod getPeriodLessonsSecondSemester() {
        return periodLessonsSecondSemester;
    }

    public void setPeriodLessonsSecondSemester(IPeriod periodLessonsSecondSemester) {
        this.periodLessonsSecondSemester = periodLessonsSecondSemester;
    }

    public IPeriod getPeriodLessonsFirstSemester() {
        return periodLessonsFirstSemester;
    }

    public void setPeriodLessonsFirstSemester(IPeriod periodLessonsFirstSemester) {
        this.periodLessonsFirstSemester = periodLessonsFirstSemester;
    }

    public Integer getKeyPeriodExamsFirstSemester() {
        return keyPeriodExamsFirstSemester;
    }

    public void setKeyPeriodExamsFirstSemester(Integer keyPeriodExamsFirstSemester) {
        this.keyPeriodExamsFirstSemester = keyPeriodExamsFirstSemester;
    }

    public Integer getKeyPeriodExamsSecondSemester() {
        return keyPeriodExamsSecondSemester;
    }

    public void setKeyPeriodExamsSecondSemester(Integer keyPeriodExamsSecondSemester) {
        this.keyPeriodExamsSecondSemester = keyPeriodExamsSecondSemester;
    }

    public Integer getKeyPeriodLessonsFirstSemester() {
        return keyPeriodLessonsFirstSemester;
    }

    public void setKeyPeriodLessonsFirstSemester(Integer keyPeriodLessonsFirstSemester) {
        this.keyPeriodLessonsFirstSemester = keyPeriodLessonsFirstSemester;
    }

    public Integer getKeyPeriodLessonsSecondSemester() {
        return keyPeriodLessonsSecondSemester;
    }

    public void setKeyPeriodLessonsSecondSemester(Integer keyPeriodLessonsSecondSemester) {
        this.keyPeriodLessonsSecondSemester = keyPeriodLessonsSecondSemester;
    }

}