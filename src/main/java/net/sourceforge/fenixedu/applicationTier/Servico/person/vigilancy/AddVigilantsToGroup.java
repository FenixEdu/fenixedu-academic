package net.sourceforge.fenixedu.applicationTier.Servico.person.vigilancy;

import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.vigilancy.VigilantGroup;
import net.sourceforge.fenixedu.domain.vigilancy.VigilantWrapper;
import pt.ist.fenixframework.Atomic;

public class AddVigilantsToGroup {

    @Atomic
    public static void run(Map<VigilantGroup, List<Person>> peopleToAdd) {

        Set<VigilantGroup> groups = peopleToAdd.keySet();
        for (VigilantGroup group : groups) {
            List<Person> people = peopleToAdd.get(group);
            for (Person person : people) {
                new VigilantWrapper(group, person);
            }
        }
    }

}