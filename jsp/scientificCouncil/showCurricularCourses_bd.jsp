<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<bean:define id="component" name="siteView" property="component"/>
<bean:define id="curricularCourses" name="component" property="curricularCourses"/>

<span class="error"><html:errors/></span>
<center><table>
<tr>
<td class="listClasses-header"><bean:message key="label.name"/></td>
</tr>
<logic:iterate id="infoCurricularCourse" name="curricularCourses">
<tr>
<td class="listClasses"><bean:write name="infoCurricularCourse" property="name"/></td>
</tr>
</logic:iterate>
</table></center>