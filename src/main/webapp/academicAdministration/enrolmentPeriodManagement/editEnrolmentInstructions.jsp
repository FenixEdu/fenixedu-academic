<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<html:xhtml />

<h2>
	<bean:message bundle="APPLICATION_RESOURCES" key="label.define.instructions" />
	:
	<bean:write name="executionSemester" property="qualifiedName" />
</h2>

<bean:define id="semester" name="executionSemester" property="externalId" />
<fr:edit id="enrolmentInstructions" name="executionSemester" property="enrolmentInstructions"
	action='<%="/manageEnrolementPeriods.do?method=prepare&semester=" + semester%>'>
	<fr:schema type="net.sourceforge.fenixedu.domain.EnrolmentInstructions" bundle="APPLICATION_RESOURCES">
		<fr:slot name="instructions" key="label.enrolmentInstructions.instructions" layout="longText" required="true">
			<fr:property name="columns" value="100" />
			<fr:property name="rows" value="80" />
		</fr:slot>
	</fr:schema>
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle1 thtop" />
		<fr:property name="columnClasses" value=",,tdclear tderror1" />
	</fr:layout>
</fr:edit>