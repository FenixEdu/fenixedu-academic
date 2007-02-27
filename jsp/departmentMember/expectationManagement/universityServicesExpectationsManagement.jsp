<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
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

	<b><bean:message key="label.personalExpectationsManagement.message.generalInformationTitle" bundle="DEPARTMENT_MEMBER_RESOURCES"/></b>
	<bean:message key="label.personalExpectationsManagement.message.generalInformationDescription" bundle="DEPARTMENT_MEMBER_RESOURCES"/>
			
	<p style='background-color: #eee; padding: 0.5em; font-size: 1.3em;'>
		<strong><bean:message key="label.personalExpectationsManagement.universityService" bundle="DEPARTMENT_MEMBER_RESOURCES"/></strong>			
	</p>
	
	<logic:notEmpty name="teacherPersonalExpectationBean">
		
		<p><em><bean:message key="label.common.executionYear" bundle="DEPARTMENT_MEMBER_RESOURCES"/>: <bean:write name="teacherPersonalExpectationBean" property="executionYear.year"/></em></p>
		
		<fr:form action="/personalExpectationManagement.do">
			<html:hidden property="method" value="prepareManageProfessionalActivities"/>
										
			<fr:edit id="teacherPersonalExpectationWithUniversityServices" name="teacherPersonalExpectationBean" schema="FillUniversityServiceExpectations">	
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle5 thlight"/>
				</fr:layout>		
			</fr:edit>			
			<fr:edit id="teacherPersonalExpectationWithMainFocusUniversityServices" name="teacherPersonalExpectationBean" schema="FillUniversityServiceMainFocusExpectations">	
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle5 thlight"/>
				</fr:layout>		
			</fr:edit>			
						
			<html:submit><bean:message key="link.continue" bundle="DEPARTMENT_MEMBER_RESOURCES"/></html:submit>
			<html:submit onclick="this.form.method.value='viewTeacherPersonalExpectations';this.form.submit();">
				<bean:message key="button.cancel" bundle="DEPARTMENT_MEMBER_RESOURCES"/>
			</html:submit>	
			
		</fr:form>
	
	</logic:notEmpty>
	
	<logic:empty name="teacherPersonalExpectationBean">
		<logic:notEmpty name="teacherPersonalExpectation">
		
			<p><em><bean:message key="label.common.executionYear" bundle="DEPARTMENT_MEMBER_RESOURCES"/>: <bean:write name="teacherPersonalExpectation" property="executionYear.year"/></em></p>
				
			<fr:form action="/personalExpectationManagement.do">
				<html:hidden property="method" value="editUniversityServicesExpectations"/>
											
				<fr:edit id="teacherPersonalExpectationWithUniversityServices" name="teacherPersonalExpectation" schema="EditUniversityServiceExpectations">	
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle5 thlight"/>
					</fr:layout>		
				</fr:edit>			
				<fr:edit id="teacherPersonalExpectationWithMainFocusUniversityServices" name="teacherPersonalExpectation" schema="EditUniversityServiceMainFocusExpectations">	
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle5 thlight"/>
					</fr:layout>		
				</fr:edit>			
							
				<html:submit><bean:message key="link.continue" bundle="DEPARTMENT_MEMBER_RESOURCES"/></html:submit>
				<html:submit onclick="this.form.method.value='viewTeacherPersonalExpectations';this.form.submit();">
					<bean:message key="button.cancel" bundle="DEPARTMENT_MEMBER_RESOURCES"/>
				</html:submit>	
				
			</fr:form>
			
		</logic:notEmpty>	
	</logic:empty>
	
</logic:present>	