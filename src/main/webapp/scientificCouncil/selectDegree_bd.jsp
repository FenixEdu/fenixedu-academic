<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<bean:define id="component" name="siteView" property="component"/>
<bean:define id="degreeList" name="component" property="degrees"/>

<span class="error"><!-- Error messages go here --><html:errors /></span>
<table width="98%" border="0" cellpadding="0" cellspacing="0">
          <tr>
            <td class="infoop"><bean:message key="message.public.degree.choose"/></td>
          </tr>
</table>
	<br />
<center><table  >
<tr><th class="listClasses-header"><bean:message key="label.degree"/> </th></tr>
<logic:iterate id="degree" name="degreeList">
<tr>
<bean:define id="degreeId" name="degree" property="externalId"/>
<td class="listClasses" ><html:link page="<%= "/curricularCourseManager.do?method=showDegreeCurricularPlans&index=" + degreeId %>"><bean:write name="degree" property="presentationName"/></html:link></td>
</tr>
</logic:iterate>
</table></center>

 
	
    