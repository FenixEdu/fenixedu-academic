package net.sourceforge.fenixedu.persistenceTier;

/**
 * @author Fernanda Quitério 23/09/2003
 *  
 */
import java.util.List;

import net.sourceforge.fenixedu.domain.IWebSite;

public interface IPersistentWebSite extends IPersistentObject {

    List readAll() throws ExcepcaoPersistencia;

    void delete(IWebSite site) throws ExcepcaoPersistencia;

}