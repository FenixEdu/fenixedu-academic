/*
 * Created on 13/Nov/2003
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
public abstract class Career extends DomainObject implements ICareer {

	private String ojbConcreteClass;
	private Integer beginYear;
	private Integer endYear;
	private Integer keyTeacher;
	private ITeacher teacher;
    private Date lastModificationDate;

	public Career() {
		this.ojbConcreteClass = this.getClass().getName();
	}

	/** Creates a new instance of Career */
	public Career(Integer idInternal) {
		setIdInternal(idInternal);
		this.ojbConcreteClass = this.getClass().getName();
	}

	/**
	 * @return Returns the beginYear.
	 */
	public Integer getBeginYear() {
		return beginYear;
	}

	/**
	 * @param beginYear
	 *            The beginYear to set.
	 */
	public void setBeginYear(Integer beginYear) {
		this.beginYear = beginYear;
	}

	/**
	 * @return Returns the endYear.
	 */
	public Integer getEndYear() {
		return endYear;
	}

	/**
	 * @param endYear
	 *            The endYear to set.
	 */
	public void setEndYear(Integer endYear) {
		this.endYear = endYear;
	}
	/**
	 * @return Returns the ojbConcreteClass.
	 */
	public String getOjbConcreteClass() {
		return ojbConcreteClass;
	}

	/**
	 * @param ojbConcreteClass The ojbConcreteClass to set.
	 */
	public void setOjbConcreteClass(String ojbConcreteClass) {
		this.ojbConcreteClass = ojbConcreteClass;
	}
	/**
	 * @return Returns the keyTeacher.
	 */
	public Integer getKeyTeacher() {
		return keyTeacher;
	}

	/**
	 * @param keyTeacher The keyTeacher to set.
	 */
	public void setKeyTeacher(Integer keyTeacher) {
		this.keyTeacher = keyTeacher;
	}

	/**
	 * @return Returns the teacher.
	 */
	public ITeacher getTeacher() {
		return teacher;
	}

	/**
	 * @param teacher The teacher to set.
	 */
	public void setTeacher(ITeacher teacher) {
		this.teacher = teacher;
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
