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

<h3 class="mbottom025"><bean:message key="label.student.enrolment.chooseExecutionPeriod" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>

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
	<br/>
	<br/>	
	<ul>
		<li>
			<bean:define id="url1">/studentEnrolments.do?method=showDegreeModulesToEnrol&scpID=<bean:write name="studentEnrolmentBean" property="studentCurricularPlan.idInternal"/>&executionPeriodID=<bean:write name="studentEnrolmentBean" property="executionPeriod.idInternal"/></bean:define>
			<html:link action='<%= url1 %>'><bean:message key="label.course.enrolments" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:link>
		</li>
		<li>
			<bean:define id="url2">/studentExtraEnrolments.do?method=prepare&scpID=<bean:write name="studentEnrolmentBean" property="studentCurricularPlan.idInternal"/>&executionPeriodID=<bean:write name="studentEnrolmentBean" property="executionPeriod.idInternal"/>&type=PROPAEDEUTICS</bean:define>
			<html:link action='<%= url2 %>'><bean:message key="label.course.enrolments" bundle="ACADEMIC_OFFICE_RESOURCES"/> <bean:message key="PROPAEDEUTICS" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:link>
		</li>
		<li>
			<bean:define id="url3">/studentExtraEnrolments.do?method=prepare&scpID=<bean:write name="studentEnrolmentBean" property="studentCurricularPlan.idInternal"/>&executionPeriodID=<bean:write name="studentEnrolmentBean" property="executionPeriod.idInternal"/>&type=EXTRA_CURRICULAR</bean:define>
			<html:link action='<%= url3 %>'><bean:message key="label.course.enrolments" bundle="ACADEMIC_OFFICE_RESOURCES"/> <bean:message key="EXTRA_CURRICULAR" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:link>
		</li>
	</ul>
	<br/>

	<logic:notEmpty name="studentEnrolments">
		<strong><bean:message key="label.student.enrolments.executionPeriod" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong>
		<fr:view name="studentEnrolments" schema="student.show.enrolments">
			<fr:layout name="tabular">	 
				<fr:property name="classes" value="tstyle4"/>
		      	<fr:property name="columnClasses" value="listClasses,,"/>
				<fr:property name="sortBy" value="name"/>
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
	<logic:empty name="studentEnrolments">
		<em><bean:message key="label.no.enrolments" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
	</logic:empty>
</logic:present>
