package net.sourceforge.fenixedu.dataTransferObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.GradeScale;
import net.sourceforge.fenixedu.domain.degree.DegreeType;

public class InfoDegree extends InfoObject implements Comparable {

    protected String sigla;

    protected String nome;

    protected String nameEn;

    protected boolean isBolonhaDegree;

    protected Enum tipoCurso;

    private List infoDegreeCurricularPlans = null;

    private List infoDegreeInfos = null;

	private List infoCoordinators;
	
	private GradeScale gradeScale;

    public InfoDegree() {
    }

    public InfoDegree(String sigla, String nome) {
        setSigla(sigla);
        setNome(nome);
    }

    public InfoDegree(String sigla, String nome, String nameEn, DegreeType degreeType) {
        setSigla(sigla);
        setNome(nome);
        setNameEn(nameEn);
        setTipoCurso(degreeType);
    }
    
    public String toString() {
        String result = "[INFOCURSO";
        result += ", sigla=" + this.sigla;
        result += ", nome=" + this.nome;
        result += ", tipoCurso=" + this.tipoCurso;
        result += "]";
        return result;
    }

    public String getSigla() {
        return this.sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }
    
    public boolean equals(Object obj) {
        boolean resultado = false;
        if (obj instanceof InfoDegree) {
            InfoDegree iL = (InfoDegree) obj;
            resultado = (getSigla().equals(iL.getSigla()));
        }
        return resultado;
    }

    public boolean isBolonhaDegree() {
        return isBolonhaDegree;
    }

    public Enum getTipoCurso() {
        return tipoCurso;
    }

    public void setTipoCurso(Enum tipoCurso) {
        this.tipoCurso = tipoCurso;
    }

    public List getInfoDegreeCurricularPlans() {
        return infoDegreeCurricularPlans;
    }

    public void setInfoDegreeCurricularPlans(List infoDegreeCurricularPlans) {
        this.infoDegreeCurricularPlans = infoDegreeCurricularPlans;
    }

    public int compareTo(Object arg0) {
        InfoDegree degree = (InfoDegree) arg0;
        return this.getNome().compareTo(degree.getNome());
    }

    public List getInfoDegreeInfos() {
        return infoDegreeInfos;
    }

    public void setInfoDegreeInfos(List infoDegreeInfos) {
        this.infoDegreeInfos = infoDegreeInfos;
    }
    
    public List getInfoCoordinators() {
        return infoCoordinators;
    }

    public void setInfoCoordinators(List infoCoordinators) {
        this.infoCoordinators = infoCoordinators;
    }

    public GradeScale getGradeScale() {
        return this.gradeScale;
    }

    public void setGradeScale(GradeScale gradeScale) {
        this.gradeScale = gradeScale;
    }

    public static InfoDegree newInfoFromDomain(Degree degree) {
        InfoDegree infoDegree = null;
        if (degree != null) {
            infoDegree = new InfoDegree();
            infoDegree.copyFromDomain(degree);
        }
        return infoDegree;
    }

    public void copyFromDomain(Degree degree) {
        super.copyFromDomain(degree);
        if (degree != null) {
            setSigla(degree.getSigla());
            setTipoCurso((degree.isBolonhaDegree()) ? degree.getBolonhaDegreeType() : degree.getTipoCurso());
            setNome(degree.getNome());
            setNameEn(degree.getNameEn());
            setGradeScale(degree.getGradeScale());

            List<InfoDegreeCurricularPlan> degreeCurricularPlans = new ArrayList<InfoDegreeCurricularPlan>();
            for (DegreeCurricularPlan dcp : degree.getDegreeCurricularPlans()) {
                degreeCurricularPlans.add(InfoDegreeCurricularPlan.newInfoFromDomain(dcp));
            }
            setInfoDegreeCurricularPlans(degreeCurricularPlans);
        }
    }

    public void prepareEnglishPresentation(Locale locale) {
        if (locale.getLanguage().equals(Locale.ENGLISH.getLanguage())) {
            if (!(this.nameEn == null) && !(this.nameEn.length() == 0) && !(this.nameEn == "")) {
                this.nome = this.nameEn;
            }
        }
    }

}
