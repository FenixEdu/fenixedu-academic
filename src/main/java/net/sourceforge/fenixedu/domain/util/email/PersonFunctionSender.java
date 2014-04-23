package net.sourceforge.fenixedu.domain.util.email;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.DelegatesGroup;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.accessControl.PersonGroup;
import net.sourceforge.fenixedu.domain.organizationalStructure.Function;
import net.sourceforge.fenixedu.domain.organizationalStructure.FunctionType;
import net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunction;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.fenixframework.Atomic;

public class PersonFunctionSender extends PersonFunctionSender_Base {

    private static final Logger logger = LoggerFactory.getLogger(PersonFunctionSender.class);

    public PersonFunctionSender(PersonFunction personFunction) {
        super();

        Person person = personFunction.getPerson();
        setPersonFunction(personFunction);
        setFromAddress(Sender.getNoreplyMail());
        addReplyTos(new CurrentUserReplyTo());
        setMembers(new PersonGroup(person));
        getRecipients().addAll(Recipient.newInstance(getPossibleReceivers(person)));
        setFromName(createFromName());
    }

    public String createFromName() {

        if (getPersonFunction() == null || getPersonFunction().getFunction() == null) {
            return getFromName();
        }

        String institutionAcronym = Unit.getInstitutionAcronym();
        Function function = getPersonFunction().getFunction();

        if (function.isOfAnyFunctionType(FunctionType.getAllDegreeDelegateFunctionTypes())) {
            String courseAcronym = function.getUnit().getDegree().getSigla();
            String functionTypeName;
            if (function.isOfFunctionType(FunctionType.DELEGATE_OF_YEAR)) {
                String year = getPersonFunction().getCurricularYear().getYear().toString();
                functionTypeName = String.format("Delegado do %sº ano", year);
            } else {
                functionTypeName = function.getTypeName().toString();
            }
            return String.format("%s (%s: %s)", institutionAcronym, courseAcronym, functionTypeName);
        } else {
            String functionTypeName = function.getTypeName().toString();
            return String.format("%s (%s)", institutionAcronym, functionTypeName);
        }
    }

    public static List<Group> getPossibleReceivers(Person person) {
        List<Group> groups = new ArrayList<Group>();
        PersonFunction delegateFunction = null;
        if (person.hasStudent()) {
            final Student delegate = person.getStudent();
            logger.info("Delegate: " + person.getName());
            final Registration lastRegistration = delegate.getLastActiveRegistration();
            // it should not be a delegate
            if (lastRegistration == null) {
                return groups;
            }
            final Degree degree = lastRegistration.getDegree();
            delegateFunction = degree.getMostSignificantDelegateFunctionForStudent(delegate, null);

            /* All other delegates from delegate degree */
            groups.add(new DelegatesGroup(degree));

            /* All delegates with same delegate function from other degrees */
            if (delegateFunction != null
                    && !delegateFunction.getFunction().getFunctionType().equals(FunctionType.DELEGATE_OF_YEAR)) {
                for (PersonFunction function : delegate.getAllActiveDelegateFunctions()) {
                    groups.add(new DelegatesGroup(function.getFunction().getFunctionType()));
                }
            }

            /* A student can have a GGAE delegate role too */
            if (person.getActiveGGAEDelegatePersonFunction() != null) {
                groups.add(new DelegatesGroup(person.getActiveGGAEDelegatePersonFunction().getFunction().getFunctionType()));
            }
        } else {
            delegateFunction = person.getActiveGGAEDelegatePersonFunction();
            groups.add(new DelegatesGroup(delegateFunction.getFunction().getFunctionType()));
        }

        return groups;
    }

    @Override
    public void delete() {
        setPersonFunction(null);
        super.delete();
    }

    @Atomic
    public static PersonFunctionSender newInstance(PersonFunction personFunction) {
        PersonFunctionSender sender = personFunction.getSender();
        return sender == null ? new PersonFunctionSender(personFunction) : sender;
    }

    @Deprecated
    public boolean hasPersonFunction() {
        return getPersonFunction() != null;
    }

}
