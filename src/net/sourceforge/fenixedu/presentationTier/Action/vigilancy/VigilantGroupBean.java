package net.sourceforge.fenixedu.presentationTier.Action.vigilancy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.vigilancy.ExamCoordinator;
import net.sourceforge.fenixedu.domain.vigilancy.Vigilant;

import org.joda.time.DateTime;

public class VigilantGroupBean extends VigilantBean implements Serializable {

    private DomainReference<Person> person;

    private DomainReference<Unit> unit;

    private DomainReference<Unit> selectedUnit;

    private DomainReference<Department> selectedDepartment;

    private DomainReference<Vigilant> selectedVigilant;

    private List<DomainReference<Employee>> employees = new ArrayList<DomainReference<Employee>>();

    private String name;

    private String ConvokeStrategy;

    private DomainReference<ExamCoordinator> examCoordinator;

    private List<DomainReference<ExamCoordinator>> examCoordinators = new ArrayList<DomainReference<ExamCoordinator>>();

    private DateTime beginFirstUnavailablePeriod;

    private DateTime endFirstUnavailablePeriod;

    private DateTime beginSecondUnavailablePeriod;

    private DateTime endSecondUnavailablePeriod;

    private boolean showCourseColumn = Boolean.TRUE;

    private boolean showVigilantColumn = Boolean.TRUE;

    private boolean showCoordinators = Boolean.TRUE;

    private List<DomainReference<Vigilant>> vigilants = new ArrayList<DomainReference<Vigilant>>();

    private String username = "";

    public boolean isShowCoordinators() {
        return showCoordinators;
    }

    public void setShowCoordinators(boolean showCoordinators) {
        this.showCoordinators = showCoordinators;
    }

    public boolean isShowCourseColumn() {
        return showCourseColumn;
    }

    public void setShowCourseColumn(boolean showCompetenceCourseColumn) {
        this.showCourseColumn = showCompetenceCourseColumn;
    }

    public boolean isShowVigilantColumn() {
        return showVigilantColumn;
    }

    public void setShowVigilantColumn(boolean showVigilantColumn) {
        this.showVigilantColumn = showVigilantColumn;
    }

    public VigilantGroupBean() {
        setPerson(null);
        setUnit(null);
        setSelectedVigilantGroup(null);
        setBeginFirstUnavailablePeriod(null);
        setEndFirstUnavailablePeriod(null);
        setBeginSecondUnavailablePeriod(null);
        setEndSecondUnavailablePeriod(null);
        setExecutionYear(null);
        setExamCoordinator(null);
        setSelectedDepartment(null);
        setSelectedUnit(null);
        setSelectedVigilant(null);
    }

    public DateTime getBeginFirstUnavailablePeriod() {
        return beginFirstUnavailablePeriod;
    }

    public void setBeginFirstUnavailablePeriod(DateTime beginFirstUnavailablePeriod) {
        this.beginFirstUnavailablePeriod = beginFirstUnavailablePeriod;
    }

    public DateTime getBeginSecondUnavailablePeriod() {
        return beginSecondUnavailablePeriod;
    }

    public void setBeginSecondUnavailablePeriod(DateTime beginSecondUnavailablePeriod) {
        this.beginSecondUnavailablePeriod = beginSecondUnavailablePeriod;
    }

    public DateTime getEndFirstUnavailablePeriod() {
        return endFirstUnavailablePeriod;
    }

    public void setEndFirstUnavailablePeriod(DateTime endFirstUnavailablePeriod) {
        this.endFirstUnavailablePeriod = endFirstUnavailablePeriod;
    }

    public DateTime getEndSecondUnavailablePeriod() {
        return endSecondUnavailablePeriod;
    }

    public void setEndSecondUnavailablePeriod(DateTime endSecondUnavailablePeriod) {
        this.endSecondUnavailablePeriod = endSecondUnavailablePeriod;
    }

