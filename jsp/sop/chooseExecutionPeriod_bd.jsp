<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
 <h2><bean:message key="title.manage.schedule"/></h2>
<br/>
<span class="error"><html:errors /></span>
<html:form action="/chooseExecutionPeriod">
<table border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td nowrap="nowrap" width="125"><bean:message key="property.executionPeriod"/>:</td>
    <td nowrap="nowrap"><jsp:include page="selectExecutionPeriodList.jsp"/></td>
  </tr>
</table>
<br />
<html:hidden property="method" value="choose"/>
<html:hidden property="page" value="1"/>
<html:submit styleClass="inputbutton">
<bean:message key="label.choose"/>
</html:submit>
</html:form>    

