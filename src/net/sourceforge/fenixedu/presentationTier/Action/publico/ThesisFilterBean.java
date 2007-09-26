package net.sourceforge.fenixedu.presentationTier.Action.publico;

import java.io.Serializable;
import java.util.Collection;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.DomainListReference;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.thesis.ThesisState;

public class ThesisFilterBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private DomainReference<Department> department;
    private DomainReference<ExecutionYear> year;
    private DomainListReference<Degree> options;

    private ThesisState state;
    
    private DomainReference<Degree> degree;

    public ThesisFilterBean() {
        super();
        
        this.department = new DomainReference<Department>(null);
        this.year       = new DomainReference<ExecutionYear>(null);
        this.degree     = new DomainReference<Degree>(null);
        
        this.options    = new DomainListReference<Degree>();
    }

    public Department getDepartment() {
        return department.getObject();
    }

    public void setDepartment(Department department) {
        this.department = new DomainReference<Department>(department);
    }

    public Collection<Degree> getDegreeOptions() {
        return this.options;
    }
    
    public void setDegreeOptions(Collection<Degree> degrees) {
        this.options = new DomainListReference<Degree>(degrees);
    }
    
    public Degree getDegree() {
        return degree.getObject();
    }

    public void setDegree(Degree degree) {
        this.degree = new DomainReference<Degree>(degree);
    }

    public ThesisState getState() {
        return state;
    }

    public void setState(ThesisState state) {
        this.state = state;
    }

    public ExecutionYear getYear() {
        return year.getObject();
    }

    public void setYear(ExecutionYear year) {
        this.year = new DomainReference<ExecutionYear>(year);
    }

}
