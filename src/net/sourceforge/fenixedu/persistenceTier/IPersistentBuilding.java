package net.sourceforge.fenixedu.persistenceTier;

/**
 * @author Luis Cruz
 * 
 */
import java.util.List;

import net.sourceforge.fenixedu.domain.IBuilding;

public interface IPersistentBuilding extends IPersistentObject {

    public List readAll() throws ExcepcaoPersistencia;
    public void delete(final IBuilding building) throws ExcepcaoPersistencia;

}