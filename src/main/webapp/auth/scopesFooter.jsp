<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/struts-example-1.0" prefix="app"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ page import="net.sourceforge.fenixedu.domain.RootDomainObject"%>

<% 
	request.setAttribute("scopes", RootDomainObject.getInstance().getAuthScopes());
%>

<div class="mtop3" style="width:600px;">
<p class="infoop3"><b><bean:message bundle="APPLICATION_RESOURCES" key="oauthapps.title.app.scopes"/></b></p>

<fr:view name="scopes">
	<fr:layout name="tabular-list"> 
		<fr:property name="subSchema" value="oauthapps.view.scope.complete"/>
		<fr:property name="subLayout" value="values"/>
	</fr:layout>
</fr:view>
</div>