<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>	
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<table>
<html:form action="/studentTests">
        
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="giveUp"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.testCode" property="testCode" value='<%=request.getAttribute("testCode").toString()%>'/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.exerciseCode" property="exerciseCode" value='<%=request.getAttribute("exerciseCode").toString()%>'/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.item" property="item" value='<%=request.getAttribute("item").toString()%>'/>
	<tr><td><h2><bean:message key="link.giveUp"/></h2></td></tr>
	<tr><td><bean:message key="message.giveUpQuestion"/></td></tr>
	
	<tr><td><html:submit bundle="HTMLALT_RESOURCES" altKey="submit.back" styleClass="inputbutton" property="back"><bean:message key="link.giveUp"/></html:submit>
</html:form></td>

<html:form action="/studentTests">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="testsFirstPage"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.testCode" property="testCode" value='<%=request.getAttribute("testCode").toString()%>'/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.objectCode" property="objectCode" value='<%=request.getAttribute("objectCode").toString()%>'/>
	<td><html:submit bundle="HTMLALT_RESOURCES" altKey="submit.back" styleClass="inputbutton" property="back"><bean:message key="button.back"/>
	</html:submit></html:form></td></tr>

</table>