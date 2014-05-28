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
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/enum" prefix="e"%>

<h2><bean:message key="manager.announcements.title.label" bundle="MANAGER_RESOURCES"/></h2>

<jsp:include flush="true" page="/messaging/context.jsp"/>

<logic:present name="unit">

	<p class="mtop2 mbottom0"><bean:message key="label.channel" bundle="MESSAGING_RESOURCES"/>: <span class="emphasis1"><bean:write name="unit" property="name"/></span></p>

	
	<%--
	<bean:message bundle="MANAGER_RESOURCES" key="manager.announcements.create.creatingForUnit.label"/>
	<bean:write name="unit" property="name"/>
	--%>
	
	<bean:define id="boards" name="unit" property="boards"/>
	<bean:define id="person" name="person" type="net.sourceforge.fenixedu.domain.Person"/>
	
	<jsp:include page="/messaging/announcements/listAnnouncementBoards.jsp" flush="true"/>		


<p class="mbottom0"><strong><bean:message key="label.createChannel" bundle="MESSAGING_RESOURCES"/>:</strong></p>

<%--
<p><span>(associada a <bean:write name="unit" property="name"/>)</span></p>
--%>

	<html:form action="/announcements/manageUnitAnnouncementBoard" >
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="createBoard"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.keyUnit" property="keyUnit"/>
		<table class="tstyle5 thlight thright">
			<tr>
				<th>
					Nome:
				</th>
				<td>
					<html:text bundle="HTMLALT_RESOURCES" altKey="text.name" property="name" size="50"/>
				</td>				
			</tr>
			<tr>
				<th>
					<bean:message key="label.mandatory" bundle="MESSAGING_RESOURCES"/>
				</th>
				<td>
					<html:checkbox bundle="HTMLALT_RESOURCES" altKey="checkbox.mandatory" property="mandatory" value="true"/>
				</td>				
			</tr>
			<tr>
				<th>
					Quem pode ler:
				</th>
				<e:labelValues id="values" bundle="ENUMERATION_RESOURCES"  enumeration="net.sourceforge.fenixedu.domain.UnitBoardPermittedGroupType" />				
				<td>
					<html:select property="unitBoardReadPermittedGroupType">
						<html:options collection="values" property="value" labelProperty="label" />
					</html:select>
				</td>				
			</tr>
			<tr>
				<th>
					Quem pode escrever:
				</th>
				<td>
					<html:select property="unitBoardWritePermittedGroupType">
						<html:options collection="values" property="value" labelProperty="label" />
					</html:select>
				</td>				
			</tr>
			<tr>
				<th>
					Quem pode gerir:
				</th>
				<td>
					<html:select property="unitBoardManagementPermittedGroupType">
						<html:options collection="values" property="value" labelProperty="label" />
					</html:select>
				</td>				
			</tr>										
		</table>
		<html:submit>
			<bean:message key="label.create" bundle="MANAGER_RESOURCES"/>
		</html:submit>
	</html:form>
		
</logic:present>