package net.sourceforge.fenixedu.persistenceTier;

import java.util.List;

import net.sourceforge.fenixedu.domain.Institution;

/**
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt)
 * @author - Nadir Tarmahomed (naat@mega.ist.utl.pt)
 */
public interface IPersistentInstitution extends IPersistentObject {
    public Institution readByName(String name) throws ExcepcaoPersistencia;

    public List<Institution> readAll() throws ExcepcaoPersistencia;
}