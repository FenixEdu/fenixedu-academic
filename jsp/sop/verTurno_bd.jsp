<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoLesson" %>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
	<tr>
  		<td class="infoselected"><p>O curso seleccionado &eacute;:</p>
			  <strong><jsp:include page="context.jsp"/></strong>
       	</td>
   	</tr>
</table>
<br />
   	<% ArrayList iA = (ArrayList) request.getAttribute("infoAulasDeTurno"); %>
<h2 class="inline"><bean:message key="property.turno"/>: </h2><h3 class="inline"><bean:write name="infoTurno" property="nome" scope="request" filter="true"/></h3>
<p><b><bean:message key="listAulas.OfTurno"/></b></p>
    <logic:present name="infoAulasDeTurno" scope="request">
<table>
	<tr>
    	<th class="listClasses-header"><bean:message key="property.aula.weekDay"/></th>
     	<th class="listClasses-header"><bean:message key="property.aula.time.begining"/></th>
        <th class="listClasses-header"><bean:message key="property.aula.time.end"/></th>
        <th class="listClasses-header"><bean:message key="property.aula.type"/></th>
        <th class="listClasses-header"><bean:message key="property.aula.sala"/></th>
 	</tr>
	<% int i = 0; %>
    <logic:iterate id="elem" name="infoAulasDeTurno">
    <% Integer iH = new Integer(((InfoLesson) iA.get(i)).getInicio().get(Calendar.HOUR_OF_DAY)); %>
    <% Integer iM = new Integer(((InfoLesson) iA.get(i)).getInicio().get(Calendar.MINUTE)); %>
    <% Integer fH = new Integer(((InfoLesson) iA.get(i)).getFim().get(Calendar.HOUR_OF_DAY)); %>
    <% Integer fM = new Integer(((InfoLesson) iA.get(i)).getFim().get(Calendar.MINUTE)); %>
   	<tr>
      	<td class="listClasses"><bean:write name="elem" property="diaSemana" /></td>
     	<td class="listClasses"><%= iH.toString()%> : <%= iM.toString()%></td>
       	<td class="listClasses"><%= fH.toString()%> : <%= fM.toString()%></td>
        <td class="listClasses"><%= ((InfoLesson) iA.get(i)).getTipo().toString()%></td>
        <td class="listClasses">
	        <logic:notEmpty name="elem" property="infoSala.nome">
    	    	<bean:write name="elem" property="infoSala.nome"/>
    	    </logic:notEmpty>	
        </td>
  	</tr>
    <% i++; %>
    </logic:iterate>           
</table>
    </logic:present>
    <logic:notPresent name="infoAulasDeTurno" scope="request">
<span class="error"><!-- Error messages go here --><bean:message key="errors.existAulas"/></span>
</logic:notPresent>