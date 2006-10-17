<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml />

<logic:present role="DEGREE_ADMINISTRATIVE_OFFICE">

	<h2><bean:message key="link.declarations" /></h2>

	<hr />
	<br />

	<fr:edit name="studentsSearchBean" schema="student.StudentsSearchBeanByNumber" >
		<fr:layout name="tabular" >
			<fr:property name="classes" value="tstyle4"/>
	        <fr:property name="columnClasses" value="listClasses,,"/>
		</fr:layout>
	</fr:edit>

	<br/>

	<logic:present name="studentsSearchBean" property="number">
		<bean:define id="students" name="studentsSearchBean" property="search"/>
		<bean:size id="studentsSize" name="students"/>
		<logic:equal name="studentsSize" value="0">
			<bean:message key="message.no.student.found"/>
		</logic:equal>
		<logic:equal name="studentsSize" value="1">
			<logic:iterate id="student" name="students">
				<bean:write name="student" property="number"/>
				<bean:write name="student" property="person.name"/>
				<br/>
				<logic:iterate id="registration" name="student" property="registrations">
					<logic:present name="registration" property="activeStudentCurricularPlan">
						<bean:define id="url" type="java.lang.String">/declarations.do?method=registrationDeclaration&amp;registrationID=<bean:write name="registration" property="idInternal"/></bean:define>
						<html:link action="<%= url %>"><bean:message key="link.declaration.registration"/></html:link>
						<br/>
					</logic:present>
				</logic:iterate>
			</logic:iterate>
		</logic:equal>
		<logic:greaterThan name="studentsSize" value="1">
			<logic:iterate id="student" name="students">
				<bean:write name="student" property="number"/>
				<bean:write name="student" property="person.name"/>
				<br/>
			</logic:iterate>
		</logic:greaterThan>
	</logic:present>

</logic:present>
