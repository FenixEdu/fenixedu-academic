
<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<bean:define id="component" name="siteView" property="component"/>
<bean:define id="curricularCourses" name="component" property="curricularCourses"/>

<span class="error"><!-- Error messages go here --><html:errors /></span>
<center><table>
<tr>
<th class="listClasses-header"><bean:message key="label.name"/></th>
<th class="listClasses-header"><bean:message key="label.curricularCourseType"/></th>
</tr>
<logic:iterate id="infoCurricularCourse" name="curricularCourses">
<tr>
<bean:define id="curricularCourseId" name="infoCurricularCourse" property="idInternal"/>
<td class="listClasses">
<html:link page="<%= "/curricularCourseManager.do?method=viewCurriculum&index=" + curricularCourseId %>" >
	<bean:write name="infoCurricularCourse" property="name"/></td>
</html:link>
<td class="listClasses"><bean:write name="infoCurricularCourse" property="ownershipType"/></td>
</tr>
</logic:iterate>
</table></center>