/*
 * InfoDegree.java
 * 
 * Created on 25 de Novembro de 2002, 1:07
 */
package net.sourceforge.fenixedu.dataTransferObject;

import java.util.List;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.IDegree;
import net.sourceforge.fenixedu.util.TipoCurso;

/**
 * @author tfc130
 */
public class InfoDegree extends InfoObject implements Comparable {

    protected String sigla;

    protected String nome;

    protected TipoCurso tipoCurso;

    private List infoDegreeCurricularPlans;

    private List infoDegreeInfos; //added by Tânia Pousão

    public InfoDegree() {
    }

    public InfoDegree(String sigla, String nome) {
        setSigla(sigla);
        setNome(nome);
    }

    public InfoDegree(String sigla, String nome, TipoCurso degreeType) {
        setSigla(sigla);
        setNome(nome);
        setTipoCurso(degreeType);
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

    public boolean equals(Object obj) {
        boolean resultado = false;
        if (obj instanceof InfoDegree) {
            InfoDegree iL = (InfoDegree) obj;
            resultado = (getSigla().equals(iL.getSigla()));
        }
        return resultado;
    }

    public String toString() {
        String result = "[INFOCURSO";
        result += ", sigla=" + this.sigla;
        result += ", nome=" + this.nome;
        result += ", tipoCurso=" + this.tipoCurso;
        result += "]";
        return result;
    }

    /**
     * @return TipoCurso
     */
    public TipoCurso getTipoCurso() {
        return tipoCurso;
    }

    /**
     * Sets the tipoCurso.
     * 
     * @param degreeType
     *            The degreeType to set
     */
    public void setTipoCurso(TipoCurso tipoCurso) {
        this.tipoCurso = tipoCurso;
    }

    /**
     * @return List
     */
    public List getInfoDegreeCurricularPlans() {
        return infoDegreeCurricularPlans;
    }

    /**
     * Sets the infoDegreeCurricularPlans.
     * 
     * @param infoDegreeCurricularPlans
     *            The infoDegreeCurricularPlans to set
     */
    public void setInfoDegreeCurricularPlans(List infoDegreeCurricularPlans) {
        this.infoDegreeCurricularPlans = infoDegreeCurricularPlans;
    }

    //alphabetic order
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

    public void copyFromDomain(IDegree degree) {
        super.copyFromDomain(degree);
        if (degree != null) {
            setNome(degree.getNome());
            setSigla(degree.getSigla());
            setTipoCurso(degree.getTipoCurso());
        }
    }

    /**
     * @param degree
     * @return
     */
    public static InfoDegree newInfoFromDomain(IDegree degree) {
        InfoDegree infoDegree = null;
        if (degree != null) {
            infoDegree = new InfoDegree();
            infoDegree.copyFromDomain(degree);
        }
        return infoDegree;
    }

    public void copyToDomain(InfoDegree infoDegree, IDegree degree) {
        super.copyToDomain(infoDegree, degree);
        degree.setNome(infoDegree.getNome());
        degree.setSigla(infoDegree.getSigla());
        degree.setTipoCurso(infoDegree.getTipoCurso());
    }

    public static IDegree newDomainFromInfo(InfoDegree infoDegree) {
        IDegree degree = null;
        if (infoDegree != null) {
            degree = new Degree();
            infoDegree.copyToDomain(infoDegree, degree);
        }
        return degree;
    }
}