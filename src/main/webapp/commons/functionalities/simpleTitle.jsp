<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>

<logic:present name="<%= org.fenixedu.bennu.portal.servlet.BennuPortalDispatcher.SELECTED_FUNCTIONALITY %>">
	<bean:write name="<%= org.fenixedu.bennu.portal.servlet.BennuPortalDispatcher.SELECTED_FUNCTIONALITY %>"
		property="parent.title.content"/> -
        <bean:write name="<%= org.fenixedu.bennu.portal.servlet.BennuPortalDispatcher.SELECTED_FUNCTIONALITY %>" property="title.content" />
</logic:present>
