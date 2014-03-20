<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<h2>Listagem de Horários por Salas</h2>

<p>
	<span class="error"><!-- Error messages go here --><html:errors /></span>
</p>

<fr:form action="/viewAllRoomsSchedulesDA.do">
	<input type="hidden" name="method" value="list"/>

	<fr:edit name="bean">
		<fr:schema type="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.ViewAllRoomsSchedulesDA$ChooseBuildingBean" bundle="SOP_RESOURCES">
			<fr:slot name="academicInterval" layout="menu-select" key="link.choose.execution.period">
				<fr:property name="format" value="\${pathName}" />
				<fr:property name="from" value="availableIntervals" />
				<fr:property name="nullOptionHidden" value="true" />
			</fr:slot>
		</fr:schema>
		<fr:layout name="flow" />
	</fr:edit>

	<html:submit onclick="this.form.method.value='choose'"><bean:message key="label.change"></bean:message></html:submit>

	<br />
	<fr:edit name="bean">
		<fr:schema type="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.ViewAllRoomsSchedulesDA$ChooseBuildingBean" bundle="SOP_RESOURCES">
			<fr:slot name="selectedBuildings" layout="option-select" key="label.manager.degrees">
				<fr:property name="from" value="availableBuildings" />
				<fr:property name="classes" value="nobullet noindent"/>
				<fr:property name="selectAllShown" value="true" />
				<fr:property name="eachSchema" value="person.name" />
				<fr:property name="eachLayout" value="values-dash" />
			</fr:slot>
		</fr:schema>
		<fr:layout name="flow" />
	</fr:edit>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" value="Submeter" styleClass="inputbutton">
		<bean:message key="label.list"/>
	</html:submit>
</fr:form>
