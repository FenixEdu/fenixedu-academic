<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>	
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<table>
<html:form action="/studentTests">
        
	<html:hidden property="method" value="giveUp"/>
	<html:hidden property="testCode" value='<%=request.getAttribute("testCode").toString()%>'/>
	<html:hidden property="exerciseCode" value='<%=request.getAttribute("exerciseCode").toString()%>'/>
	<html:hidden property="item" value='<%=request.getAttribute("item").toString()%>'/>
	<tr><td><h2><bean:message key="link.giveUp"/></h2></td></tr>
	<tr><td><bean:message key="message.giveUpQuestion"/></td></tr>
	
	<tr><td><html:submit styleClass="inputbutton" property="back"><bean:message key="link.giveUp"/></html:submit>
</html:form></td>

<html:form action="/studentTests">
	<html:hidden property="method" value="testsFirstPage"/>
	<html:hidden property="testCode" value='<%=request.getAttribute("testCode").toString()%>'/>
	<html:hidden property="objectCode" value='<%=request.getAttribute("objectCode").toString()%>'/>
	<td><html:submit styleClass="inputbutton" property="back"><bean:message key="button.back"/>
	</html:submit></html:form></td></tr>

</table>