/*
 * Created on Feb 18, 2004
 *  
 */
package Dominio.student;

import Dominio.IDegree;
import Dominio.IDomainObject;
import Dominio.IExecutionYear;
import Dominio.IStudent;
import Util.DelegateYearType;

/**
 * @author <a href="mailto:lesa@mega.ist.utl.pt">Leonor Almeida </a>
 * @author <a href="mailto:shmc@mega.ist.utl.pt">Sergio Montelobo </a>
 *  
 */
public interface IDelegate extends IDomainObject {
    public IDegree getDegree();

    public void setDegree(IDegree degree);

    public IExecutionYear getExecutionYear();

    public void setExecutionYear(IExecutionYear executionYear);

    public IStudent getStudent();

    public void setStudent(IStudent student);

    public DelegateYearType getYearType();

    public void setYearType(DelegateYearType yearType);

    public Boolean getType();

    public void setType(Boolean type);
}