<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%--<logic:present name="infoDegree">--%>
	
<h2><bean:message key="message.editDegree" /></h2>
<br/>
<table>
<html:form action="/editDegree" method="get">
	<html:hidden property="page" value="1"/>
	
<tr>
	<td>
		<bean:message key="message.degreeName"/>
	</td>
	<td>
		<html:textarea cols="60" property="name" />
		
	</td>
	<td>
		<span class="error"><html:errors property="name"  /></span>
	</td>
</tr>
<tr>
	<td>
		<bean:message key="message.degreeCode"/>
	</td>
	<td>
		<html:textarea cols="60" property="code"  />
	</td>
	<td>
		<span class="error"><html:errors property="code" /></span>
	</td>
</tr>
				
<tr>
	<td>
		<bean:message key="message.degreeType"/>
	</td>
	<td>
		
		<html:select property="degreeType">
    		<html:option key="option.editDegree.licenciatura" value="1"/>
    		<html:option key="option.editDegree.mestrado" value="2"/>
    	</html:select>
		
	</td>
</tr>

</table>
<br />
<html:submit styleClass="inputbutton">
<bean:message key="button.save"/>
</html:submit>
<html:reset  styleClass="inputbutton">
<bean:message key="label.clear"/>
</html:reset>			
<html:hidden property="method" value="edit" />
</html:form>
</logic:present> 	