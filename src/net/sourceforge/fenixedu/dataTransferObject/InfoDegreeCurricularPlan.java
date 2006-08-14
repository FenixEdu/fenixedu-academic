package net.sourceforge.fenixedu.dataTransferObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.GradeScale;
import net.sourceforge.fenixedu.domain.degree.degreeCurricularPlan.DegreeCurricularPlanState;
import net.sourceforge.fenixedu.util.DateFormatUtil;
import net.sourceforge.fenixedu.util.MarkType;

/**
 * @author David Santos
 * 
 * 19/Mar/2003
 */
public class InfoDegreeCurricularPlan extends InfoObject implements Comparable {

	private DegreeCurricularPlan degreeCurricularPlan;

	private boolean showEnVersion = false;

    public InfoDegreeCurricularPlan(final DegreeCurricularPlan degreeCurricularPlan) {
    	this.degreeCurricularPlan = degreeCurricularPlan;
    }

    public boolean equals(Object obj) {
    	return obj instanceof InfoDegreeCurricularPlan && degreeCurricularPlan == ((InfoDegreeCurricularPlan) obj).degreeCurricularPlan;
    }

    public String toString() {
    	return degreeCurricularPlan.toString();
    }

    public String getLabel() {
        final String degreeName = degreeCurricularPlan.getDegree().getName();
        final String initialDateString = DateFormatUtil.format("dd/MM/yyyy", degreeCurricularPlan.getInitialDate());

        final int labelSize = degreeName.length() + initialDateString.length() + degreeCurricularPlan.getName().length() + 4;

        final StringBuilder stringBuilder = new StringBuilder(labelSize);
        stringBuilder.append(degreeName);
        stringBuilder.append(" ");
        stringBuilder.append(initialDateString);
        stringBuilder.append(" - ");
        stringBuilder.append(degreeCurricularPlan.getName());
        return stringBuilder.toString();
    }

    /**
     * @return Needed Credtis to Finish the Degree
     */
    public Double getNeededCredits() {
        return degreeCurricularPlan.getNeededCredits();
    }

    /**
     * @return Date
     */
    public Date getEndDate() {
        return degreeCurricularPlan.getEndDate();
    }

    /**
     * @return Date
     */
    public Date getInitialDate() {
        return degreeCurricularPlan.getInitialDate();
    }

    /**
     * @return String
     */
    public String getName() {
        return degreeCurricularPlan.getName();
    }

    public String getPresentationName() {
        return degreeCurricularPlan.getDegree().getName() + " " + getName();
    }

    /**
     * @return DegreeCurricularPlanState
     */
    public DegreeCurricularPlanState getState() {
        return degreeCurricularPlan.getState();
    }

    public Integer getDegreeDuration() {
        return degreeCurricularPlan.getDegreeDuration();
    }

    public Integer getMinimalYearForOptionalCourses() {
        return degreeCurricularPlan.getMinimalYearForOptionalCourses();
    }

    public MarkType getMarkType() {
        return degreeCurricularPlan.getMarkType();
    }

    /**
     * @return
     */
    public Integer getNumerusClausus() {
        return degreeCurricularPlan.getNumerusClausus();
    }

    // alphabetical order
    public int compareTo(Object arg0) {
        InfoDegreeCurricularPlan degreeCurricularPlan = (InfoDegreeCurricularPlan) arg0;
        return this.getName().compareTo(degreeCurricularPlan.getName());
    }

    /**
     * @return
     */
    public List<InfoCurricularCourse> getCurricularCourses() {
    	final List<InfoCurricularCourse> infoCurricularCourses = new ArrayList<InfoCurricularCourse>();
    	for (final CurricularCourse curricularCourse : degreeCurricularPlan.getCurricularCoursesSet()) {
    		infoCurricularCourses.add(InfoCurricularCourse.newInfoFromDomain(curricularCourse));
    	}
        return infoCurricularCourses;
    }

    public String getDescription() {
    	return showEnVersion ? getDescriptionEn() : degreeCurricularPlan.getDescription();
    }

    public String getDescriptionEn() {
        return degreeCurricularPlan.getDescriptionEn();
    }

    /**
     * @return InfoDegree
     */
    public InfoDegree getInfoDegree() {
    	final InfoDegree infoDegree = InfoDegree.newInfoFromDomain(degreeCurricularPlan.getDegree());
    	if (showEnVersion) { 
    		infoDegree.prepareEnglishPresentation(Locale.ENGLISH);
    	}
        return infoDegree;
    }


    public void prepareEnglishPresentation(Locale locale) {
        if (locale.getLanguage().equals(Locale.ENGLISH.getLanguage())) {
        	showEnVersion = true;
        }
    }

    /**
     * @param plan
     * @return
     */
    public static InfoDegreeCurricularPlan newInfoFromDomain(DegreeCurricularPlan plan) {
    	return plan == null ? null : new InfoDegreeCurricularPlan(plan);
    }

    public String getAnotation() {
        return degreeCurricularPlan.getAnotation();
    }

    public List<InfoExecutionDegree> getInfoExecutionDegrees() {
    	final List<InfoExecutionDegree> infoExeutionDegrees = new ArrayList<InfoExecutionDegree>();
    	for (final ExecutionDegree executionDegree : degreeCurricularPlan.getExecutionDegreesSet()) {
    		infoExeutionDegrees.add(InfoExecutionDegree.newInfoFromDomain(executionDegree));
    	}
        return infoExeutionDegrees;
    }

	public GradeScale getGradeScale() {
		return degreeCurricularPlan.getGradeScale();
	}

	@Override
	public Integer getIdInternal() {
		return degreeCurricularPlan.getIdInternal();
	}

}