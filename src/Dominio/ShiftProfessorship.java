package Dominio;

/**
 * @author Tânia & Alexandra
 *  
 */
public class ShiftProfessorship extends DomainObject implements IShiftProfessorship
{
    private IProfessorship professorship = null;
    private Integer keyProfessorship = null;

    private ITurno shift = null;
    private Integer keyShift = null;

    private Double percentage = null;

    /* construtor */
    public ShiftProfessorship()
    {
    }

    public boolean equals(Object obj)
    {
        boolean resultado = false;
        if (obj instanceof IShiftProfessorship)
        {
            IShiftProfessorship shiftProfessorship = (IShiftProfessorship) obj;

            resultado =
                (getProfessorship().equals(shiftProfessorship.getProfessorship()))
                    && (getShift().equals(shiftProfessorship.getShift()));
        }
        return resultado;
    }

    public String toString()
    {
        String result = "[CREDITS_TEACHER";
        result += ", codInt=" + getIdInternal();
        result += ", shift=" + getShift();
        result += "]";
        return result;
    }

    public Integer getKeyProfessorship()
    {
        return keyProfessorship;
    }
    public void setKeyProfessorship(Integer integer)
    {
        keyProfessorship = integer;
    }

    public IProfessorship getProfessorship()
    {
        return professorship;
    }
    public void setProfessorship(IProfessorship professorship)
    {
        this.professorship = professorship;
    }

    public ITurno getShift()
    {
        return this.shift;
    }
    public void setShift(ITurno shift)
    {
        this.shift = shift;
    }

    public Integer getKeyShift()
    {
        return keyShift;
    }
    public void setKeyShift(Integer integer)
    {
        keyShift = integer;
    }

    public Double getPercentage()
    {
        return this.percentage;
    }
    public void setPercentage(Double percentage)
    {
        this.percentage = percentage;
    }
}
