/*
 * Created on 7/Nov/2003
 * 
 */
package DataBeans.teacher;

import java.util.Date;

import DataBeans.ISiteComponent;
import DataBeans.InfoObject;
import DataBeans.InfoTeacher;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 * 
 */
public class InfoWeeklyOcupation extends InfoObject implements ISiteComponent {

	private InfoTeacher infoTeacher;
    private Integer research;
    private Integer management;
    private Integer lecture;
    private Integer support;
    private Integer other;
    private Date lastModificationDate;

	public InfoWeeklyOcupation() {
	}

    /**
     * @return Returns the infoTeacher.
     */
    public InfoTeacher getInfoTeacher()
    {
        return infoTeacher;
    }

    /**
     * @param infoTeacher The infoTeacher to set.
     */
    public void setInfoTeacher(InfoTeacher infoTeacher)
    {
        this.infoTeacher = infoTeacher;
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
    
    public boolean equals(Object obj) {
        boolean resultado = false;
        if (obj instanceof InfoWeeklyOcupation) {
            resultado = getInfoTeacher().equals(((InfoWeeklyOcupation) obj).getInfoTeacher());
        }
        return resultado;
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
     * @return Returns the lecture.
     */
    public Integer getLecture()
    {
        return lecture;
    }

    /**
     * @param lecture The lecture to set.
     */
    public void setLecture(Integer lecture)
    {
        this.lecture = lecture;
    }

    /**
     * @return Returns the support.
     */
    public Integer getSupport()
    {
        return support;
    }

    /**
     * @param support The support to set.
     */
    public void setSupport(Integer support)
    {
        this.support = support;
    }

}
