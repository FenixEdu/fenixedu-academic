package net.sourceforge.fenixedu.persistenceTier.grant;

/**
 * 
 * @author Barbosa
 * @author Pica
 */
import java.util.List;

import net.sourceforge.fenixedu.domain.grant.contract.IGrantType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;

public interface IPersistentGrantType extends IPersistentObject {
    public IGrantType readGrantTypeBySigla(String sigla) throws ExcepcaoPersistencia;

    public List readAll() throws ExcepcaoPersistencia;

}