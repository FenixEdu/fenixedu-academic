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
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>

<br/>
<bean:message key="message.copySite.information.destination" />
<strong><bean:write name="siteView" property="commonComponent.executionCourse.nome"/></strong>
<bean:message key="message.copySite.information.destination.ofperiod" />
<strong>
<bean:write name="siteView" property="commonComponent.executionCourse.infoExecutionPeriod.name"/>
<bean:write name="siteView" property="commonComponent.executionCourse.infoExecutionPeriod.infoExecutionYear.year"/>
</strong>
<bean:message key="message.copySite.information.whatIsCopied" />
<br/><br/>
<span class="error"><!-- Error messages go here --><html:errors /></span>
<logic:present name="<%= PresentationConstants.LIST_EXECUTION_PERIODS %>">
	<html:form action="/copySiteExecutionCourse">  
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="prepareChooseExecDegreeAndCurYear"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.objectCode" property="objectCode"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1" />
		<table>
			<tr>
				<td>
					<bean:message key="label.copySite.execution.period"/>
				</td>
				<td>
					<html:select bundle="HTMLALT_RESOURCES" altKey="select.executionPeriod" property="executionPeriod">
						<html:option value="" key="label.copySite.select.one">
							<bean:message key="label.copySite.select.one"/>
						</html:option>
						<html:optionsCollection name="<%= PresentationConstants.LIST_EXECUTION_PERIODS %>"/>
					</html:select>
				</td>
			</tr>
		</table>
		<br />
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message key="button.continue"/></html:submit>
	</html:form>
</logic:present>
<logic:notPresent name="<%= PresentationConstants.LIST_EXECUTION_PERIODS %>">
	<span class="error"><!-- Error messages go here -->
		<html:errors /><bean:message key="error.copySite.noExecutionPeriods"/>
	</span>
</logic:notPresent>