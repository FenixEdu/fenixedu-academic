package net.sourceforge.fenixedu.persistenceTier;

import java.util.List;

import net.sourceforge.fenixedu.domain.IWebSiteItem;
import net.sourceforge.fenixedu.domain.IWebSiteSection;

/**
 * @author Fernanda Quitério 23/09/2003
 *  
 */
public interface IPersistentWebSiteItem extends IPersistentObject {
    public IWebSiteItem readByWebSiteSectionAndName(IWebSiteSection webSiteSection, String name)
            throws ExcepcaoPersistencia;

    public List readAllWebSiteItemsByWebSiteSection(IWebSiteSection webSiteSection)
            throws ExcepcaoPersistencia;

    public List readPublishedWebSiteItemsByWebSiteSection(IWebSiteSection webSiteSection)
            throws ExcepcaoPersistencia;

    public void delete(IWebSiteItem webSiteItem) throws ExcepcaoPersistencia;

}