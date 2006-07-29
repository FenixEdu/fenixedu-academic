<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<bean:define id="component" name="siteView" property="component"/>
<bean:define id="basicCurricularCourses" name="component" property="basicCurricularCourses"/>
<bean:define id="nonBasicCurricularCourses" name="component" property="nonBasicCurricularCourses"/>
<bean:define id="curricularPlan" name="component" property="infoDegreeCurricularPlan"/>

<span class="error"><!-- Error messages go here --><html:errors /></span>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
          <tr>
            <td class="infoop"><bean:message key="message.basicCurricularCoursesSelection"/></td>
          </tr>
</table>
	<br />
<html:form action="/basicCurricularCourseManager">
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="setBasicCurricularCourses"/>
<bean:define id="curricularPlanId" name="curricularPlan" property="idInternal"/>

<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.curricularIndex" property="curricularIndex" value="<%= curricularPlanId.toString() %>"/>
<table>
<tr><td>&nbsp;</td><td>&nbsp;</td></tr>
<logic:iterate id="infoCurricularCourse" name="basicCurricularCourses">
<tr>

<td><html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.basicCurricularCourses"  property="basicCurricularCourses"><bean:write name="infoCurricularCourse" property="idInternal"/> </html:multibox ></td>
<td><bean:write name="infoCurricularCourse" property="name"/></td>
</tr>
</logic:iterate>
<logic:iterate id="infoCurricularCourse" name="nonBasicCurricularCourses">
<tr>

<td><html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.basicCurricularCourses"  property="basicCurricularCourses"><bean:write name="infoCurricularCourse" property="idInternal"/> </html:multibox ></td>
<td><bean:write name="infoCurricularCourse" property="name"/></td>
</tr>
</logic:iterate>


</table>
<br />
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
			<bean:message key="button.save"/>
		</html:submit>
		<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset"  styleClass="inputbutton">
			<bean:message key="label.clear"/>
		</html:reset>			
</html:form>