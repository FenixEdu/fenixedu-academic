<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
<h2><bean:message key="label.course.enrolments" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>


<html:messages id="message" message="true" bundle="ACADEMIC_OFFICE_RESOURCES">
	<p>
		<span class="error"><!-- Error messages go here --><bean:write name="message" /></span>
	</p>
</html:messages>

<p class="mtop15 mbottom025"><strong><bean:message key="label.student.enrolment.chooseExecutionPeriod" bundle="ACADEMIC_OFFICE_RESOURCES"/>:</strong></p>

<fr:form action="/studentEnrolments.do?method=showDegreeModulesToEnrol">
	<fr:edit id="studentEnrolment"
			 name="studentEnrolmentBean"
			 type="net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.studentEnrolment.StudentEnrolmentBean"
			 schema="student.enrolment.choose.executionPeriod">
		<fr:destination name="postBack" path="/studentEnrolments.do?method=postBack"/>
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 thright thlight mtop025 mbottom05"/>
			<fr:property name="columnClasses" value=",,tdclear tderror1"/>
		</fr:layout>
	</fr:edit>
</fr:form>	

<logic:present name="studentEnrolmentBean" property="executionPeriod">
	<ul class="mvert1">
		<li>
			<bean:define id="url1">/bolonhaStudentEnrollment.do?method=prepare&amp;scpID=<bean:write name="studentEnrolmentBean" property="studentCurricularPlan.idInternal"/>&amp;executionPeriodID=<bean:write name="studentEnrolmentBean" property="executionPeriod.idInternal"/>&amp;withRules=false</bean:define>
			<html:link action='<%= url1 %>'><bean:message key="label.course.enrolmentWithoutRules" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:link>
		</li>
		<li>
			<bean:define id="url2">/bolonhaStudentEnrollment.do?method=prepare&amp;scpID=<bean:write name="studentEnrolmentBean" property="studentCurricularPlan.idInternal"/>&amp;executionPeriodID=<bean:write name="studentEnrolmentBean" property="executionPeriod.idInternal"/>&amp;withRules=true</bean:define>
			<html:link action='<%= url2 %>'><bean:message key="label.course.enrolmentWithRules" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:link>
		</li>
		<li>
			<bean:define id="url3">/studentExtraEnrolments.do?method=prepare&amp;scpID=<bean:write name="studentEnrolmentBean" property="studentCurricularPlan.idInternal"/>&amp;executionPeriodID=<bean:write name="studentEnrolmentBean" property="executionPeriod.idInternal"/>&amp;type=PROPAEDEUTICS</bean:define>
			<html:link action='<%= url3 %>'><bean:message key="label.course.enrolments" bundle="ACADEMIC_OFFICE_RESOURCES"/> <bean:message key="PROPAEDEUTICS" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:link>
		</li>
		<li>
			<bean:define id="url4">/studentExtraEnrolments.do?method=prepare&amp;scpID=<bean:write name="studentEnrolmentBean" property="studentCurricularPlan.idInternal"/>&amp;executionPeriodID=<bean:write name="studentEnrolmentBean" property="executionPeriod.idInternal"/>&amp;type=EXTRA_CURRICULAR</bean:define>
			<html:link action='<%= url4 %>'><bean:message key="label.course.enrolments" bundle="ACADEMIC_OFFICE_RESOURCES"/> <bean:message key="EXTRA_CURRICULAR" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:link>
		</li>
	</ul>

	<logic:notEmpty name="studentEnrolments">
		<p class="mtop2 mbottom0"><strong><bean:message key="label.student.enrolments.executionPeriod" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></p>
		<fr:view name="studentEnrolments" schema="student.show.enrolments">
			<fr:layout name="tabular">	 
				<fr:property name="classes" value="tstyle2"/>
		      	<fr:property name="columnClasses" value="nowrap,acenter,nowrap,smalltxt color888,acenter"/>
				<fr:property name="sortBy" value="name"/>
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
	<logic:empty name="studentEnrolments">
		<p class="mtop15">
			<em><bean:message key="label.no.enrolments" bundle="ACADEMIC_OFFICE_RESOURCES"/>.</em>
		</p>
	</logic:empty>
</logic:present>

<fr:form action="/studentEnrolments.do?method=backViewRegistration">
	<fr:edit id="studentEnrolment-back" name="studentEnrolmentBean" visible="false" />
	<html:cancel><bean:message key="button.back" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:cancel>
</fr:form>