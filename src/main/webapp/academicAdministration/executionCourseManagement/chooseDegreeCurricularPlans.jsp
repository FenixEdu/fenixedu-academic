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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/enum" prefix="e" %>

<html:form action="/createExecutionCourses" >
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="createExecutionCourses"/>

	<bean:define id="degreeType" name="degreeType" type="java.lang.String"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeType" property="degreeType" value="<%= degreeType %>"/>
	<bean:define id="degreeTypeName">
		<bean:message bundle="ENUMERATION_RESOURCES" key="<%= degreeType %>"/>
	</bean:define>
	
	<logic:messagesPresent message="true" property="success">
		<p>
			<span class="success0">
				<html:messages id="messages" message="true" bundle="MANAGER_RESOURCES" property="success">
					<bean:write name="messages" />
				</html:messages>
			</span>
		</p>
	</logic:messagesPresent>
	<logic:messagesPresent message="true" property="error">
		<p>
			<span class="error0">
				<html:messages id="messages" message="true" bundle="MANAGER_RESOURCES" property="error">
					<bean:write name="messages" />
				</html:messages>
			</span>
		</p>
	</logic:messagesPresent>
	
	<table>
		<tr>
			<th colspan="2">
				<bean:message bundle="MANAGER_RESOURCES" key="label.manager.executionCourseManagement.executionPeriod" />: 
				<html:select bundle="HTMLALT_RESOURCES" altKey="select.executionPeriodID" property="executionPeriodID" size="1">
					<html:option value=""></html:option>
					<html:options collection="executionPeriods" property="externalId" labelProperty="description"/>
				</html:select>
			</th>
		</tr>
		
		<tr><td>&nbsp;</td></tr>

		<tr>
			<th colspan="2">
				<bean:message bundle="MANAGER_RESOURCES" key="label.manager.degreeCurricularPlans" />:
			</th>
		</tr>
		<logic:notEmpty name="degreeCurricularPlans">
		<logic:iterate id="degreeCurricularPlan" name="degreeCurricularPlans" >
			<tr>
				<td>
					<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.degreeCurricularPlansIDs" property="degreeCurricularPlansIDs">
						<bean:write name="degreeCurricularPlan" property="externalId"/>
					</html:multibox>
				</td>
				<td>
					<bean:write name="degreeCurricularPlan" property="infoDegree.nome"/> -
					<bean:write name="degreeCurricularPlan" property="name"/>
				</td>
			</tr>
		</logic:iterate>
		</logic:notEmpty>
		<logic:empty name="degreeCurricularPlans">
			<tr><td>
				<p class="warning0">
					<bean:message bundle="MANAGER_RESOURCES" key="message.manager.executionCourseManagement.create.noDegrees" arg0="<%= degreeTypeName %>"/>
				</p>
			</td></tr>
		</logic:empty>

		<tr><td>&nbsp;</td></tr>	
		
		<tr>
			<td>
				<html:submit>
					<bean:message bundle="MANAGER_RESOURCES" key="label.create"/>
				</html:submit>
			</td>
		</tr>
	</table>
	
</html:form>