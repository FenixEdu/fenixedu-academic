package net.sourceforge.fenixedu.presentationTier.Action.publico;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.thesis.ThesisState;

public class ThesisFilterBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Department department;
	private ExecutionYear year;
	private Collection<Degree> options;

	private ThesisState state;

	private Degree degree;

	public ThesisFilterBean() {
		super();

		this.department = null;
		this.year = null;
		this.degree = null;

		this.options = new ArrayList<Degree>();
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public Collection<Degree> getDegreeOptions() {
		return this.options;
	}

	public void setDegreeOptions(Collection<Degree> degrees) {
		this.options = degrees;
	}

	public Degree getDegree() {
		return degree;
	}

	public void setDegree(Degree degree) {
		this.degree = degree;
	}

	public ThesisState getState() {
		return state;
	}

	public void setState(ThesisState state) {
		this.state = state;
	}

	public ExecutionYear getYear() {
		return year;
	}

	public void setYear(ExecutionYear year) {
		this.year = year;
	}

}
