package DataBeans;

import java.util.List;

import Dominio.ICurricularSemester;

/**
 * @author dcs-rjao
 * 
 * 19/Mar/2003
 */

public class InfoCurricularSemester extends InfoObject {

    private Integer semester;

    private InfoCurricularYear infoCurricularYear;

    //	private List associatedInfoCurricularCourses;
    private List infoScopes;

    public InfoCurricularSemester() {
        setSemester(null);
        //		setAssociatedInfoCurricularCourses(null);
        setInfoScopes(null);
        setInfoCurricularYear(null);
    }

    public InfoCurricularSemester(Integer semester,
            InfoCurricularYear curricularYear) {
        this();
        setSemester(semester);
        setInfoCurricularYear(curricularYear);
    }

    public boolean equals(Object obj) {
        boolean resultado = false;
        if (obj instanceof InfoCurricularSemester) {
            InfoCurricularSemester infoCurricularSemester = (InfoCurricularSemester) obj;
            resultado = (this.getSemester().equals(
                    infoCurricularSemester.getSemester()) && (this
                    .getInfoCurricularYear().equals(infoCurricularSemester
                    .getInfoCurricularYear())));
        }
        return resultado;
    }

    public String toString() {
        String result = "[" + this.getClass().getName() + ": ";
        result += "semester = " + this.getSemester() + "; ";
        result += "infoCurricularYear = " + this.getInfoCurricularYear() + "; ";
        result += "idInternal = " + this.getIdInternal() + "]";
        return result;
    }

    //	/**
    //	 * @return List
    //	 */
    //	public List getAssociatedInfoCurricularCourses() {
    //		return associatedInfoCurricularCourses;
    //	}

    /**
     * @return ICurricularYear
     */
    public InfoCurricularYear getInfoCurricularYear() {
        return infoCurricularYear;
    }

    /**
     * @return Integer
     */
    public Integer getSemester() {
        return semester;
    }

    //	/**
    //	 * Sets the associatedInfoCurricularCourses.
    //	 * @param associatedInfoCurricularCourses The
    // associatedInfoCurricularCourses to set
    //	 */
    //	public void setAssociatedInfoCurricularCourses(List
    // associatedCurricularCourses) {
    //		this.associatedInfoCurricularCourses = associatedCurricularCourses;
    //	}

    /**
     * Sets the infoCurricularYear.
     * 
     * @param infoCurricularYear
     *            The infoCurricularYear to set
     */
    public void setInfoCurricularYear(InfoCurricularYear curricularYear) {
        this.infoCurricularYear = curricularYear;
    }

    /**
     * Sets the semester.
     * 
     * @param semester
     *            The semester to set
     */
    public void setSemester(Integer semester) {
        this.semester = semester;
    }

    /**
     * @return List
     */
    public List getInfoScopes() {
        return infoScopes;
    }

    /**
     * Sets the infoScopes.
     * 
     * @param infoScopes
     *            The infoScopes to set
     */
    public void setInfoScopes(List infoScopes) {
        this.infoScopes = infoScopes;
    }

    public void copyFromDomain(ICurricularSemester curricularSemester) {
        super.copyFromDomain(curricularSemester);
        if (curricularSemester != null) {
            setSemester(curricularSemester.getSemester());
        }
    }

    public static InfoCurricularSemester newInfoFromDomain(
            ICurricularSemester curricularSemester) {
        InfoCurricularSemester infoCurricularSemester = null;
        if (curricularSemester != null) {
            infoCurricularSemester = new InfoCurricularSemester();
            infoCurricularSemester.copyFromDomain(curricularSemester);
        }
        return infoCurricularSemester;
    }
}