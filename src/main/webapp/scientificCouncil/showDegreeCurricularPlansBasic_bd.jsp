<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<bean:define id="component" name="siteView" property="component"/>
<bean:define id="degreeCurricularPlans" name="component" property="degreeCurricularPlans"/>

<span class="error"><!-- Error messages go here --><html:errors /></span>
<table width="98%" border="0" cellpadding="0" cellspacing="0">
          <tr>
            <td class="infoop"><bean:message key="message.public.degreeCurricularPlan.choose"/></td>
          </tr>
</table>
	<br />
<center><table>
<tr><th class="listClasses-header"><bean:message key="label.degreeCurricularPlan"/> </th></tr>
<logic:iterate id="degreeCurricularPlan" name="degreeCurricularPlans">
<tr>
<bean:define id="degreeCurricularPlanId" name="degreeCurricularPlan" property="externalId"/>
<td class="listClasses"><html:link page="<%= "/basicCurricularCourseManager.do?method=showBasicCurricularCourses&index=" + degreeCurricularPlanId %>"><bean:write name="degreeCurricularPlan" property="name"/></html:link></td>
</tr>
</logic:iterate>
</table></center>

 
	
    