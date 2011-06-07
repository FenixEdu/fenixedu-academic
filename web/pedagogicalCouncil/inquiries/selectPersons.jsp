<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<html:xhtml />

<h2>QUC - Garantia da Qualidade das UC</h2>

<h3>
	Seleccionar as pessoas que vão fazer parte do processo de Auditoria
</h3>

<p><b><bean:write name="auditProcessBean" property="executionCourse.name"/></b></p>

<logic:present name="success">
	<span class="success"><bean:message key="label.inquiry.audit.process.success" bundle="INQUIRIES_RESOURCES"/></span>
</logic:present>

<html:messages id="messages" message="true" bundle="INQUIRIES_RESOURCES">
	<p><span class="error"><bean:write name="messages" filter="false"/></span></p>
</html:messages>

<fr:form action="/qucAudit.do?method=selectPersons">
	<fr:edit id="auditProcessBean" name="auditProcessBean">
		<fr:schema type="net.sourceforge.fenixedu.dataTransferObject.inquiries.AuditProcessBean" bundle="APPLICATION_RESOURCES">
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
			<fr:property name="classes" value="tstyle1"/>
	       	<fr:property name="columnClasses" value=",,noborder"/>
		</fr:layout>
	</fr:edit>
	<html:submit><bean:message key="button.submit"/></html:submit>
</fr:form>
