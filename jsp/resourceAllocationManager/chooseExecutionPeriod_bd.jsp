<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<em><bean:message key="title.resourceAllocationManager.management"/></em>
<h2><bean:message key="title.manage.schedule"/></h2>

<p>
	<span class="error"><!-- Error messages go here --><html:errors /></span>
</p>

<html:form action="/chooseExecutionPeriod" focus="index">
<table class="tstyle5">
  <tr>
    <td><bean:message key="property.executionPeriod"/>:</td>
    <td><jsp:include page="selectExecutionPeriodList.jsp"/></td>
  </tr>
</table>

<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="choose"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
<bean:message key="label.choose"/>
</html:submit>
</html:form>    

