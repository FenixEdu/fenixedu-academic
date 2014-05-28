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
<%@page contentType="text/html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %><html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/struts-example-1.0" prefix="app" %>
<h2><bean:message bundle="MANAGER_RESOURCES" key="link.manager.teachersManagement.removeECAssociation" /></h2>
<span class="error"><!-- Error messages go here --><html:errors /></span>
<html:form action="/dissociateProfShipsAndRespFor">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="dissociateProfShipsAndRespFor"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.teacherId" property="teacherId"/> 
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="0"/>
	<p class="infoop">
		<bean:message bundle="MANAGER_RESOURCES" key="message.manager.teachersManagement.choosePSorRF"/>
	</p>
	<logic:present name="person">
		<b>
			<bean:message bundle="MANAGER_RESOURCES" key="label.manager.teachersManagement.teacher"/>&nbsp;
			<bean:write name="person" property="istUsername"/>&nbsp;-&nbsp;
			<bean:write name="person" property="name"/>
		</b>
		<br /><br />	
		<table>
			<bean:size id="professorshipsListSize" name="person" property="professorships"/>
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.professorshipsListSize" property="professorshipsListSize" value="<%=professorshipsListSize.toString()%>"/>
			<logic:greaterThan name="professorshipsListSize" value="0">
				<tr>
					<th class="listClasses-header"><bean:message bundle="MANAGER_RESOURCES" key="label.manager.teachersManagement.dissociate"/></th>
					<th class="listClasses-header"><bean:message bundle="MANAGER_RESOURCES" key="label.manager.teachersManagement.professorShips"/></th>
				</tr>
				<logic:iterate id="professorship" name="person" property="professorships">			
					<tr>
						<td class="listClasses">
							<bean:define id="internalId" name="professorship" property="externalId"/>
							<html:checkbox bundle="HTMLALT_RESOURCES" altKey="checkbox.toDelete" name="professorship" property="responsibleFor" value="false" indexed="true"/> 
							<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.externalId" name="professorship" property="externalId" indexed="true" value="<%= internalId.toString() %>"/>
						</td>
						<td class="listClasses"><bean:write name="professorship" property="executionCourse.nome"/></td>
					</tr>
				</logic:iterate>
			</logic:greaterThan>
			<logic:equal name="professorshipsListSize" value="0">
				<tr>
					<td colspan="2">
						<i><bean:message bundle="MANAGER_RESOURCES" key="message.manager.teachersManagement.noProfessorships"/></i>
					</td>
				</tr>
			</logic:equal>
		</table>
		<br /><br />
		<table>
			<bean:size id="responsibleForListSize" name="person" property="responsableProfessorships"/>
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.responsibleForListSize" property="responsibleForListSize" value="<%=responsibleForListSize.toString()%>"/>			
			<logic:greaterThan name="responsibleForListSize" value="0">
				<tr>
					<th class="listClasses-header"><bean:message bundle="MANAGER_RESOURCES" key="label.manager.teachersManagement.dissociate"/></th>
					<th class="listClasses-header"><bean:message bundle="MANAGER_RESOURCES" key="label.manager.teachersManagement.responsibleFor"/></th>
				</tr>
				<logic:iterate id="responsibleFor" name="person" property="responsableProfessorships">			
					<tr>
						<td class="listClasses">
							<bean:define id="internalId" name="responsibleFor" property="externalId"/>
							<html:checkbox  bundle="HTMLALT_RESOURCES" altKey="checkbox.toDelete" name="responsibleFor" property="responsibleFor" indexed="true"/> 
							<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.externalId" name="responsibleFor" property="externalId" indexed="true" value="<%= internalId.toString() %>"/>
						</td>
						<td class="listClasses"><bean:write name="responsibleFor" property="executionCourse.nome"/></td>
					</tr>
				</logic:iterate>
			</logic:greaterThan>
			<logic:equal name="responsibleForListSize" value="0">
				<tr>
					<td colspan="2">
						<i><bean:message bundle="MANAGER_RESOURCES" key="message.manager.teachersManagement.noResponsibleFor"/></i>
					</td>
				</tr>			
			</logic:equal>
		</table>
	</logic:present>
	<p>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message bundle="MANAGER_RESOURCES" key="button.manager.teachersManagement.dissociate"/>                    		         	
		</html:submit> 
	</p>
</html:form>