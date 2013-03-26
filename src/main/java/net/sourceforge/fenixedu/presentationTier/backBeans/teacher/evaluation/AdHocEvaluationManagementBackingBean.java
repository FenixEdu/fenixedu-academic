package net.sourceforge.fenixedu.presentationTier.backBeans.teacher.evaluation;

import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.EditAdHocEvaluation;
import net.sourceforge.fenixedu.domain.AdHocEvaluation;
import net.sourceforge.fenixedu.domain.GradeScale;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.ServiceUtils;

import org.apache.commons.beanutils.BeanComparator;

public class AdHocEvaluationManagementBackingBean extends EvaluationManagementBackingBean {
    protected final ResourceBundle enumerationBundle = getResourceBundle("resources/EnumerationResources");

    protected String name;
    protected AdHocEvaluation adHocEvaluation;
    protected Integer adHocEvaluationID;

    public AdHocEvaluationManagementBackingBean() {
        super();
    }

    public String createAdHocEvaluation() {
        try {
            final Object[] args = { getExecutionCourseID(), getName(), getDescription(), getGradeScale() };
            ServiceUtils.executeService("CreateAdHocEvaluation", args);
        } catch (final FenixFilterException e) {
            return "";
        } catch (final FenixServiceException e) {
            setErrorMessage(e.getMessage());
            return "";
        } catch (final DomainException e) {
            setErrorMessage(e.getKey());
            return "";
        }
        return "adHocEvaluationsIndex";
    }

    public String editAdHocEvaluation() {
        try {

            EditAdHocEvaluation.run(getExecutionCourseID(), getAdHocEvaluationID(), getName(), getDescription(), getGradeScale());
        } catch (final FenixServiceException e) {
            setErrorMessage(e.getMessage());
            return "";
        } catch (DomainException e) {
            setErrorMessage(e.getKey());
            return "";
        }
        return "adHocEvaluationsIndex";
    }

    private AdHocEvaluation getAdHocEvaluation() {
        if (this.adHocEvaluation == null && this.getAdHocEvaluationID() != null) {
            this.adHocEvaluation = (AdHocEvaluation) rootDomainObject.readEvaluationByOID(getAdHocEvaluationID());
        }
        return this.adHocEvaluation;
    }

    public String deleteAdHocEvaluation() {
        try {
            final Object[] args = { getExecutionCourseID(), getAdHocEvaluationID() };
            ServiceUtils.executeService("DeleteEvaluation", args);
        } catch (FenixFilterException e) {
        } catch (FenixServiceException e) {
            setErrorMessage(e.getMessage());
        } catch (DomainException e) {
            setErrorMessage(e.getKey());
        }
        return "adHocEvaluationsIndex";
    }

    public List<AdHocEvaluation> getAssociatedAdHocEvaluations() throws FenixFilterException, FenixServiceException {
        List<AdHocEvaluation> associatedAdHocEvaluations = getExecutionCourse().getAssociatedAdHocEvaluations();
        Collections.sort(associatedAdHocEvaluations, new BeanComparator("creationDateTime"));
        return associatedAdHocEvaluations;
    }

    public String getName() {
        if (this.name == null && this.getAdHocEvaluation() != null) {
            this.name = this.getAdHocEvaluation().getName();
        }
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getDescription() {
        if (this.description == null && this.getAdHocEvaluation() != null) {
            this.description = this.getAdHocEvaluation().getDescription();
        }
        return this.description;
    }

    public Integer getAdHocEvaluationID() {
        if (this.adHocEvaluationID == null) {
            if (this.getRequestParameter("adHocEvaluationID") != null
                    && !this.getRequestParameter("adHocEvaluationID").equals("")) {
                this.adHocEvaluationID = Integer.valueOf(this.getRequestParameter("adHocEvaluationID"));
            }
        }
        return this.adHocEvaluationID;
    }

    public void setAdHocEvaluationID(Integer adHocEvaluationID) {
        this.adHocEvaluationID = adHocEvaluationID;
    }

    @Override
    public GradeScale getGradeScale() {
        if (gradeScale == null && this.getAdHocEvaluation() != null) {
            this.gradeScale = getAdHocEvaluation().getGradeScale();
        }
        return this.gradeScale;
    }
}