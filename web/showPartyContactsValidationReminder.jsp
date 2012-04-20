<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>


<html:html xhtml="true">
	<head>
		<title>
			<bean:message key="dot.title" bundle="GLOBAL_RESOURCES" /> - <bean:message key="label.contact.validation.title" bundle="APPLICATION_RESOURCES"/>
		</title>

		<link href="<%= request.getContextPath() %>/CSS/logdotist.css" rel="stylesheet" type="text/css" />

		<meta http-equiv="Content-Type" content="text/html; charset=<%= net.sourceforge.fenixedu._development.PropertiesManager.DEFAULT_CHARSET %>" />
		
	</head>
	<body>
	
		<div id="container">
			<div id="dotist_id">
				<img alt="<bean:message key="institution.logo" bundle="IMAGE_RESOURCES" />"
						src="<bean:message key="dot.logo" bundle="GLOBAL_RESOURCES" arg0="<%= request.getContextPath() %>"/>" />
			</div>
			<div id="txt">
				<h1><bean:message key="label.person.title.contactAndAuthorization.pending" bundle="APPLICATION_RESOURCES"/></h1>
				<div class="warning1" style="margin: 20px; padding: 20px">
					<bean:define id="person" name="<%=pt.ist.fenixWebFramework.servlets.filters.SetUserViewFilter.USER_SESSION_ATTRIBUTE%>" property="person"/>
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
							<fr:form action="/person/visualizePersonalInfo.do?contentContextPath_PATH=/pessoal/pessoal#pendingContacts">
								<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit"><bean:message bundle="APPLICATION_RESOURCES" key="label.proceed"/></html:submit>
							</fr:form>
						</td>
					</tr>
				</table>
			</div>
		</div>
	</body>
</html:html>
