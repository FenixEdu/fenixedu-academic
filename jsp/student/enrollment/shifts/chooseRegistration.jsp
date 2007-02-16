<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="STUDENT">
    <h2><bean:message key="title.student.shift.enrollment" bundle="STUDENT_RESOURCES"/></h2>

	<logic:messagesPresent message="true">
		<ul class="mtop15 mbottom1 nobullet list2">
			<html:messages id="messages" message="true" bundle="STUDENT_RESOURCES">
				<li><span class="error0"><bean:write name="messages" /></span></li>
			</html:messages>
		</ul>
	</logic:messagesPresent>

    <logic:empty name="toEnrol">
    	<span class="error0">
	    	<bean:message bundle="STUDENT_RESOURCES"  key="label.enrollment.courses.chooseRegistration.notoEnrol"/>
    	</span>
    </logic:empty>

	<logic:notEmpty name="toEnrol">
	    <h3 class="mtop15 mbottom025"><bean:message key="label.studentRegistrations" bundle="APPLICATION_RESOURCES"/></h3>
		<fr:view name="toEnrol" schema="student.registrationsWithStartData" >
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4 thlight mtop025 asdasd"/>
				<fr:property name="columnClasses" value=",tdhl1,,"/>
				<fr:property name="linkFormat(enrol)" value="/studentShiftEnrollmentManager.do?method=prepareStartViewWarning&registrationOID=${idInternal}" />
				<fr:property name="key(enrol)" value="enrol.in.shift"/>
				<fr:property name="bundle(enrol)" value="STUDENT_RESOURCES"/>
				<fr:property name="contextRelative(enrol)" value="true"/>
			</fr:layout>
		</fr:view>
	</logic:notEmpty>

</logic:present>
