<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<html:xhtml/>

<logic:present role="role(STUDENT)">
	
	<em><bean:message key="title.student.portalTitle" bundle="STUDENT_RESOURCES" /></em>
	<h2><bean:message key="label.title.seniorInfo"/></h2>

	<bean:define id="senior" name="senior" type="net.sourceforge.fenixedu.domain.student.Senior"/>
	
	<logic:notEmpty name="senior">
			 	
		<p><html:img align="middle" src="<%= request.getContextPath() +"/person/retrievePersonalPhoto.do?method=retrieveOwnPhoto" %>" altKey="personPhoto" bundle="IMAGE_RESOURCES" /></p>
		  	   			
		<fr:view name="senior" schema="ViewSeniorInfo">			
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1 thlight thright"/>
				<fr:property name="columnClasses" value="aleft,,,,"/>   		
			</fr:layout>							
		</fr:view>
				
		<fr:view name="senior" schema="ViewSeniorExpectedInfo">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1 thlight thright mtop05"/>
				<fr:property name="columnClasses" value="aleft,,,,"/>   		
			</fr:layout>
		</fr:view>
			  		 
	    <p class="mtop15 mbottom05"><b><bean:message key="label.senior.specialtyField"/></b></p>
	    <p class="mtop05"><bean:write name="senior" property="specialtyField" filter="false"/></p>
	  		 
	    <p class="mtop15 mbottom05"><b><bean:message key="label.senior.professionalInterests"/></b></p>
	    <p class="mtop05"><bean:write name="senior" property="professionalInterests" filter="false"/></p>
	  		 
	    <p class="mtop15 mbottom05"><b><bean:message key="label.senior.languageSkills"/></b></p>
	    <p class="mtop05"><bean:write name="senior" property="languageSkills" filter="false"/></p>
	  		 
	    <p class="mtop15 mbottom05"><b><bean:message key="label.senior.informaticsSkills"/></b></p>
	    <p class="mtop05"><bean:write name="senior" property="informaticsSkills" filter="false"/></p>
	  
	    <p class="mtop15 mbottom05"><b><bean:message key="label.senior.extracurricularActivities"/></b></p>
	    <p class="mtop05"><bean:write name="senior" property="extracurricularActivities" filter="false"/></p>
	   
	    <p class="mtop15 mbottom05"><b><bean:message key="label.senior.professionalExperience"/></b></p>
	    <p class="mtop05"><bean:write name="senior" property="professionalExperience" filter="false"/></p>     	
	     		  		   
	    <fr:view name="senior" schema="ViewSeniorInfoLastModificationDate">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4 thlight tdcenter mtop1"/>
				<fr:property name="columnClasses" value="aleft,,,,"/>   		
			</fr:layout>
		</fr:view>			
	
		<p class="invisible">
			<html:link page="<%="/seniorInformation.do?method=prepare&amp;page=0&registrationOID=" + senior.getRegistration().getExternalId().toString()%>">
				<bean:message key="label.return" bundle="APPLICATION_RESOURCES"/>
			</html:link>
		</p>
	
	</logic:notEmpty>	
</logic:present>