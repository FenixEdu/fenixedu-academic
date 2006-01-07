package net.sourceforge.fenixedu.persistenceTier;

import java.util.List;

import net.sourceforge.fenixedu.domain.WebSiteItem;
import net.sourceforge.fenixedu.domain.WebSiteSection;

/**
 * @author Fernanda Quitério 23/09/2003
 *  
 */
public interface IPersistentWebSiteItem extends IPersistentObject {
    public WebSiteItem readByWebSiteSectionAndName(WebSiteSection webSiteSection, String name)
            throws ExcepcaoPersistencia;

    public List readAllWebSiteItemsByWebSiteSection(WebSiteSection webSiteSection)
            throws ExcepcaoPersistencia;

    public List readPublishedWebSiteItemsByWebSiteSection(WebSiteSection webSiteSection)
            throws ExcepcaoPersistencia;

    public void delete(WebSiteItem webSiteItem) throws ExcepcaoPersistencia;

}