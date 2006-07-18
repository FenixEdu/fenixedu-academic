<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<h2><bean:message key="label.delegate.curricularCourses" /></h2>
<logic:present name="infoCurricularCoursesAuthorizationToEdit">
	<table width="90%"cellpadding="5" border="0">
		<tr>
			<th class="listClasses-header"><bean:message key="label.delegate.curricularCourse.code" />
			</th>
			<th class="listClasses-header" style="text-align:left"><bean:message key="label.delegate.curricularCourse.name" />
			</th>
			<th class="listClasses-header"><bean:message key="label.delegate.curricularCourse.YearSemBranch" />
			</th>
		</tr>
		<logic:iterate id="infoCurricularCourseAuthorizationToEdit" name="infoCurricularCoursesAuthorizationToEdit">
			<bean:define id="infoCurricularCourse" name="infoCurricularCourseAuthorizationToEdit" property="infoCurricularCourse" />
			<bean:define id="canEdit" name="infoCurricularCourseAuthorizationToEdit" property="canEdit" />
			<tr>
				<logic:equal name="canEdit" value="true">
						<td class="listClasses">
							<html:link page="/studentReport.do?page=0&amp;method=prepareEdit" paramId="curricularCourseId" paramName="infoCurricularCourse" paramProperty="idInternal">
								<bean:write name="infoCurricularCourse" property="code"/>
							</html:link>
						</td>			
						<td class="listClasses" style="text-align:left">
							<html:link page="/studentReport.do?page=0&amp;method=prepareEdit" paramId="curricularCourseId" paramName="infoCurricularCourse" paramProperty="idInternal">
								<bean:write name="infoCurricularCourse" property="name"/>
							</html:link>
						</td>
				</logic:equal>
				<logic:equal name="canEdit" value="false">
					<td class="listClasses">
						<html:link page="/studentReport.do?page=0&amp;method=read" paramId="curricularCourseId" paramName="infoCurricularCourse" paramProperty="idInternal">
							<bean:write name="infoCurricularCourse" property="code"/>
						</html:link>
					</td>
					<td class="listClasses" style="text-align:left">					
						<html:link page="/studentReport.do?page=0&amp;method=read" paramId="curricularCourseId" paramName="infoCurricularCourse" paramProperty="idInternal">
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