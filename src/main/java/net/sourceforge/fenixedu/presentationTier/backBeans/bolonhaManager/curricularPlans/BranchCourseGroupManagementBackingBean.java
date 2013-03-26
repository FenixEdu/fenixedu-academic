package net.sourceforge.fenixedu.presentationTier.backBeans.bolonhaManager.curricularPlans;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.faces.model.SelectItem;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.degreeStructure.BranchType;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.injectionCode.IllegalDataAccessException;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;
import pt.utl.ist.fenix.tools.util.i18n.Language;

public class BranchCourseGroupManagementBackingBean extends CourseGroupManagementBackingBean {

    private String branchTypeName;
    private List<SelectItem> branchTypes = null;

    public String getBranchTypeName() {
        return (branchTypeName != null) ? branchTypeName : BranchType.MAJOR.getName();//BundleUtil.getStringFromResourceBundle("resources.BolonhaManagerResources", "choose");
    }

    public void setBranchTypeName(String branchTypeName) {
        this.branchTypeName = branchTypeName;
    }

    public BranchType getBranchType() {
        return BranchType.valueOf(getBranchTypeName());
        /*
        if(branchType == null && getCourseGroupID() != null) {
            CourseGroup group = getCourseGroup(getCourseGroupID());
            return (group instanceof BranchCourseGroup ? ((BranchCourseGroup) group).getBranchType() : branchType);
        }
            return branchType;
            */
    }

    public List<SelectItem> getBranchTypes() {
        return (branchTypes == null) ? (branchTypes = readBranchTypes()) : branchTypes;
    }

    private List<SelectItem> readBranchTypes() {
        final List<SelectItem> result = new ArrayList<SelectItem>();
        final List<BranchType> entries = new ArrayList<BranchType>(Arrays.asList(BranchType.values()));
        for (BranchType entry : entries) {
            result.add(new SelectItem(entry.name(), entry.getDescription(Language.getLocale())));
        }
        return result;
    }

    public String createBranchCourseGroup() {
        try {
            executeCreation(getDegreeCurricularPlanID(), getParentCourseGroupID(), getName(), getNameEn(), getBranchType(),
                    getBeginExecutionPeriodID(), getFinalEndExecutionPeriodID());
            addInfoMessage(bolonhaBundle.getString("branchCourseGroupCreated"));
            return "editCurricularPlanStructure";
        } catch (IllegalDataAccessException e) {
            this.addErrorMessage(bolonhaBundle.getString("error.notAuthorized"));
            return "editCurricularPlanStructure";
        } catch (final FenixServiceException e) {
            addErrorMessage(bolonhaBundle.getString(e.getMessage()));
        } catch (final DomainException e) {
            addErrorMessage(domainExceptionBundle.getString(e.getMessage()));
        }
        return "";
    }

    @Service
    @Checked("RolePredicates.BOLOGNA_OR_DEGREE_ADMINISTRATIVE_OFFICE_SUPER_USER_OR_MANAGER_OR_OPERATOR_PREDICATE")
    private void executeCreation(final Integer degreeCurricularPlanID, final Integer parentCourseGroupID, final String name,
            final String nameEn, final BranchType branchType, final Integer beginExecutionPeriodID,
            final Integer endExecutionPeriodID) throws FenixServiceException {

        final DegreeCurricularPlan degreeCurricularPlan = rootDomainObject.readDegreeCurricularPlanByOID(degreeCurricularPlanID);
        if (degreeCurricularPlan == null) {
            throw new FenixServiceException("error.noDegreeCurricularPlan");
        }
        final CourseGroup parentCourseGroup = (CourseGroup) rootDomainObject.readDegreeModuleByOID(parentCourseGroupID);
        if (parentCourseGroup == null) {
            throw new FenixServiceException("error.noCourseGroup");
        }

        final ExecutionSemester beginExecutionPeriod;
        if (beginExecutionPeriodID == null) {
            beginExecutionPeriod = ExecutionSemester.readActualExecutionSemester();
        } else {
            beginExecutionPeriod = rootDomainObject.readExecutionSemesterByOID(beginExecutionPeriodID);
        }

        final ExecutionSemester endExecutionPeriod =
                (endExecutionPeriodID == null) ? null : rootDomainObject.readExecutionSemesterByOID(endExecutionPeriodID);

        degreeCurricularPlan.createBranchCourseGroup(parentCourseGroup, name, nameEn, branchType, beginExecutionPeriod,
                endExecutionPeriod);
    }

}
