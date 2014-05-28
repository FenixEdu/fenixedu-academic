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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/struts-example-1.0" prefix="app" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3.TimeTableType" %>
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
		
	</logic:notEmpty>	

	
<logic:empty name="component" property="associatedCurricularCourses" >
		<bean:message key="message.public.notfound.curricularCourses"/>
</logic:empty>
</logic:present>		
