package net.sourceforge.fenixedu.presentationTier.Action.commons.delegates.delegate;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(module = "delegate", path = "/findDelegates", scope = "request", parameter = "method")
@Forwards(value = {
        @Forward(name = "showDelegates", path = "/commons/delegates/showDelegates.jsp", tileProperties = @Tile(
                title = "private.delegate.view.delegates")),
        @Forward(name = "searchDelegates", path = "/commons/delegates/searchDelegates.jsp", tileProperties = @Tile(
                title = "private.delegate.view.delegates")) })
public class FindDelegatesDispatchActionForDelegate extends
        net.sourceforge.fenixedu.presentationTier.Action.commons.delegates.FindDelegatesDispatchAction {
}