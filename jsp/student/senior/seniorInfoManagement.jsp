<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<html:xhtml/>

<logic:present role="STUDENT">

	<h2><bean:message key="label.title.seniorInfo"/></h2>
	
	<logic:empty name="senior">
		<bean:message key="error.senior.studentNotASenior"/>
	</logic:empty>

	<logic:notEmpty name="senior">
		
		<bean:define id="personID" name="senior" property="student.person.idInternal"/>
		<fr:form action="/seniorInformation.do?method=change">
			
		    <table width="100%" cellspacing="0">
				<tr>
					<td class="infoop"><span class="emphasis-box">info</span></td>
		     		<td class="infoop"><bean:message key="label.senior.personalInfoWarning"/></td>
			   	</tr>	   		
		    </table>
						
		 	<p><html:img align="middle" src="<%= request.getContextPath() +"/person/retrievePersonalPhoto.do?method=retrieveOwnPhoto" %>" altKey="personPhoto" bundle="IMAGE_RESOURCES" /></p>
			
			<fr:view name="senior" schema="ViewSeniorInfo">			
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle4 thlight tdcenter mtop05"/>
					<fr:property name="columnClasses" value="aleft,,,,"/>   		
				</fr:layout>							
			</fr:view>
					
			<fr:edit id="editSeniorExpectedInfoID" name="senior" schema="EditSeniorExpectedInfo">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle4 thlight mtop05"/>
					<fr:property name="columnClasses" value="aleft,,,,"/>   		
				</fr:layout>
			</fr:edit>
			
			<p><bean:message key="label.senior.specialtyField_"/>&nbsp;(<bean:message key="label.senior.specialtyFieldFinalWork"/>)</p>
			<fr:edit id="EditSeniorSpecialtyFieldID" name="senior" schema="EditSeniorSpecialtyField"> 
				<fr:layout name="flow">
	                <fr:property name="eachInline" value="true"/>
	                <fr:property name="labelExcluded" value="true"/>
	            </fr:layout>
			</fr:edit>
			
			<p><bean:message key="label.senior.professionalInterests_"/></p>
			<fr:edit id="EditSeniorProfessionalInterestsID" name="senior" schema="EditSeniorProfessionalInterests"> 
				<fr:layout name="flow">
	                <fr:property name="eachInline" value="true"/>
	                <fr:property name="labelExcluded" value="true"/>
	            </fr:layout>
			</fr:edit>
			
			<p><bean:message key="label.senior.languageSkills_"/></p>
			<fr:edit id="EditSeniorLanguageSkillsID" name="senior" schema="EditSeniorLanguageSkills"> 
				<fr:layout name="flow">
	                <fr:property name="eachInline" value="true"/>
	                <fr:property name="labelExcluded" value="true"/>
	            </fr:layout>	
			</fr:edit>
			
			<p><bean:message key="label.senior.informaticsSkills_"/></p>
			<fr:edit id="EditSeniorInformaticsSkillssID" name="senior" schema="EditSeniorInformaticsSkillss">
				<fr:layout name="flow">
	                <fr:property name="eachInline" value="true"/>
	                <fr:property name="labelExcluded" value="true"/>
	            </fr:layout>		
			</fr:edit>
			
			<p><bean:message key="label.senior.extracurricularActivities_"/></p>
			<fr:edit id="EditSeniorExtracurricularActivitiesID" name="senior" schema="EditSeniorExtracurricularActivities"> 
				<fr:layout name="flow">
	                <fr:property name="eachInline" value="true"/>
	                <fr:property name="labelExcluded" value="true"/>
	            </fr:layout>
			</fr:edit>
					
			<p><bean:message key="label.senior.professionalExperience_"/></p>
			<fr:edit id="EditSeniorProfessionalExperienceID" name="senior" schema="EditSeniorProfessionalExperience"> 
				<fr:layout name="flow">
	                <fr:property name="eachInline" value="true"/>
	                <fr:property name="labelExcluded" value="true"/>
	            </fr:layout>		
			</fr:edit>
			
			<fr:view name="senior" schema="ViewSeniorInfoLastModificationDate">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle4 thlight tdcenter mtop05"/>
					<fr:property name="columnClasses" value="aleft,,,,"/>   		
				</fr:layout>
			</fr:view>
	   	         
			<p>
				<html:submit><bean:message key="button.change"/></html:submit>
				<html:reset><bean:message key="button.reset"/></html:reset>
			</p>
			
	 	 </fr:form>
	</logic:notEmpty>
	
</logic:present>