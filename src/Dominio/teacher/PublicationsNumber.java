/*
 * Created on 21/Nov/2003
 *
 */
package Dominio.teacher;

import java.util.Date;

import Dominio.DomainObject;
import Dominio.ITeacher;
import Util.PublicationType;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *
 */
public class PublicationsNumber extends DomainObject implements IPublicationsNumber
{
    private Date lastModificationDate;
    private Integer keyTeacher;
    private ITeacher teacher;
    private Integer national;
    private Integer international;
    private PublicationType publicationType;
    
    /**
     * 
     */
    public PublicationsNumber()
    {
        super();
    }
    /**
     * @return Returns the international.
     */
    public Integer getInternational()
    {
        return international;
    }

    /**
     * @param international The international to set.
     */
    public void setInternational(Integer international)
    {
        this.international = international;
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

    /**
     * @return Returns the national.
     */
    public Integer getNational()
    {
        return national;
    }

    /**
     * @param national The national to set.
     */
    public void setNational(Integer national)
    {
        this.national = national;
    }

    /**
     * @return Returns the publicationType.
     */
    public PublicationType getPublicationType()
    {
        return publicationType;
    }

    /**
     * @param publicationType The publicationType to set.
     */
    public void setPublicationType(PublicationType publicationType)
    {
        this.publicationType = publicationType;
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

}
