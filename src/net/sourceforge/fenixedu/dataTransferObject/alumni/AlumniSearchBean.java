package net.sourceforge.fenixedu.dataTransferObject.alumni;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.student.Registration;

public class AlumniSearchBean implements Serializable {

    private int totalItems;

    private ExecutionYear firstExecutionYear;
    private ExecutionYear finalExecutionYear;
    private String name;
    private Integer studentNumber;
    private String documentIdNumber;
    private DegreeType degreeType;
    private Degree degree;
    private List<Registration> alumni;

    public AlumniSearchBean() {
	this("", ExecutionYear.readFirstExecutionYear(), ExecutionYear.readLastExecutionYear());
    }

    public AlumniSearchBean(DegreeType degreeType, String name, ExecutionYear firstYear, ExecutionYear lastYear) {
	this(name, firstYear, lastYear);
	setDegreeType(degreeType);
    }

    public AlumniSearchBean(String name, ExecutionYear firstYear, ExecutionYear lastYear) {
	setName(name);
	setFirstExecutionYear(firstYear);
	setFinalExecutionYear(lastYear);
	setDegreeType(null);
    }

    public ExecutionYear getFirstExecutionYear() {
	return (this.firstExecutionYear != null) ? this.firstExecutionYear : ExecutionYear.readFirstExecutionYear();
    }

    public void setFirstExecutionYear(ExecutionYear executionYear) {
	this.firstExecutionYear = executionYear;
    }

    public ExecutionYear getFinalExecutionYear() {
	return (this.finalExecutionYear != null) ? this.finalExecutionYear : ExecutionYear.readLastExecutionYear();
    }

    public void setFinalExecutionYear(ExecutionYear executionYear) {
	this.finalExecutionYear = executionYear;
    }

    public List<Registration> getAlumni() {
	if (this.alumni == null)
	    return null;
	List<Registration> alumni = new ArrayList<Registration>();
	for (Registration reference : this.alumni) {
	    alumni.add(reference);
	}
	return alumni;
    }

    public void setAlumni(List<Registration> alumni) {
	if (this.alumni == null) {
	    this.alumni = new ArrayList<Registration>(alumni.size());
	} else {
	    this.alumni.clear();
	}

	for (Registration person : alumni) {
	    this.alumni.add(person);
	}
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public DegreeType getDegreeType() {
	return degreeType;
    }

    public void setDegreeType(DegreeType arg) {
	this.degreeType = arg;
    }

    public String getSearchElementsAsParameters() {
	String urlParameters = "&amp;beansearch=" + this.getDegreeType() + ":" + this.getName() + ":";
	urlParameters += (this.getFirstExecutionYear() == null ? "null" : this.getFirstExecutionYear().getIdInternal()) + ":";
	urlParameters += (this.getFinalExecutionYear() == null ? "null" : this.getFinalExecutionYear().getIdInternal());
	return urlParameters;
    }

    public static AlumniSearchBean getBeanFromParameters(String requestParameter) {
	final String[] values = requestParameter.split(":");
	final String firstYear = values[2];
	final String finalYear = values[3];

	ExecutionYear first = (firstYear.equals("null") ? ExecutionYear.readFirstExecutionYear() : RootDomainObject.getInstance()
		.readExecutionYearByOID(Integer.valueOf(firstYear)));
	ExecutionYear last = (finalYear.equals("null") ? ExecutionYear.readLastExecutionYear() : RootDomainObject.getInstance()
		.readExecutionYearByOID(Integer.valueOf(finalYear)));

	if (values[0].equals("null")) {
	    return new AlumniSearchBean(values[1], first, last);
	} else {
	    return new AlumniSearchBean(DegreeType.valueOf(values[0]), values[1], first, last);
	}
    }

    public int getTotalItems() {
	return totalItems;
    }

    public void setTotalItems(int totalItems) {
	this.totalItems = totalItems;
    }

    public Degree getDegree() {
	return this.degree;
    }

    public void setDegree(Degree degree) {
	this.degree = degree;
    }

    public Integer getStudentNumber() {
	return studentNumber;
    }

    public void setStudentNumber(Integer studentNumber) {
	this.studentNumber = studentNumber;
    }

    public String getDocumentIdNumber() {
	return documentIdNumber;
    }

    public void setDocumentIdNumber(String documentIdNumber) {
	this.documentIdNumber = documentIdNumber;
    }

}
