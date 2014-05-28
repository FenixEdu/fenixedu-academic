<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
<table width="98%" border="0" cellpadding="0" cellspacing="0">
	<tr>
   		<td class="infoselected"><p>O curso seleccionado &eacute;:</p>
			  <strong><jsp:include page="context.jsp"/></strong>
       	</td>
   	</tr>
</table>
<br />
<h2><bean:message key="title.criarAula"/></h2>
<span class="error"><!-- Error messages go here --><html:errors /></span>
<bean:message key="message.createLesson"/>
	<html:form action="/criarAulaForm" focus="nomeSala" >
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="2"/>
<table cellspacing="0">
	<tr>
    	<td nowrap class="formTD"><bean:message key="property.aula.weekDay"/>: </td>
      	<td nowrap class="formTD"><html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.diaSemana" property="diaSemana"/><bean:write name="weekDayString"/></td>
   	</tr>
    <tr>
       	<td nowrap="nowrap" class="formTD"><bean:message key="property.aula.time.begining"/>: </td>
        <td nowrap="nowrap">
        	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.horaInicio" property="horaInicio" write="true"/>:
           	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.minutosInicio" property="minutosInicio" write="true"/>
     	</td>
    </tr>
    <tr>
   		<td nowrap="nowrap" class="formTD"><bean:message key="property.aula.time.end"/>: </td>
        <td nowrap="nowrap">
	    	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.horaFim" property="horaFim" write="true"/>:
          	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.minutosFim" property="minutosFim" write="true"/>
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
   		<td nowrap class="formTD"><bean:message key="property.aula.disciplina"/>: </td>
      	<td><jsp:include page="selectExecutionCourseList.jsp"/></td>
   </tr>
   <tr>
    	<td><bean:message key="property.aula.sala"/>: </td> 
       	<td>
     		<html:select bundle="HTMLALT_RESOURCES" altKey="select.nomeSala" property="nomeSala" size="1" >
               	<html:options collection="listaSalas" property="value" labelProperty="label"/>
           </html:select>
      	</td>
  	</tr>
</table>
<br />
<html:hidden alt="<%= PresentationConstants.EXECUTION_PERIOD_OID %>" property="<%= PresentationConstants.EXECUTION_PERIOD_OID %>"
			 value="<%= pageContext.findAttribute("executionPeriodOID").toString() %>"/>
<html:hidden alt="<%= PresentationConstants.EXECUTION_DEGREE_OID %>" property="<%= PresentationConstants.EXECUTION_DEGREE_OID %>"
			 value="<%= pageContext.findAttribute("executionDegreeOID").toString() %>"/>
<html:hidden alt="<%= PresentationConstants.CURRICULAR_YEAR_OID %>" property="<%= PresentationConstants.CURRICULAR_YEAR_OID %>"
			 value="<%= pageContext.findAttribute("curricularYearOID").toString() %>"/>
<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.operation" property="operation" styleClass="inputbutton"><bean:message key="label.create"/></html:submit>
<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset" value="Limpar" styleClass="inputbutton"><bean:message key="label.clear"/></html:reset>
	</html:form>
