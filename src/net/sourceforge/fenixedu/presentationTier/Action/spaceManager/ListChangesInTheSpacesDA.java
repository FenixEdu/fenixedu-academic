package net.sourceforge.fenixedu.presentationTier.Action.spaceManager;

import java.util.ArrayList;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.DomainObjectActionLog;
import net.sourceforge.fenixedu.domain.space.Space;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;
import pt.utl.ist.fenix.tools.util.CollectionPager;

@Mapping(module = "SpaceManager", path = "/listChangesInTheSpaces", scope = "session", parameter = "method")
@Forwards(value = { @Forward(name = "listChangesInTheSpaces", path = "/spaceManager/listChangesInTheSpaces.jsp",
        tileProperties = @Tile(title = "private.spacemanagement.listchanges")) })
public class ListChangesInTheSpacesDA extends FenixDispatchAction {

    public ActionForward changesList(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        Set<DomainObjectActionLog> listOfChangesInSpaces = Space.getListOfChangesInSpacesOrderedByInstant();
        CollectionPager<DomainObjectActionLog> collectionPager =
                new CollectionPager<DomainObjectActionLog>(
                        listOfChangesInSpaces != null ? listOfChangesInSpaces : new ArrayList(), 50);

        final String pageNumberString = request.getParameter("pageNumber");
        final Integer pageNumber =
                !StringUtils.isEmpty(pageNumberString) ? Integer.valueOf(pageNumberString) : Integer.valueOf(1);

        request.setAttribute("pageNumber", pageNumber);
        request.setAttribute("domainObjectActionLogs", collectionPager.getPage(pageNumber.intValue()));
        request.setAttribute("numberOfPages", Integer.valueOf(collectionPager.getNumberOfPages()));

        return mapping.findForward("listChangesInTheSpaces");
    }
}
