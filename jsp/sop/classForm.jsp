<%@ page language="java" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<html:form action="/ClassManagerDA" >
	<span class="error"><html:errors/></span>	
	
	<logic:notPresent name="<%= SessionConstants.CLASS_VIEW %>" scope="session">
		<input type="hidden" name="method" value="createClass">
	</logic:notPresent>
	<logic:present name="<%= SessionConstants.CLASS_VIEW %>" scope="session">
		<input type="hidden" name="method" value="editClass"/>
		<input type="hidden" name="change" value= "1"/>
		<table border="0" cellspacing="0" cellpadding="0">
		  <tr>
		   	<td nowrap="nowrap" class="infoop">
		   		<html:link page="/ClassShiftManagerDA.do?method=viewClassShiftList"><bean:message key="label.add.shifts"/></html:link>
		   	<td/>
		  </tr>
		  <tr>
		   	<td nowrap="nowrap">
				<br />		   	
				<bean:message key="message.class.changeName"/>
		   	<td/>
		  </tr>
		</table>
	</logic:present>
	<html:hidden property="page" value="1"/>
	<table border="0" cellspacing="0" cellpadding="0">
	  <tr>
	    <td nowrap="nowrap">
	    	<br />
		    <bean:message key="property.class.nameShift" />
		</td>
	    <td nowrap="nowrap">
	    	<br />
	    	<html:text property="className" value=""/> <html:submit styleClass="inputbuttonSmall"><bean:message key="label.ok"/></html:submit>
	    </td>
	  </tr>
	</table>
</html:form>