package net.sourceforge.fenixedu.persistenceTier;

import java.util.List;

import net.sourceforge.fenixedu.domain.IInstitution;

/**
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt)
 * @author - Nadir Tarmahomed (naat@mega.ist.utl.pt)
 */
public interface IPersistentInstitution extends IPersistentObject {
    public IInstitution readByName(String name) throws ExcepcaoPersistencia;

    public List<IInstitution> readAll() throws ExcepcaoPersistencia;
}