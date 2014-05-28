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
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>


<html:html xhtml="true">
	<head>
		<title>
			<bean:message key="dot.title" bundle="GLOBAL_RESOURCES" /> - <bean:message key="label.contact.validation.title" bundle="APPLICATION_RESOURCES"/>
		</title>

		<link href="<%= request.getContextPath() %>/CSS/logdotist.css" rel="stylesheet" type="text/css" />

		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		
	</head>
	<body>
	
		<div id="container">
			<div id="dotist_id">
				<img alt="<%=org.fenixedu.bennu.portal.domain.PortalConfiguration.getInstance().getApplicationTitle().getContent() %>"
						src="<bean:message key="dot.logo" bundle="GLOBAL_RESOURCES" arg0="<%= request.getContextPath() %>"/>" />
			</div>
			<div id="txt">
				<h1><bean:message key="label.person.title.contactAndAuthorization.pending" bundle="APPLICATION_RESOURCES"/></h1>
				<div class="warning1" style="margin: 20px; padding: 20px">
					<bean:define id="person" name="LOGGED_USER_ATTRIBUTE" property="person"/>
					<bean:define id="pendingContacts" name="person" property="allPendingPartyContacts"/>
					<ul>
					<logic:iterate id="pendingContact" name="pendingContacts">
						<li>
							<bean:message key="<%=  "label.partyContacts." + pendingContact.getClass().getSimpleName() %>" /> (<bean:message name="pendingContact" property="type.qualifiedName" bundle="ENUMERATION_RESOURCES" />) - <bean:write name="pendingContact" property="presentationValue"/>
						</li>
					</logic:iterate>
					</ul>
				</div>
				<bean:message key="label.contact.validation.text" bundle="APPLICATION_RESOURCES"/>
			</div>
			
			
			<br />
			<div align="center">
				<table>
					<tr>
						<td>
							<fr:form action="/person/visualizePersonalInfo.do#pendingContacts">
								<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit"><bean:message bundle="APPLICATION_RESOURCES" key="label.proceed"/></html:submit>
							</fr:form>
						</td>
					</tr>
				</table>
			</div>
		</div>
	</body>
</html:html>
