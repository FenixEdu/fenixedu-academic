/*
 * IExecutionDegree.java
 * 
 * Created on 2 de Novembro de 2002, 20:50
 */

package net.sourceforge.fenixedu.domain;

import java.io.Serializable;
import java.util.List;

/**
 * @author rpfi
 */
public interface IExecutionDegree extends Serializable, IDomainObject {

    IExecutionYear getExecutionYear();

    IDegreeCurricularPlan getCurricularPlan();

    List getCoordinatorsList();

    Boolean getTemporaryExamMap();

    ICampus getCampus();

    void setCurricularPlan(IDegreeCurricularPlan curricularPlan);

    void setExecutionYear(IExecutionYear newExecutionYear);

    void setCoordinatorsList(List coordinatorsList);

    void setTemporaryExamMap(Boolean bool);

    void setCampus(ICampus campus);

    // added by amsg 4 Jun 2004
    IPeriod getPeriodLessonsFirstSemester();

    IPeriod getPeriodExamsFirstSemester();

    IPeriod getPeriodLessonsSecondSemester();

    IPeriod getPeriodExamsSecondSemester();

    void setPeriodLessonsFirstSemester(IPeriod p);

    void setPeriodExamsFirstSemester(IPeriod p);

    void setPeriodLessonsSecondSemester(IPeriod p);

    void setPeriodExamsSecondSemester(IPeriod p);
}