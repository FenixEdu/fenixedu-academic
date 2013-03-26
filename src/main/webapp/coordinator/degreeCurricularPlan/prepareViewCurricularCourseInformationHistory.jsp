<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/taglibs-string.tld" prefix="str" %>
<logic:present name="infoExecutionYears">
	<bean:define id="infoCurricularCourseName"><%=pageContext.findAttribute("infoCurricularCourseName")%></bean:define>
	<h2><str:upperCase><bean:message key="label.coordinator.degreeCurricularPlan.history.information"/></str:upperCase>&nbsp;-&nbsp;<bean:write name="infoCurricularCourseName"/></h2>	
	<%-- <h2><bean:write name="infoCurricularCourseName"/></h2> --%>
	<span class="error"><!-- Error messages go here --><html:errors /></span>
	<p>(<html:link page="<%="/degreeCurricularPlanManagement.do?method=viewActiveCurricularCourseInformation&amp;infoExecutionDegreeCode=" + pageContext.findAttribute("infoExecutionDegreeCode") +"&amp;infoCurricularCourseCode=" + pageContext.findAttribute("infoCurricularCourseCode") + "&amp;degreeCurricularPlanID=" + pageContext.findAttribute("degreeCurricularPlanID")%>">
		<font color="#0066CC"><bean:message key="link.coordinator.degreeCurricularPlan.see"/>&nbsp;<bean:message key="label.coordinator.degreeCurricularPlan.current.information"/></font>
	</html:link>)</p>
	<table>
		<tr>
			<td><bean:message key="title.masterDegree.administrativeOffice.chooseExecutionYear"/>:</td>
		</tr>
		<logic:iterate id="executionYear" name="infoExecutionYears">
			<tr>
				<td>
<%--					<bean:define id="stringExecutionYear" name="executionYear" property="label"/>
					<html:link page="<%="/degreeCurricularPlanManagement.do?method=viewCurricularCourseInformationHistory&amp;infoExecutionDegreeCode=" + pageContext.findAttribute("infoExecutionDegreeCode") +"&amp;infoCurricularCourseCode=" + pageContext.findAttribute("infoCurricularCourseCode") + "&amp;executionYear=" + stringExecutionYear + "&amp;infoCurricularCourseName=" + infoCurricularCourseName + "&amp;degreeCurricularPlanID=" + pageContext.findAttribute("degreeCurricularPlanID")%>">
						<bean:write name="executionYear" property="label"/>
					</html:link> --%>
					<bean:define id="stringExecutionYear" name="executionYear" property="year"/>
					<html:link page="<%="/degreeCurricularPlanManagement.do?method=viewCurricularCourseInformationHistory&amp;infoExecutionDegreeCode=" + pageContext.findAttribute("infoExecutionDegreeCode") +"&amp;infoCurricularCourseCode=" + pageContext.findAttribute("infoCurricularCourseCode") + "&amp;executionYear=" + stringExecutionYear + "&amp;infoCurricularCourseName=" + infoCurricularCourseName + "&amp;degreeCurricularPlanID=" + pageContext.findAttribute("degreeCurricularPlanID")%>">
						<bean:write name="executionYear" property="year"/>
					</html:link>
				</td>
			</tr>
		</logic:iterate>
	</table>
</logic:present>