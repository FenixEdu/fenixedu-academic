/*
 * Created on Nov 16, 2003 by jpvl
 *  
 */
package Dominio.teacher.professorship;

import Dominio.DomainObject;
import Dominio.IProfessorship;
import Dominio.ITurno;

/**
 * @author jpvl
 */
public class ShiftProfessorship extends DomainObject implements IShiftProfessorship
{
    private Integer keyProfessorship;
    private Integer keyShift;
    private Float percentage;
    private IProfessorship professorship;
    private ITurno shift;
    /**
	 * @return Returns the keyProfessorship.
	 */
    public Integer getKeyProfessorship()
    {
        return this.keyProfessorship;
    }

    /**
	 * @return Returns the keyShift.
	 */
    public Integer getKeyShift()
    {
        return this.keyShift;
    }

    /**
	 * @return Returns the percentage.
	 */
    public Float getPercentage()
    {
        return this.percentage;
    }

    /**
	 * @return Returns the professorship.
	 */
    public IProfessorship getProfessorship()
    {
        return this.professorship;
    }

    /**
	 * @return Returns the shift.
	 */
    public ITurno getShift()
    {
        return this.shift;
    }

    /**
	 * @param keyProfessorship
	 *                   The keyProfessorship to set.
	 */
    public void setKeyProfessorship(Integer keyProfessorship)
    {
        this.keyProfessorship = keyProfessorship;
    }

    /**
	 * @param keyShift
	 *                   The keyShift to set.
	 */
    public void setKeyShift(Integer keyShift)
    {
        this.keyShift = keyShift;
    }

    /**
	 * @param percentage
	 *                   The percentage to set.
	 */
    public void setPercentage(Float percentage)
    {
        this.percentage = percentage;
    }

    /**
	 * @param professorship
	 *                   The professorship to set.
	 */
    public void setProfessorship(IProfessorship professorship)
    {
        this.professorship = professorship;
    }

    /**
	 * @param shift
	 *                   The shift to set.
	 */
    public void setShift(ITurno shift)
    {
        this.shift = shift;
    }

}
