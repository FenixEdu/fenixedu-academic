<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<%--
<span class="error"><html:errors property="error.default" /></span>



<logic:present name="<%= SessionConstants.INFO_EVALUATION %>" >	
<bean:define id="evaluation" name="<%= SessionConstants.INFO_EVALUATION %>" />
</logic:present> 	

 <span class="error"><html:errors property="evaluationElements"/></span> --%>

<logic:present name="siteView">
<bean:define id="evaluation" name="siteView" property="component"/>

<h2><bean:message key="label.evaluation"/></h2> 

<html:form action="/editEvaluation">
	<html:hidden property="page" value="1"/>
<table >	
<tr>
	<td>
		<strong><bean:message key="label.evaluation"/></strong>
	</td>
	
</tr>
<tr>
	<td>
		<html:textarea  property="evaluationElements" cols="50" rows="8"/>
	</td>
</tr>
<tr>
<td>
		<strong><bean:message key="label.evaluation.eng"/></strong>
	</td>
</tr>	
<tr>
	<td>
		<html:textarea  property="evaluationElementsEn" cols="50" rows="8"/>
	</td>
</tr>
<tr></tr>
<html:hidden property="method" value="editEvaluation" />
<html:hidden  property="objectCode" value="<%= pageContext.findAttribute("objectCode").toString() %>" />
<tr><td> <html:submit styleClass="inputbutton">
		<bean:message key="button.save"/>
	</html:submit>
	
	<html:reset  styleClass="inputbutton">
	<bean:message key="label.clear"/>
</html:reset>
	</td>
	</tr>
			
</table>   
</html:form>

</logic:present>