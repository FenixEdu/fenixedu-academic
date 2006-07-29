<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<h2><bean:message key="label.students.listMarks" /></h2>
<logic:present name="curricularCourses">
	<bean:size id="listSize" name="curricularCourses"/>
	<logic:equal name="listSize" value="0">
		<bean:message key="error.nonExisting.AssociatedCurricularCourses"/>
	</logic:equal>
	<logic:notEqual name="listSize" value="0">
		<table width="100%">
			<logic:iterate id="curricularCourseElem" name="curricularCourses"  type="net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse" length="1">
				<tr>
					<td class="infoselected">
						<b><bean:message key="label.masterDegree.administrativeOffice.degree"/>:</b>
						<bean:write name="curricularCourseElem" property="infoDegreeCurricularPlan.infoDegree.nome" />
						<br />
						<b><bean:message key="label.curricularPlan" />:</b>
						<bean:write name="curricularCourseElem" property="infoDegreeCurricularPlan.name" />
					</td>
				</tr>
			</logic:iterate>	
		</table>
		<br />
		<span class="error"><!-- Error messages go here --><html:errors /></span>
		<table>
			<tr>
				<td>
					<bean:message key="title.masterDegree.administrativeOffice.chooseCurricularCourse" />:
					<br /><br />
				</td>
			</tr>
			<!-- Curricular Course -->
			<logic:iterate id="curricularCourseElem" name="curricularCourses"  type="net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse">
			   	<bean:define id="curricularCourse" name="curricularCourseElem" property="name"/>
					<tr>
						<td>
							<html:link page="<%="/marksManagement.do?method=getStudentMarksList&amp;degreeId=" + pageContext.findAttribute("degreeId") + "&amp;courseId=" + curricularCourseElem.getIdInternal() + "&amp;objectCode=" + curricularCourseElem.getInfoDegreeCurricularPlan().getIdInternal()%>">
								<bean:write name="curricularCourseElem" property="name"/>
							</html:link>
						</td>
					</tr>
			</logic:iterate>
		</table>
	</logic:notEqual>
</logic:present>