<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>

<span class="error"><html:errors property="error.default" /></span>

<logic:present name="<%= SessionConstants.INFO_EVALUATION %>" >	
<bean:define id="evaluation" name="<%= SessionConstants.INFO_EVALUATION %>" />
</logic:present>	

 <span class="error"><html:errors property="evaluationElements"/></span>
<h2><bean:message key="label.evaluation"/></h2> 
<table >
<html:form action="/editEvaluation">
	
<tr>
	<td>
		<strong><bean:message key="label.evaluation"/></strong>
	</td>
	
</tr>
<tr>
	<logic:present name="evaluation">
	<td>
		<html:textarea name="evaluation" property="evaluationElements" cols="50" rows="8"/>
	</td>
	</logic:present>	
	<logic:notPresent name="evaluation">
	<td>
		<html:textarea  property="evaluationElements" cols="50" rows="8"/>
	</td>
	</logic:notPresent>
</tr>
<tr></tr>
<html:hidden property="method" value="editEvaluation" />
<tr><td> <html:submit styleClass="inputbutton">
		<bean:message key="button.save"/>
	</html:submit>
	
	<html:reset  styleClass="inputbutton">
	<bean:message key="label.clear"/>
</html:reset>
	</td>
	</tr>
			
   
</html:form>

</table>