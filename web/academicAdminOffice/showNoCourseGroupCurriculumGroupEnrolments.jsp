<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<bean:define id="type" name="enrolmentBean" property="groupType"/>
<bean:define id="actionName" name="actionName" />

<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
<h2><strong><bean:message key="label.course.enrolments" bundle="ACADEMIC_OFFICE_RESOURCES"/> <bean:message key="<%= type.toString() %>" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></h2>

<html:messages id="message" message="true" bundle="ACADEMIC_OFFICE_RESOURCES" property="error">
	<p>
		<span class="error0"><!-- Error messages go here --><bean:write name="message" /></span>
	</p>
</html:messages>
<html:messages id="error" message="true" bundle="APPLICATION_RESOURCES" property="enrolmentError" >
	<br/>
	<span class="error"><!-- Error messages go here --><bean:write name="error" /></span>
	<br/>
</html:messages>

<fr:form action='<%= "/" + actionName + ".do" %>'>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="chooseCurricular"/>
	<fr:edit id="enrolmentBean" name="enrolmentBean" visible="false"/>
	<logic:present name="enrolments">
		<bean:define id="bean" name="enrolmentBean" type="net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.studentEnrolment.NoCourseGroupEnrolmentBean"/>
		<bean:define id="url">/<%= actionName %>.do?method=delete&amp;enrolment=${idInternal}&amp;scpID=<%= bean.getStudentCurricularPlan().getIdInternal().toString() %>&amp;executionPeriodID=<%= bean.getExecutionPeriod().getIdInternal().toString() %></bean:define>
		<fr:view name="enrolments" property="enrolments" schema="student.no.course.group.enrolments">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4"/>
		      	<fr:property name="columnClasses" value="listClasses,,"/>

				<fr:property name="linkFormat(unenrol)" value="<%= url %>" />
				<fr:property name="key(unenrol)" value="link.student.unenrol"/>
				<fr:property name="bundle(unenrol)" value="ACADEMIC_OFFICE_RESOURCES"/>
				<fr:property name="contextRelative(unenrol)" value="true"/>
				<fr:property name="confirmationKey(unenrol)" value="label.student.noCourseGroupCurriculumGroup.unenrol.confirmation.message" />
				<fr:property name="confirmationBundle(unenrol)" value="ACADEMIC_OFFICE_RESOURCES" />

				<fr:property name="sortBy" value="executionPeriod=desc,name=asc" />
			</fr:layout>
		</fr:view>
	</logic:present>
	
	<logic:notPresent name="enrolments">
		<p class="mtop2">
			<em><bean:message key="label.no.extra.enrolments" bundle="ACADEMIC_OFFICE_RESOURCES"/>.</em>
		</p>
	</logic:notPresent>
	
	<p class="mtop15">
		<html:submit><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="button.enrol"/></html:submit>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" onclick="this.form.method.value='back'; return true;"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="back"/></html:submit>
	</p>
</fr:form>
