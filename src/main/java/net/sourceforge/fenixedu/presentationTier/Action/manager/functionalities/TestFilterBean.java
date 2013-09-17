package net.sourceforge.fenixedu.presentationTier.Action.manager.functionalities;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.Map;

import net.sourceforge.fenixedu.domain.Person;
import pt.ist.fenixframework.FenixFramework;

/**
 * Helper used to collect information to test the functionalitis availability.
 * 
 * @author cfgi
 */
public class TestFilterBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private String personName;
    private Person person;
    private String personId;
    private String parameters;

    public TestFilterBean() {
        super();

        this.person = null;
    }

    public String getPersonName() {
        return this.personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public Person getPerson() {
        return this.person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public String getPersonId() {
        return this.personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getParameters() {
        return this.parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    public Map<String, String[]> getParametersMap() {
        Map<String, String[]> map = new Hashtable<String, String[]>();

        String parameters = getParameters();
        if (parameters != null && parameters.trim().length() > 0) {
            String[] parameterParts = parameters.split(",");
            for (String parameterPart : parameterParts) {
                String part = parameterPart.trim();

                if (part.length() > 0) {
                    String[] components = part.split("=");

                    String name;
                    String value;

                    name = components[0].trim();
                    value = components.length > 1 ? components[1] : "";

                    if (map.containsKey(name)) {
                        String[] values = map.get(name);
                        String[] newValues = new String[values.length + 1];

                        System.arraycopy(values, 0, newValues, 0, values.length);
                        newValues[values.length] = value;
                    } else {
                        map.put(name, new String[] { value });
                    }
                }
            }
        }

        return map;
    }

    public Person getTargetPerson() {
        Person person = getPerson();

        if (person != null) {
            updateInfo(person);
            return person;
        } else {
            String oid = getPersonId();

            if (oid == null) {
                return null;
            }

            person = FenixFramework.getDomainObject(oid);
            setPerson(person);

            updateInfo(person);
            return person;
        }
    }

    private void updateInfo(Person person) {
        if (person != null) {
            setPerson(person);
            setPersonId(person.getExternalId());
        }
    }

}
