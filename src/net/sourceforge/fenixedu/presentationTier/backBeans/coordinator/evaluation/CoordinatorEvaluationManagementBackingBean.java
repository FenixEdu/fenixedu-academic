/*
 * Created on Oct 19, 2005
 *  by jdnf
 */
package net.sourceforge.fenixedu.presentationTier.backBeans.coordinator.evaluation;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import javax.faces.component.html.HtmlInputHidden;
import javax.faces.model.SelectItem;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.IEvaluation;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.backBeans.base.FenixBackingBean;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.collections.comparators.ReverseComparator;

public class CoordinatorEvaluationManagementBackingBean extends FenixBackingBean {

    private Integer degreeCurricularPlanID;    
    private HtmlInputHidden degreeCurricularPlanIdHidden;

    private Integer executionPeriodID;
    private Integer curricularYearID;   
    
    private HtmlInputHidden executionPeriodIdHidden;
    private HtmlInputHidden curricularYearIdHidden;
    private IExecutionPeriod executionPeriod;    

    private Integer executionCourseID;
    private HtmlInputHidden executionCourseIdHidden;

    private List<IExecutionCourse> executionCourses;
    private List<SelectItem> executionPeriodsLabels;
    private List<SelectItem> curricularYearsLabels; 

    private Integer evaluationID;
    private HtmlInputHidden evaluationIdHidden;
    protected IEvaluation evaluation;

    public HtmlInputHidden getDegreeCurricularPlanIdHidden() {
        if (this.degreeCurricularPlanIdHidden == null) {
            final Integer degreeCurricularPlanId = this.getDegreeCurricularPlanID();
            this.degreeCurricularPlanIdHidden = new HtmlInputHidden();
            this.degreeCurricularPlanIdHidden.setValue(degreeCurricularPlanId);
        }
        return degreeCurricularPlanIdHidden;
    }

    public void setDegreeCurricularPlanIdHidden(HtmlInputHidden degreeCurricularPlanIdHidden) {
        if (degreeCurricularPlanIdHidden != null) {
            setDegreeCurricularPlanID(Integer
                    .valueOf(degreeCurricularPlanIdHidden.getValue().toString()));
        }
        this.degreeCurricularPlanIdHidden = degreeCurricularPlanIdHidden;
    }

    public Integer getDegreeCurricularPlanID() {
        if (this.degreeCurricularPlanID == null) {
            if (this.degreeCurricularPlanIdHidden != null) {
                this.degreeCurricularPlanID = Integer.valueOf(this.degreeCurricularPlanIdHidden
                        .getValue().toString());
            } else if (this.getRequestParameter("degreeCurricularPlanID") != null) {
                this.degreeCurricularPlanID = Integer.valueOf(this
                        .getRequestParameter("degreeCurricularPlanID"));
            } else if (this.getRequestAttribute("degreeCurricularPlanID") != null) {
                this.degreeCurricularPlanID = Integer.valueOf(this.getRequestAttribute(
                        "degreeCurricularPlanID").toString());
            }
        }
        return degreeCurricularPlanID;
    }

    public void setDegreeCurricularPlanID(Integer degreeCurricularPlanID) {
        this.degreeCurricularPlanID = degreeCurricularPlanID;
    }

    public List<SelectItem> getExecutionPeriodsLabels() {
        if (this.executionPeriodsLabels == null) {
            this.executionPeriodsLabels = new ArrayList();
            
            final List<InfoExecutionPeriod> infoExecutionPeriods = getExecutionPeriods();
            final ComparatorChain comparatorChain = new ComparatorChain();
            comparatorChain
                    .addComparator(new ReverseComparator(new BeanComparator("infoExecutionYear.year")));
            comparatorChain.addComparator(new ReverseComparator(new BeanComparator("semester")));
            Collections.sort(infoExecutionPeriods, comparatorChain);
            for (final InfoExecutionPeriod infoExecutionPeriod : infoExecutionPeriods) {
                final SelectItem selectItem = new SelectItem();
                selectItem.setValue(infoExecutionPeriod.getIdInternal());
                selectItem.setLabel(infoExecutionPeriod.getName() + " - "
                        + infoExecutionPeriod.getInfoExecutionYear().getYear());
                this.executionPeriodsLabels.add(selectItem);
            }
        }
        return this.executionPeriodsLabels;
    }

    protected List<InfoExecutionPeriod> getExecutionPeriods() {
        try {
            final Object args[] = { getDegreeCurricularPlanID() };
            return (List<InfoExecutionPeriod>) ServiceManagerServiceFactory.executeService(
                    getUserView(), "ReadExecutionPeriodsByDegreeCurricularPlan", args);
        } catch (FenixFilterException e) {
        } catch (FenixServiceException e) {
        }
        return new ArrayList();
    }

