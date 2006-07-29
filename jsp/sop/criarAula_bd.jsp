<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
	<tr>
   		<td class="infoselected"><p>O curso seleccionado &eacute;:</p>
			<strong><jsp:include page="context.jsp"/></strong>
       	</td>
   	</tr>
</table>
<br />
<h2><bean:message key="title.criarAula"/></h2>
    <span class="error"><!-- Error messages go here --><html:errors /></span>
<br/>
<br/>
<bean:message key="message.weekdays"/>
<br/>
<html:form action="/criarAulaForm" focus="diaSemana">
<table cellspacing="0">
	<tr>
      	<td nowrap class="formTD"><bean:message key="property.aula.weekDay"/>: </td>
        <td nowrap class="formTD">
        	<html:text bundle="HTMLALT_RESOURCES" altKey="text.diaSemana" property="diaSemana"  size="2"/>
       	</td>
   	</tr>
   	<tr>
    	<td nowrap="nowrap" class="formTD"><bean:message key="property.aula.time.begining"/>: </td>
        <td nowrap="nowrap">
          	<html:text bundle="HTMLALT_RESOURCES" altKey="text.horaInicio" property="horaInicio"  size="2"/> :
            <html:text bundle="HTMLALT_RESOURCES" altKey="text.minutosInicio" property="minutosInicio" size="2"/>
     	</td>
   	</tr>
    <tr>
        <td nowrap="nowrap" class="formTD"><bean:message key="property.aula.time.end"/>: </td>
        <td nowrap="nowrap">
         	<html:text bundle="HTMLALT_RESOURCES" altKey="text.horaFim" property="horaFim"  size="2"/>
            	:
            <html:text bundle="HTMLALT_RESOURCES" altKey="text.minutosFim" property="minutosFim"  size="2"/>
        </td> 
 	</tr> 
    <tr>
       <td nowrap class="formTD"><bean:message key="property.aula.type"/>: </td>
       <td nowrap class="formTD">
         	<html:select bundle="HTMLALT_RESOURCES" altKey="select.tipoAula" property="tipoAula" size="1">
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
<html:hidden alt="<%= SessionConstants.EXECUTION_PERIOD_OID %>" property="<%= SessionConstants.EXECUTION_PERIOD_OID %>"
			 value="<%= pageContext.findAttribute("executionPeriodOID").toString() %>"/>
<html:hidden alt="<%= SessionConstants.EXECUTION_DEGREE_OID %>" property="<%= SessionConstants.EXECUTION_DEGREE_OID %>"
			 value="<%= pageContext.findAttribute("executionDegreeOID").toString() %>"/>
<html:hidden alt="<%= SessionConstants.CURRICULAR_YEAR_OID %>" property="<%= SessionConstants.CURRICULAR_YEAR_OID %>"
			 value="<%= pageContext.findAttribute("curricularYearOID").toString() %>"/>
<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.operation" property="operation" styleClass="inputbutton"><bean:message key="lable.chooseRoom"/></html:submit>
<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset" value="Limpar" styleClass="inputbutton"><bean:message key="label.clear"/></html:reset>
</html:form>
