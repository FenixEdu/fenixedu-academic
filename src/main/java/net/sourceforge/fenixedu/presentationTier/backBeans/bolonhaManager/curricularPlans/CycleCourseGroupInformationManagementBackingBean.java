package net.sourceforge.fenixedu.presentationTier.backBeans.bolonhaManager.curricularPlans;

import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleCourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleCourseGroupInformation;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class CycleCourseGroupInformationManagementBackingBean extends CurricularCourseManagementBackingBean {

    private Integer courseGroupID;
    private Integer informationExecutionYearId;

    private String graduatedTitle;
    private String graduatedTitleEn;

    private Integer informationId;
    private String editGraduatedTitle;
    private String editGraduatedTitleEn;

    private Integer editInformationExecutionYearId;

    public CycleCourseGroup getCourseGroup(Integer courseGroupID) {
        return (CycleCourseGroup) rootDomainObject.readDegreeModuleByOID(courseGroupID);
    }

    public List<CycleCourseGroupInformation> getCycleCourseGroupInformationList() {
        CycleCourseGroup courseGroup = getCourseGroup(getCourseGroupID());
        return courseGroup.getCycleCourseGroupInformationOrderedByExecutionYear();
    }

    public String createCourseGroupInformation() {
        try {
            CycleCourseGroup courseGroup = getCourseGroup(getCourseGroupID());
            courseGroup.createCycleCourseGroupInformation(getInformationExecutionYear(), getGraduatedTitle(),
                    getGraduatedTitleEn());

            this.addInfoMessage(bolonhaBundle.getString("cycleCourseGroupInformationAdded"));

            setInformationExecutionYearId(null);
            setGraduatedTitle("");
            setGraduatedTitleEn("");

            return "editCurricularPlanStructure";
        } catch (DomainException e) {
            this.addErrorMessage(bolonhaBundle.getString(e.getMessage()));
            return "";
        }
    }

    public String prepareEditCourseGroupInformation() {
        CycleCourseGroupInformation information = getInformation();
        setEditGraduatedTitle(information.getGraduatedTitlePt());
        setEditGraduatedTitleEn(information.getGraduatedTitleEn());
        setEditInformationExecutionYearId(information.getExecutionYear().getExternalId());

        return "";
    }

    public String editCourseGroupInformation() {
        try {
            CycleCourseGroupInformation information = getInformation();
            information.edit(getEditInformationExecutionYear(), getEditGraduatedTitle(), getEditGraduatedTitleEn());

            this.addInfoMessage(bolonhaBundle.getString("cycleCourseGroupInformationEdit"));

            setEditGraduatedTitle("");
            setEditGraduatedTitleEn("");
            setEditInformationExecutionYearId(null);

            return "editCurricularPlanStructure";
        } catch (DomainException e) {
            this.addErrorMessage(bolonhaBundle.getString(e.getMessage()));
            return "";
        }
    }

    /* GETTERS AND SETTERS */

    public String getGraduatedTitle() {
        return graduatedTitle;
    }

    public void setGraduatedTitle(String graduatedTitle) {
        this.graduatedTitle = graduatedTitle;
    }

    public String getGraduatedTitleEn() {
        return graduatedTitleEn;
    }

    public void setGraduatedTitleEn(String graduatedTitleEn) {
        this.graduatedTitleEn = graduatedTitleEn;
    }

    @Override
    public Integer getCourseGroupID() {
        return (this.courseGroupID != null) ? this.courseGroupID : getAndHoldIntegerParameter("courseGroupID");
    }

    @Override
    public void setCourseGroupID(Integer courseGroupID) {
        this.courseGroupID = courseGroupID;
    }

    public Integer getInformationExecutionYearId() {
        return informationExecutionYearId != null ? informationExecutionYearId : Integer.valueOf(NO_SELECTION);
    }

    public void setInformationExecutionYearId(Integer informationExecutionYearId) {
        this.informationExecutionYearId = informationExecutionYearId;
    }

    public ExecutionYear getInformationExecutionYear() {
        return rootDomainObject.readExecutionYearByOID(getInformationExecutionYearId());
    }

    public Integer getInformationId() {
        return informationId != null ? informationId : (informationId = getAndHoldIntegerParameter("informationId"));
    }

    public CycleCourseGroupInformation getInformation() {
        return getInformationId() != null ? rootDomainObject.readCycleCourseGroupInformationByOID(getInformationId()) : null;
    }

    public void setInformationId(Integer informationId) {
        this.informationId = informationId;
    }

    public String getEditGraduatedTitle() {
        return editGraduatedTitle;
    }

    public void setEditGraduatedTitle(String editGraduatedTitle) {
        this.editGraduatedTitle = editGraduatedTitle;
    }

    public String getEditGraduatedTitleEn() {
        return editGraduatedTitleEn;
    }

    public void setEditGraduatedTitleEn(String editGraduatedTitleEn) {
        this.editGraduatedTitleEn = editGraduatedTitleEn;
    }

    public Integer getEditInformationExecutionYearId() {
        return editInformationExecutionYearId;
    }

    public void setEditInformationExecutionYearId(Integer editExecutionYearId) {
        this.editInformationExecutionYearId = editExecutionYearId;
    }

    public ExecutionYear getEditInformationExecutionYear() {
        return rootDomainObject.readExecutionYearByOID(getEditInformationExecutionYearId());
    }

}
