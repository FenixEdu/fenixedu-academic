/*
 * IStudent.java
 *
 * Created on 28 of December 2002, 17:05
 */

package Dominio;

import Util.TipoCurso;

/**
 *
 * @author Ricardo Nortadas
 */

public interface IStudent {
    Integer getNumber();
    Integer getState();
    IPessoa getPerson();
    TipoCurso getDegreeType();
    
    void setNumber(Integer number);
    void setState(Integer state);
    void setPerson(IPessoa person);
    void setDegreeType(TipoCurso degreeType);
}
