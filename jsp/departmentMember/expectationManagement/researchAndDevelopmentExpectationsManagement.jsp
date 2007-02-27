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
	
	<%-- ****** Researcher And Development Expectations ****** --%>			
	
	<div class="infoop2">
		<p><bean:message key="label.personalExpectationsManagement.message.generalInformationTitle" bundle="DEPARTMENT_MEMBER_RESOURCES"/>:</p>
		<p><bean:message key="label.personalExpectationsManagement.message.generalInformationDescription" bundle="DEPARTMENT_MEMBER_RESOURCES"/></p>
	</div>
			
	<h3 class="separator2 mtop2"><bean:message key="label.personalExpectationsManagement.investigation" bundle="DEPARTMENT_MEMBER_RESOURCES"/></h3>

	<logic:notEmpty name="teacherPersonalExpectationBean">		

		<p><em><bean:message key="label.common.executionYear" bundle="DEPARTMENT_MEMBER_RESOURCES"/>: <bean:write name="teacherPersonalExpectationBean" property="executionYear.year"/></em></p>
		
		<fr:form action="/personalExpectationManagement.do">
			<html:hidden property="method" value="prepareManageUniversityServices"/>
		
				<%-- Publications And Projects  --%>
			
			<fr:hasMessages for="teacherPersonalExpectationWithPublications" type="conversion">
				<p><span class="error0">			
					<fr:message for="teacherPersonalExpectationWithPublications" show="message"/>
				</span></p>
			</fr:hasMessages> 
			
			<p class="mbottom05"><strong><bean:message key="label.personalExpectationsManagement.publicationsAndProjects" bundle="DEPARTMENT_MEMBER_RESOURCES"/></strong></p>
			<fr:edit id="teacherPersonalExpectationWithPublications" name="teacherPersonalExpectationBean" schema="FillPublicationsAndProjectsExpectations">	
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle5 thlight thright mtop05 thright mtop05"/>
					<fr:property name="columnClasses" value="width14em,,tdclear tderror1"/>
				</fr:layout>		
			</fr:edit>			
	
			
			<%-- Publications And Projects Main Focus --%>
			<fr:edit id="teacherPersonalExpectationWithMainFocusPublications" name="teacherPersonalExpectationBean" schema="FillPublicationsAndProjectsMainFocusExpectations">	
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle5 thlight thright mtop05"/>
					<fr:property name="columnClasses" value="width14em,,tdclear tderror1"/>
				</fr:layout>		
			</fr:edit>			
				
			
			<%-- Orientation --%>
			<fr:hasMessages for="teacherPersonalExpectationWithOrientations" type="conversion">
				<p><span class="error0">			
					<fr:message for="teacherPersonalExpectationWithOrientations" show="message"/>
				</span></p>
			</fr:hasMessages> 
			
			<p class="mbottom05"><strong><bean:message key="label.personalExpectationsManagement.orientation" bundle="DEPARTMENT_MEMBER_RESOURCES"/></strong></p>
			<fr:edit id="teacherPersonalExpectationWithOrientations" name="teacherPersonalExpectationBean" schema="FillOrientationsExpectations">	
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle5 thlight thright mtop05"/>
					<fr:property name="columnClasses" value="width14em,,tdclear tderror1"/>
				</fr:layout>		
			</fr:edit>			
	

			<%-- Orientation Main Focus --%>
			<fr:edit id="teacherPersonalExpectationWithOrientationMainFocus" name="teacherPersonalExpectationBean" schema="FillOrientationMainFocusExpectations">	
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
				<html:hidden property="method" value="editResearchAndDevelopmentExpectations"/>
			
				<%-- Publications And Projects  --%>
				<fr:hasMessages for="teacherPersonalExpectationWithPublications" type="conversion">
					<p><span class="error0">			
						<fr:message for="teacherPersonalExpectationWithPublications" show="message"/>
					</span></p>
				</fr:hasMessages>
				
				<p class="mbottom05"><strong><bean:message key="label.personalExpectationsManagement.publicationsAndProjects" bundle="DEPARTMENT_MEMBER_RESOURCES"/></strong></p>
				<fr:edit id="teacherPersonalExpectationWithPublications" name="teacherPersonalExpectation" schema="EditPublicationsAndProjectsExpectations">	
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle5 thlight thright mtop05"/>
						<fr:property name="columnClasses" value="width14em,,tdclear tderror1"/>
					</fr:layout>		
				</fr:edit>			
		

				<%-- Publications And Projects Main Focus --%>
				<fr:edit id="teacherPersonalExpectationWithMainFocusPublications" name="teacherPersonalExpectation" schema="EditPublicationsAndProjectsMainFocusExpectations">	
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle5 thlight thright mtop05"/>
						<fr:property name="columnClasses" value="width14em,,tdclear tderror1"/>
					</fr:layout>		
				</fr:edit>			
					

				<%-- Orientation --%>
				<fr:hasMessages for="teacherPersonalExpectationWithOrientations" type="conversion">
					<p><span class="error0">			
						<fr:message for="teacherPersonalExpectationWithOrientations" show="message"/>
					</span></p>
				</fr:hasMessages>
				
				<p class="mbottom05"><strong><bean:message key="label.personalExpectationsManagement.orientation" bundle="DEPARTMENT_MEMBER_RESOURCES"/></strong></p>
				<fr:edit id="teacherPersonalExpectationWithOrientations" name="teacherPersonalExpectation" schema="EditOrientationsExpectations">	
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle5 thlight thright mtop05"/>
						<fr:property name="columnClasses" value="width14em,,tdclear tderror1"/>
					</fr:layout>		
				</fr:edit>			
		
				<%-- Orientation Main Focus --%>
				<fr:edit id="teacherPersonalExpectationWithOrientationMainFocus" name="teacherPersonalExpectation" schema="EditOrientationMainFocusExpectations">	
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
	</logic:empty>
		
</logic:present>	