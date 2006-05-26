package net.sourceforge.fenixedu.dataTransferObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionDegree;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.struts.util.LabelValueBean;
import org.apache.struts.util.MessageResources;

public class InfoExecutionDegree extends InfoObject {

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
    
    public InfoExecutionDegree() {
    }

    public InfoExecutionDegree(InfoDegreeCurricularPlan infoDegreeCurricularPlan, InfoExecutionYear infoExecutionYear) {
        setInfoDegreeCurricularPlan(infoDegreeCurricularPlan);
        setInfoExecutionYear(infoExecutionYear);
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

    public boolean equals(Object obj) {
        boolean result = false;
        if (obj instanceof InfoExecutionDegree) {
            InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) obj;
            result = getIdInternal().equals(infoExecutionDegree.getIdInternal());
        }
        return result;
    }

    public String toString() {
        String result = "[INFOEXECUTIONDEGREE";
        result += ", infoExecutionYear=" + infoExecutionYear;
        result += ", infoDegreeCurricularPlan=" + infoDegreeCurricularPlan;
        if (coordinatorsList != null) {
            result += ", coordinatorsList=" + coordinatorsList.size();
        } else {
            result += ", coordinatorsList is NULL";
        }
        result += ", infoCampus= " + infoCampus;
        result += "]";
        return result;
    }

    public static List buildLabelValueBeansForList(List executionDegrees,
            MessageResources messageResources) {
        List copyExecutionDegrees = new ArrayList();
        copyExecutionDegrees.addAll(executionDegrees);
        List result = new ArrayList();
        Iterator iter = executionDegrees.iterator();
        while (iter.hasNext()) {
            final InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) iter.next();
            List equalDegrees = (List) CollectionUtils.select(copyExecutionDegrees, new Predicate() {
                public boolean evaluate(Object arg0) {
                    InfoExecutionDegree infoExecutionDegreeElem = (InfoExecutionDegree) arg0;
                    if (infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getSigla()
                            .equals(
                                    infoExecutionDegreeElem.getInfoDegreeCurricularPlan()
                                            .getInfoDegree().getSigla())) {
                        return true;
                    }
                    return false;
                }
            });
            if (equalDegrees.size() == 1) {
                copyExecutionDegrees.remove(infoExecutionDegree);

                String degreeType = null;
                if (messageResources != null) {
                    degreeType = messageResources.getMessage(infoExecutionDegree
                            .getInfoDegreeCurricularPlan().getInfoDegree().getTipoCurso().toString());
                }
                if (degreeType == null)
                    degreeType = infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree()
                            .getTipoCurso().toString();

                result.add(new LabelValueBean(degreeType + "  "
                        + infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getNome(),
                        infoExecutionDegree.getIdInternal().toString()));
            } else {
                String degreeType = null;
                if (messageResources != null) {
                    degreeType = messageResources.getMessage(infoExecutionDegree
                            .getInfoDegreeCurricularPlan().getInfoDegree().getTipoCurso().toString());
                }
                if (degreeType == null)
                    degreeType = infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree()
                            .getTipoCurso().toString();

                result.add(new LabelValueBean(degreeType + "  "
                        + infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getNome()
                        + " - " + infoExecutionDegree.getInfoDegreeCurricularPlan().getName(),
                        infoExecutionDegree.getIdInternal().toString()));
            }
        }
        return result;

    }

    public static InfoExecutionDegree newInfoFromDomain(ExecutionDegree executionDegree) {
        InfoExecutionDegree infoExecutionDegree = null;
        if (executionDegree != null) {
            infoExecutionDegree = new InfoExecutionDegree();
            infoExecutionDegree.copyFromDomain(executionDegree);
        }
        return infoExecutionDegree;
    }

    public void copyFromDomain(ExecutionDegree executionDegree) {
        super.copyFromDomain(executionDegree);
        if (executionDegree != null) {
            setBolonha(executionDegree.isBolonha());
            setTemporaryExamMap(executionDegree.getTemporaryExamMap());
        }
    }

}
