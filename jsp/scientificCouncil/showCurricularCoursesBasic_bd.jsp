<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<bean:define id="component" name="siteView" property="component"/>
<bean:define id="basicCurricularCourses" name="component" property="basicCurricularCourses"/>
<bean:define id="nonBasicCurricularCourses" name="component" property="nonBasicCurricularCourses"/>

<span class="error"><html:errors/></span>

<html:form action="/basicCurricularCourseManager">
<html:hidden property="method" value="setBasicCurricularCourses"/>
<table>
<tr><td>&nbsp;</td><td>&nbsp;</td></tr>
<logic:iterate id="infoCurricularCourse" name="basicCurricularCourses">
<tr>

<td><html:multibox  property="basicCurricularCourses"><bean:write name="infoCurricularCourse" property="idInternal"/> </html:multibox ></td>
<td><bean:write name="infoCurricularCourse" property="name"/></td>
</tr>
</logic:iterate>
<logic:iterate id="infoCurricularCourse" name="nonBasicCurricularCourses">
<tr>

<td><html:multibox  property="basicCurricularCourses"><bean:write name="infoCurricularCourse" property="idInternal"/> </html:multibox ></td>
<td><bean:write name="infoCurricularCourse" property="name"/></td>
</tr>
</logic:iterate>


</table>
<br />
		<html:submit styleClass="inputbutton">
			<bean:message key="button.save"/>
		</html:submit>
		<html:reset  styleClass="inputbutton">
			<bean:message key="label.clear"/>
		</html:reset>			
</html:form>