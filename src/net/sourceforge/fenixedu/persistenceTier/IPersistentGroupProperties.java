/*
 * Created on 12/Mai/2003
 *
 */
package net.sourceforge.fenixedu.persistenceTier;
import java.util.List;

import net.sourceforge.fenixedu.domain.IGroupProperties;

/**
 * @author asnr and scpo
 *
 */
public interface IPersistentGroupProperties extends IPersistentObject{
    public List readAll() throws ExcepcaoPersistencia;

    public List readGroupPropertiesByName (String name) throws ExcepcaoPersistencia;

    public void delete(IGroupProperties groupProperties) throws ExcepcaoPersistencia;

}
