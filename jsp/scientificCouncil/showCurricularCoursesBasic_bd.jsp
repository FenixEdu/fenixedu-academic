<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<bean:define id="component" name="siteView" property="component"/>
<bean:define id="basicCurricularCourses" name="component" property="basicCurricularCourses"/>
<bean:define id="nonBasicCurricularCourses" name="component" property="nonBasicCurricularCourses"/>

<span class="error"><html:errors/></span>

<html:form action="/basicCurricularCourseManager">
<table>
<tr><td>&nbsp;</td><td>&nbsp;</td></tr>
<logic:iterate id="infoCurricularCourse" name="basicCurricularCourses">
<tr>
<bean:define id="idInternal" name="infoCurricularCourse" property="idInternal"/>
<td><html:multibox  property="basicCurricularCourses" name="infoCurricularCourse" property="idInternal" value="<%= idInternal.toString() %>" /></td>
<td><bean:write name="infoCurricularCourse" property="name"/></td>
</tr>
</logic:iterate>
<logic:iterate id="infoCurricularCourse" name="nonBasicCurricularCourses">
<tr>
<bean:define id="idInternal" name="infoCurricularCourse" property="idInternal"/>
<td><html:multibox  property="nonBasicCurricularCourses" value="<%= idInternal.toString() %>" /></td>
<td><bean:write name="infoCurricularCourse" property="name"/></td>
</tr>
</logic:iterate>


</table>
</html:form>