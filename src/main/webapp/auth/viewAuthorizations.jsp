<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page isELIgnored="true"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/struts-example-1.0" prefix="app"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>

<html:xhtml />
<%@ page
	import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants"%>


<h2 class="mtop15">
	<bean:message key="oauthapps.label.app.details" bundle="APPLICATION_RESOURCES" />
</h2>

<h2></h2>

<fr:view name="application" layout="tabular" schema="oauthapps.view.app.basic.details" type="net.sourceforge.fenixedu.domain.ExternalApplication">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4 thlight thright"/>
	</fr:layout>
</fr:view>
<logic:notEmpty name="authorizations">

<h2 class="mtop25">
	<bean:message key="oauthapps.label.manage.authorizations" bundle="APPLICATION_RESOURCES" />
</h2>



<fr:view name="authorizations" schema="oauthapps.view.authorizations">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4 thcenter thcenter"/>
		<fr:property name="columnClasses" value="tdcenter, tdcenter, tdcenter, tdcenter, tdcenter"/>
		
		<fr:property name="linkFormat(revokeAuthorization)" value="<%= "/externalApps.do?method=revokeAuth&authorizationOid=${externalId}" %>" />
		<fr:property name="key(revokeAuthorization)" value="oauthapps.label.revoke.authorization"/>
		<fr:property name="bundle(revokeAuthorization)" value="APPLICATION_RESOURCES"/>
	</fr:layout>
</fr:view>


<p>
	<html:link page="/externalApps.do?method=revokeApplication" paramId="appOid" paramName="application" paramProperty="externalId">
		<bean:message bundle="APPLICATION_RESOURCES" key="oauthapps.label.revoke.all.authorizations"/>
	</html:link>
</p>

<bean:define id="confirm_revoke">
	<bean:message bundle="APPLICATION_RESOURCES" key="oauthapps.label.confirm.revoke.authorization"/> 
</bean:define>

<bean:define id="confirm_delete">
	<bean:message bundle="APPLICATION_RESOURCES" key="oauthapps.label.confirm.delete.application"/> 
</bean:define>

<script type="text/javascript">
		$("table img").width("75px").height("75px");
		$("a[href*=revokeAuth]").click(function(e) {
			   answer = confirm('<%= confirm_revoke %>');
			   return answer;
			});
		$("a[href*=revokeApplication]").click(function(e) {
			   answer = confirm('<%= confirm_delete %>');
			   return answer;
			});
</script>
</logic:notEmpty>

<logic:empty name="authorizations">
	<bean:message key="oauthapps.label.no.authorization" bundle="APPLICATION_RESOURCES" />
</logic:empty>

<jsp:include page="/auth/scopesFooter.jsp"></jsp:include>
	