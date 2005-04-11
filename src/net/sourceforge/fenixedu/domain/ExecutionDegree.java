/*
 * ExecutionDegree.java
 *
 * Created on 2 de Novembro de 2002, 20:53
 */

package net.sourceforge.fenixedu.domain;

import java.util.List;

/**
 * 
 * @author rpfi
 */

public class ExecutionDegree extends ExecutionDegree_Base {
    private IExecutionYear executionYear;

    private IDegreeCurricularPlan degreeCurricularPlan;

    private List coordinatorsList;

    private ICampus campus;

    private IPeriod periodLessonsFirstSemester;

    private IPeriod periodExamsFirstSemester;

    private IPeriod periodLessonsSecondSemester;

    private IPeriod periodExamsSecondSemester;

    /** Construtor sem argumentos publico requerido pela moldura de objectos OJB */
    public ExecutionDegree() {
    }

    public ExecutionDegree(IExecutionYear executionYear, IDegreeCurricularPlan curricularPlan) {
        setExecutionYear(executionYear);
        setDegreeCurricularPlan(curricularPlan);
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
        //result += ", keyExecutionYear=" + academicYear;
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
     * @return
     */
    public IDegreeCurricularPlan getDegreeCurricularPlan() {
        return degreeCurricularPlan;
    }

    /**
     * @param plan
     */
    public void setDegreeCurricularPlan(IDegreeCurricularPlan degreeCurricularPlan) {
        this.degreeCurricularPlan = degreeCurricularPlan;
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

    
}