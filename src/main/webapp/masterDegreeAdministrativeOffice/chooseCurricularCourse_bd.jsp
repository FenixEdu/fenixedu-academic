<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ page import="org.apache.struts.Globals" %>
<logic:present name="jspTitle">
	<h2><bean:write name="jspTitle" /></h2>
	<br />
</logic:present>
<span class="error"><!-- Error messages go here --><html:errors /></span>
<br />
<logic:present name="curricularCourses">
	<bean:message key="label.masterDegree.administrativeOffice.executionYear"/>:<bean:write name="executionYear" />
	&nbsp;-&nbsp;
	<bean:message key="label.masterDegree.administrativeOffice.degree"/>:<bean:write name="degree" />
	<br />
	<bean:message key="title.masterDegree.administrativeOffice.chooseCurricularCourse" />
	<br /><br />
	<bean:define id="path" type="java.lang.String" scope="request" property="path" name="<%= Globals.MAPPING_KEY %>" />
	<table>
		<!-- CurricularCourse -->
		<logic:iterate id="curricularCourseElem" name="curricularCourses"  type="net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse">
		   	<bean:define id="curricularCourse" name="curricularCourseElem" property="name"/>
				<tr>
					<td>
						<html:link page="<%= path + ".do?method=chooseCurricularCourse&amp;courseID=" + curricularCourseElem.getExternalId() + "&amp;executionYear=" + pageContext.findAttribute("executionYear") + "&amp;jspTitle=" + pageContext.findAttribute("jspTitle") + "&amp;degree=" + pageContext.findAttribute("degree") + "&amp;curricularCourse=" + pageContext.findAttribute("curricularCourse") %>">
							<bean:write name="curricularCourseElem" property="name"/>
						</html:link>
					</td>
				</tr>
		</logic:iterate>
	</table>
</logic:present>