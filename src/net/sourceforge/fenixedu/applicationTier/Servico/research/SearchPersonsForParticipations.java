package net.sourceforge.fenixedu.applicationTier.Servico.research;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.applicationTier.Servico.commons.SearchObjects;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.Person;

public class SearchPersonsForParticipations extends SearchObjects {

    @Override
    public List<DomainObject> run(Class type, String value, int limit, Map<String, String> arguments) {

        if(type != Person.class)
            return null;
        
        final List<DomainObject> objects = super.run(type, value, limit, arguments);
        List personsWrapped = new ArrayList();

        for (Object object : objects) {
            final Person person = (Person) object;
            if (person != null)
                personsWrapped.add(new PersonWrapper(person));
        }

        return personsWrapped;
    }

    public static class PersonWrapper {
        private Integer idInternal;

        private String text;

        public PersonWrapper() {
            super();
        }

        public PersonWrapper(Person person) {
            this();
            this.idInternal = person.getIdInternal();
            this.text = buildText(person);
        }

        public Integer getIdInternal() {
            return idInternal;
        }

        public void setIdInternal(Integer idInternal) {
            this.idInternal = idInternal;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        private String buildText(Person person) {

            final ResourceBundle bundle = ResourceBundle.getBundle("resources.ResearcherResources",
                    Locale.getDefault());
            final StringBuilder text = new StringBuilder();
            text.append(person.getName());

            if (person.hasTeacher()) {
                text.append(" - " + bundle.getString("label.teacher"));
                text.append(" - " + bundle.getString("label.number"));
                text.append(": " + person.getTeacher().getTeacherNumber());
                return text.toString();
            }
            if (person.hasExternalPerson()) {
                text.append(" - " + bundle.getString("label.externalPerson"));
                if (person.getExternalPerson().hasInstitutionUnit()) {
                    text.append(" - " + bundle.getString("label.institutionUnit"));
                    text.append(": " + person.getExternalPerson().getInstitutionUnit().getName());
                }
                return text.toString();
            }
            if (person.hasStudent()) {
                text.append(" - " + bundle.getString("label.student"));
                text.append(" - " + bundle.getString("label.number"));
                text.append(": " + person.getStudent().getNumber());
                return text.toString();
            }
            if (person.hasEmployee()) {
                text.append(" - " + bundle.getString("label.employee"));
                text.append(" - " + bundle.getString("label.number"));
                text.append(": " + person.getEmployee().getEmployeeNumber());
                return text.toString();
            }
            if (person.hasIstUsername()) {
                text.append(" - " + person.getIstUsername());
                return text.toString();
            }
            return text.append(" - ???").toString();
        }
    }
}
