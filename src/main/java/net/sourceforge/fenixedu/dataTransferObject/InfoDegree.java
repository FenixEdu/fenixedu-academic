package net.sourceforge.fenixedu.dataTransferObject;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DegreeInfo;
import net.sourceforge.fenixedu.domain.GradeScale;
import net.sourceforge.fenixedu.domain.degree.DegreeType;

import org.apache.commons.lang.StringUtils;

import pt.utl.ist.fenix.tools.util.i18n.Language;

public class InfoDegree extends InfoObject implements Comparable {

    private final Degree degreeDomainReference;

    private boolean showEnVersion = (Language.getUserLanguage() == Language.en);

    public InfoDegree(final Degree degree) {
        degreeDomainReference = degree;
    }

    public Degree getDegree() {
        return degreeDomainReference;
    }

    @Override
    public String toString() {
        return getDegree().toString();
    }

    public String getSigla() {
        return getDegree().getSigla();
    }

    public String getNome() {
        return showEnVersion && !StringUtils.isEmpty(getNameEn()) ? getNameEn() : getDegree().getNome();
    }

    public String getPresentationName() {
        return getDegree().getPresentationName();
    }

    public String getNameEn() {
        return getDegree().getNameEn();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof InfoDegree && getDegree().equals(((InfoDegree) obj).getDegree());
    }

    public boolean isBolonhaDegree() {
        return getDegree().isBolonhaDegree();
    }

    public DegreeType getDegreeType() {
        return getDegree().getDegreeType();
    }

    public DegreeType getTipoCurso() {
        return getDegree().getDegreeType();
    }

    public List<InfoDegreeCurricularPlan> getInfoDegreeCurricularPlans() {
        final List<InfoDegreeCurricularPlan> infoDegreeCurricularPlans = new ArrayList<InfoDegreeCurricularPlan>();
        for (final DegreeCurricularPlan degreeCurricularPlan : getDegree().getDegreeCurricularPlansSet()) {
            infoDegreeCurricularPlans.add(InfoDegreeCurricularPlan.newInfoFromDomain(degreeCurricularPlan));
        }
        return infoDegreeCurricularPlans;
    }

    @Override
    public int compareTo(Object arg0) {
        InfoDegree degree = (InfoDegree) arg0;
        return this.getNome().compareTo(degree.getNome());
    }

    public List<InfoDegreeInfo> getInfoDegreeInfos() {
        final List<InfoDegreeInfo> infoDegreeInfos = new ArrayList<InfoDegreeInfo>();
        for (final DegreeInfo degreeInfo : getDegree().getDegreeInfosSet()) {
            infoDegreeInfos.add(InfoDegreeInfo.newInfoFromDomain(degreeInfo));
        }
        return infoDegreeInfos;
    }

    public GradeScale getGradeScale() {
        return getDegree().getGradeScale();
    }

    public static InfoDegree newInfoFromDomain(final Degree degree) {
        return degree == null ? null : new InfoDegree(degree);
    }

    @Override
    public Integer getIdInternal() {
        return getDegree().getIdInternal();
    }

    @Override
    public void setIdInternal(Integer integer) {
        throw new Error("Method should not be called!");
    }

}
