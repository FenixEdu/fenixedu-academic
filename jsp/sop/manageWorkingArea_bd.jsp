<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<h2><bean:message key="title.manage.working.area"/></h2>
<br />

<bean:message key="list.title.working.areas"/>
<br />
<logic:present name="<%= SessionConstants.LIST_WORKING_AREAS %>" scope="request">
	<logic:iterate id="infoWorkingArea" name="<%= SessionConstants.LIST_WORKING_AREAS %>">
		<bean:write name="infoWorkingArea" property="name" /> - 
		<bean:write name="infoWorkingArea" property="infoExecutionYear.year" />
		<br />
	</logic:iterate>
</logic:present>

<logic:notPresent name="<%= SessionConstants.LIST_WORKING_AREAS %>" scope="request">
	<span class="error">
		<bean:message key="errors.working.area.none"/>
	</span>
</logic:notPresent>

<br />

<logic:present name="<%= SessionConstants.LIST_EXECUTION_PERIODS %>" scope="request">

	<html:form action="/createWorkingAreaForm">
		Criar Área de Trabalho: 
		<span class="error"><html:errors /></span>
		<br />
		<table>
			<tr>
				<td>
				</td>
				<td>
				Semestre
				</td>
				<td>
				Ano Lectivo
				</td>
			</tr>
			<tr>
				<td>
				Para o periodo:
				</td>
				<td>
				<html:text property="semesterToCreate" size="2"/>
				</td>
				<td>
				<html:text property="yearToCreate"  size="10"/>
				</td>
			</tr>
			<tr>
				<td>
				Importar dados do periodo:
				</td>
				<td>
				<html:text property="semesterToExportDataFrom" size="2"/>
				</td>
				<td>
				<html:text property="yearToExportDataFrom" size="10"/>
				</td>
			</tr>
			<tr>
				<td>
				</td>
				<td>
					<html:hidden property="method" value="createWorkingArea"/>
					<html:hidden property="page" value="1"/>
					<html:submit property="operation" styleClass="inputbutton">
						<bean:message key="label.create"/>
					</html:submit>
				</td>
				<td>
					<html:reset value="Limpar" styleClass="inputbutton">
						<bean:message key="label.clear"/>
					</html:reset>
				</td>
			</tr>
		</table>
	</html:form>

	<bean:message key="list.title.execution.periods"/>
	<br />
	<logic:iterate id="infoExecutionPeriod" name="<%= SessionConstants.LIST_EXECUTION_PERIODS %>">
		<bean:write name="infoExecutionPeriod" property="name" /> - 
		<bean:write name="infoExecutionPeriod" property="infoExecutionYear.year" /> 
		<bean:define id="year" name="infoExecutionPeriod" property="infoExecutionYear.year"/>
		<bean:define id="semester" name="infoExecutionPeriod" property="semester"/>
		<html:link page="<%= "/manageWorkingArea.do?method=createWorkingArea"
								+ "&amp;year="
								+ year
								+ "&amp;semester="
								+ semester %>" >
			<bean:message key="link.export.to.working.area"/>
		</html:link>
		<br />
	</logic:iterate>

</logic:present>

<logic:notPresent name="<%= SessionConstants.LIST_EXECUTION_PERIODS %>" scope="request">
	<span class="error">
		<html:errors /><bean:message key="errors.execution.period.none"/>
	</span>
</logic:notPresent>
