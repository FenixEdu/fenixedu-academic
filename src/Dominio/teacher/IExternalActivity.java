/*
 * Created on 15/Nov/2003
 *
 */
package Dominio.teacher;

import Dominio.IDomainObject;
import Dominio.ITeacher;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *
 */
public interface IExternalActivity extends IDomainObject
{
    public String getActivity();
    public ITeacher getTeacher();
    public void setActivity(String activity);
    public void setTeacher(ITeacher teacher);
}
