/*
 * Created on Nov 16, 2003 by jpvl
 *  
 */
package Dominio.teacher.professorship;

import Dominio.IDomainObject;
import Dominio.IProfessorship;
import Dominio.ITurno;

/**
 * @author jpvl
 */
public interface IShiftProfessorship extends IDomainObject
{

    public Float getPercentage();
    public void setPercentage(Float percentage);

    public IProfessorship getProfessorship();
    public void setProfessorship(IProfessorship professorship);

    public ITurno getShift();
    public void setShift(ITurno shift);

}