    public String getConvokeStrategy() {
        return ConvokeStrategy;
    }

    public void setConvokeStrategy(String convokeStrategy) {
        ConvokeStrategy = convokeStrategy;
    }

    public Vigilant getSelectedVigilant() {
        return this.selectedVigilant.getObject();
    }

    public void setSelectedVigilant(Vigilant vigilant) {
        this.selectedVigilant = new DomainReference<Vigilant>(vigilant);
    }

    public Person getPerson() {
        return (this.person == null) ? null : this.person.getObject();
    }

    public void setPerson(Person person) {
        this.person = (person != null) ? new DomainReference<Person>(person) : null;
    }

    public void setExamCoordinator(ExamCoordinator examCoordinator) {
        this.examCoordinator = (examCoordinator != null) ? new DomainReference<ExamCoordinator>(
                examCoordinator) : null;
    }

    public ExamCoordinator getExamCoordinator() {
        return (this.examCoordinator == null) ? null : this.examCoordinator.getObject();
    }

    public Unit getUnit() {
        return (this.unit != null) ? this.unit.getObject() : null;
    }

    public void setUnit(Unit unit) {
        this.unit = (unit != null) ? new DomainReference<Unit>(unit) : null;
    }

    public List getEmployees() {
        List employees = new ArrayList<Employee>();
        for (DomainReference<Employee> employee : this.employees) {
            if (employee != null)
                employees.add(employee.getObject());
        }
        return employees;
    }

    public void setEmployees(List<Employee> employeesList) {
        this.employees = new ArrayList<DomainReference<Employee>>();
        for (Employee employee : employeesList) {
            if (employee != null) {
                this.employees.add(new DomainReference(employee));
            }
        }
    }

    public List getExamCoordinators() {
        List coordinators = new ArrayList<ExamCoordinator>();
        for (DomainReference<ExamCoordinator> coordinator : this.examCoordinators) {
            if (coordinator != null)
                coordinators.add(coordinator.getObject());
        }
        return coordinators;
    }

    public void setExamCoordinators(List<ExamCoordinator> coordinatorsList) {
        this.examCoordinators = new ArrayList<DomainReference<ExamCoordinator>>();
        for (ExamCoordinator coordinator : coordinatorsList) {
            if (coordinator != null) {
                this.examCoordinators.add(new DomainReference(coordinator));
            }
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List getVigilants() {
        List vigilants = new ArrayList<Vigilant>();
        for (DomainReference<Vigilant> vigilant : this.vigilants) {
            if (vigilant != null)
                vigilants.add(vigilant.getObject());
        }
        return vigilants;
    }

    public void setVigilants(List<Vigilant> vigilants) {
        this.vigilants = new ArrayList<DomainReference<Vigilant>>();
        for (Vigilant vigilant : vigilants) {
            if (vigilant != null) {
                this.vigilants.add(new DomainReference(vigilant));
            }
        }
    }

    public List<Vigilant> getVigilantsWithIncompatiblePerson() {
        List<Vigilant> vigilants = this.getVigilants();
        List<Vigilant> vigilantsWithIncompatiblePerson = new ArrayList<Vigilant>();

        for (Vigilant vigilant : vigilants) {
            if (vigilant.hasIncompatiblePerson()) {
                vigilantsWithIncompatiblePerson.add(vigilant);
            }
        }

        return vigilantsWithIncompatiblePerson;
    }

    public void resetEmployees() {
        this.employees = new ArrayList<DomainReference<Employee>>();
    }

    public Unit getSelectedUnit() {
        return selectedUnit.getObject();
    }

    public void setSelectedUnit(Unit unit) {
        this.selectedUnit = new DomainReference<Unit>(unit);
    }

    public Department getSelectedDepartment() {
        return selectedDepartment.getObject();
    }

    public void setSelectedDepartment(Department department) {
        this.selectedDepartment = new DomainReference<Department>(department);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
