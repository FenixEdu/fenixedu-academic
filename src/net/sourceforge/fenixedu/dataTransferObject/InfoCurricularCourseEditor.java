package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.GradeScale;
import net.sourceforge.fenixedu.domain.curriculum.CurricularCourseType;
import net.sourceforge.fenixedu.util.CurricularCourseExecutionScope;

public class InfoCurricularCourseEditor extends InfoObject implements ISiteComponent {

    private String name;

    private String code;

    private Double credits;

    private Double theoreticalHours;

    private Double praticalHours;

    private Double theoPratHours;

    private Double labHours;

    private CurricularCourseType type;

    private CurricularCourseExecutionScope curricularCourseExecutionScope;

    private Boolean mandatory;

    private Boolean basic;

    private String chosen;

    private Integer maximumValueForAcumulatedEnrollments;

    private Integer minimumValueForAcumulatedEnrollments;

    private Integer enrollmentWeigth;

    private Double ectsCredits;

    private Double weigth;

    private Boolean mandatoryEnrollment;

    private Boolean enrollmentAllowed;
    
    private String acronym;
    
    private String nameEn;
    
    private GradeScale gradeScale;

    private InfoDegreeCurricularPlan infoDegreeCurricularPlan;

    /**
     * @return
     */
    public Boolean getBasic() {
        return basic;
    }

    /**
     * @param basic
     */
    public void setBasic(Boolean basic) {
        this.basic = basic;
    }

    public InfoCurricularCourseEditor() {
    }

    public String getOwnershipType() {
        String result = "";
        if (getBasic() != null) {
            if (getBasic().booleanValue()) {
                result = "Básica";
            } else {
                result = "Não Básica";
            }
        }
        return result;
    }

    /**
     * Returns the code.
     * 
     * @return String
     */
    public String getCode() {
        return code;
    }

    /**
     * Returns the nome.
     * 
     * @return String
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the code.
     * 
     * @param code
     *            The code to set
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Sets the nome.
     * 
     * @param nome
     *            The nome to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the credits.
     * 
     * @return Double
     */
    public Double getCredits() {
        return credits;
    }

    /**
     * Returns the labHours.
     * 
     * @return Double
     */
    public Double getLabHours() {
        return labHours;
    }

    /**
     * Returns the praticalHours.
     * 
     * @return Double
     */
    public Double getPraticalHours() {
        return praticalHours;
    }

    /**
     * Returns the theoPratHours.
     * 
     * @return Double
     */
    public Double getTheoPratHours() {
        return theoPratHours;
    }

    /**
     * Returns the theoreticalHours.
     * 
     * @return Double
     */
    public Double getTheoreticalHours() {
        return theoreticalHours;
    }

    /**
     * Sets the credits.
     * 
     * @param credits
     *            The credits to set
     */
    public void setCredits(Double credits) {
        this.credits = credits;
    }

    /**
     * Sets the labHours.
     * 
     * @param labHours
     *            The labHours to set
     */
    public void setLabHours(Double labHours) {
        this.labHours = labHours;
    }

    /**
     * Sets the praticalHours.
     * 
     * @param praticalHours
     *            The praticalHours to set
     */
    public void setPraticalHours(Double praticalHours) {
        this.praticalHours = praticalHours;
    }

    /**
     * Sets the theoPratHours.
     * 
     * @param theoPratHours
     *            The theoPratHours to set
     */
    public void setTheoPratHours(Double theoPratHours) {
        this.theoPratHours = theoPratHours;
    }

    /**
     * Sets the theoreticalHours.
     * 
     * @param theoreticalHours
     *            The theoreticalHours to set
     */
    public void setTheoreticalHours(Double theoreticalHours) {
        this.theoreticalHours = theoreticalHours;
    }

    /**
     * @return CurricularCourseType
     */
    public CurricularCourseType getType() {
        return type;
    }

    /**
     * Sets the type.
     * 
     * @param type
     *            The type to set
     */
    public void setType(CurricularCourseType type) {
        this.type = type;
    }

    public CurricularCourseExecutionScope getCurricularCourseExecutionScope() {
        return curricularCourseExecutionScope;
    }

    public Boolean getMandatory() {
        return mandatory;
    }

    public void setCurricularCourseExecutionScope(CurricularCourseExecutionScope scope) {
        curricularCourseExecutionScope = scope;
    }

    public void setMandatory(Boolean boolean1) {
        mandatory = boolean1;
    }

    public boolean infoCurricularCourseIsMandatory() {
        return mandatory.booleanValue();
    }

    /**
     * @return Returns the chosen.
     */
    public String getChosen() {
        return chosen;
    }

    /**
     * @param chosen
     *            The chosen to set.
     */
    public void setChosen(String chosen) {
        this.chosen = chosen;
    }

    /**
     * @return Returns the ectsCredits.
     */
    public Double getEctsCredits() {
        return ectsCredits;
    }

    /**
     * @param ectsCredits
     *            The ectsCredits to set.
     */
    public void setEctsCredits(Double ectsCredits) {
        this.ectsCredits = ectsCredits;
    }

