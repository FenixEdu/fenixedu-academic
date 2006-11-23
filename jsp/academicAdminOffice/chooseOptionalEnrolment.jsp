<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><strong><bean:message key="label.student.optional.enrolments" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></h2>
<br/>
<fr:form action="/studentOptionalEnrolments.do?method=enrolOptionalCourse">
	<fr:edit id="optionalEnrolmentBean"
			 name="optionalBean"
			 type="net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.studentEnrolment.StudentOptionalEnrolmentBean"
			 schema="studentOptionalEnrolment.base">
		<fr:destination name="postBack" path="/studentOptionalEnrolments.do?method=postBack"/>
		<fr:layout name="tabular" >
				<fr:property name="classes" value="tstyle4"/>
		        <fr:property name="columnClasses" value="listClasses,,"/>
		</fr:layout>
	</fr:edit>
</fr:form>

<html:messages id="message" message="true" bundle="ACADEMIC_OFFICE_RESOURCES">
	<br/>
	<span class="error"><!-- Error messages go here --><bean:write name="message" /></span>
	<br/>
</html:messages>


<br />
<br />
<br />
<logic:present name="optionalBean" property="degreeCurricularPlan">
	<fr:form action="/studentOptionalEnrolments.do?method=back">
		<fr:edit id="degreeCurricularPlan"
				 name="optionalBean">
			<fr:layout name="student-optional-enrolments">
				<fr:property name="linkURL" value="/studentOptionalEnrolments.do?method=enrol"/>
			</fr:layout>
		</fr:edit>
		<br/>
		<br/>		
		<html:submit><bean:message key="back" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:submit>
	</fr:form>
</logic:present>