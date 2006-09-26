package net.sourceforge.fenixedu.domain.parking;

import java.text.MessageFormat;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.parking.ParkingRequest.ParkingRequestFactoryCreator;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.util.LanguageUtils;

public class ParkingParty extends ParkingParty_Base {

    public ParkingParty(Party party) {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setParty(party);
        setAuthorized(Boolean.FALSE);
        setAcceptedRegulation(Boolean.FALSE);
    }

    public boolean getHasAllNecessaryPersonalInfo() {
        Person person = (Person) getParty();
        return (!StringUtils.isEmpty(person.getWorkPhone()) || !StringUtils.isEmpty(person.getMobile()))
                && (isEmployee() || !StringUtils.isEmpty(person.getEmail()));
    }

    private boolean isEmployee() {
        if (getParty().isPerson()) {
            Person person = (Person) getParty();
            Teacher teacher = person.getTeacher();
            if (teacher == null) {
                Employee employee = person.getEmployee();
                if (employee != null) {
                    return true;
                }
            }
        }
        return false;
    }

    public ParkingRequest getFirstRequest() {
        return (getParkingRequests().isEmpty() ? null : getParkingRequests().iterator().next());
    }

    public ParkingRequestFactoryCreator getParkingRequestFactoryCreator() {
        return new ParkingRequestFactoryCreator(this);
    }

    public String getParkingAcceptedRegulationMessage() {
        ResourceBundle bundle = ResourceBundle.getBundle("resources.ParkingResources", LanguageUtils
                .getLocale());
        String name = getParty().getName();
        String number = "";
        if (getParty().isPerson()) {
            Person person = (Person) getParty();
            Teacher teacher = person.getTeacher();
            if (teacher == null) {
                Employee employee = person.getEmployee();
                if (employee == null) {
                    Student student = person.getStudent();
                    if (student != null) {
                        number = student.getNumber().toString();
                    }
                } else {
                    number = employee.getEmployeeNumber().toString();
                }

            } else {
                number = teacher.getTeacherNumber().toString();
            }

        }

        return MessageFormat.format(bundle.getString("message.acceptedRegulation"), new Object[] { name,
                number });
    }

    public boolean isStudent() {
        if (getParty().isPerson()) {
            Person person = (Person) getParty();
            Teacher teacher = person.getTeacher();
            if (teacher == null) {
                Employee employee = person.getEmployee();
                if (employee == null) {
                    Student student = person.getStudent();
                    if (student != null) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
