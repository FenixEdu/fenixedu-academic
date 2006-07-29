<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>


<html:form action="/roomExamSearch">
		
<h2>Selecionar Salas</h2>

Preencher apenas os campos necessï¿?rios. <br/>
Se quiser ver todas as salas não preencha nenhum.
<%-- clique 
<html:link page="<%= "/roomExamSearch.do?method=search" %>">aqui</html:link>. 
--%>
<br/>
<span class="error"><!-- Error messages go here --><html:errors /></span>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="search"/> 
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/> 
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionPeriodOID" property="executionPeriodOID"/> 

<br/>

<table>
	<tr>
		<td><bean:message key="property.room.name"/></td>
		<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.name" maxlength="20" size="20" property="name"/></td>
	</tr>
	<tr>
		<td><bean:message key="property.room.building"/></td>
		<td>
		   	<html:select bundle="HTMLALT_RESOURCES" altKey="select.building" property="building">
		       	<option value="" selected="selected"></option>           
			    <html:options collection="public.buildings" property="value" labelProperty="label"/>                  	             
		    </html:select>
		</td>
	</tr>
	<tr>
		<td><bean:message key="property.room.floor"/></td>
		<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.floor" maxlength="2" size="2" property="floor"/></td>
	</tr>
	<tr>
		<td><bean:message key="property.room.type"/></td>
		<td>
			<html:select bundle="HTMLALT_RESOURCES" altKey="select.type" property="type">
			    <html:options collection="public.types" property="value" labelProperty="label"/>                  	             
		    </html:select>
	</tr>
	<tr>
		<td><bean:message key="property.room.capacity.normal"/></td>
		<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.normal" maxlength="3" size="3" property="normal"/></td>
	</tr>
	<tr>
		<td><bean:message key="property.room.capacity.exame"/></td>
		<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.exam" maxlength="3" size="3" property="exam"/></td>
	</tr>
</table>
<br/>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
		<bean:message key="lable.choose"/>
	</html:submit>

<%--	
	<html:cancel bundle="HTMLALT_RESOURCES" altKey="cancel.cancel" value="Cancelar" styleClass="inputbutton">
		<bean:message key="label.cancel"/>
	</html:cancel>
--%>
</html:form>	