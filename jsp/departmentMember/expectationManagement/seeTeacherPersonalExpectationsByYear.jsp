<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<html:xhtml/>

<logic:present role="DEPARTMENT_MEMBER,DEPARTMENT_ADMINISTRATIVE_OFFICE">

	<logic:notEmpty name="teacherPersonalExpectation">
			
	<%-- ****** Education Expectations ****** --%>
		
		<p style='background-color: #eee; padding: 0.5em; font-size: 1.3em;'>
			<strong><bean:message key="label.personalExpectationsManagement.education" bundle="DEPARTMENT_MEMBER_RESOURCES"/></strong>
		</p>		
		
			<%-- Graduations --%>
		
		<div style="font: bold 12px Verdana, Arial, Helvetica, sans-serif;">
			<bean:message key="label.personalExpectationsManagement.graduations" bundle="DEPARTMENT_MEMBER_RESOURCES"/>
		</div>			
		<fr:view name="teacherPersonalExpectation" schema="ViewGraduationExpectations" layout="tabular"/>			
		
			<%-- Cientific Pos-Graduations --%>
		
		<div style="font: bold 12px Verdana, Arial, Helvetica, sans-serif;">
			<bean:message key="label.personalExpectationsManagement.cientificPosGraduations" bundle="DEPARTMENT_MEMBER_RESOURCES"/>
		</div>
		<fr:view name="teacherPersonalExpectation" schema="ViewCientificPosGraduationsExpectations" layout="tabular"/>	
		
			<%-- Professional Pos-Graduations --%>
		
		<div style="font: bold 12px Verdana, Arial, Helvetica, sans-serif;">
			<bean:message key="label.personalExpectationsManagement.professionalPosGraduations" bundle="DEPARTMENT_MEMBER_RESOURCES"/>
		</div>
		<fr:view name="teacherPersonalExpectation" schema="ViewProfessionalPosGraduationsExpectations" layout="tabular"/>	
		
			<%-- Seminaries --%>
		
		<div style="font: bold 12px Verdana, Arial, Helvetica, sans-serif;">
			<bean:message key="label.personalExpectationsManagement.seminaries" bundle="DEPARTMENT_MEMBER_RESOURCES"/>
		</div>
		<fr:view name="teacherPersonalExpectation" schema="ViewSeminariesExpectations" layout="tabular"/>	
		
			<%-- Main Focus --%>			
		<fr:view name="teacherPersonalExpectation" schema="ViewEducationMainFocusExpectations" layout="tabular"/>	
		
		<logic:empty name="noEdit">
			<logic:equal name="teacherPersonalExpectation" property="allowedToEditExpectation" value="true">
				<p><html:link page="/personalExpectationManagement.do?method=prepareEditEducationExpectations" paramId="teacherPersonalExpectationID" paramName="teacherPersonalExpectation" paramProperty="idInternal">
					<bean:message key="link.edit" bundle="DEPARTMENT_MEMBER_RESOURCES"/>										
				</html:link></p>		
			</logic:equal>
		</logic:empty>	
													
	<%-- ****** Researche And Development Expectations ****** --%>			
		
		<p style='background-color: #eee; padding: 0.5em; font-size: 1.3em;'>
			<strong><bean:message key="label.personalExpectationsManagement.investigation" bundle="DEPARTMENT_MEMBER_RESOURCES"/></strong>
		</p>	
	
			<%-- Publications And Projects  --%>
		
		<div style="font: bold 12px Verdana, Arial, Helvetica, sans-serif;">
			<bean:message key="label.personalExpectationsManagement.publicationsAndProjects" bundle="DEPARTMENT_MEMBER_RESOURCES"/>
		</div>
		<fr:view name="teacherPersonalExpectation" schema="ViewPublicationsAndProjectsExpectations" layout="tabular"/>	
	
			<%-- Publications And Projects Main Focus --%>
		<fr:view name="teacherPersonalExpectation" schema="ViewPublicationsAndProjectsMainFocusExpectations" layout="tabular"/>	
	
			
			<%-- Orientation --%>
		
		<div style="font: bold 12px Verdana, Arial, Helvetica, sans-serif;">
			<bean:message key="label.personalExpectationsManagement.orientation" bundle="DEPARTMENT_MEMBER_RESOURCES"/>
		</div>
		<fr:view name="teacherPersonalExpectation" schema="ViewOrientationsExpectations" layout="tabular"/>	
	
	
			<%-- Orientation Main Focus --%>
		<fr:view name="teacherPersonalExpectation" schema="ViewOrientationMainFocusExpectations" layout="tabular"/>	
	
		<logic:empty name="noEdit">
			<logic:equal name="teacherPersonalExpectation" property="allowedToEditExpectation" value="true">
				<p><html:link page="/personalExpectationManagement.do?method=prepareEditResearchAndDevelopmentExpectations" paramId="teacherPersonalExpectationID" paramName="teacherPersonalExpectation" paramProperty="idInternal">
					<bean:message key="link.edit" bundle="DEPARTMENT_MEMBER_RESOURCES"/>										
				</html:link></p>		
			</logic:equal>
		</logic:empty>
		
	<%-- ****** University Service Expectations ****** --%>	
						
		<p style='background-color: #eee; padding: 0.5em; font-size: 1.3em;'>
			<strong><bean:message key="label.personalExpectationsManagement.universityService" bundle="DEPARTMENT_MEMBER_RESOURCES"/></strong>			
		</p>	
		
		<fr:view name="teacherPersonalExpectation" schema="ViewUniversityServiceExpectations" layout="tabular"/>							
		<fr:view name="teacherPersonalExpectation" schema="ViewUniversityServiceMainFocusExpectations" layout="tabular"/>	
	
		<logic:empty name="noEdit">	
			<logic:equal name="teacherPersonalExpectation" property="allowedToEditExpectation" value="true">
				<p><html:link page="/personalExpectationManagement.do?method=prepareEditUniversityServicesExpectations" paramId="teacherPersonalExpectationID" paramName="teacherPersonalExpectation" paramProperty="idInternal">
					<bean:message key="link.edit" bundle="DEPARTMENT_MEMBER_RESOURCES"/>										
				</html:link></p>		
			</logic:equal>
		</logic:empty>
							
	<%-- ****** Professional Activity Expectations ****** --%>		
		
		<p style='background-color: #eee; padding: 0.5em; font-size: 1.3em;'>
			<strong><bean:message key="label.personalExpectationsManagement.professionalActivity" bundle="DEPARTMENT_MEMBER_RESOURCES"/></strong>
		</p>			
		
		<fr:view name="teacherPersonalExpectation" schema="ViewProfessionalActivitiesExpectations" layout="tabular"/>							
		<fr:view name="teacherPersonalExpectation" schema="ViewProfessionalActivitiesMainFocusExpectations" layout="tabular"/>	
	
		<logic:empty name="noEdit">
			<logic:equal name="teacherPersonalExpectation" property="allowedToEditExpectation" value="true">
				<p><html:link page="/personalExpectationManagement.do?method=prepareEditProfessionalActivitiesExpectations" paramId="teacherPersonalExpectationID" paramName="teacherPersonalExpectation" paramProperty="idInternal">
					<bean:message key="link.edit" bundle="DEPARTMENT_MEMBER_RESOURCES"/>										
				</html:link></p>		
			</logic:equal>
		</logic:empty>	
										
	</logic:notEmpty>	
	
</logic:present>	