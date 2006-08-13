package net.sourceforge.fenixedu.dataTransferObject;

import java.util.Date;

import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.GradeScale;
import net.sourceforge.fenixedu.domain.degree.degreeCurricularPlan.DegreeCurricularPlanState;
import net.sourceforge.fenixedu.util.MarkType;

/**
 * @author David Santos
 * 
 * 19/Mar/2003
 */
public class InfoDegreeCurricularPlanEditor extends InfoObject {

    private InfoDegree infoDegree;

    private String name;

    private DegreeCurricularPlanState state;

    private Date initialDate;

    private Date endDate;

    private Integer degreeDuration;

    private Integer minimalYearForOptionalCourses;

    private Double neededCredits;

    private MarkType markType;

    private Integer numerusClausus;

    private String description;

    private String descriptionEn;

    private String anotation;
    
    private GradeScale gradeScale;


    public InfoDegreeCurricularPlanEditor() {
    }

    /**
     * @return Needed Credtis to Finish the Degree
     */
    public Double getNeededCredits() {
        return neededCredits;
    }

    /**
     * @param neededCredits
     */
    public void setNeededCredits(Double neededCredits) {
        this.neededCredits = neededCredits;
    }

    /**
     * @return Date
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * @return InfoDegree
     */
    public InfoDegree getInfoDegree() {
        return infoDegree;
    }

    /**
     * @return Date
     */
    public Date getInitialDate() {
        return initialDate;
    }

    /**
     * @return String
     */
    public String getName() {
        return name;
    }

    public String getPresentationName() {
        return getInfoDegree().getNome() + " " + getName();
    }

    /**
     * @return DegreeCurricularPlanState
     */
    public DegreeCurricularPlanState getState() {
        return state;
    }

    /**
     * Sets the endDate.
     * 
     * @param endDate
     *            The endDate to set
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /**
     * Sets the infoDegree.
     * 
     * @param infoDegree
     *            The infoDegree to set
     */
    public void setInfoDegree(InfoDegree infoDegree) {
        this.infoDegree = infoDegree;
    }

    /**
     * Sets the initialDate.
     * 
     * @param initialDate
     *            The initialDate to set
     */
    public void setInitialDate(Date initialDate) {
        this.initialDate = initialDate;
    }

    /**
     * Sets the name.
     * 
     * @param name
     *            The name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the state.
     * 
     * @param state
     *            The state to set
     */
    public void setState(DegreeCurricularPlanState state) {
        this.state = state;
    }

    public Integer getDegreeDuration() {
        return degreeDuration;
    }

    public Integer getMinimalYearForOptionalCourses() {
        return minimalYearForOptionalCourses;
    }

    public void setDegreeDuration(Integer integer) {
        degreeDuration = integer;
    }

    public void setMinimalYearForOptionalCourses(Integer integer) {
        minimalYearForOptionalCourses = integer;
    }

    public MarkType getMarkType() {
        return markType;
    }

    public void setMarkType(MarkType type) {
        markType = type;
    }

    /**
     * @return
     */
    public Integer getNumerusClausus() {
        return numerusClausus;
    }

    /**
     * @param integer
     */
    public void setNumerusClausus(Integer integer) {
        numerusClausus = integer;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescriptionEn() {
        return descriptionEn;
    }

    public void setDescriptionEn(String descriptionEn) {
        this.descriptionEn = descriptionEn;
    }

    public void copyToDomain(InfoDegreeCurricularPlanEditor infoDegreeCurricularPlan,
            DegreeCurricularPlan degreeCurricularPlan) {
        super.copyToDomain(infoDegreeCurricularPlan, degreeCurricularPlan);
        degreeCurricularPlan.setName(infoDegreeCurricularPlan.getName());
        degreeCurricularPlan.setState(infoDegreeCurricularPlan.getState());
        degreeCurricularPlan.setInitialDate(infoDegreeCurricularPlan.getInitialDate());
        degreeCurricularPlan.setEndDate(infoDegreeCurricularPlan.getEndDate());
        degreeCurricularPlan.setMarkType(infoDegreeCurricularPlan.getMarkType());
        degreeCurricularPlan.setNeededCredits(infoDegreeCurricularPlan.getNeededCredits());
        degreeCurricularPlan.setNumerusClausus(infoDegreeCurricularPlan.getNumerusClausus());
        degreeCurricularPlan.setAnotation(infoDegreeCurricularPlan.getAnotation());
        degreeCurricularPlan.setGradeScale(infoDegreeCurricularPlan.getGradeScale());
    }

    public String getAnotation() {
        return anotation;
    }

    public void setAnotation(String anotation) {
        this.anotation = anotation;
    }

	public GradeScale getGradeScale() {
		return this.gradeScale;
	}

	public void setGradeScale(GradeScale gradeScale) {
		this.gradeScale = gradeScale;
	}

}