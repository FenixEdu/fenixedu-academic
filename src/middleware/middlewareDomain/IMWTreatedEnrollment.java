package middleware.middlewareDomain;


/**
 * @author David Santos
 * Feb 5, 2004
 */
public interface IMWTreatedEnrollment /*extends IDomainObject*/
{
	public abstract Integer getBranchcode();
	public abstract void setBranchcode(Integer param);
	public abstract Integer getCurricularcoursesemester();
	public abstract void setCurricularcoursesemester(Integer param);
	public abstract Integer getCurricularcourseyear();
	public abstract void setCurricularcourseyear(Integer param);
	public abstract Integer getDegreecode();
	public abstract void setDegreecode(Integer param);
	public abstract Integer getEnrolmentyear();
	public abstract void setEnrolmentyear(Integer param);
	public abstract java.sql.Date getExamdate();
	public abstract void setExamdate(java.sql.Date param);
	public abstract String getGrade();
	public abstract void setGrade(String param);
	public abstract Integer getIdinternal();
	public abstract void setIdinternal(Integer param);
	public abstract Integer getNumber();
	public abstract void setNumber(Integer param);
	public abstract String getRemarks();
	public abstract void setRemarks(String param);
	public abstract Integer getSeason();
	public abstract void setSeason(Integer param);
	public abstract Integer getTeachernumber();
	public abstract void setTeachernumber(Integer param);
	public abstract String getUniversitycode();
	public abstract void setUniversitycode(String param);
	public abstract String getCoursecode();
	public abstract void setCoursecode(String coursecode);
	public abstract String toString();
	public abstract String toFlatString();
}