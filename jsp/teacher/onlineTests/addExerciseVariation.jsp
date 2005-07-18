<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
 
<h2><bean:message key="title.addExerciseVariations"/></h2>
<br/>
<table>
	<tr><td class="infoop"><bean:message key="message.addExerciseVariation.information" /></td></tr>
</table>
<br/>
<html:form action="/exercisesManagement" enctype="multipart/form-data">
<html:hidden property="page" value="0"/>
<html:hidden property="method" value="loadExerciseVariationsFile"/>
<html:hidden property="exerciseCode"/>
<html:hidden property="objectCode" value="<%= pageContext.findAttribute("objectCode").toString() %>"/>
<logic:present name="order">
	<html:hidden property="order" value="<%=(pageContext.findAttribute("order")).toString()%>"/>
</logic:present>
<logic:present name="asc">
	<html:hidden property="asc" value="<%=(pageContext.findAttribute("asc")).toString()%>"/>
</logic:present>
<span class="error"><html:errors/></span>
	<table>
		<tr>
			<td><bean:message key="label.xmlZipFile"/></td>
		</tr>
		<tr>
			<td><html:file property="xmlZipFile" size="50"/></td>
		<tr/>
	</table>	
	<br />
	<br />
	<html:submit styleClass="inputbutton"><bean:message key="button.insert"/></html:submit>
	<html:submit styleClass="inputbutton" onclick="javascript:document.forms[0].method.value='prepareChooseExerciseType';"><bean:message key="label.create"/></html:submit>
	<html:submit styleClass="inputbutton" onclick="javascript:document.forms[0].method.value='exercisesFirstPage';"><bean:message key="label.back"/></html:submit>
</html:form> 

