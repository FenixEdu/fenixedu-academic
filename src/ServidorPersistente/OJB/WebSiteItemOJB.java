package ServidorPersistente.OJB;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.IWebSiteItem;
import Dominio.IWebSiteSection;
import Dominio.WebSiteItem;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentWebSiteItem;
import ServidorPersistente.exceptions.ExistingPersistentException;

/**
 * @author  Fernanda Quitério
 * 23/09/2003
 * 
 */
public class WebSiteItemOJB extends ObjectFenixOJB implements IPersistentWebSiteItem
{

    public IWebSiteItem readByWebSiteSectionAndName(IWebSiteSection webSiteSection, String title)
        throws ExcepcaoPersistencia
    {
        IWebSiteItem item = null;
        if (webSiteSection != null && title != null)
        {
            Criteria criteria = new Criteria();

            criteria.addEqualTo("webSiteSection.name", webSiteSection.getName());
            criteria.addEqualTo("title", title);

            item = (IWebSiteItem) queryObject(WebSiteItem.class, criteria);
        }
        return item;
    }

    public List readAllWebSiteItemsByWebSiteSection(IWebSiteSection webSiteSection)
        throws ExcepcaoPersistencia
    {
        List items = null;
        if (webSiteSection != null)
        {
            Criteria criteria = new Criteria();

            criteria.addEqualTo("webSiteSection.name", webSiteSection.getName());
            criteria.addEqualTo("webSiteSection.webSite.name", webSiteSection.getWebSite().getName());

            items = queryList(WebSiteItem.class, criteria, true);
        }
        return items;
    }

    public List readPublishedWebSiteItemsByWebSiteSection(IWebSiteSection webSiteSection)
        throws ExcepcaoPersistencia
    {
        List items = null;
        if (webSiteSection != null)
        {
            Criteria criteria = new Criteria();

            criteria.addEqualTo("webSiteSection.name", webSiteSection.getName());
            criteria.addEqualTo("webSiteSection.webSite.name", webSiteSection.getWebSite().getName());
            criteria.addEqualTo("published", Boolean.TRUE);

            items = queryList(WebSiteItem.class, criteria, true);
        }
        return items;
    }

    public void lockWrite(IWebSiteItem item) throws ExcepcaoPersistencia
    {

        IWebSiteItem itemFromDB = null;
        if (item == null)
            // Should we throw an exception saying nothing to write or
            // something of the sort?
            // By default, if OJB received a null object it would complain.
            return;

        // read item		
        itemFromDB = this.readByWebSiteSectionAndName(item.getWebSiteSection(), item.getTitle());

        // if (item not in database) then write it
        if (itemFromDB == null)
            super.lockWrite(item);
        // else if (item is mapped to the database then write any existing changes)
        else if (
            (item instanceof WebSiteItem) && itemFromDB.getIdInternal().equals(item.getIdInternal()))
        {

            super.lockWrite(item);
            // No need to werite it because it is already mapped.
        } else
            throw new ExistingPersistentException();
    }

    public void delete(IWebSiteItem item) throws ExcepcaoPersistencia
    {
        super.delete(item);
    }

    public void deleteAll() throws ExcepcaoPersistencia
    {

        String oqlQuery = "select all from " + WebSiteItem.class.getName();
        super.deleteAll(oqlQuery);
    }

}
