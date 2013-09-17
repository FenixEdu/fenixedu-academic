package net.sourceforge.fenixedu.applicationTier.Servico.manager;


import net.sourceforge.fenixedu.applicationTier.Filtro.SiteManagerAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.Item;
import net.sourceforge.fenixedu.domain.Site;
import pt.ist.fenixframework.Atomic;

/**
 * @author Fernanda Quitério
 * 
 */
public class DeleteItem {

    protected Boolean run(Site site, final Item item) {
        if (item != null) {
            item.delete();
        }
        return Boolean.TRUE;
    }

    // Service Invokers migrated from Berserk

    private static final DeleteItem serviceInstance = new DeleteItem();

    @Atomic
    public static Boolean runDeleteItem(Site site, Item item) throws NotAuthorizedException {
        SiteManagerAuthorizationFilter.instance.execute(site);
        return serviceInstance.run(site, item);
    }

}