/*
 * Created on Feb 18, 2004
 *  
 */
package Dominio.student;

import Dominio.ICurso;
import Dominio.IDomainObject;
import Dominio.IExecutionYear;
import Dominio.IStudent;
import Util.DelegateType;

/**
 * @author <a href="mailto:lesa@mega.ist.utl.pt">Leonor Almeida</a>
 * @author <a href="mailto:shmc@mega.ist.utl.pt">Sergio Montelobo</a>
 *  
 */
public interface IDelegate extends IDomainObject
{
    public ICurso getDegree();
    public void setDegree(ICurso degree);
    public IExecutionYear getExecutionYear();
    public void setExecutionYear(IExecutionYear executionYear);
    public IStudent getStudent();
    public void setStudent(IStudent student);
    public DelegateType getType();
    public void setType(DelegateType type);
}
