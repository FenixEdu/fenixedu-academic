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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ page import="org.apache.struts.Globals" %>
 <bean:define id="rooms" name="infoExam" property="associatedRooms"/>
 <html:form action="/distributeStudentsByRoom">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="distribute" />	
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.objectCode" property="objectCode"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.evaluationCode" property="evaluationCode"/>
	<logic:present name="<%= Globals.ERROR_KEY %>">
		<span class="error"><!-- Error messages go here --><html:errors /></span><br/><br/>
	</logic:present>
	<table width="98%" cellpadding="0" cellspacing="0">
		<tr>
			<td class="infoop"><bean:message key="label.distribute.information" /></td>
		</tr>
	</table>
	<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.enroll" property="enroll" value="true"><bean:message key="label.distribute.enrolled"/> (<bean:write name="infoExam" property="enrolledStudents"/>)</html:radio><br/>
	<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.enroll" property="enroll" value="false"><bean:message key="label.distribute.attend"/> (<bean:write name="attendStudents"/>)</html:radio><br/><br/> 
	<bean:message key="label.distribution.information"/>
    <br />
    <br />    
	<table>
		<logic:iterate id="infoRoom" name="rooms" indexId="roomIndex" type="net.sourceforge.fenixedu.dataTransferObject.InfoRoom">
			<tr>
				<td>
					<b><%= roomIndex.intValue() + 1 %>.</b>
				</td>
				<td>
					<html:select bundle="HTMLALT_RESOURCES" altKey="select.rooms" property="rooms" value="<%= infoRoom.getExternalId().toString() %>">
						<html:options collection="rooms" property="externalId" labelProperty="nome" />
					</html:select>
				</td>
			</tr> 
		</logic:iterate>
	</table>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
		<bean:message key="label.ok"/>
	</html:submit>
</html:form>