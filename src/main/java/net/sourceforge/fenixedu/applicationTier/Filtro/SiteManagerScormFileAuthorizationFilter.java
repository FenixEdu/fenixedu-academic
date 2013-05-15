package net.sourceforge.fenixedu.applicationTier.Filtro;

import net.sourceforge.fenixedu.applicationTier.Servico.manager.CreateScormFile.CreateScormFileItemForItemArgs;
import net.sourceforge.fenixedu.domain.Site;

public class SiteManagerScormFileAuthorizationFilter extends SiteManagerAuthorizationFilter {

    @Override
    protected Site getSite(Object[] parameters) {
        CreateScormFileItemForItemArgs args = (CreateScormFileItemForItemArgs) parameters[0];
        return args.getSite();
    }

}
