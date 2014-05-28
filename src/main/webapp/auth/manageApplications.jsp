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

<h2>
	<bean:message key="oauthapps.label.manage.applications" bundle="APPLICATION_RESOURCES" />
</h2>

<logic:present role="role(DEVELOPER)">

<login:present name="allowIstIds">
    <html:link page="/externalApps.do?method=allowIstIds">
		<logic:equal name="allowIstIds" value="true">
	 		allow.ist.id is <b>ON</b>!
		</logic:equal>
		<logic:equal name="allowIstIds" value="false">
	 		allow.ist.id is off!
		</logic:equal>
	</html:link>
</login:present>


<div class="infoop2" style="width:500px;">
	<bean:message key="oauthapps.text.manage.applications" bundle="APPLICATION_RESOURCES" /></br>
	<html:link  target="_blank" page="/externalApps.do?method=showServiceAgreement">
		<bean:message key="oauthapps.label.show.service.agreement" bundle="APPLICATION_RESOURCES"/>
	</html:link>
</div>

<logic:notEmpty name="appsOwned">
		<fr:view name="appsOwned" schema="oauthapps.view.apps">
			<fr:layout name="tabular">
			
					<fr:property name="classes" value="tstyle4 thcenter thcenter"/>
					<fr:property name="columnClasses" value="tdcenter, tdcenter, tdcenter, tdcenter, tdcenter"/>
								
					<fr:property name="linkFormat(viewApplicationDetails)" value="<%= "/externalApps.do?method=viewApplicationDetails&appOid=${externalId}" %>" />
					<fr:property name="key(viewApplicationDetails)" value="oauthapps.label.view.application.details"/>
					<fr:property name="bundle(viewApplicationDetails)" value="APPLICATION_RESOURCES"/>
					
					<fr:property name="linkFormat(editApplication)" value="<%= "/externalApps.do?method=prepareEditApplication&appOid=${externalId}" %>" />
					<fr:property name="key(editApplication)" value="oauthapps.label.edit.application"/>
					<fr:property name="bundle(editApplication)" value="APPLICATION_RESOURCES"/>
					
					<fr:property name="linkFormat(deleteApplication)" value="<%= "/externalApps.do?method=deleteApplication&appOid=${externalId}" %>" />				
					<fr:property name="key(deleteApplication)" value="oauthapps.label.delete.application"/>
					<fr:property name="bundle(deleteApplication)" value="APPLICATION_RESOURCES"/>
						
			</fr:layout>
		</fr:view>
</logic:notEmpty>

<logic:empty name="appsOwned">
	<p><bean:message key="oauthapps.label.no.apps" bundle="APPLICATION_RESOURCES" /></p>
</logic:empty>

<p>
	<html:link page="/externalApps.do?method=prepareCreateApplication">
		<bean:message key="oauthapps.label.create.application" bundle="APPLICATION_RESOURCES"/>
	</html:link>
</p>

<jsp:include page="/auth/scopesFooter.jsp"></jsp:include>

<logic:notEmpty name="appsBanned">
	<br><br><h2>	
		<bean:message bundle="APPLICATION_RESOURCES" key="oauthapps.label.manage.banned.application"/>
	</h2>
	<fr:view name="appsBanned" schema="oauthapps.view.apps">
		<fr:layout name="tabular">			
				<fr:property name="classes" value="tstyle4 thcenter thcenter"/>
				<fr:property name="columnClasses" value="tdcenter, tdcenter, tdcenter, tdcenter, tdcenter"/>								
				<fr:property name="linkFormat(viewApplicationDetails)" value="<%= "/externalApps.do?method=viewApplicationDetails&appOid=${externalId}" %>" />
				<fr:property name="key(viewApplicationDetails)" value="oauthapps.label.view.application.details"/>
				<fr:property name="bundle(viewApplicationDetails)" value="APPLICATION_RESOURCES"/>											
		</fr:layout>
	</fr:view>
</logic:notEmpty>

<bean:define id="confirm">
	<bean:message bundle="APPLICATION_RESOURCES" key="oauthapps.label.confirm.delete.application"/> 
</bean:define>
<br><br>

<script type="text/javascript">
		$("table img").width("75px").height("75px");
		$("a[href*=deleteApplication]").click(function(e) {
			   answer = confirm('<%= confirm %>');
			   return answer;
			});
</script>
</logic:present>

<logic:notPresent role="role(DEVELOPER)">
<style>
.page{
    margin-top: 1%;
    padding: 0;
    height: 50%;
    width: 75%;
    display: block; 
    border:solid #000 1px;
}
.agreement-content{
    padding:5px;
    overflow: scroll; overflow-x:hidden;
    height:300px;
    /*-webkit-overflow-scrolling: touch;*/    
}
</style>
<div class="infoop2">
	<p><bean:message bundle="APPLICATION_RESOURCES" key="oauthapps.text.manage.applications.register"/></p>
</div>
<div class="page">
<div class="agreement-content">
	<bean:write name="serviceAgreement" filter="false"/>
</div>
</div>

<div style="width: 80%; margin: 1em 1em 0 0; padding: 0 1em 1em 1em; text-align: left;">
				<form action="<%= request.getContextPath() + "/person/externalApps.do?method=agreeServiceAgreement"%>" method="post">
					<html:hidden property="method" value="agreeServiceAgreement"/>
					<p>
						<input type="checkbox" name="agreedServiceAgreement"/>
						<bean:message bundle="APPLICATION_RESOURCES" key="oauthapps.text.manage.applications.agree.terms"/>
					</p>
					<p>
						<html:submit>
							<bean:message bundle="APPLICATION_RESOURCES" key="label.submit"/>
						</html:submit>
					</p>
				</form>
</div>
</logic:notPresent>