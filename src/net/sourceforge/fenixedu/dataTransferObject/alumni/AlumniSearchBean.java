package net.sourceforge.fenixedu.dataTransferObject.alumni;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.student.Registration;

public class AlumniSearchBean implements Serializable {

    private int totalItems;

    private DomainReference<ExecutionYear> firstExecutionYear;
    private DomainReference<ExecutionYear> finalExecutionYear;
    private String name;
    private DegreeType degreeType;
    private List<DomainReference<Registration>> alumni;

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
	return (this.firstExecutionYear != null) ? this.firstExecutionYear.getObject() : ExecutionYear.readFirstExecutionYear();
    }

    public void setFirstExecutionYear(ExecutionYear executionYear) {
	this.firstExecutionYear = new DomainReference<ExecutionYear>(executionYear);
    }

    public ExecutionYear getFinalExecutionYear() {
	return (this.finalExecutionYear != null) ? this.finalExecutionYear.getObject() : ExecutionYear.readLastExecutionYear();
    }

    public void setFinalExecutionYear(ExecutionYear executionYear) {
	this.finalExecutionYear = new DomainReference<ExecutionYear>(executionYear);
    }

    public List<Registration> getAlumni() {
	if (this.alumni == null)
	    return null;
	List<Registration> alumni = new ArrayList<Registration>();
	for (DomainReference<Registration> reference : this.alumni) {
	    alumni.add(reference.getObject());
	}
	return alumni;
    }

    public void setAlumni(List<Registration> alumni) {
	if (this.alumni == null) {
	    this.alumni = new ArrayList<DomainReference<Registration>>(alumni.size());
	} else {
	    this.alumni.clear();
	}

	for (Registration person : alumni) {
	    this.alumni.add(new DomainReference<Registration>(person));
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
}
