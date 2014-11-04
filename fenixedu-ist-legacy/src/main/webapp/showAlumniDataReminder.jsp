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
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<html:html xhtml="true">
	<head>
		<title>
			<bean:message key="dot.title" bundle="GLOBAL_RESOURCES" /> - <bean:message key="message.alumni.data.reminder.title" bundle="APPLICATION_RESOURCES"/>
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
				<h1><bean:message key="message.alumni.data.reminder.title" bundle="APPLICATION_RESOURCES"/></h1>
				<bean:message key="message.alumni.data.reminder.text" arg0="<%=org.fenixedu.academic.domain.organizationalStructure.Unit.getInstitutionAcronym()%>" bundle="APPLICATION_RESOURCES" />	
				<bean:message key="message.alumni.data.reminder.advantages" arg0="<%=org.fenixedu.academic.domain.organizationalStructure.Unit.getInstitutionAcronym()%>" bundle="APPLICATION_RESOURCES" />   	   	
   				<p><strong><bean:message key="message.alumni.data.reminder.fillInData" bundle="APPLICATION_RESOURCES" /></strong></p>   	
   				<p><bean:message key="message.alumni.data.reminder.moreInformation" bundle="APPLICATION_RESOURCES" />: <a href="<%= org.fenixedu.academic.domain.Installation.getInstance().getInstituitionURL() %>pt/alumni/" target="_blank"><%= org.fenixedu.academic.domain.Installation.getInstance().getInstituitionURL() %>pt/alumni/</a></p>   		
  			</div>
			
			<br />

			<div align="center">
				<table>
					<tr>
						<td>
							<fr:form action="/alumni/index.do">
								<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit"><bean:message bundle="APPLICATION_RESOURCES" key="label.proceed"/></html:submit>
							</fr:form>
						</td>
					</tr>
				</table>
			</div>
		</div>
		
	</body>
</html:html>
