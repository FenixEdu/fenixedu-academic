/*
 * ISchoolClass.java
 *
 * Created on 17 de Outubro de 2002, 18:35
 */

package Dominio;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * @author Luis Cruz & Sara Ribeiro
 */

public interface ISchoolClass extends Serializable, IDomainObject {
    public String getNome();

    public Integer getAnoCurricular();

    public IExecutionDegree getExecutionDegree();

    public IExecutionPeriod getExecutionPeriod();

    public List getAssociatedShifts();

    public void setNome(String nome);

    public void setAnoCurricular(Integer anoCurricular);

    void setExecutionDegree(IExecutionDegree executionDegree);

    void setExecutionPeriod(IExecutionPeriod executionPeriod);

    public void setAssociatedShifts(List list);
}