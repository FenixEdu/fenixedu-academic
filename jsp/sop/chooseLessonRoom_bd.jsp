<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
	<tr>
   		<td class="infoselected"><p>O curso seleccionado &eacute;:</p>
			  <strong><jsp:include page="context.jsp"/></strong>
       	</td>
   	</tr>
</table>
<br />
<h2><bean:message key="title.criarAula"/></h2>
<span class="error"><html:errors/></span>
	<html:form action="/criarAulaForm">
	<html:hidden property="page" value="1"/>
<table cellspacing="0">
	<tr>
    	<td nowrap class="formTD"><bean:message key="property.aula.weekDay"/>: </td>
      	<td nowrap class="formTD"><html:hidden property="diaSemana"/><bean:write name="weekDayString"/></td>
   	</tr>
    <tr>
       	<td nowrap="nowrap" class="formTD"><bean:message key="property.aula.time.begining"/>: </td>
        <td nowrap="nowrap">
        	<html:hidden property="horaInicio" write="true"/>:
           	<html:hidden property="minutosInicio" write="true"/>
     	</td>
    </tr>
    <tr>
   		<td nowrap="nowrap" class="formTD"><bean:message key="property.aula.time.end"/>: </td>
        <td nowrap="nowrap">
	    	<html:hidden property="horaFim" write="true"/>:
          	<html:hidden property="minutosFim" write="true"/>
        </td> 
    </tr> 
    <tr>
    	<td nowrap class="formTD"><bean:message key="property.aula.type"/>: </td>
        <td nowrap class="formTD">
           	<html:select property="tipoAula" size="1">
               <option selected="selected"></option>
            <html:options collection="tiposAula" property="value" labelProperty="label"/>
            </html:select>
    	</td>
   	</tr>
  	<tr>
   		<td nowrap class="formTD"><bean:message key="property.aula.disciplina"/>: </td>
      	<td><jsp:include page="selectExecutionCourseList.jsp"/></td>
   </tr>
   <tr>
    	<td><bean:message key="property.aula.sala"/>: </td> 
       	<td>
     		<html:select property="nomeSala" size="1" >
               	<html:options collection="listaSalas" property="value" labelProperty="label"/>
           </html:select>
      	</td>
  	</tr>
</table>
<br />
<html:submit property="operation" styleClass="inputbutton"><bean:message key="label.create"/></html:submit>
<html:reset value="Limpar" styleClass="inputbutton"><bean:message key="label.clear"/></html:reset>
	</html:form>
</html:html>