<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<html:xhtml />

<em><bean:message key="pedagogical.council" bundle="PEDAGOGICAL_COUNCIL"/></em>
<h2><bean:message key="title.inquiry.quc.auditProcesses" bundle="INQUIRIES_RESOURCES"/></h2>

<h3>
	<bean:write name="auditProcessBean" property="executionCourse.name"/>
	<bean:define id="executionSemester" name="auditProcessBean" property="executionCourse.executionPeriod"/>
	(<bean:write name="executionSemester" property="semester"/>º Semestre <bean:write name="executionSemester" property="executionYear.year"/>)
</h3>

<p class="mvert05"><bean:message key="message.audit.selectPersons" bundle="INQUIRIES_RESOURCES"/></p>

<html:messages id="messages" message="true" bundle="INQUIRIES_RESOURCES">
	<p><span class="error"><bean:write name="messages" filter="false"/></span></p>
</html:messages>

<fr:edit id="auditProcessBean" name="auditProcessBean" action="/qucAudit.do?method=selectPersons">
	<fr:schema type="net.sourceforge.fenixedu.dataTransferObject.inquiries.AuditSelectPersonsECBean" bundle="APPLICATION_RESOURCES">
		<fr:slot name="teacher" layout="autoComplete" key="teacher.docente" required="true">
			<fr:property name="size" value="80"/>
			<fr:property name="labelField" value="name"/>
			<fr:property name="format" value="${name} / ${istUsername}"/>
			<fr:property name="serviceArgs" value="slot=name"/>
			<fr:property name="minChars" value="3"/>
			<fr:property name="serviceName" value="SearchEmployeesAndTeachers"/>
			<fr:property name="indicatorShown" value="true"/>		
			<fr:property name="className" value="net.sourceforge.fenixedu.domain.Person"/>
			<fr:property name="errorStyleClass" value="error0"/>
		</fr:slot>
		<fr:slot name="student" layout="autoComplete" key="student" required="true">
			<fr:property name="size" value="80"/>
			<fr:property name="labelField" value="name"/>
			<fr:property name="format" value="${name} / ${istUsername}"/>
			<fr:property name="serviceArgs" value="slot=name,size=30"/>
			<fr:property name="minChars" value="3"/>
			<fr:property name="serviceName" value="SearchPeopleByNameOrISTID"/>
			<fr:property name="indicatorShown" value="true"/>		
			<fr:property name="className" value="net.sourceforge.fenixedu.domain.Person"/>
			<fr:property name="errorStyleClass" value="error0"/>
		</fr:slot>
	</fr:schema>
	<fr:layout name="tabular" >
		<fr:property name="classes" value="tstyle5 thlight thright"/>
       	<fr:property name="columnClasses" value=",,noborder"/>
	</fr:layout>
	<fr:destination name="cancel" path="/qucAudit.do?method=searchExecutionCourse"/>
</fr:edit>
