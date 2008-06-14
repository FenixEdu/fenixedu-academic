<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<html:xhtml/>

<em><bean:message key="label.departmentAdmOffice"/></em>
<h2><bean:message key="label.visualize.teachers.expectations"/></h2>

<logic:present role="DEPARTMENT_ADMINISTRATIVE_OFFICE">
	
	<bean:define id="teacher" name="teacherPersonalExpectation" property="teacher" />	
	<p><bean:message key="label.common.executionYear" bundle="DEPARTMENT_ADM_OFFICE_RESOURCES"/>: <bean:write name="teacherPersonalExpectation" property="executionYear.year"/></p>
	
	<ul class="list5 mvert15">
		<li>
			<html:link page="/listTeachersPersonalExpectations.do?method=listTeachersPersonalExpectationsByExecutionYear" paramId="executionYearId" paramName="teacherPersonalExpectation" paramProperty="executionYear.idInternal">		
				<bean:message bundle="DEPARTMENT_ADM_OFFICE_RESOURCES" key="link.return"/>
			</html:link>
		</li>
	</ul>	
		
	<fr:view name="teacher" schema="seeTeacherInformationForTeacherPersonalExpectation">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight thright thbgnone"/>
			<fr:property name="columnClasses" value=",bold"/>
		</fr:layout>
	</fr:view>
			
	<jsp:include page="../../../departmentMember/expectationManagement/seeTeacherPersonalExpectationsByYear.jsp"/>
	
	<logic:notEmpty name="teacherPersonalExpectation">
		<logic:notEmpty name="teacherPersonalExpectation" property="autoEvaluation">
			<h3 class="separator2 mtop2"><bean:message key="label.teacher.auto.evaluation" bundle="DEPARTMENT_ADM_OFFICE_RESOURCES"/></h3>
			<fr:view name="teacherPersonalExpectation" property="autoEvaluation" layout="html"/>									
		</logic:notEmpty>	
		<logic:notEmpty name="teacherPersonalExpectation" property="tutorComment">
			<h3 class="separator2 mtop2"><bean:message key="label.teacher.evaluation" bundle="DEPARTMENT_ADM_OFFICE_RESOURCES"/></h3>
			<fr:view name="teacherPersonalExpectation" property="tutorComment" layout="html"/>									
		</logic:notEmpty>
	</logic:notEmpty>		
			
</logic:present>