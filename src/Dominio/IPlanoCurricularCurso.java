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
    
    public String getName();
    public ICurso getCurso();
    
    public void setName(String name);
    public void setCurso(ICurso curso);
}
