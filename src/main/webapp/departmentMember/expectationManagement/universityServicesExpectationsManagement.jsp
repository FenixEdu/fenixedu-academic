<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<html:xhtml/>

<em><bean:message key="label.departmentMember" bundle="DEPARTMENT_MEMBER_RESOURCES"/></em>
<h2><bean:message key="link.personalExpectationsManagement" bundle="DEPARTMENT_MEMBER_RESOURCES"/></h2>

<logic:present role="DEPARTMENT_MEMBER">
	
	<logic:messagesPresent message="true">
		<p>
			<span class="error0"><!-- Error messages go here -->
				<html:messages id="message" message="true" bundle="SOP_RESOURCES">
					<bean:write name="message"/>
				</html:messages>
			</span>
		<p>
	</logic:messagesPresent>

	<%-- ****** University Service Expectations ****** --%>						

	<div class="infoop2">
		<p><bean:message key="label.personalExpectationsManagement.message.generalInformationTitle" bundle="DEPARTMENT_MEMBER_RESOURCES"/>:</p>
		<p><bean:message key="label.personalExpectationsManagement.message.generalInformationDescription" bundle="DEPARTMENT_MEMBER_RESOURCES"/></p>
	</div>
	
	<h3 class="separator2 mtop2"><bean:message key="label.personalExpectationsManagement.universityService" bundle="DEPARTMENT_MEMBER_RESOURCES"/></h3>
	
	<logic:notEmpty name="teacherPersonalExpectationBean">
		
		<p><em><bean:message key="label.common.executionYear" bundle="DEPARTMENT_MEMBER_RESOURCES"/>: <bean:write name="teacherPersonalExpectationBean" property="executionYear.year"/></em></p>
		
		<fr:form action="/personalExpectationManagement.do">
			<html:hidden property="method" value="prepareManageProfessionalActivities"/>
										
			<fr:edit id="teacherPersonalExpectationWithUniversityServices" name="teacherPersonalExpectationBean" schema="FillUniversityServiceExpectations">	
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle5 thlight thright mtop05"/>
					<fr:property name="columnClasses" value="width14em,,tdclear tderror1"/>
				</fr:layout>		
			</fr:edit>			
			<fr:edit id="teacherPersonalExpectationWithMainFocusUniversityServices" name="teacherPersonalExpectationBean" schema="FillUniversityServiceMainFocusExpectations">	
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle5 thlight thright mtop05"/>
					<fr:property name="columnClasses" value="width14em,,tdclear tderror1"/>
				</fr:layout>		
			</fr:edit>			
						
			<html:submit><bean:message key="link.continue" bundle="DEPARTMENT_MEMBER_RESOURCES"/></html:submit>
			<html:cancel onclick="this.form.method.value='viewTeacherPersonalExpectations';this.form.submit();">
				<bean:message key="button.cancel" bundle="DEPARTMENT_MEMBER_RESOURCES"/>
			</html:cancel>	
			
		</fr:form>
	
	</logic:notEmpty>
	
	<logic:empty name="teacherPersonalExpectationBean">
		<logic:notEmpty name="teacherPersonalExpectation">
		
			<p><em><bean:message key="label.common.executionYear" bundle="DEPARTMENT_MEMBER_RESOURCES"/>: <bean:write name="teacherPersonalExpectation" property="executionYear.year"/></em></p>
				
			<fr:form action="/personalExpectationManagement.do">
				<html:hidden property="method" value="editUniversityServicesExpectations"/>
											
				<fr:edit id="teacherPersonalExpectationWithUniversityServices" name="teacherPersonalExpectation" schema="EditUniversityServiceExpectations">	
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle5 thlight thright mtop05"/>
					</fr:layout>		
				</fr:edit>			
				<fr:edit id="teacherPersonalExpectationWithMainFocusUniversityServices" name="teacherPersonalExpectation" schema="EditUniversityServiceMainFocusExpectations">	
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle5 thlight thright mtop05"/>
						<fr:property name="columnClasses" value="width14em,,tdclear tderror1"/>
					</fr:layout>		
				</fr:edit>			
							
				<html:submit><bean:message key="button.submit" bundle="DEPARTMENT_MEMBER_RESOURCES"/></html:submit>
				<html:cancel onclick="this.form.method.value='viewTeacherPersonalExpectations';this.form.submit();">
					<bean:message key="button.cancel" bundle="DEPARTMENT_MEMBER_RESOURCES"/>
				</html:cancel>	
				
			</fr:form>
			
		</logic:notEmpty>	
	</logic:empty>
	
</logic:present>	