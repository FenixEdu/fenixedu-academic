package net.sourceforge.fenixedu.presentationTier.Action.departmentMember.teacherServiceDistribution;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProcess;

public class TeacherServiceBean implements Serializable {

	private DomainReference<ExecutionYear> executionYear;
	private DomainReference<ExecutionPeriod> executionPeriod;
	private DomainReference<Department> department;
	private List<DomainReference<TSDProcess>> tsdProcesss;
	private DomainReference<TSDProcess> copyFromtsdProcess;
	
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
		this.setCopyFromTSDProcess(null);
		this.tsdProcesss = new ArrayList<DomainReference<TSDProcess>>();
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
	
	public void setTSDProcess(List<TSDProcess> serviceDistributions) {
		this.tsdProcesss = new ArrayList<DomainReference<TSDProcess>>();
		for(TSDProcess distribution : serviceDistributions) {
			this.tsdProcesss.add(new DomainReference<TSDProcess>(distribution));
		}
	}
	
	public List<TSDProcess> getTSDProcess() {
		List<TSDProcess> distributions = new ArrayList<TSDProcess>();
		for(DomainReference<TSDProcess> distribution : this.tsdProcesss) {
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
	
	public void setCopyFromTSDProcess(TSDProcess tsdProcess) {
		this.copyFromtsdProcess = new DomainReference<TSDProcess>(tsdProcess);
	}
	
	public TSDProcess getCopyFromTSDProcess() {
		return this.copyFromtsdProcess.getObject();
	}
	
}