    /**
     * @return Returns the enrollmentWeigth.
     */
    public Integer getEnrollmentWeigth() {
        return enrollmentWeigth;
    }

    /**
     * @param enrollmentWeigth
     *            The enrollmentWeigth to set.
     */
    public void setEnrollmentWeigth(Integer enrollmentWeigth) {
        this.enrollmentWeigth = enrollmentWeigth;
    }

    /**
     * @return Returns the maximumValueForAcumulatedEnrollments.
     */
    public Integer getMaximumValueForAcumulatedEnrollments() {
        return maximumValueForAcumulatedEnrollments;
    }

    /**
     * @param maximumValueForAcumulatedEnrollments
     *            The maximumValueForAcumulatedEnrollments to set.
     */
    public void setMaximumValueForAcumulatedEnrollments(Integer maximumValueForAcumulatedEnrollments) {
        this.maximumValueForAcumulatedEnrollments = maximumValueForAcumulatedEnrollments;
    }

    /**
     * @return Returns the minimumValueForAcumulatedEnrollments.
     */
    public Integer getMinimumValueForAcumulatedEnrollments() {
        return minimumValueForAcumulatedEnrollments;
    }

    /**
     * @param minimumValueForAcumulatedEnrollments
     *            The minimumValueForAcumulatedEnrollments to set.
     */
    public void setMinimumValueForAcumulatedEnrollments(Integer minimumValueForAcumulatedEnrollments) {
        this.minimumValueForAcumulatedEnrollments = minimumValueForAcumulatedEnrollments;
    }

    /**
     * @return Returns the weigth.
     */
    public Double getWeigth() {
        return weigth;
    }

    /**
     * @param weigth
     *            The weigth to set.
     */
    public void setWeigth(Double weigth) {
        this.weigth = weigth;
    }

    /**
     * @return Returns the mandatoryEnrollment.
     */
    public Boolean getMandatoryEnrollment() {
        return mandatoryEnrollment;
    }

    /**
     * @param mandatoryEnrollment
     *            The mandatoryEnrollment to set.
     */
    public void setMandatoryEnrollment(Boolean mandatoryEnrollment) {
        this.mandatoryEnrollment = mandatoryEnrollment;
    }

    public Boolean getEnrollmentAllowed() {
        return enrollmentAllowed;
    }

    public void setEnrollmentAllowed(Boolean enrollmentAllowed) {
        this.enrollmentAllowed = enrollmentAllowed;
    }
    
    public String getAcronym(){
    	return acronym;
    }
    
    public void setAcronym(String acronym){
    	this.acronym = acronym;
    }

    public void copyFromDomain(CurricularCourse curricularCourse) {
        super.copyFromDomain(curricularCourse);
        if (curricularCourse != null) {
            setBasic(curricularCourse.getBasic());
            setCode(curricularCourse.getCode());
            setCredits(curricularCourse.getCredits());
            setEctsCredits(curricularCourse.getEctsCredits());
            setEnrollmentWeigth(curricularCourse.getEnrollmentWeigth());
            setLabHours(curricularCourse.getLabHours());
            setMandatory(curricularCourse.getMandatory());
            setMandatoryEnrollment(curricularCourse.getMandatoryEnrollment());
            setMaximumValueForAcumulatedEnrollments(curricularCourse
                    .getMaximumValueForAcumulatedEnrollments());
            setMinimumValueForAcumulatedEnrollments(curricularCourse
                    .getMinimumValueForAcumulatedEnrollments());
            setName(curricularCourse.getName());
            setPraticalHours(curricularCourse.getPraticalHours());
            setTheoPratHours(curricularCourse.getTheoPratHours());
            setTheoreticalHours(curricularCourse.getTheoreticalHours());
            setType(curricularCourse.getType());
            setWeigth(curricularCourse.getWeigth());
            setAcronym(curricularCourse.getAcronym());
            setNameEn(curricularCourse.getNameEn());
			setEnrollmentAllowed(curricularCourse.getEnrollmentAllowed());
			setGradeScale(curricularCourse.getGradeScale());
        }
    }

    public static InfoCurricularCourseEditor newInfoFromDomain(CurricularCourse curricularCourse) {
        InfoCurricularCourseEditor infoCurricularCourse = null;
        if (curricularCourse != null) {
            infoCurricularCourse = new InfoCurricularCourseEditor();
            infoCurricularCourse.copyFromDomain(curricularCourse);
        }
        return infoCurricularCourse;
    }

    public String getNameEn() {
        return nameEn;
    }
    

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }
    
    public String getNameAndCode() {
        return getCode() + " - " + getName();
    }

	public GradeScale getGradeScale() {
		return gradeScale;
	}

	public void setGradeScale(GradeScale gradeScale) {
		this.gradeScale = gradeScale;
	}

	public InfoDegreeCurricularPlan getInfoDegreeCurricularPlan() {
		return infoDegreeCurricularPlan;
	}

	public void setInfoDegreeCurricularPlan(InfoDegreeCurricularPlan infoDegreeCurricularPlan) {
		this.infoDegreeCurricularPlan = infoDegreeCurricularPlan;
	}

}