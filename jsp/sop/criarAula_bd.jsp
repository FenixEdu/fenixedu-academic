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
<br/>
<br/>
<bean:message key="message.weekdays"/>
<br/>
<html:form action="/criarAulaForm">
<table cellspacing="0">
	<tr>
      	<td nowrap class="formTD"><bean:message key="property.aula.weekDay"/>: </td>
        <td nowrap class="formTD">
        	<html:text property="diaSemana"  size="2"/>
       	</td>
   	</tr>
   	<tr>
    	<td nowrap="nowrap" class="formTD"><bean:message key="property.aula.time.begining"/>: </td>
        <td nowrap="nowrap">
          	<html:text property="horaInicio"  size="1"/> :
            <html:text property="minutosInicio" size="1"/>
     	</td>
   	</tr>
    <tr>
        <td nowrap="nowrap" class="formTD"><bean:message key="property.aula.time.end"/>: </td>
        <td nowrap="nowrap">
         	<html:text property="horaFim"  size="1"/>
            	:
            <html:text property="minutosFim"  size="1"/>
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
       <td nowrap class="formTD"><bean:message key="property.aula.disciplina"/></td>
       <td align="left" height="40"><jsp:include page="selectExecutionCourseList.jsp"/></td>
    </tr>
</table>
<br />
<html:submit property="operation" styleClass="inputbutton"><bean:message key="lable.chooseRoom"/></html:submit>
<html:reset value="Limpar" styleClass="inputbutton"><bean:message key="label.clear"/></html:reset>
	</html:form>
</html:html>