package net.sourceforge.fenixedu.dataTransferObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DegreeInfo;
import net.sourceforge.fenixedu.domain.GradeScale;

public class InfoDegree extends InfoObject implements Comparable {

	private final Degree degree;

	private boolean showEnVersion = false;

    public InfoDegree(final Degree degree) {
    	this.degree = degree;
    }

    public String toString() {
    	return degree.toString();
    }

    public String getSigla() {
        return degree.getSigla();
    }

    public String getNome() {
    	return showEnVersion ? degree.getNameEn() : degree.getNome();
    }

    public String getNameEn() {
    	return degree.getNameEn();
    }

    public boolean equals(Object obj) {
    	return obj instanceof InfoDegree && degree.equals(((InfoDegree) obj).degree); 
    }

    public boolean isBolonhaDegree() {
    	return degree.isBolonhaDegree();
    }

    public Enum getTipoCurso() {
    	return degree.getTipoCurso();
    }

    public List getInfoDegreeCurricularPlans() {
    	final List<InfoDegreeCurricularPlan> infoDegreeCurricularPlans = new ArrayList<InfoDegreeCurricularPlan>();
    	for (final DegreeCurricularPlan degreeCurricularPlan : degree.getDegreeCurricularPlansSet()) {
    		infoDegreeCurricularPlans.add(InfoDegreeCurricularPlan.newInfoFromDomain(degreeCurricularPlan));
    	}
    	return infoDegreeCurricularPlans;
    }

    public int compareTo(Object arg0) {
        InfoDegree degree = (InfoDegree) arg0;
        return this.getNome().compareTo(degree.getNome());
    }

    public List getInfoDegreeInfos() {
    	final List<InfoDegreeInfo> infoDegreeInfos = new ArrayList<InfoDegreeInfo>();
    	for (final DegreeInfo degreeInfo : degree.getDegreeInfosSet()) {
    		infoDegreeInfos.add(InfoDegreeInfo.newInfoFromDomain(degreeInfo));
    	}
    	return infoDegreeInfos;
    }

    public GradeScale getGradeScale() {
    	return degree.getGradeScale();
    }

    public static InfoDegree newInfoFromDomain(final Degree degree) {
    	return degree == null ? null : new InfoDegree(degree);
    }

    public void prepareEnglishPresentation(Locale locale) {
        if (locale.getLanguage().equals(Locale.ENGLISH.getLanguage())) {
            if (getNameEn() != null && getNameEn().length() != 0) {
                showEnVersion = true;
            }
        }
    }

	@Override
	public Integer getIdInternal() {
		return degree.getIdInternal();
	}

    @Override
    public void setIdInternal(Integer integer) {
        throw new Error("Method should not be called!");
    }

}
