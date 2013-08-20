package net.sourceforge.fenixedu.dataTransferObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.GradeScale;
import net.sourceforge.fenixedu.domain.degree.degreeCurricularPlan.DegreeCurricularPlanState;
import net.sourceforge.fenixedu.util.MarkType;
import pt.utl.ist.fenix.tools.util.DateFormatUtil;
import pt.utl.ist.fenix.tools.util.i18n.Language;

/**
 * @author David Santos
 * 
 *         19/Mar/2003
 */
public class InfoDegreeCurricularPlan extends InfoObject implements Comparable {

    private final DegreeCurricularPlan degreeCurricularPlanDomainReference;

    private final boolean showEnVersion = (Language.getUserLanguage() == Language.en);

    public InfoDegreeCurricularPlan(final DegreeCurricularPlan degreeCurricularPlan) {
        degreeCurricularPlanDomainReference = degreeCurricularPlan;
    }

    public DegreeCurricularPlan getDegreeCurricularPlan() {
        return degreeCurricularPlanDomainReference;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof InfoDegreeCurricularPlan
                && getDegreeCurricularPlan() == ((InfoDegreeCurricularPlan) obj).getDegreeCurricularPlan();
    }

    @Override
    public String toString() {
        return getDegreeCurricularPlan().toString();
    }

    public String getLabel() {
        final String degreeName = getDegreeCurricularPlan().getDegree().getName();
        final String initialDateString = DateFormatUtil.format("dd/MM/yyyy", getDegreeCurricularPlan().getInitialDate());

        final int labelSize = degreeName.length() + initialDateString.length() + getDegreeCurricularPlan().getName().length() + 4;

        final StringBuilder stringBuilder = new StringBuilder(labelSize);
        stringBuilder.append(degreeName);
        stringBuilder.append(" ");
        stringBuilder.append(initialDateString);
        stringBuilder.append(" - ");
        stringBuilder.append(getDegreeCurricularPlan().getName());
        return stringBuilder.toString();
    }

    /**
     * @return Needed Credtis to Finish the Degree
     */
    public Double getNeededCredits() {
        return getDegreeCurricularPlan().getNeededCredits();
    }

    /**
     * @return Date
     */
    public Date getEndDate() {
        return getDegreeCurricularPlan().getEndDate();
    }

    /**
     * @return Date
     */
    public Date getInitialDate() {
        return getDegreeCurricularPlan().getInitialDate();
    }

    /**
     * @return String
     */
    public String getName() {
        return getDegreeCurricularPlan().getName();
    }

    public String getPresentationName() {
        return getDegreeCurricularPlan().getDegree().getName() + " " + getName();
    }

    /**
     * @return DegreeCurricularPlanState
     */
    public DegreeCurricularPlanState getState() {
        return getDegreeCurricularPlan().getState();
    }

    public Integer getDegreeDuration() {
        return getDegreeCurricularPlan().getDegreeDuration();
    }

    public Integer getMinimalYearForOptionalCourses() {
        return getDegreeCurricularPlan().getMinimalYearForOptionalCourses();
    }

    public MarkType getMarkType() {
        return getDegreeCurricularPlan().getMarkType();
    }

    /**
     * @return
     */
    public Integer getNumerusClausus() {
        return getDegreeCurricularPlan().getNumerusClausus();
    }

    // alphabetical order
    @Override
    public int compareTo(Object arg0) {
        InfoDegreeCurricularPlan degreeCurricularPlan = (InfoDegreeCurricularPlan) arg0;
        return this.getName().compareTo(degreeCurricularPlan.getName());
    }

    /**
     * @return
     */
    public List<InfoCurricularCourse> getCurricularCourses() {
        final List<InfoCurricularCourse> infoCurricularCourses = new ArrayList<InfoCurricularCourse>();
        for (final CurricularCourse curricularCourse : getDegreeCurricularPlan().getCurricularCoursesSet()) {
            infoCurricularCourses.add(InfoCurricularCourse.newInfoFromDomain(curricularCourse));
        }
        return infoCurricularCourses;
    }

    public String getDescription() {
        return showEnVersion ? getDescriptionEn() : getDegreeCurricularPlan().getDescription();
    }

    public String getDescriptionEn() {
        return getDegreeCurricularPlan().getDescriptionEn();
    }

    /**
     * @return InfoDegree
     */
    public InfoDegree getInfoDegree() {
        return InfoDegree.newInfoFromDomain(getDegreeCurricularPlan().getDegree());
    }

    /**
     * @param plan
     * @return
     */
    public static InfoDegreeCurricularPlan newInfoFromDomain(DegreeCurricularPlan plan) {
        return plan == null ? null : new InfoDegreeCurricularPlan(plan);
    }

    public String getAnotation() {
        return getDegreeCurricularPlan().getAnotation();
    }

    public List<InfoExecutionDegree> getInfoExecutionDegrees() {
        final List<InfoExecutionDegree> infoExeutionDegrees = new ArrayList<InfoExecutionDegree>();
        for (final ExecutionDegree executionDegree : getDegreeCurricularPlan().getExecutionDegreesSet()) {
            infoExeutionDegrees.add(InfoExecutionDegree.newInfoFromDomain(executionDegree));
        }
        return infoExeutionDegrees;
    }

    public GradeScale getGradeScale() {
        return getDegreeCurricularPlan().getGradeScale();
    }

    @Override
    public String getExternalId() {
        return getDegreeCurricularPlan().getExternalId();
    }

}