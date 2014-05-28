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
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<html:xhtml/>

<logic:present role="role(STUDENT)">

	<h2><bean:message key="label.title.seniorInfo"/></h2>
	
	<logic:empty name="senior">
		<bean:message key="error.senior.studentNotASenior" arg0="<%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%>"/>
	</logic:empty>

	<logic:notEmpty name="senior">
		
		<bean:define id="personID" name="senior" property="student.person.externalId"/>
		<fr:form action="/seniorInformation.do?method=change">

		<div class="infoop2">
			<bean:message key="label.senior.personalInfoWarning"/>
		</div>

					
		 	<p><html:img align="middle" src="<%= request.getContextPath() +"/person/retrievePersonalPhoto.do?method=retrieveOwnPhoto" %>" altKey="personPhoto" bundle="IMAGE_RESOURCES" /></p>
			
			<fr:view name="senior" schema="ViewSeniorInfo">			
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle1 thlight thright mtop05"/>
					<fr:property name="columnClasses" value="aleft,,,,"/>   		
				</fr:layout>							
			</fr:view>
					
			<fr:edit id="editSeniorExpectedInfoID" name="senior" schema="EditSeniorExpectedInfo">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle5 thlight thright thmiddle mtop05"/>
					<fr:property name="columnClasses" value=",,tderror1 tdclear"/>   		
				</fr:layout>
			</fr:edit>
			
			<p class="mtop15"><bean:message key="label.senior.specialtyField_"/>&nbsp;(<bean:message key="label.senior.specialtyFieldFinalWork"/>)</p>
			<fr:edit id="EditSeniorSpecialtyFieldID" name="senior" schema="EditSeniorSpecialtyField"> 
				<fr:layout name="flow">
	                <fr:property name="eachInline" value="true"/>
	                <fr:property name="labelExcluded" value="true"/>
	            </fr:layout>
			</fr:edit>
			
			<p class="mtop2"><bean:message key="label.senior.professionalInterests_"/></p>
			<fr:edit id="EditSeniorProfessionalInterestsID" name="senior" schema="EditSeniorProfessionalInterests"> 
				<fr:layout name="flow">
	                <fr:property name="eachInline" value="true"/>
	                <fr:property name="labelExcluded" value="true"/>
	            </fr:layout>
			</fr:edit>
			
			<p class="mtop2"><bean:message key="label.senior.languageSkills_"/></p>
			<fr:edit id="EditSeniorLanguageSkillsID" name="senior" schema="EditSeniorLanguageSkills"> 
				<fr:layout name="flow">
	                <fr:property name="eachInline" value="true"/>
	                <fr:property name="labelExcluded" value="true"/>
	            </fr:layout>	
			</fr:edit>
			
			<p class="mtop2"><bean:message key="label.senior.informaticsSkills_"/></p>
			<fr:edit id="EditSeniorInformaticsSkillssID" name="senior" schema="EditSeniorInformaticsSkillss">
				<fr:layout name="flow">
	                <fr:property name="eachInline" value="true"/>
	                <fr:property name="labelExcluded" value="true"/>
	            </fr:layout>		
			</fr:edit>
			
			<p class="mtop2"><bean:message key="label.senior.extracurricularActivities_"/></p>
			<fr:edit id="EditSeniorExtracurricularActivitiesID" name="senior" schema="EditSeniorExtracurricularActivities"> 
				<fr:layout name="flow">
	                <fr:property name="eachInline" value="true"/>
	                <fr:property name="labelExcluded" value="true"/>
	            </fr:layout>
			</fr:edit>
					
			<p class="mtop2"><bean:message key="label.senior.professionalExperience_"/></p>
			<fr:edit id="EditSeniorProfessionalExperienceID" name="senior" schema="EditSeniorProfessionalExperience"> 
				<fr:layout name="flow">
	                <fr:property name="eachInline" value="true"/>
	                <fr:property name="labelExcluded" value="true"/>
	            </fr:layout>		
			</fr:edit>
			
			<fr:view name="senior" schema="ViewSeniorInfoLastModificationDate">
				<fr:layout name="tabular">
					<fr:property name="classes" value="thlight tdcenter mtop05"/>
				</fr:layout>
			</fr:view>
	   	         
			<p class="mtop15">
				<html:submit><bean:message key="button.change"/></html:submit>
				<html:reset><bean:message key="button.reset"/></html:reset>
			</p>
			
	 	 </fr:form>
	</logic:notEmpty>
	
</logic:present>