<%@ page language="java" %>
<%@ page import="org.apache.struts.action.Action" %>
<%@ page import ="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>


<html:form action="/roomExamSearch">
		
<h2>Selecionar Salas</h2>

Preencher apenas os campos necessários. <br/>
Se quiser ver todas as salas não preencha nenhum.
<%-- clique 
<html:link page="<%= "/roomExamSearch.do?method=search" %>">aqui</html:link>. 
--%>
<br/>
<span class="error"><html:errors/></span>
<html:hidden property="method" value="search"/> 
<html:hidden property="page" value="1"/> 

<br/>

<table>
	<tr>
		<td><bean:message key="property.room.name"/></td>
		<td><html:text maxlength="20" size="20" property="name"/></td>
	</tr>
	<tr>
		<td><bean:message key="property.room.building"/></td>
		<td>
		   	<html:select property="building">
		       	<option value="" selected="selected"></option>           
			    <html:options collection="public.buildings" property="value" labelProperty="label"/>                  	             
		    </html:select>
		</td>
	</tr>
	<tr>
		<td><bean:message key="property.room.floor"/></td>
		<td><html:text maxlength="2" size="2" property="floor"/></td>
	</tr>
	<tr>
		<td><bean:message key="property.room.type"/></td>
		<td>
			<html:select property="type">
			    <html:options collection="public.types" property="value" labelProperty="label"/>                  	             
		    </html:select>
	</tr>
	<tr>
		<td><bean:message key="property.room.capacity.normal"/></td>
		<td><html:text maxlength="3" size="3" property="normal"/></td>
	</tr>
	<tr>
		<td><bean:message key="property.room.capacity.exame"/></td>
		<td><html:text maxlength="3" size="3" property="exam"/></td>
	</tr>
</table>
<br/>
	<html:submit styleClass="inputbutton">
		<bean:message key="lable.choose"/>
	</html:submit>

<%--	
	<html:cancel value="Cancelar" styleClass="inputbutton">
		<bean:message key="label.cancel"/>
	</html:cancel>
--%>
</html:form>	