<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Academic.

    FenixEdu Academic is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Academic is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml />
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ page import="org.fenixedu.academic.ui.struts.action.resourceAllocationManager.utils.PresentationConstants"%>

<h2><bean:message bundle="MANAGER_RESOURCES" key="title.manage.execution.periods" /></h2>
<br />

<bean:message bundle="MANAGER_RESOURCES" key="message.manage.execution.periods" />
<br />
<br />

	<%--
	<html:form action="/createExecutionPeriodForm">
		Criar Periodo Execução: 
		<span class="error"><!-- Error messages go here --><html:errors /></span>
		<br />
		<table>
			<tr>
				<td></td>
				<td>Semestre</td>
				<td>Ano Lectivo</td>
			</tr>
			<tr>
				<td>Para o periodo:</td>
				<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.semesterToCreate" property="semesterToCreate" size="2"/></td>
				<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.yearToCreate" property="yearToCreate"  size="10"/></td>
			</tr>
			<tr>
				<td>Importar dados do periodo:</td>
				<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.semesterToExportDataFrom" property="semesterToExportDataFrom" size="2"/></td>
				<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.yearToExportDataFrom" property="yearToExportDataFrom" size="10"/></td>
			</tr>
			<tr>
				<td></td>
				<td>
					<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="createExecutionPeriod"/>
					<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
					<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.operation" property="operation" styleClass="inputbutton">
						<bean:message bundle="MANAGER_RESOURCES" key="label.create"/>
					</html:submit>
				</td>
				<td>
					<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset" value="Limpar" styleClass="inputbutton">
						<bean:message bundle="MANAGER_RESOURCES" key="label.clear"/>
					</html:reset>
				</td>
			</tr>
		</table>
	</html:form>
 --%>

<logic:notEmpty name="periods">
<div class="col-lg-6"> 
	<bean:message bundle="MANAGER_RESOURCES" key="list.title.execution.periods" />
	<br />
	<logic:messagesPresent message="true" property="success">
		<p>
			<span class="success0">
				<html:messages id="messages" message="true" bundle="MANAGER_RESOURCES" property="success">
					<!-- Success messages go here  --><bean:write name="messages" />
				</html:messages>
			</span>
		</p>
	</logic:messagesPresent>
	<logic:messagesPresent message="true" property="error">
		<p>
			<span class="error0">
				<html:messages id="messages" message="true" bundle="MANAGER_RESOURCES" property="error">
					<!-- Error messages go here  --><bean:write name="messages" />
				</html:messages>
			</span>
		</p>
	</logic:messagesPresent>
	<table class="table table-bordered table-striped text-center">
		<thead>
			<th><bean:message bundle="MANAGER_RESOURCES" key="label.manager.execution.period.semester" /></th>
			<th><bean:message bundle="MANAGER_RESOURCES" key="label.manager.execution.period.executionYear" /></th>
			<th><bean:message bundle="MANAGER_RESOURCES" key="label.manager.execution.period.state" /></th>
			<th><bean:message bundle="MANAGER_RESOURCES" key="label.manager.execution.period.operations" /></th>			
			<th></th>			
		</thead>
		<logic:iterate id="infoExecutionPeriod"	name="periods">
			<bean:define id="year" name="infoExecutionPeriod" property="executionYear.year" />
			<bean:define id="semester" name="infoExecutionPeriod" property="childOrder" />
			<bean:define id="periodState" name="infoExecutionPeriod" property="state.stateCode" />
			<tr>
				<td><bean:write name="infoExecutionPeriod" property="name" /></td>
				<td><bean:write name="infoExecutionPeriod" property="executionYear.year" /></td>
				<td><bean:write name="infoExecutionPeriod" property="state" /></td>
				<td>
					<logic:equal name="infoExecutionPeriod" property="state.stateCode" value="NO">
						<html:link module="/manager"
							page="<%= "/manageExecutionPeriods.do?method=alterExecutionPeriodState"
									+ "&amp;year=" + year + "&amp;semester=" + semester + "&amp;periodState=O" %>">
							<bean:message bundle="MANAGER_RESOURCES" key="link.open.execution.period" />
						</html:link>
					</logic:equal>
					<logic:equal name="infoExecutionPeriod" property="state.stateCode" value="O">
						<html:link module="/manager"
							page="<%= "/manageExecutionPeriods.do?method=alterExecutionPeriodState"
									+ "&amp;year=" + year + "&amp;semester=" + semester + "&amp;periodState=NO" %>">
							<bean:message bundle="MANAGER_RESOURCES" key="link.not.open.execution.period" />
						</html:link>
						<br />
						<bean:define id="deleteConfirm">
							return confirm('<bean:message bundle="MANAGER_RESOURCES" key="label.executionPeriod.make.current.confirm"/>')
						</bean:define>	
						<html:link module="/manager"
								page="<%= "/manageExecutionPeriods.do?method=alterExecutionPeriodState"
										+ "&amp;year=" + year + "&amp;semester=" + semester + "&amp;periodState=C" %>"
								onclick="<%= deleteConfirm %>">
							<bean:message bundle="MANAGER_RESOURCES" key="link.current.execution.period" />
						</html:link>
						<br />
						<html:link module="/manager"
							page="<%= "/manageExecutionPeriods.do?method=alterExecutionPeriodState"
									+ "&amp;year=" + year + "&amp;semester=" + semester + "&amp;periodState=CL" %>">
							<bean:message bundle="MANAGER_RESOURCES" key="link.close.execution.period" />
						</html:link>
					</logic:equal>
					<logic:equal name="infoExecutionPeriod" property="state.stateCode" value="C">
					</logic:equal>
					<logic:equal name="infoExecutionPeriod" property="state.stateCode" value="CL">
						<html:link module="/manager"
							page="<%= "/manageExecutionPeriods.do?method=alterExecutionPeriodState"
									+ "&amp;year=" + year + "&amp;semester=" + semester + "&amp;periodState=O" %>">
							<bean:message bundle="MANAGER_RESOURCES" key="link.open.execution.period" />
						</html:link>
					</logic:equal>
				</td>
				
				<td> 
					<bean:define id="editURL">
						/manageExecutionPeriods.do?method=edit&amp;executionPeriodID=<bean:write name="infoExecutionPeriod" property="externalId"/>
					</bean:define>
					<html:link page="<%= editURL %>">								
						<bean:message bundle="MANAGER_RESOURCES" key="link.edit.execution.period" />
					</html:link>
				</td>	
			</tr>
		</logic:iterate>
	</table>
</div>
</logic:notEmpty>

<logic:empty name="periods">
	<span class="error"><!-- Error messages go here --> <html:errors />
		<bean:message bundle="MANAGER_RESOURCES" key="errors.execution.period.none" />
	</span>
</logic:empty>
