/*
 * Created on Jan 21, 2004
 */
package net.sourceforge.fenixedu.persistenceTier.grant;

import net.sourceforge.fenixedu.domain.grant.contract.IGrantProject;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;

/**
 * @author pica
 * @author barbosa
 */
public interface IPersistentGrantProject extends IPersistentObject {
    public IGrantProject readGrantProjectByNumber(String number) throws ExcepcaoPersistencia;
}