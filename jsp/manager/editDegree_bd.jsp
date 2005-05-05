<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<h2><bean:message key="message.editDegree" /></h2>
<br>

<span class="error"><html:errors/></span>

<table>
<html:form action="/editDegree" method="get">
	<html:hidden property="page" value="1"/>
	
<tr>
	<td>
		<bean:message key="message.degreeName"/>
	</td>
	<td>
		<html:text size="60" property="name" />
		
	</td>
</tr>
<tr>
	<td>
		<bean:message key="message.degreeNameEn"/>
	</td>
	<td>
		<html:text size="60" property="nameEn" />
	</td>
</tr>
<tr>
	<td>
		<bean:message key="message.degreeCode"/>
	</td>
	<td>
		<html:text size="13" property="code"  />
	</td>
</tr>
				
<tr>
	<td>
		<bean:message key="message.manager.degreeType"/>
	</td>
	<td>
		
		<html:select property="degreeType">
    		<html:option key="option.editDegree.degree" value="1"/>
    		<html:option key="option.editDegree.masterDegree" value="2"/>
    	</html:select>
		
	</td>
</tr>

</table>
<html:hidden property="method" value="edit" />
<html:hidden property="degreeId" value="<%= request.getParameter("degreeId") %>"/>

	

<br>
<html:submit styleClass="inputbutton">
<bean:message key="button.save"/>
</html:submit>
<html:reset  styleClass="inputbutton">
<bean:message key="label.clear"/>
</html:reset>			


</html:form>