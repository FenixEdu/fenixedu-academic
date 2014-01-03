<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<html:xhtml/>

<em><bean:message key="label.departmentAdmOffice"/></em>
<logic:notEmpty name="period">
	<h2><bean:message key="label.editTeacherExpectationVisualizationPeriodManagement.title"/></h2>
</logic:notEmpty>
<logic:empty name="period">	
	<h2><bean:message key="label.createTeacherExpectationVisualizationPeriodManagement.title"/></h2>
</logic:empty>

<logic:present role="role(DEPARTMENT_ADMINISTRATIVE_OFFICE)">
	
	<logic:messagesPresent message="true">
		<p>
			<span class="error0"><!-- Error messages go here -->
				<html:messages id="message" message="true" bundle="DEPARTMENT_ADM_OFFICE_RESOURCES">
					<bean:write name="message"/>
				</html:messages>
			</span>
		<p>
	</logic:messagesPresent>

	<logic:notEmpty name="period">		
		<p class="mtop15 mbottom05"><bean:message key="label.common.executionYear"/>: <bean:write name="period" property="executionYear.year"/></p>							
		<bean:define id="executionYearId" name="period" property="executionYear.externalId"/>
		<fr:edit id="editInterval" name="period" schema="editTeacherPersonalExpectationsVisualizationPeriod" layout="tabular">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thlight thright mtop05"/>
				<fr:property name="columnClasses" value=",,tdclear tderror1"/>
			</fr:layout>
			<fr:destination name="success" path="<%= "/teacherPersonalExpectationsVisualizationPeriod.do?method=showPeriodInExecutionYear&executionYearId=" + executionYearId %>"/>
			<fr:destination name="cancel" path="/teacherPersonalExpectationsVisualizationPeriod.do?method=showPeriod"/>
		</fr:edit>		
	</logic:notEmpty>	
		
	<logic:empty name="period">		
		<p class="mtop15 mbottom05"><bean:message key="label.common.executionYear"/>: <bean:write name="executionYear" property="year"/></p>
		<bean:define id="executionYearId" name="executionYear" property="externalId"/>
		<fr:create id="createInterval" type="net.sourceforge.fenixedu.domain.TeacherPersonalExpectationsVisualizationPeriod" schema="createTeacherPersonalExpectationsVisualizationPeriod">
		   	<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thlight thright mtop05"/>
				<fr:property name="columnClasses" value=",,tdclear tderror1"/>
			</fr:layout>
		   	<fr:hidden slot="executionYear" name="executionYear"/>
		   	<fr:hidden slot="department" name="department"/>
			<fr:destination name="success" path="<%= "/teacherPersonalExpectationsVisualizationPeriod.do?method=showPeriodInExecutionYear&executionYearId=" + executionYearId %>"/>
			<fr:destination name="cancel" path="/teacherPersonalExpectationsVisualizationPeriod.do?method=showPeriod"/>
		</fr:create>		
	</logic:empty>
	
</logic:present>
