package net.sourceforge.fenixedu.dataTransferObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.degreeCurricularPlan.DegreeCurricularPlanState;
import net.sourceforge.fenixedu.util.MarkType;

/**
 * @author David Santos
 * 
 * 19/Mar/2003
 */
public class InfoDegreeCurricularPlan extends InfoObject implements Comparable {

    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

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

    // by gedl AT rnl dot IST dot UTL dot PT (August the 3rd, 2003)
    private List curricularCourses;

    public InfoDegreeCurricularPlan() {
    }

    public InfoDegreeCurricularPlan(String name, InfoDegree infoDegree) {
        this();
        setName(name);
        setInfoDegree(infoDegree);
    }

    public InfoDegreeCurricularPlan(String nome, InfoDegree infoDegree, DegreeCurricularPlanState state,
            Date initialDate, Date endDate) {
        setName(nome);
        setInfoDegree(infoDegree);
        setState(state);
        setInitialDate(initialDate);
        setEndDate(endDate);
    }

    public boolean equals(Object obj) {
        boolean result = false;
        if (obj instanceof InfoDegreeCurricularPlan) {
            InfoDegreeCurricularPlan infoCurricularPlan = (InfoDegreeCurricularPlan) obj;
            result = (this.getName().equals(infoCurricularPlan.getName()) && this.getInfoDegree()
                    .equals(infoCurricularPlan.getInfoDegree()));
        }
        return result;
    }

    public String toString() {
        String result = "[" + this.getClass().getName() + ": ";
        result += "name = " + this.name + "; ";
        result += "initialDate = " + this.initialDate + "; ";
        result += "endDate = " + this.endDate + "; ";
        result += "state = " + this.state + "; ";
        result += "NeededCredits = " + this.neededCredits + "; ";
        result += "degree = " + this.infoDegree + ";";
        result += "Numerus Clausus = " + this.numerusClausus + ";";
        result += "Curricular Courses=" + this.curricularCourses + ";";
        result += "Descrição = " + this.description + ";";
        result += "Anotation = " + this.anotation + ";";
        result += "]";
        return result;
    }

    public String getLabel() {
        final String degreeName = infoDegree.getNome();
        final String initialDateString = simpleDateFormat.format(initialDate);

        final int labelSize = degreeName.length() + initialDateString.length() + name.length() + 4;

        final StringBuilder stringBuilder = new StringBuilder(labelSize);
        stringBuilder.append(degreeName);
        stringBuilder.append(" ");
        stringBuilder.append(initialDateString);
        stringBuilder.append(" - ");
        stringBuilder.append(name);
        return stringBuilder.toString();
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

    // alphabetical order
    public int compareTo(Object arg0) {
        InfoDegreeCurricularPlan degreeCurricularPlan = (InfoDegreeCurricularPlan) arg0;
        return this.getName().compareTo(degreeCurricularPlan.getName());
    }

    /**
     * @return
     */
    public List getCurricularCourses() {
        return curricularCourses;
    }

    public void setCurricularCourses(List list) {
        curricularCourses = list;
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

    public void prepareEnglishPresentation(String language) {
        if (language.equals("en")) {
            this.description = this.descriptionEn;
            this.infoDegree.prepareEnglishPresentation(language);
        }
    }

    public void copyFromDomain(IDegreeCurricularPlan plan) {
        super.copyFromDomain(plan);
        if (plan != null) {
            setName(plan.getName());
            setState(plan.getState());
            setInitialDate(plan.getInitialDate());
            setEndDate(plan.getEndDate());
            setMarkType(plan.getMarkType());
            setDescription(plan.getDescription());
            setDescriptionEn(plan.getDescriptionEn());
            setNeededCredits(plan.getNeededCredits());
            setDegreeDuration(plan.getDegreeDuration());
            setMinimalYearForOptionalCourses(plan.getMinimalYearForOptionalCourses());
            setAnotation(plan.getAnotation());
            setInfoDegree(InfoDegree.newInfoFromDomain(plan.getDegree()));
        }
    }

    /**
     * @param plan
     * @return
     */
    public static InfoDegreeCurricularPlan newInfoFromDomain(IDegreeCurricularPlan plan) {
        InfoDegreeCurricularPlan infoDegreeCurricularPlan = null;
        if (plan != null) {
            infoDegreeCurricularPlan = new InfoDegreeCurricularPlan();
            infoDegreeCurricularPlan.copyFromDomain(plan);
        }
        return infoDegreeCurricularPlan;
    }

    public void copyToDomain(InfoDegreeCurricularPlan infoDegreeCurricularPlan,
            IDegreeCurricularPlan degreeCurricularPlan) {
        super.copyToDomain(infoDegreeCurricularPlan, degreeCurricularPlan);
        degreeCurricularPlan.setName(infoDegreeCurricularPlan.getName());
        degreeCurricularPlan.setState(infoDegreeCurricularPlan.getState());
        degreeCurricularPlan.setInitialDate(infoDegreeCurricularPlan.getInitialDate());
        degreeCurricularPlan.setEndDate(infoDegreeCurricularPlan.getEndDate());
        degreeCurricularPlan.setMarkType(infoDegreeCurricularPlan.getMarkType());
        degreeCurricularPlan.setNeededCredits(infoDegreeCurricularPlan.getNeededCredits());
        degreeCurricularPlan.setNumerusClausus(infoDegreeCurricularPlan.getNumerusClausus());
        degreeCurricularPlan.setAnotation(infoDegreeCurricularPlan.getAnotation());
    }

    public String getAnotation() {
        return anotation;
    }

    public void setAnotation(String anotation) {
        this.anotation = anotation;
    }

}