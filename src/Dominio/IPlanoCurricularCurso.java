/*
 * IPlanoCurricularCurso.java
 *
 * Created on 20 de Dezembro de 2002, 15:45
 */

package Dominio;


/**
 *
 * @author  Nuno Nunes & Joana Mota
 */

public interface IPlanoCurricularCurso {
    public java.lang.String getNome();
    public java.lang.String getSigla();
    public ICurso getCurso();
    
    public void setNome(java.lang.String nome);
    public void setSigla(java.lang.String sigla);
    public void setCurso(ICurso curso);
}
