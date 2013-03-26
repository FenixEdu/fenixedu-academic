<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<h2><bean:message bundle="MANAGER_RESOURCES"
	key="title.extraCurricularActivities" /></h2>

<logic:messagesPresent message="true">
	<p><span class="error0"> <html:messages id="message"
		message="true" bundle="MANAGER_RESOURCES">
		<bean:write name="message" />
	</html:messages> </span>
	<p>
</logic:messagesPresent>

<fr:create action="/manageExtraCurricularActivities.do?method=prepare"
	type="net.sourceforge.fenixedu.domain.student.curriculum.ExtraCurricularActivityType"
	schema="extraCurricularActivityType.create">
    <fr:layout name="tabular">
        <fr:property name="classes" value="tstyle1" />
        <fr:property name="columnClasses" value=",,noborder" />
    </fr:layout>
</fr:create>

<fr:view name="types" schema="extraCurricularActivityType.view">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle1" />
		<fr:property name="columnClasses" value=",,noborder" />
	</fr:layout>
</fr:view>