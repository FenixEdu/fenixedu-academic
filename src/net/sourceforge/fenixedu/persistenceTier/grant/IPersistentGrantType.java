package net.sourceforge.fenixedu.persistenceTier.grant;

/**
 * 
 * @author Barbosa
 * @author Pica
 */
import java.util.List;

import net.sourceforge.fenixedu.domain.grant.contract.GrantType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;

public interface IPersistentGrantType extends IPersistentObject {
    public GrantType readGrantTypeBySigla(String sigla) throws ExcepcaoPersistencia;

    public List readAll() throws ExcepcaoPersistencia;

}