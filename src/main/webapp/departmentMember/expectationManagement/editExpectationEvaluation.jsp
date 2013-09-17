<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<html:xhtml/>

<em><bean:message key="label.departmentMember" bundle="DEPARTMENT_MEMBER_RESOURCES"/></em>
<h2><bean:message key="label.evaluate.expectations" bundle="DEPARTMENT_MEMBER_RESOURCES"/></h2>

<logic:present role="DEPARTMENT_MEMBER">
	
	<logic:messagesPresent message="true">
		<p>
			<span class="error0"><!-- Error messages go here -->
				<html:messages id="message" message="true" bundle="DEPARTMENT_MEMBER_RESOURCES">
					<bean:write name="message"/>
				</html:messages>
			</span>
		<p>
	</logic:messagesPresent>
	
	<logic:notEmpty name="teacherPersonalExpectation">
		
		<p><bean:message key="label.common.executionYear" bundle="DEPARTMENT_MEMBER_RESOURCES"/>: <bean:write name="teacherPersonalExpectation" property="executionYear.year"/></p>
		
		<fr:view name="teacherPersonalExpectation" property="teacher" schema="seeTeacherInformationForTeacherPersonalExpectation">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2 thlight thright thbgnone"/>
				<fr:property name="columnClasses" value=",bold"/>
			</fr:layout>
		</fr:view>
		
		<ul class="list5 mtop05">
			<li>						
				<html:link page="/evaluateExpectations.do?method=seeTeacherPersonalExpectation" paramId="teacherPersonalExpectationID" paramName="teacherPersonalExpectation" paramProperty="externalId">
					<bean:message key="label.see.teacher.personal.expectations" bundle="DEPARTMENT_MEMBER_RESOURCES"/>
				</html:link>		
			</li>
		</ul>	

		<bean:define id="submitURL">/evaluateExpectations.do?method=chooseTeacherInExecutionYear&executionYearID=<bean:write name="teacherPersonalExpectation" property="executionYear.externalId"/></bean:define>					
		<fr:edit id="teacherPersonalExpectationWithEvaluation" name="teacherPersonalExpectation" schema="editExpectationEvaluation" action="<%= submitURL %>">			
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thlight"/>
			</fr:layout>
			<fr:destination name="cancel" path="/evaluateExpectations.do?method=chooseTeacher"/>
		</fr:edit>
		
	</logic:notEmpty>
	
</logic:present>