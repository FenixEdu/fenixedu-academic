package net.sourceforge.fenixedu.domain.parking;

import java.text.MessageFormat;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.parking.ParkingRequest.ParkingRequestFactoryCreator;
import net.sourceforge.fenixedu.domain.student.Student;

public class ParkingParty extends ParkingParty_Base {

    public ParkingParty(Party party) {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setParty(party);
        setAuthorized(Boolean.FALSE);
        setAcceptedRegulation(Boolean.FALSE);
    }

    public boolean getHasAllNecessaryPersonalInfo() {
        return ((((Person) getParty()).getWorkPhone().length() != 0 || ((Person) getParty()).getMobile()
                .length() != 0) && ((Person) getParty()).getEmail().length() != 0);
    }

    public ParkingRequest getFirstRequest() {
        return getParkingRequests().iterator().next();
    }

    public ParkingRequestFactoryCreator getParkingRequestFactoryCreator() {
        return new ParkingRequestFactoryCreator(this);
    }

    public String getParkingAcceptedRegulationMessage() {
        ResourceBundle bundle = ResourceBundle.getBundle("resources.ParkingResources");
        String name = getParty().getName();
        String number = "";
        if (getParty().isPerson()) {
            Person person = (Person) getParty();
            // ((Person)getParty()).
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
        String id = "";
        if (getParty().isPerson()) {
            id = ((Person) getParty()).getDocumentIdNumber();
        }

        return MessageFormat.format(bundle.getString("message.acceptedRegulation"), new Object[] { name,
                number, id });
    }
}
