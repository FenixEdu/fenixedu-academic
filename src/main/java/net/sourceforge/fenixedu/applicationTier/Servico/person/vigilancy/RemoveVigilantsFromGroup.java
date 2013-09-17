package net.sourceforge.fenixedu.applicationTier.Servico.person.vigilancy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.vigilancy.VigilantGroup;
import net.sourceforge.fenixedu.domain.vigilancy.VigilantWrapper;
import pt.ist.fenixframework.Atomic;

public class RemoveVigilantsFromGroup {

    @Atomic
    public static List<VigilantWrapper> run(Map<VigilantGroup, List<VigilantWrapper>> vigilantsToRemove) {

        List<VigilantWrapper> unableToRemove = new ArrayList<VigilantWrapper>();

        Set<VigilantGroup> groups = vigilantsToRemove.keySet();

        for (VigilantGroup group : groups) {
            List<VigilantWrapper> vigilantWrappers = vigilantsToRemove.get(group);

            for (VigilantWrapper vigilantWrapper : vigilantWrappers) {
                try {
                    vigilantWrapper.delete();
                } catch (DomainException e) {
                    unableToRemove.add(vigilantWrapper);
                }
            }
        }
        return unableToRemove;
    }
}