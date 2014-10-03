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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<h2><bean:message key="label.delegate.curricularCourses" bundle="DELEGATES_RESOURCES" /></h2>
<logic:present name="infoCurricularCoursesAuthorizationToEdit">
	<table width="90%"cellpadding="5" border="0">
		<tr>
			<th class="listClasses-header"><bean:message key="label.delegate.curricularCourse.code" bundle="DELEGATES_RESOURCES"/>
			</th>
			<th class="listClasses-header" style="text-align:left"><bean:message key="label.delegate.curricularCourse.name" bundle="DELEGATES_RESOURCES"/>
			</th>
			<th class="listClasses-header"><bean:message key="label.delegate.curricularCourse.YearSemBranch" bundle="DELEGATES_RESOURCES"/>
			</th>
		</tr>
		<logic:iterate id="infoCurricularCourseAuthorizationToEdit" name="infoCurricularCoursesAuthorizationToEdit">
			<bean:define id="infoCurricularCourse" name="infoCurricularCourseAuthorizationToEdit" property="infoCurricularCourse" />
			<bean:define id="canEdit" name="infoCurricularCourseAuthorizationToEdit" property="canEdit" />
			<tr>
				<logic:equal name="canEdit" value="true">
						<td class="listClasses">
							<html:link page="/studentReport.do?page=0&amp;method=prepareEdit" paramId="curricularCourseId" paramName="infoCurricularCourse" paramProperty="externalId">
								<bean:write name="infoCurricularCourse" property="code"/>
							</html:link>
						</td>			
						<td class="listClasses" style="text-align:left">
							<html:link page="/studentReport.do?page=0&amp;method=prepareEdit" paramId="curricularCourseId" paramName="infoCurricularCourse" paramProperty="externalId">
								<bean:write name="infoCurricularCourse" property="name"/>
							</html:link>
						</td>
				</logic:equal>
				<logic:equal name="canEdit" value="false">
					<td class="listClasses">
						<html:link page="/studentReport.do?page=0&amp;method=read" paramId="curricularCourseId" paramName="infoCurricularCourse" paramProperty="externalId">
							<bean:write name="infoCurricularCourse" property="code"/>
						</html:link>
					</td>
					<td class="listClasses" style="text-align:left">					
						<html:link page="/studentReport.do?page=0&amp;method=read" paramId="curricularCourseId" paramName="infoCurricularCourse" paramProperty="externalId">
							<bean:write name="infoCurricularCourse" property="name"/>
						</html:link>
					</td>
				</logic:equal>
				<td class="listClasses" style="text-align:left">
					<logic:iterate id="infoScope" name="infoCurricularCourse" property="infoScopes">
						<bean:write name="infoScope" property="infoCurricularSemester.infoCurricularYear.year"/>&nbsp;
						<bean:write name="infoScope" property="infoCurricularSemester.semester"/>&nbsp;
						<bean:write name="infoScope" property="infoBranch.acronym"/>
						<br />
					</logic:iterate>
				</td>
			</tr>
		</logic:iterate>
 	</table>
 </logic:present>