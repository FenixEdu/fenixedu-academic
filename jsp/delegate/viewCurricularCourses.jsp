<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<h2><bean:message key="label.delegate.curricularCourses" /></h2>
<logic:present name="infoCurricularCourses">
	<table width="90%"cellpadding="5" border="0">
		<tr>
			<td class="listClasses-header"><bean:message key="label.delegate.curricularCourse.code" />
			</td>
			<td class="listClasses-header" style="text-align:left"><bean:message key="label.delegate.curricularCourse.name" />
			</td>
			<td class="listClasses-header"><bean:message key="label.delegate.curricularCourse.YearSemBranch" />
			</td>
		</tr>
		<logic:iterate id="infoCurricularCourse" name="infoCurricularCourses">
			<tr>
				<bean:define id="year" name="infoDelegate" property="yearType.value" />
				<logic:iterate id="infoScope" name="infoCurricularCourse" property="infoScopes">
					<logic:equal name="infoScope" property="infoCurricularSemester.infoCurricularYear.year" value="<%= year.toString() %>">
						<bean:define id="canEdit" value="true"/>
					</logic:equal>
				</logic:iterate>
				<logic:present name="canEdit">
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
				</logic:present>
				<logic:notPresent name="canEdit">
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
				</logic:notPresent>
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