    public List<SelectItem> getCurricularYearsLabels() {
        if (this.curricularYearsLabels == null) {
            this.curricularYearsLabels = new ArrayList();
            this.curricularYearsLabels.add(new SelectItem(0, "Todos"));
            for (int i = 1; i <= 5; i++) {
                this.curricularYearsLabels.add(new SelectItem(i, i + " º"));
            }    
        }        
        return this.curricularYearsLabels;
    }
    
    public IExecutionCourse getExecutionCourse() throws FenixFilterException, FenixServiceException {

        final Object[] argsToReadExecutionCourse = { ExecutionCourse.class, this.getExecutionCourseID() };
        return (IExecutionCourse) ServiceUtils.executeService(null, "ReadDomainObject",
                argsToReadExecutionCourse);
    }

    protected List<IExecutionCourse> getExecutionCourses() {
        if (this.executionCourses != null) {
            return this.executionCourses;
        }
        try {
            final Object args[] = { getDegreeCurricularPlanID(), getExecutionPeriodID(),
                    getCurricularYearID() };
            this.executionCourses = (List) ServiceManagerServiceFactory.executeService(getUserView(),
                    "ReadExecutionCoursesByDegreeCurricularPlanAndExecutionPeriodAndCurricularYear",
                    args);
            return this.executionCourses;
        } catch (FenixFilterException e) {
        } catch (FenixServiceException e) {
            setErrorMessage(e.getMessage());
        }
        return new ArrayList();
    }

    protected IExecutionPeriod getExecutionPeriod() {
        if (executionPeriod == null) {
            try {
                final Object args[] = { ExecutionPeriod.class, getExecutionPeriodID() };
                return (IExecutionPeriod) ServiceUtils.executeService(null, "ReadDomainObject", args);
            } catch (Exception e) {
            }
        }
        return this.executionPeriod;
    }

    protected InfoExecutionPeriod getCurrentExecutionPeriod() {
        try {
            final Object args[] = {};
            return (InfoExecutionPeriod) ServiceManagerServiceFactory.executeService(getUserView(),
                    "ReadCurrentExecutionPeriod", args);
        } catch (FenixFilterException e) {
        } catch (FenixServiceException e) {
        }
        return null;
    }
    
    /**
     * It's necessary to put these attibutes in request for the next back bean
     */
    protected void setRequestCommonAttributes() {
        setRequestAttribute("degreeCurricularPlanID", getDegreeCurricularPlanID());
        setRequestAttribute("executionPeriodID", getExecutionPeriodID());
        setRequestAttribute("curricularYearID", getCurricularYearID());
    }

    public Calendar getExecutionPeriodBeginDate() {
        final Calendar date = Calendar.getInstance();
        date.setTime(this.getExecutionPeriod().getBeginDate());
        return date;
    }

    public Calendar getExecutionPeriodEndDate() {
        final Calendar date = Calendar.getInstance();
        date.setTime(this.getExecutionPeriod().getEndDate());
        return date;
    }

    public String searchExecutionCourses() {
        clearAttributes();
        return "";
    }

    protected void clearAttributes() {
        this.executionCourses = null;
    }
    
    public Integer getExecutionCourseID() {
        if (this.executionCourseID == null) {
            if (this.getExecutionCourseIdHidden().getValue() != null) {
                this.executionCourseID = Integer.valueOf(this.getExecutionCourseIdHidden().getValue()
                        .toString());
            } else if (this.getRequestParameter("executionCourseID") != null) {
                this.executionCourseID = Integer.valueOf(this.getRequestParameter("executionCourseID"));
            } else if (this.getRequestAttribute("executionCourseID") != null) {
                this.executionCourseID = Integer.valueOf(this.getRequestAttribute("executionCourseID")
                        .toString());
            }
        }
        return this.executionCourseID;
    }

    public void setExecutionCourseID(Integer executionCourseID) {
        if (executionCourseID != null) {
            this.getExecutionCourseIdHidden().setValue(executionCourseID);
        }
        this.executionCourseID = executionCourseID;
    }

    public HtmlInputHidden getExecutionCourseIdHidden() {
        if (this.executionCourseIdHidden == null) {
            this.executionCourseIdHidden = new HtmlInputHidden();
            this.executionCourseIdHidden.setValue(this.getExecutionCourseID());
        }
        return executionCourseIdHidden;
    }

    public void setExecutionCourseIdHidden(HtmlInputHidden executionCourseIdHidden) {
        if (executionCourseIdHidden != null) {
            this.setExecutionCourseID(Integer.valueOf(executionCourseIdHidden.getValue().toString()));
        }
        this.executionCourseIdHidden = executionCourseIdHidden;
    }

