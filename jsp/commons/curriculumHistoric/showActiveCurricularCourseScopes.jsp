<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<h2><bean:message key="label.curricularPlan"  bundle="CURRICULUM_HISTORIC_RESOURCES"/>&nbsp;-&nbsp;
	<bean:write name="degreeCurricularPlan" property="name"/></h2>
<span class="error"><!-- Error messages go here --><html:errors bundle="CURRICULUM_HISTORIC_RESOURCES"/></span>
<br />
<logic:present name="allActiveCurricularCourseScopes">
			<logic:iterate id="curricularCourseScopeElemList" name="allActiveCurricularCourseScopes" length="1">
				<logic:iterate id="curricularCourseScopeElem" name="curricularCourseScopeElemList" type="net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseScope" length="1">
					<bean:define id="currentSemester" name="curricularCourseScopeElem" property="infoCurricularSemester.semester"/>
				</logic:iterate>
			</logic:iterate>
			<bean:define id="executionYear" name="executionYearID" />
	<table>
		<tr>
			<th class="listClasses-header"><bean:message key="label.curricularCourseScope.curricularYear" bundle="CURRICULUM_HISTORIC_RESOURCES"/></th>
			<th class="listClasses-header"><bean:message key="label.curricularCourseScope.curricularSemester" bundle="CURRICULUM_HISTORIC_RESOURCES"/></th>
			<th class="listClasses-header"><bean:message key="label.curricularCourse" bundle="CURRICULUM_HISTORIC_RESOURCES"/></th>
			<th class="listClasses-header"><bean:message key="label.curricularCourseScope.branch" bundle="CURRICULUM_HISTORIC_RESOURCES"/></th>
		</tr>
		<logic:iterate id="curricularCourseScopeElemList" name="allActiveCurricularCourseScopes">
			<logic:iterate id="curricularCourseScopeElem" name="curricularCourseScopeElemList" type="net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseScope" length="1" >
				<logic:notEqual name="curricularCourseScopeElem" property="infoCurricularSemester.semester" value="<%= pageContext.findAttribute("currentSemester").toString()%>">
					<tr>
						<th class="listClasses-header"><bean:message key="label.curricularCourseScope.curricularYear" bundle="CURRICULUM_HISTORIC_RESOURCES"/></th>
						<th class="listClasses-header"><bean:message key="label.curricularCourseScope.curricularSemester" bundle="CURRICULUM_HISTORIC_RESOURCES"/></th>
						<th class="listClasses-header"><bean:message key="label.curricularCourse" bundle="CURRICULUM_HISTORIC_RESOURCES"/></th>
						<th class="listClasses-header"><bean:message key="label.curricularCourseScope.branch" bundle="CURRICULUM_HISTORIC_RESOURCES"/></th>
					</tr>
					<bean:define id="currentSemester" name="curricularCourseScopeElem" property="infoCurricularSemester.semester"/>
				</logic:notEqual>
				<tr>
					<td class="listClasses"><bean:write name="curricularCourseScopeElem" property="infoCurricularSemester.infoCurricularYear.year"/></td>
					<td class="listClasses"><bean:write name="curricularCourseScopeElem" property="infoCurricularSemester.semester"/></td>
					<td class="listClasses" style="text-align:left">
					<bean:define id="curricularCourseCode" name="curricularCourseScopeElem" property="infoCurricularCourse.idInternal"/>
						<html:link page="<%="/showCurriculumHistoric.do?method=showCurriculumHistoric&amp;curricularCourseCode=" + curricularCourseCode +"&amp;semester=" + pageContext.findAttribute("currentSemester").toString() + "&amp;executionYearID=" + pageContext.findAttribute("executionYear").toString() %>">
							<bean:write name="curricularCourseScopeElem" property="infoCurricularCourse.name"/>
						</html:link>
					</td>
					<td class="listClasses">
						<bean:write name="curricularCourseScopeElem" property="infoBranch.prettyCode"/>
						<logic:iterate id="curricularCourseScopeElem" name="curricularCourseScopeElemList" type="net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseScope" offset="1">
							<br /><bean:write name="curricularCourseScopeElem" property="infoBranch.prettyCode"/>				
						</logic:iterate>
					</td>
				</tr>
			</logic:iterate>
		</logic:iterate>
	</table>
</logic:present>
