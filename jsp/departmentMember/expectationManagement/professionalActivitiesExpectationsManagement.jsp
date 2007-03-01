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

	<%-- ****** Professional Activity Expectations ****** --%>					
		
	<div class="infoop2">
		<p><bean:message key="label.personalExpectationsManagement.message.generalInformationTitle" bundle="DEPARTMENT_MEMBER_RESOURCES"/>:</p>
		<p><bean:message key="label.personalExpectationsManagement.message.generalInformationDescription" bundle="DEPARTMENT_MEMBER_RESOURCES"/></p>
	</div>
	
	
	<h3 class="separator2 mtop2"><bean:message key="label.personalExpectationsManagement.professionalActivity" bundle="DEPARTMENT_MEMBER_RESOURCES"/></h3>
	
	<logic:notEmpty name="teacherPersonalExpectationBean">				
	
		<p><em><bean:message key="label.common.executionYear" bundle="DEPARTMENT_MEMBER_RESOURCES"/>: <bean:write name="teacherPersonalExpectationBean" property="executionYear.year"/></em></p>	
	
		<fr:form action="/personalExpectationManagement.do">
			<html:hidden property="method" value="createTeacherPersonalExpectations"/>
				
			<fr:edit id="teacherPersonalExpectationWithProfessionalActivities" name="teacherPersonalExpectationBean" schema="FillProfessionalActivitiesExpectations">	
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle5 thlight thright mtop05"/>
					<fr:property name="columnClasses" value="width14em,,tdclear tderror1"/>
				</fr:layout>		
			</fr:edit>	
										
			<fr:edit id="teacherPersonalExpectationWithMainFocusProfessionalActivities" name="teacherPersonalExpectationBean" schema="FillProfessionalActivitiesMainFocusExpectations">	
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle5 thlight thright mtop05"/>
					<fr:property name="columnClasses" value="width14em,,tdclear tderror1"/>
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
				<html:hidden property="method" value="editProfessionalActivitiesExpectations"/>
					
				<fr:edit id="teacherPersonalExpectationWithProfessionalActivities" name="teacherPersonalExpectation" schema="EditProfessionalActivitiesExpectations">	
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle5 thlight thright mtop05"/>
						<fr:property name="columnClasses" value="width14em,,tdclear tderror1"/>
					</fr:layout>		
				</fr:edit>	
											
				<fr:edit id="teacherPersonalExpectationWithMainFocusProfessionalActivities" name="teacherPersonalExpectation" schema="EditProfessionalActivitiesMainFocusExpectations">	
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle5 thlight thright mtop05"/>
						<fr:property name="columnClasses" value="width14em,,tdclear tderror1"/>
					</fr:layout>		
				</fr:edit>	
		
				<html:submit><bean:message key="button.submit" bundle="DEPARTMENT_MEMBER_RESOURCES"/></html:submit>
				<html:submit onclick="this.form.method.value='viewTeacherPersonalExpectations';this.form.submit();">
					<bean:message key="button.cancel" bundle="DEPARTMENT_MEMBER_RESOURCES"/>
				</html:submit>	
				
			</fr:form>
			
		</logic:notEmpty>
	</logic:empty>
	
</logic:present>	