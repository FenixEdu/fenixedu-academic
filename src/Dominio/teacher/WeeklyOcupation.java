/*
 * Created on 15/Nov/2003
 *
 */
package Dominio.teacher;

import java.util.Date;

import Dominio.DomainObject;
import Dominio.ITeacher;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *
 */
public class WeeklyOcupation extends DomainObject implements IWeeklyOcupation
{
    private Integer research;
    private Integer management;
    private Integer other;
    private ITeacher teacher;
    private Integer keyTeacher;
    private Date lastModificationDate;

    /**
     * 
     */
    public WeeklyOcupation()
    {
    }
    
    public WeeklyOcupation(Integer idInternal)
    {
        setIdInternal(idInternal);
    }
    
    /**
     * @return Returns the other.
     */
    public Integer getOther()
    {
        return other;
    }

    /**
     * @param other The other to set.
     */
    public void setOther(Integer other)
    {
        this.other = other;
    }

    /**
     * @return Returns the research.
     */
    public Integer getResearch()
    {
        return research;
    }

    /**
     * @param research The research to set.
     */
    public void setResearch(Integer research)
    {
        this.research = research;
    }
    /**
     * @return Returns the teacher.
     */
    public ITeacher getTeacher()
    {
        return teacher;
    }

    /**
     * @param teacher The teacher to set.
     */
    public void setTeacher(ITeacher teacher)
    {
        this.teacher = teacher;
    }

    /**
     * @return Returns the keyTeacher.
     */
    public Integer getKeyTeacher()
    {
        return keyTeacher;
    }

    /**
     * @param keyTeacher The keyTeacher to set.
     */
    public void setKeyTeacher(Integer keyTeacher)
    {
        this.keyTeacher = keyTeacher;
    }

    /**
     * @return Returns the management.
     */
    public Integer getManagement()
    {
        return management;
    }

    /**
     * @param management The management to set.
     */
    public void setManagement(Integer management)
    {
        this.management = management;
    }

    /**
     * @return Returns the lastModificationDate.
     */
    public Date getLastModificationDate()
    {
        return lastModificationDate;
    }

    /**
     * @param lastModificationDate The lastModificationDate to set.
     */
    public void setLastModificationDate(Date lastModificationDate)
    {
        this.lastModificationDate = lastModificationDate;
    }

}
