/*
 * Created on 18/Dez/2003
 *
 */
package net.sourceforge.fenixedu.persistenceTier;

import java.util.List;

import net.sourceforge.fenixedu.domain.IBranch;
import net.sourceforge.fenixedu.domain.IScientificArea;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 */
public interface IPersistentScientificArea extends IPersistentObject {
    public IScientificArea readByName(String name) throws ExcepcaoPersistencia;

    public List readAllByBranch(IBranch branch) throws ExcepcaoPersistencia;
}