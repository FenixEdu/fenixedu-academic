<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<html:xhtml/>

<h2><bean:message key="link.personalExpectationsManagement" bundle="DEPARTMENT_MEMBER_RESOURCES"/></h2>

<logic:present role="role(DEPARTMENT_MEMBER)">
	
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
	

	<div class="infoop2">
		<p><bean:message key="label.personalExpectationsManagement.message.generalInformationTitle" bundle="DEPARTMENT_MEMBER_RESOURCES"/>:</p>
		<p><bean:message key="label.personalExpectationsManagement.message.generalInformationDescription" bundle="DEPARTMENT_MEMBER_RESOURCES"/></p>
	</div>
	
	<h3 class="separator2 mtop2"><bean:message key="label.personalExpectationsManagement.education" bundle="DEPARTMENT_MEMBER_RESOURCES"/></h3>	
		
	<logic:notEmpty name="teacherPersonalExpectationBean">
	
		<p><em><bean:message key="label.common.executionYear" bundle="DEPARTMENT_MEMBER_RESOURCES"/>: <bean:write name="teacherPersonalExpectationBean" property="executionYear.year"/></em></p>
			
		<fr:form action="/personalExpectationManagement.do"> 
			<html:hidden property="method" value="prepareManageResearchAndDevelopment"/>
			
			<%-- Graduations --%>
			<fr:hasMessages for="teacherPersonalExpectationWithGraduations" type="conversion">
				<p><span class="error0">			
					<fr:message for="teacherPersonalExpectationWithGraduations" show="message"/>
				</span></p>
			</fr:hasMessages>

			<p class="mbottom05"><strong><bean:message key="label.personalExpectationsManagement.graduations" bundle="DEPARTMENT_MEMBER_RESOURCES"/></strong></p>
			<fr:edit id="teacherPersonalExpectationWithGraduations" name="teacherPersonalExpectationBean" schema="FillGraduationExpectations">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle5 thlight thright mtop05"/>
					<fr:property name="columnClasses" value="width14em,,tdclear tderror1"/>					
				</fr:layout>		
			</fr:edit>			
			
			<%-- Cientific Pos-Graduations --%>
			<fr:hasMessages for="teacherPersonalExpectationWithCientificPosGraduations" type="conversion">
				<p><span class="error0">			
					<fr:message for="teacherPersonalExpectationWithCientificPosGraduations" show="message"/>
				</span></p>
			</fr:hasMessages>
			
			<p class="mbottom05"><strong><bean:message key="label.personalExpectationsManagement.cientificPosGraduations" bundle="DEPARTMENT_MEMBER_RESOURCES"/></strong></p>
			<fr:edit id="teacherPersonalExpectationWithCientificPosGraduations" name="teacherPersonalExpectationBean" schema="FillCientificPosGraduationsExpectations" layout="tabular">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle5 thlight thright mtop05"/>
					<fr:property name="columnClasses" value="width14em,,tdclear tderror1"/>
				</fr:layout>		
			</fr:edit>			
			
			
			<%-- Professional Pos-Graduations --%>
			<fr:hasMessages for="teacherPersonalExpectationWithProfessionalPosGraduations" type="conversion">
				<p><span class="error0">			
					<fr:message for="teacherPersonalExpectationWithProfessionalPosGraduations" show="message"/>
				</span></p>
			</fr:hasMessages>
			
			<p class="mbottom05"><strong><bean:message key="label.personalExpectationsManagement.professionalPosGraduations" bundle="DEPARTMENT_MEMBER_RESOURCES"/></strong></p>
			</div>
			<fr:edit id="teacherPersonalExpectationWithProfessionalPosGraduations" name="teacherPersonalExpectationBean" schema="FillProfessionalPosGraduationsExpectations" layout="tabular">	
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle5 thlight thright mtop05"/>
					<fr:property name="columnClasses" value="width14em,,tdclear tderror1"/>
				</fr:layout>		
			</fr:edit>			
			

			<%-- Seminaries --%>
			<fr:hasMessages for="teacherPersonalExpectationWithSeminaries" type="conversion">
				<p><span class="error0">			
					<fr:message for="teacherPersonalExpectationWithSeminaries" show="message"/>
				</span></p>
			</fr:hasMessages>
			
			<p class="mbottom05"><strong><bean:message key="label.personalExpectationsManagement.seminaries" bundle="DEPARTMENT_MEMBER_RESOURCES"/></strong></p>
			<fr:edit id="teacherPersonalExpectationWithSeminaries" name="teacherPersonalExpectationBean" schema="FillSeminariesExpectations" layout="tabular">	
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle5 thlight thright mtop05"/>
					<fr:property name="columnClasses" value="width14em,,tdclear tderror1"/>
				</fr:layout>		
			</fr:edit>			
			
			
			<%-- Main Focus --%>			
			<fr:edit id="teacherPersonalExpectationWithEducationMainFocus" name="teacherPersonalExpectationBean" schema="FillEducationMainFocusExpectations" layout="tabular">					
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle5 thlight thright mtop05"/>
					<fr:property name="columnClasses" value="width14em,,tdclear tderror1"/>
				</fr:layout>		
			</fr:edit>			
			
			<p class="smalltxt"><em><bean:message key="message.requiredField" bundle="DEPARTMENT_MEMBER_RESOURCES" /></em></p>				
			
			<p>
				<html:submit><bean:message key="button.submit" bundle="DEPARTMENT_MEMBER_RESOURCES"/></html:submit>
				<html:cancel onclick="this.form.method.value='viewTeacherPersonalExpectations';this.form.submit();">
					<bean:message key="button.cancel" bundle="DEPARTMENT_MEMBER_RESOURCES"/>
				</html:cancel>	
			<p>	
			
		</fr:form>	
		
	</logic:notEmpty>
	
	<logic:empty name="teacherPersonalExpectationBean">	
		<logic:notEmpty name="teacherPersonalExpectation">
			
			<p><em><bean:message key="label.common.executionYear" bundle="DEPARTMENT_MEMBER_RESOURCES"/>: <bean:write name="teacherPersonalExpectation" property="executionYear.year"/></em></p>
				
			<fr:form action="/personalExpectationManagement.do"> 
				<html:hidden property="method" value="editEducationExpectations"/>
								
				<%-- Graduations --%>								
				<fr:hasMessages for="teacherPersonalExpectationWithGraduations" type="conversion">
					<p><span class="error0">			
						<fr:message for="teacherPersonalExpectationWithGraduations" show="message"/>
					</span></p>
				</fr:hasMessages>
			
				<p class="mbottom05"><strong><bean:message key="label.personalExpectationsManagement.graduations" bundle="DEPARTMENT_MEMBER_RESOURCES"/></strong></p>			
				<fr:edit id="teacherPersonalExpectationWithGraduations" name="teacherPersonalExpectation" schema="EditGraduationExpectations">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle5 thlight thright mtop05"/>
						<fr:property name="columnClasses" value="width14em,,tdclear tderror1"/>
						<fr:property name="" value=""/>
					</fr:layout>		
				</fr:edit>			
				
				
				<%-- Cientific Pos-Graduations --%>
				<fr:hasMessages for="teacherPersonalExpectationWithCientificPosGraduations" type="conversion">
					<p><span class="error0">			
						<fr:message for="teacherPersonalExpectationWithCientificPosGraduations" show="message"/>
					</span></p>
				</fr:hasMessages>
				
				<p class="mbottom05"><strong><bean:message key="label.personalExpectationsManagement.cientificPosGraduations" bundle="DEPARTMENT_MEMBER_RESOURCES"/></strong></p>
				<fr:edit id="teacherPersonalExpectationWithCientificPosGraduations" name="teacherPersonalExpectation" schema="EditCientificPosGraduationsExpectations" layout="tabular">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle5 thlight thright mtop05"/>
						<fr:property name="columnClasses" value="width14em,,tdclear tderror1"/>
					</fr:layout>		
				</fr:edit>			
				
				
				<%-- Professional Pos-Graduations --%>
				<fr:hasMessages for="teacherPersonalExpectationWithProfessionalPosGraduations" type="conversion">
					<p><span class="error0">			
						<fr:message for="teacherPersonalExpectationWithProfessionalPosGraduations" show="message"/>
					</span></p>
				</fr:hasMessages>
				
				<p class="mbottom05"><strong><bean:message key="label.personalExpectationsManagement.professionalPosGraduations" bundle="DEPARTMENT_MEMBER_RESOURCES"/></strong></p>
				<fr:edit id="teacherPersonalExpectationWithProfessionalPosGraduations" name="teacherPersonalExpectation" schema="EditProfessionalPosGraduationsExpectations" layout="tabular">	
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle5 thlight thright mtop05"/>
						<fr:property name="columnClasses" value="width14em,,tdclear tderror1"/>
					</fr:layout>		
				</fr:edit>			
				
				
				<%-- Seminaries --%>
				<fr:hasMessages for="teacherPersonalExpectationWithSeminaries" type="conversion">
					<p><span class="error0">			
						<fr:message for="teacherPersonalExpectationWithSeminaries" show="message"/>
					</span></p>
				</fr:hasMessages>
				
				<p class="mbottom05"><strong><bean:message key="label.personalExpectationsManagement.seminaries" bundle="DEPARTMENT_MEMBER_RESOURCES"/></strong></p>
				<fr:edit id="teacherPersonalExpectationWithSeminaries" name="teacherPersonalExpectation" schema="EditSeminariesExpectations" layout="tabular">	
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle5 thlight thright mtop05"/>
						<fr:property name="columnClasses" value="width14em,,tdclear tderror1"/>
					</fr:layout>		
				</fr:edit>			
				
				
				<%-- Main Focus --%>
				<fr:edit id="teacherPersonalExpectationWithEducationMainFocus" name="teacherPersonalExpectation" schema="EditEducationMainFocusExpectations" layout="tabular">					
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle5 thlight thright mtop05"/>
						<fr:property name="columnClasses" value="width14em,,tdclear tderror1"/>
					</fr:layout>		
				</fr:edit>			

				<p class="smalltxt"><em><bean:message key="message.requiredField" bundle="DEPARTMENT_MEMBER_RESOURCES" /></em></p>				
			
				<p>
					<html:submit><bean:message key="button.submit" bundle="DEPARTMENT_MEMBER_RESOURCES"/></html:submit>
					<html:cancel onclick="this.form.method.value='viewTeacherPersonalExpectations';this.form.submit();">
						<bean:message key="button.cancel" bundle="DEPARTMENT_MEMBER_RESOURCES"/>
					</html:cancel>	
				<p>
				
			</fr:form>				
		
		</logic:notEmpty>		
	</logic:empty>
	
</logic:present>	