package net.sourceforge.fenixedu.persistenceTier;

/**
 * @author Fernanda Quitério 23/09/2003
 *  
 */
import java.util.List;

import net.sourceforge.fenixedu.domain.WebSite;

public interface IPersistentWebSite extends IPersistentObject {

    List readAll() throws ExcepcaoPersistencia;

    void delete(WebSite site) throws ExcepcaoPersistencia;

}