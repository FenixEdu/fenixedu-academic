package net.sourceforge.fenixedu.presentationTier.Action.departmentMember.teacherServiceDistribution;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TeacherServiceDistribution;

public class TeacherServiceBean implements Serializable {

	private DomainReference<ExecutionYear> executionYear;
	private DomainReference<ExecutionPeriod> executionPeriod;
	private DomainReference<Department> department;
	private List<DomainReference<TeacherServiceDistribution>> teacherServiceDistributions;
	private DomainReference<TeacherServiceDistribution> copyFromteacherServiceDistribution;
	
	private String name;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public TeacherServiceBean() {
		this.setExecutionPeriod(null);
		this.setExecutionYear(null);
		this.setDepartment(null);
		this.setCopyFromTeacherServiceDistribution(null);
		this.teacherServiceDistributions = new ArrayList<DomainReference<TeacherServiceDistribution>>();
	}
	
	public void setExecutionYear(ExecutionYear executionYear) {
		this.executionYear = new DomainReference<ExecutionYear>(executionYear);
	}
	
	public ExecutionYear getExecutionYear() {
		return this.executionYear.getObject();
	}
	
	public void setExecutionPeriod(ExecutionPeriod executionPeriod) {
		this.executionPeriod = new DomainReference<ExecutionPeriod>(executionPeriod);
	}
	
	public ExecutionPeriod getExecutionPeriod() {
		return this.executionPeriod.getObject();
	}
	
	public void setTeacherServiceDistribution(List<TeacherServiceDistribution> serviceDistributions) {
		this.teacherServiceDistributions = new ArrayList<DomainReference<TeacherServiceDistribution>>();
		for(TeacherServiceDistribution distribution : serviceDistributions) {
			this.teacherServiceDistributions.add(new DomainReference<TeacherServiceDistribution>(distribution));
		}
	}
	
	public List<TeacherServiceDistribution> getTeacherServiceDistribution() {
		List<TeacherServiceDistribution> distributions = new ArrayList<TeacherServiceDistribution>();
		for(DomainReference<TeacherServiceDistribution> distribution : this.teacherServiceDistributions) {
			distributions.add(distribution.getObject());
		}
		return distributions;
	}
	
	public void setDepartment(Department department) {
		this.department = new DomainReference<Department>(department);
	}
	
	public Department getDepartment() {
		return this.department.getObject();
	}
	
	public void setCopyFromTeacherServiceDistribution(TeacherServiceDistribution teacherServiceDistribution) {
		this.copyFromteacherServiceDistribution = new DomainReference<TeacherServiceDistribution>(teacherServiceDistribution);
	}
	
	public TeacherServiceDistribution getCopyFromTeacherServiceDistribution() {
		return this.copyFromteacherServiceDistribution.getObject();
	}
	
}
