package net.sourceforge.fenixedu.applicationTier.Servico.person.vigilancy;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.vigilancy.Vigilant;
import net.sourceforge.fenixedu.domain.vigilancy.VigilantGroup;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class RemoveVigilantsFromGroup extends Service {

    public List<Vigilant> run(List<Vigilant> vigilants, VigilantGroup group) throws ExcepcaoPersistencia {

        List<Vigilant> unableToRemove = new ArrayList<Vigilant>();
        for (Vigilant vigilant : vigilants) {
            if (vigilant.getVigilantGroupsCount() == 1) {
                try {
                    vigilant.delete();
                } catch (DomainException e) {
                    unableToRemove.add(vigilant);
                }
            } else {
                group.removeVigilants(vigilant);
            }
        }
        return unableToRemove;
    }

}