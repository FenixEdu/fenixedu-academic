package net.sourceforge.fenixedu.persistenceTier;

import java.util.List;

import net.sourceforge.fenixedu.domain.IWorkLocation;

/**
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt)
 * @author - Nadir Tarmahomed (naat@mega.ist.utl.pt)
 */
public interface IPersistentWorkLocation extends IPersistentObject {
    public IWorkLocation readByName(String name) throws ExcepcaoPersistencia;

    public List readAll() throws ExcepcaoPersistencia;
}