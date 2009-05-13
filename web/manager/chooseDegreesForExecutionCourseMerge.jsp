<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>


<%@page
	import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants"%>
<%@page import="net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicInterval"%>

<xhtml />

<p>
<h2><bean:message key="title.execution.course.merge" bundle="SOP_RESOURCES" /></h2>
</p>

<span class="error"><!-- Error messages go here --><html:errors /></span>

<fr:form action="/chooseDegreesForExecutionCourseMerge.do?method=academicIntervalPostBack">
	<fr:edit name="<%=PresentationConstants.CONTEXT_SELECTION_BEAN%>"
		schema="academicInterval.chooseWithPostBack">
		<fr:destination name="academicIntervalPostBack"
			path="/chooseDegreesForExecutionCourseMerge.do?method=academicIntervalPostBack" />
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thright mtop15" />
			<fr:property name="columnClasses" value=",,tdclear tderror1" />
		</fr:layout>
	</fr:edit>
</fr:form>

<html:form action="/chooseDegreesForExecutionCourseMerge.do?method=chooseDegreesAndExecutionPeriod">

	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page"
		property="<%=PresentationConstants.ACADEMIC_INTERVAL%>"
		value="<%=((AcademicInterval) request.getAttribute(PresentationConstants.ACADEMIC_INTERVAL))
				.getResumedRepresentationInStringFormat()%>" />
	<br />

	<br />
	<br />
	<table>
		<tr>
			<td><strong>Escolha a Licenciatura de Origem</strong> <br />
			<br />
			<table>
				<logic:iterate id="degree" name="sourceDegrees">
					<tr>
						<td class="listClasses"><html:radio bundle="HTMLALT_RESOURCES"
							altKey="radio.sourceDegreeId" property="sourceDegreeId" idName="degree" value="idInternal" />
						</td>
						<td class="listClasses"><bean:write name="degree" property="sigla" /></td>
						<td class="listClasses" style="text-align: left"><bean:write name="degree"
							property="presentationName" /></td>
					</tr>
				</logic:iterate>
			</table>
			</td>
			<td><strong>Escolha a Licenciatura de Destino</strong> <br />
			<br />
			<table>
				<logic:iterate id="degree" name="destinationDegrees">
					<tr>
						<td class="listClasses"><html:radio bundle="HTMLALT_RESOURCES"
							altKey="radio.destinationDegreeId" property="destinationDegreeId" idName="degree"
							value="idInternal" /></td>
						<td class="listClasses"><bean:write name="degree" property="sigla" /></td>
						<td class="listClasses" style="text-align: left"><bean:write name="degree"
							property="presentationName" /></td>
					</tr>
				</logic:iterate>
			</table>
			</td>
		</tr>
	</table>
	<br />
	<br />
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
		<bean:message bundle="MANAGER_RESOURCES" key="button.save" />
	</html:submit>
	<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset" styleClass="inputbutton">
		<bean:message bundle="MANAGER_RESOURCES" key="label.clear" />
	</html:reset>
</html:form>