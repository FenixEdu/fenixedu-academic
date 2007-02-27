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
				<html:messages id="message" message="true" bundle="DEPARTMENT_MEMBER_RESOURCES">
					<bean:write name="message"/>
				</html:messages>
			</span>
		<p>
	</logic:messagesPresent>
	
	<%-- ****** Education Expectations ****** --%>
	
	<b><bean:message key="label.personalExpectationsManagement.message.generalInformationTitle" bundle="DEPARTMENT_MEMBER_RESOURCES"/></b>
	<bean:message key="label.personalExpectationsManagement.message.generalInformationDescription" bundle="DEPARTMENT_MEMBER_RESOURCES"/>
								
	<p style='background-color: #eee; padding: 0.5em; font-size: 1.3em;'>
		<strong><bean:message key="label.personalExpectationsManagement.education" bundle="DEPARTMENT_MEMBER_RESOURCES"/></strong>
	</p>	
	
	<logic:notEmpty name="teacherPersonalExpectationBean">
	
		<p><em><bean:message key="label.common.executionYear" bundle="DEPARTMENT_MEMBER_RESOURCES"/>: <bean:write name="teacherPersonalExpectationBean" property="executionYear.year"/></em></p>
					
		<fr:form action="/personalExpectationManagement.do"> 
			<html:hidden property="method" value="prepareManageResearchAndDevelopment"/>
				
				<%-- Graduations --%>
			
			<div style="font: bold 12px Verdana, Arial, Helvetica, sans-serif;">
				<bean:message key="label.personalExpectationsManagement.graduations" bundle="DEPARTMENT_MEMBER_RESOURCES"/>
			</div>			
			<fr:edit id="teacherPersonalExpectationWithGraduations" name="teacherPersonalExpectationBean" schema="FillGraduationExpectations">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle5 thlight"/>
				</fr:layout>		
			</fr:edit>			
			
				<%-- Cientific Pos-Graduations --%>
			
			<div style="font: bold 12px Verdana, Arial, Helvetica, sans-serif;">
				<bean:message key="label.personalExpectationsManagement.cientificPosGraduations" bundle="DEPARTMENT_MEMBER_RESOURCES"/>
			</div>
			<fr:edit id="teacherPersonalExpectationWithCientificPosGraduations" name="teacherPersonalExpectationBean" schema="FillCientificPosGraduationsExpectations" layout="tabular">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle5 thlight"/>
				</fr:layout>		
			</fr:edit>			
				<%-- Professional Pos-Graduations --%>
			
			<div style="font: bold 12px Verdana, Arial, Helvetica, sans-serif;">
				<bean:message key="label.personalExpectationsManagement.professionalPosGraduations" bundle="DEPARTMENT_MEMBER_RESOURCES"/>
			</div>
			<fr:edit id="teacherPersonalExpectationWithProfessionalPosGraduations" name="teacherPersonalExpectationBean" schema="FillProfessionalPosGraduationsExpectations" layout="tabular">	
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle5 thlight"/>
				</fr:layout>		
			</fr:edit>			
			
				<%-- Seminaries --%>
			
			<div style="font: bold 12px Verdana, Arial, Helvetica, sans-serif;">
				<bean:message key="label.personalExpectationsManagement.seminaries" bundle="DEPARTMENT_MEMBER_RESOURCES"/>
			</div>
			<fr:edit id="teacherPersonalExpectationWithSeminaries" name="teacherPersonalExpectationBean" schema="FillSeminariesExpectations" layout="tabular">	
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle5 thlight"/>
				</fr:layout>		
			</fr:edit>			
			
				<%-- Main Focus --%>			
				
			<fr:edit id="teacherPersonalExpectationWithEducationMainFocus" name="teacherPersonalExpectationBean" schema="FillEducationMainFocusExpectations" layout="tabular">					
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
				<html:hidden property="method" value="editEducationExpectations"/>
					
					<%-- Graduations --%>
				
				<div style="font: bold 12px Verdana, Arial, Helvetica, sans-serif;">
					<bean:message key="label.personalExpectationsManagement.graduations" bundle="DEPARTMENT_MEMBER_RESOURCES"/>
				</div>			
				<fr:edit id="teacherPersonalExpectationWithGraduations" name="teacherPersonalExpectation" schema="EditGraduationExpectations">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle5 thlight"/>
					</fr:layout>		
				</fr:edit>			
				
					<%-- Cientific Pos-Graduations --%>
				
				<div style="font: bold 12px Verdana, Arial, Helvetica, sans-serif;">
					<bean:message key="label.personalExpectationsManagement.cientificPosGraduations" bundle="DEPARTMENT_MEMBER_RESOURCES"/>
				</div>
				<fr:edit id="teacherPersonalExpectationWithCientificPosGraduations" name="teacherPersonalExpectation" schema="EditCientificPosGraduationsExpectations" layout="tabular">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle5 thlight"/>
					</fr:layout>		
				</fr:edit>			
					<%-- Professional Pos-Graduations --%>
				
				<div style="font: bold 12px Verdana, Arial, Helvetica, sans-serif;">
					<bean:message key="label.personalExpectationsManagement.professionalPosGraduations" bundle="DEPARTMENT_MEMBER_RESOURCES"/>
				</div>
				<fr:edit id="teacherPersonalExpectationWithProfessionalPosGraduations" name="teacherPersonalExpectation" schema="EditProfessionalPosGraduationsExpectations" layout="tabular">	
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle5 thlight"/>
					</fr:layout>		
				</fr:edit>			
				
					<%-- Seminaries --%>
				
				<div style="font: bold 12px Verdana, Arial, Helvetica, sans-serif;">
					<bean:message key="label.personalExpectationsManagement.seminaries" bundle="DEPARTMENT_MEMBER_RESOURCES"/>
				</div>
				<fr:edit id="teacherPersonalExpectationWithSeminaries" name="teacherPersonalExpectation" schema="EditSeminariesExpectations" layout="tabular">	
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle5 thlight"/>
					</fr:layout>		
				</fr:edit>			
				
					<%-- Main Focus --%>			
					
				<fr:edit id="teacherPersonalExpectationWithEducationMainFocus" name="teacherPersonalExpectation" schema="EditEducationMainFocusExpectations" layout="tabular">					
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