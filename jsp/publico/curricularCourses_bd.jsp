<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ page import="ServidorApresentacao.TagLib.sop.v3.TimeTableType" %>
<logic:notPresent name="siteView" property="component" >
<table align="center"  cellpadding='0' cellspacing='0'>
			<tr align="center">
				<td>
					<h2><bean:message key="message.public.notfound.executionCourse"/></h2>
				</td>
			</tr>
		</table>
</logic:notPresent>

<logic:present name="siteView" property="component">
	<h2><bean:message key="property.executionCourse.associatedCurricularCourses"/></h2>
	<bean:define id="component" name="siteView" property="component"/>

	<logic:notEmpty name="component" property="associatedCurricularCourses" >
		<table align="center" cellspacing="0" cellpadding="5" >
			<tr class="timeTable_line" align="center">
				<td class="degreetablestd">
					<bean:message key="property.curricularCourse.name"/>
				</td>
				<td class="degreetablestd">
					<bean:message key="property.degree.initials"/>
				</td>
				<td class="degreetablestd">
					<bean:message key="property.curricularCourse.branch"/>
				</td>
				<td class="degreetablestd">
					<bean:message key="property.curricularCourse.curricularYear"/>
				</td>
				<td class="degreetablestd">
					<bean:message key="property.curricularCourse.semester"/>
				</td>
			</tr>
			
			<logic:iterate id="curricularCourse" name="component" property="associatedCurricularCourses" >
				
				<logic:iterate id="infoCurricularCourseScope" name="curricularCourse" property="infoScopes">
					<%-- FIXME: hardcoded semester 2 --%>
					<logic:equal name="infoCurricularCourseScope" property="infoCurricularSemester.semester" value="2">
						
						<tr class="timeTable_line" align="center">
							<td class="degreetablestd">
								<bean:write name="curricularCourse" property="name"/>
							</td>
							<td class="degreetablestd">
								<bean:write name="curricularCourse" property="infoDegreeCurricularPlan.infoDegree.sigla"/>
							</td>
							<td class="degreetablestd">
								<bean:write name="infoCurricularCourseScope" property="infoBranch.name"/>&nbsp;
							</td>
							<td class="degreetablestd">
								<bean:write name="infoCurricularCourseScope" property="infoCurricularSemester.infoCurricularYear.year"/>&nbsp;
							</td>
							<td class="degreetablestd">
								<bean:write name="infoCurricularCourseScope" property="infoCurricularSemester.semester"/>&nbsp;
							</td>
						</tr>
						
					</logic:equal>
					
				</logic:iterate>
				
			</logic:iterate>
			
		</table>
		
	</logic:notEmpty>	

	
<logic:empty name="component" property="associatedCurricularCourses" >
		<bean:message key="message.public.notfound.curricularCourses"/>
</logic:empty>
</logic:present>		
