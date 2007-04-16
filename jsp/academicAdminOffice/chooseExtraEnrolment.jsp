<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
<bean:define id="type" name="extraEnrolmentBean" property="groupType"/>
<h2><strong><bean:message key="label.course.enrolments" bundle="ACADEMIC_OFFICE_RESOURCES"/> <bean:message key="<%= type.toString() %>" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></h2>
<br/>


<fr:form action="/studentExtraEnrolments.do">
	<fr:edit id="extraEnrolmentBean"
			 name="extraEnrolmentBean"
			 schema="studentOptionalEnrolment.base">
		<fr:destination name="postBack" path="/studentExtraEnrolments.do?method=postBack"/>
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
<logic:present name="extraEnrolmentBean" property="degreeCurricularPlan">
	<fr:form action="/studentExtraEnrolments.do?method=back2">
		<fr:edit id="degreeCurricularPlan"
				 name="extraEnrolmentBean">
			<fr:layout name="student-optional-enrolments">
				<fr:property name="linkFormat" value="/studentExtraEnrolments.do?method=enrol&amp;scpID=${studentCurricularPlan.idInternal}&amp;executionPeriodID=${executionPeriod.idInternal}&amp;degreeType=${degreeType}&amp;degreeID=${degree.idInternal}&amp;dcpID=${degreeCurricularPlan.idInternal}&amp;type=${groupType}"/>
			</fr:layout>
		</fr:edit>
		<br/>
		<br/>		
		<html:submit><bean:message key="back" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:submit>
	</fr:form>
</logic:present>