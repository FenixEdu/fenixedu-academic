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
public class ExternalActivity extends DomainObject implements IExternalActivity
{
    private String activity;
    private ITeacher teacher;
    private Integer keyTeacher;
    private Date lastModificationDate;

    /**
     * 
     */
    public ExternalActivity()
    {
    }
    
    public ExternalActivity(Integer idInternal)
    {
        setIdInternal(idInternal);
    }

    /**
     * @return Returns the activity.
     */
    public String getActivity()
    {
        return activity;
    }

    /**
     * @param activity The activity to set.
     */
    public void setActivity(String activity)
    {
        this.activity = activity;
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