    public Integer getExecutionPeriodID() {
        if (this.executionPeriodID == null) {
            if (this.getExecutionPeriodIdHidden().getValue() != null) {
                this.executionPeriodID = Integer.valueOf(this.getExecutionPeriodIdHidden().getValue()
                        .toString());
            } else if (this.getRequestParameter("executionPeriodID") != null) {
                this.executionPeriodID = Integer.valueOf(this.getRequestParameter("executionPeriodID"));
            } else if (this.getRequestAttribute("executionPeriodID") != null) {
                this.executionPeriodID = Integer.valueOf(this.getRequestAttribute("executionPeriodID")
                        .toString());
            } else {
                final InfoExecutionPeriod currentExecutionPeriod = getCurrentExecutionPeriod();
                this.executionPeriodID = (currentExecutionPeriod != null) ? currentExecutionPeriod
                        .getIdInternal() : null;
            }
        }
        return executionPeriodID;
    }

    public void setExecutionPeriodID(Integer executionPeriodID) {
        if (executionPeriodID != null) {
            getExecutionPeriodIdHidden().setValue(executionPeriodID);
        }
        this.executionPeriodID = executionPeriodID;
    }

    public Integer getCurricularYearID() {
        if (this.curricularYearID == null) {
            if (getCurricularYearIdHidden().getValue() != null) {
                this.curricularYearID = Integer.valueOf(getCurricularYearIdHidden().getValue()
                        .toString());
            } else if (this.getRequestParameter("curricularYearID") != null) {
                this.curricularYearID = Integer.valueOf(this.getRequestParameter("curricularYearID"));
            } else if (this.getRequestAttribute("curricularYearID") != null) {
                this.curricularYearID = Integer.valueOf(this.getRequestAttribute("curricularYearID")
                        .toString());
            } else {
                this.curricularYearID = Integer.valueOf(1);
            }
        }
        return curricularYearID;
    }

    public void setCurricularYearID(Integer curricularYearID) {
        if (curricularYearID != null) {
            getCurricularYearIdHidden().setValue(curricularYearID);
        }
        this.curricularYearID = curricularYearID;
    }

    public HtmlInputHidden getExecutionPeriodIdHidden() {
        if (this.executionPeriodIdHidden == null) {
            this.executionPeriodIdHidden = new HtmlInputHidden();
            this.executionPeriodIdHidden.setValue(getExecutionPeriodID());
        }
        return executionPeriodIdHidden;
    }

    public void setExecutionPeriodIdHidden(HtmlInputHidden executionPeriodIdHidden) {
        if (executionPeriodIdHidden != null) {
            setExecutionPeriodID(Integer.valueOf(executionPeriodIdHidden.getValue().toString()));
        }
        this.executionPeriodIdHidden = executionPeriodIdHidden;
    }

    public HtmlInputHidden getCurricularYearIdHidden() {
        if (this.curricularYearIdHidden == null) {
            this.curricularYearIdHidden = new HtmlInputHidden();
            this.curricularYearIdHidden.setValue(getCurricularYearID());
        }
        return curricularYearIdHidden;
    }

    public void setCurricularYearIdHidden(HtmlInputHidden curricularYearIdHidden) {
        if (curricularYearIdHidden != null) {
            setCurricularYearID(Integer.valueOf(curricularYearIdHidden.getValue().toString()));
        }
        this.curricularYearIdHidden = curricularYearIdHidden;
    }
    
    public Integer getEvaluationID() {
        if (this.evaluationID == null) {
            if (this.getRequestParameter("evaluationID") != null) {
                setEvaluationID(Integer.valueOf(this.getRequestParameter("evaluationID")));
            } else if (this.getEvaluationIdHidden().getValue() != null) {
                setEvaluationID(Integer.valueOf(this.getEvaluationIdHidden().getValue().toString()));
            }
        }
        return this.evaluationID;
    }

    public void setEvaluationID(Integer evaluationID) {
        this.evaluationID = evaluationID;
    }

    public HtmlInputHidden getEvaluationIdHidden() {
        if (this.evaluationIdHidden == null) {
            this.evaluationIdHidden = new HtmlInputHidden();
            this.evaluationIdHidden.setValue(getEvaluationID());
        }
        return evaluationIdHidden;
    }

    public void setEvaluationIdHidden(HtmlInputHidden evaluationIdHidden) {
        if (evaluationIdHidden != null) {
            setEvaluationID(Integer.valueOf(evaluationIdHidden.getValue().toString()));
        }
        this.evaluationIdHidden = evaluationIdHidden;
    }
}
