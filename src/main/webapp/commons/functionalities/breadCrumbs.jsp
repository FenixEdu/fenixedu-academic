<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<div class="breadcrumbs">
	<fr:view name="<%= org.fenixedu.bennu.portal.servlet.BennuPortalDispatcher.SELECTED_FUNCTIONALITY %>" 
			 type="org.fenixedu.bennu.portal.domain.MenuFunctionality"
			 layout="bread-crumbs"/>
</div>
