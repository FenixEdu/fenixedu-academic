/*
 * Created on 7/Nov/2003
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package DataBeans.teacher;

import DataBeans.ISiteComponent;
import DataBeans.InfoObject;
import DataBeans.InfoTeacher;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class InfoWeeklyOcupation extends InfoObject implements ISiteComponent {

	private InfoTeacher infoTeacher;
    private Integer research;
    private Integer other;

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

    public boolean equals(Object obj) {
        boolean resultado = false;
        if (obj instanceof InfoWeeklyOcupation) {
            resultado = getInfoTeacher().equals(((InfoWeeklyOcupation) obj).getInfoTeacher());
        }
        return resultado;
    }
}
