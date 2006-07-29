<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoLesson" %>
	
	<% ArrayList iA = (ArrayList) request.getAttribute("listaAulas"); %>
<table width="100%" cellspacing="0">
	<tr>
	    <td class="infoselected"><p>O curso seleccionado &eacute;:</p>
			<strong><jsp:include page="context.jsp"/></strong>
	     </td>
	</tr>
	</table>
<br />
<h2><bean:message key="title.manage.aulas"/></h2>
<br />
<span class="error"><!-- Error messages go here --><html:errors /></span>
<p><b><bean:message key="listAulas.existing"/></b></p>
	<html:form action="/manipularAulasForm">
    <logic:present name="listaAulas" scope="request">
<table>
	<tr>
    	<td class="listClasses-header"></td>
       	<th class="listClasses-header"><bean:message key="property.aula.weekDay"/></th>
        <th class="listClasses-header"><bean:message key="property.aula.time.begining"/></th>
    	<th class="listClasses-header"><bean:message key="property.aula.time.end"/></th>
       	<th class="listClasses-header"><bean:message key="property.aula.type"/></th>
        <th class="listClasses-header"><bean:message key="property.aula.sala"/></th>
  	</tr>
		<% int i = 0; %>
<logic:iterate id="elem" name="listaAulas">
     	<% Integer iH = new Integer(((InfoLesson) iA.get(i)).getInicio().get(Calendar.HOUR_OF_DAY)); %>
        <% Integer iM = new Integer(((InfoLesson) iA.get(i)).getInicio().get(Calendar.MINUTE)); %>
        <% Integer fH = new Integer(((InfoLesson) iA.get(i)).getFim().get(Calendar.HOUR_OF_DAY)); %>
        <% Integer fM = new Integer(((InfoLesson) iA.get(i)).getFim().get(Calendar.MINUTE)); %>
        <% String appendStartMinute = ""; %>
        <% String appendEndMinute = ""; %>
        <% if (iM.intValue() == 0) { %>
        <% 	appendStartMinute = "0"; } %>
        <% if (fM.intValue() == 0) { %>
        <% 	appendEndMinute = "0"; } %>
    <tr>
 		<td class="listClasses"><html:radio bundle="HTMLALT_RESOURCES" altKey="radio.indexAula" property="indexAula" value="<%= (new Integer(i)).toString() %>"/></td>
        <td class="listClasses"><bean:write name="elem" property="diaSemana" /></td>
       	<td class="listClasses"><%= iH.toString()%> : <%= iM.toString() + appendStartMinute %></td>
        <td class="listClasses"><%= fH.toString()%> : <%= fM.toString() + appendEndMinute %></td>
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
<br />
<br />
<html:hidden alt="<%= SessionConstants.EXECUTION_PERIOD_OID %>" property="<%= SessionConstants.EXECUTION_PERIOD_OID %>"
			 value="<%= pageContext.findAttribute("executionPeriodOID").toString() %>"/>
<html:hidden alt="<%= SessionConstants.EXECUTION_DEGREE_OID %>" property="<%= SessionConstants.EXECUTION_DEGREE_OID %>"
			 value="<%= pageContext.findAttribute("executionDegreeOID").toString() %>"/>
<html:hidden alt="<%= SessionConstants.CURRICULAR_YEAR_OID %>" property="<%= SessionConstants.CURRICULAR_YEAR_OID %>"
			 value="<%= pageContext.findAttribute("curricularYearOID").toString() %>"/>
<html:hidden alt="<%= SessionConstants.EXECUTION_COURSE_OID %>" property="<%= SessionConstants.EXECUTION_COURSE_OID %>"
			 value="<%= pageContext.findAttribute("executionCourseOID").toString() %>"/>
<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.operation" property="operation" styleClass="inputbutton"><bean:message key="label.edit.Aula"/></html:submit>
<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.operation" property="operation" styleClass="inputbutton"><bean:message key="label.delete.Aula"/></html:submit>
	</logic:present>
    <logic:notPresent name="listaAulas" scope="request">
<span class="errors"><bean:message key="errors.existAulas"/></span>
<br />
<br />          
<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.operation" property="operation" styleClass="inputbutton"><bean:message key="label.back"/></html:submit>
  	</logic:notPresent>
</html:form>