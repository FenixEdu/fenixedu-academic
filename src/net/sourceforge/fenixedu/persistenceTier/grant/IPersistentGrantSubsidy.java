package net.sourceforge.fenixedu.persistenceTier.grant;

import java.util.List;

import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;

/**
 * @author Pica
 * @author Barbosa
 */
public interface IPersistentGrantSubsidy extends IPersistentObject {
    public List readAllSubsidiesByGrantContract(Integer idContract) throws ExcepcaoPersistencia;

    public List readAllSubsidiesByGrantContractAndState(Integer idContract, Integer state)
            throws ExcepcaoPersistencia;
}