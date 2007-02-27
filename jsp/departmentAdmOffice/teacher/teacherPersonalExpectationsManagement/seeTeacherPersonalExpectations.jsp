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
	<p><em><bean:message key="label.common.executionYear" bundle="DEPARTMENT_ADM_OFFICE_RESOURCES"/>: <bean:write name="teacherPersonalExpectation" property="executionYear.year"/></em></p>
	
	<ul class="mvert15">
		<li>
			<html:link page="/listTeachersPersonalExpectations.do?method=listTeachersPersonalExpectationsByExecutionYear" paramId="executionYearId" paramName="teacherPersonalExpectation" paramProperty="executionYear.idInternal">		
				<bean:message bundle="DEPARTMENT_ADM_OFFICE_RESOURCES" key="link.return"/>
			</html:link>
		</li>
	</ul>	
		
	<fr:view name="teacher" schema="seeTeacherInformationForTeacherPersonalExpectation">
		<fr:layout name="tabular" />
	</fr:view>
			
	<jsp:include page="../../../departmentMember/expectationManagement/seeTeacherPersonalExpectationsByYear.jsp"/>
			
</logic:present>