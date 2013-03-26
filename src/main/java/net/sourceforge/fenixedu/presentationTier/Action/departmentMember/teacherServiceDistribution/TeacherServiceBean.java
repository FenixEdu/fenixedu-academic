package net.sourceforge.fenixedu.presentationTier.Action.departmentMember.teacherServiceDistribution;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProcess;

public class TeacherServiceBean implements Serializable {

    private ExecutionYear executionYear;
    private ExecutionSemester executionSemester;
    private Department department;
    private List<TSDProcess> tsdProcesss;
    private TSDProcess copyFromtsdProcess;

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
        this.tsdProcesss = new ArrayList<TSDProcess>();
    }

    public void setExecutionYear(ExecutionYear executionYear) {
        this.executionYear = executionYear;
    }

    public ExecutionYear getExecutionYear() {
        return this.executionYear;
    }

    public void setExecutionPeriod(ExecutionSemester executionSemester) {
        this.executionSemester = executionSemester;
    }

    public ExecutionSemester getExecutionPeriod() {
        return this.executionSemester;
    }

    public void setTSDProcess(List<TSDProcess> serviceDistributions) {
        this.tsdProcesss = new ArrayList<TSDProcess>();
        for (TSDProcess distribution : serviceDistributions) {
            this.tsdProcesss.add(distribution);
        }
    }

    public List<TSDProcess> getTSDProcess() {
        List<TSDProcess> distributions = new ArrayList<TSDProcess>();
        for (TSDProcess distribution : this.tsdProcesss) {
            distributions.add(distribution);
        }
        return distributions;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Department getDepartment() {
        return this.department;
    }

    public void setCopyFromTSDProcess(TSDProcess tsdProcess) {
        this.copyFromtsdProcess = tsdProcess;
    }

    public TSDProcess getCopyFromTSDProcess() {
        return this.copyFromtsdProcess;
    }

}
