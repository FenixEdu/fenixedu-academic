package net.sourceforge.fenixedu.persistenceTier;

import java.util.List;

import net.sourceforge.fenixedu.domain.WebSite;
import net.sourceforge.fenixedu.domain.WebSiteSection;

/**
 * @author Fernanda Quitério 23/09/2003
 *  
 */
public interface IPersistentWebSiteSection extends IPersistentObject {

    public void delete(WebSiteSection section) throws ExcepcaoPersistencia;

    public WebSiteSection readByWebSiteAndName(WebSite webSite, String name)
            throws ExcepcaoPersistencia;

    public WebSiteSection readByName(String name) throws ExcepcaoPersistencia;

    public List readByWebSite(WebSite webSite) throws ExcepcaoPersistencia;

    public List readAll() throws ExcepcaoPersistencia;
}