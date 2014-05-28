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
<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>

<em><bean:message key="label.teacherPortal"/></em>
<h2><bean:message key="title.teacherInformation"/></h2>
<html:form action="/oldCientificPublication">

<h3>
	<logic:present name="infoOldPublication">
		<bean:message key="message.publications.insertPublication" />
	</logic:present>
	<logic:notPresent name="infoOldPublication">
		<bean:message key="message.publications.editPublication" />
	</logic:notPresent>
</h3>
<p class="infoop"><span class="emphasis-box">1</span>
	<logic:present name="infoOldPublication">
		<bean:message key="message.publications.managementInsert" />
	</logic:present>
	<logic:notPresent name="infoOldPublication">
		<bean:message key="message.publications.managementEdit" />
	</logic:notPresent></p>
	<span class="error"><!-- Error messages go here -->
		<html:errors/>
	</span>
	<br />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.externalId" property="externalId"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.infoTeacher#externalId" property="infoTeacher#externalId"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.oldPublicationTypeString" property="oldPublicationTypeString" value="Cientific"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="edit"/>
<table>
	<tr>
		<td><bean:message key="message.publications.publication" /></td>
	</tr>
	<tr>
		<td><html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.publication" property="publication" cols="90%" rows="4"/></td>
	<tr/>
</table>
<br/>
<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.confirm" styleClass="inputbutton" property="confirm"><bean:message key="button.save"/>
</html:submit> 
<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset" styleClass="inputbutton"><bean:message key="label.clear"/>
</html:reset>  
</html:form>
