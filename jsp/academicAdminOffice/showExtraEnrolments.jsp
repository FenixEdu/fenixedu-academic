<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
<bean:define id="type" name="extraEnrolmentBean" property="groupType"/>
<h2><strong><bean:message key="label.course.enrolments" bundle="ACADEMIC_OFFICE_RESOURCES"/> <bean:message key="<%= type.toString() %>" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></h2>

<html:messages id="message" message="true" bundle="ACADEMIC_OFFICE_RESOURCES">
	<br/>
	<span class="error"><!-- Error messages go here --><bean:write name="message" /></span>
	<br/>
</html:messages>
<br/>

<fr:form action="/studentExtraEnrolments.do">
	<html:hidden property="method" value="chooseCurricular"/>
	<fr:edit id="extraEnrolmentBean" name="extraEnrolmentBean" visible="false"/>
	<logic:present name="extraEnrolments">
		<bean:define id="bean" name="extraEnrolmentBean" type="net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.studentEnrolment.StudentExtraEnrolmentBean"/>
		<bean:define id="url">/studentExtraEnrolments.do?method=delete&amp;enrolment=${idInternal}&amp;scpID=<%= bean.getStudentCurricularPlan().getIdInternal().toString() %>&amp;executionPeriodID=<%= bean.getExecutionPeriod().getIdInternal().toString() %>&amp;type=<%= bean.getGroupType().toString() %></bean:define>
		<fr:view name="extraEnrolments" property="enrolments" schema="student.studentExtraEnrolments">
			<fr:layout name="tabular">	 
				<fr:property name="classes" value="tstyle4"/>
		      	<fr:property name="columnClasses" value="listClasses,,"/>
				<fr:property name="linkFormat(enrolment)" value="<%= url %>" />
				<fr:property name="key(enrolment)" value="link.student.unenrol"/>
				<fr:property name="bundle(enrolment)" value="ACADEMIC_OFFICE_RESOURCES"/>
				<fr:property name="contextRelative(enrolment)" value="true"/>      	
			</fr:layout>
		</fr:view>
	</logic:present>
	<logic:notPresent name="extraEnrolments">
		<em><bean:message key="label.no.extra.enrolments" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
	</logic:notPresent>
	<br />
	<br />	
	<html:submit><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="button.enrol"/></html:submit>
	<html:submit onclick="this.form.method.value='back'; return true;"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="back"/></html:submit>
</fr:form>
