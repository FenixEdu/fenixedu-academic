<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="java.util.List" %>
  
 


<logic:present name="siteView" property="component">

	<h2><bean:message key="property.executionCourse.associatedCurricularCourses"/></h2>
	<bean:define id="component" name="siteView" property="component"/>

	<logic:notEmpty name="component" property="associatedCurricularCourses" >

		<table align="center" cellspacing="1" cellpadding="5" >
			<tr>
			<th class="listClasses-header">
					<bean:message key="property.curricularCourse.name"/>
				</th>
				<th class="listClasses-header">
					<bean:message key="property.degree.initials"/>
				</th>
				<th class="listClasses-header">
					<bean:message key="property.curricularCourse.branch"/>
				</th>
				<th class="listClasses-header">
					<bean:message key="property.curricularCourse.curricularYear"/>
				</th>
				<th class="listClasses-header">
					<bean:message key="property.curricularCourse.semester"/>
				</th>
			</tr>
			
			<logic:iterate id="curricularCourse" name="component" property="associatedCurricularCourses" >
				
				<logic:iterate id="infoCurricularCourseScope" name="curricularCourse" property="infoScopes">
					<%-- FIXME: hardcoded semester 2 --%>
					<logic:equal name="infoCurricularCourseScope" property="infoCurricularSemester.semester" value="2">
						
						<tr>
						<td class="listClasses">
								<bean:write name="curricularCourse" property="name"/>
							</td>
							<td class="listClasses">
								<bean:write name="curricularCourse" property="infoDegreeCurricularPlan.infoDegree.sigla"/>
							</td>
							<td class="listClasses">
								<bean:write name="infoCurricularCourseScope" property="infoBranch.name"/>&nbsp;
							</td>
							<td class="listClasses">
								<bean:write name="infoCurricularCourseScope" property="infoCurricularSemester.infoCurricularYear.year"/>&nbsp;
							</td>
							<td class="listClasses">
								<bean:write name="infoCurricularCourseScope" property="infoCurricularSemester.semester"/>&nbsp;
							</td>
						</tr>
						
					</logic:equal>
					
				</logic:iterate>
				
			</logic:iterate>
			
		</table>
		<br/>
		<br/>
		<br/>
		<logic:notEmpty name="component" property="infoShiftsWithAssociatedClassesList" >
		
			<table align="center" cellspacing="1" cellpadding="5" >
			<tr>
				<th class="listClasses-header">
					<bean:message key="property.shift"/>
				</th>
				<th class="listClasses-header">
					<bean:message key="property.classes"/>
				</th>
			</tr>
				<logic:iterate id="shift" name="component" property="infoShiftsWithAssociatedClassesList" >		
						
						<tr>
							<td class="listClasses" >
								<bean:write name="shift" property="infoShift.nome"/>
							</td>
							
							<logic:notEmpty name="shift" property="infoClasses">	
								<td class="listClasses" colspan="0" align="center">
								<logic:iterate id="infoClasse" name="shift" property="infoClasses" >	
										<bean:write name="infoClasse" property="nome"/>&nbsp;&nbsp;															
								</logic:iterate>
								</td>
							</logic:notEmpty>

							<logic:empty name="shift" property="infoClasses">
								<td class="listClasses">
									&nbsp;
								</td>
							</logic:empty>
						</tr>
						
				</logic:iterate>
				</table>
		</logic:notEmpty>	
			
	</logic:notEmpty>	
	
	
<logic:empty name="component" property="associatedCurricularCourses" >
		<bean:message key="message.public.notfound.curricularCourses"/>
</logic:empty>
</logic:present>		
	