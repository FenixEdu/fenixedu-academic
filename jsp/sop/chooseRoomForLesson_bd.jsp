<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<table width="100%" cellspacing="0">
	<tr>
		<td class="infoselected">
			<p>
				O curso seleccionado &eacute;:
			</p>
			<strong>
				<jsp:include page="context.jsp"/>
			</strong>
       	</td>
   	</tr>
</table>
<br />
<h2>
	<bean:message key="title.manage.aulas"/>
</h2>
<span class="error">
	<html:errors/>
</span>
<html:form action="/manageLesson" focus="nomeSala">
	<html:hidden property="method" value="createEditLesson"/>
	<html:hidden property="page" value="2"/>
	
	<logic:present name="action">
		<html:hidden property="action"
					 value="<%= pageContext.findAttribute("action").toString() %>"/>
	</logic:present>
	<html:hidden property="<%= SessionConstants.EXECUTION_PERIOD_OID %>"
				 value="<%= pageContext.findAttribute("executionPeriodOID").toString() %>"/>
	<html:hidden property="<%= SessionConstants.EXECUTION_DEGREE_OID %>"
				 value="<%= pageContext.findAttribute("executionDegreeOID").toString() %>"/>
	<html:hidden property="<%= SessionConstants.CURRICULAR_YEAR_OID %>"
				 value="<%= pageContext.findAttribute("curricularYearOID").toString() %>"/>
	<html:hidden property="<%= SessionConstants.EXECUTION_COURSE_OID %>"
				 value="<%= pageContext.findAttribute("executionCourseOID").toString() %>"/>
	<html:hidden property="<%= SessionConstants.SHIFT_OID %>"
				 value="<%= pageContext.findAttribute("shiftOID").toString() %>"/>
	<logic:present name="lessonOID">
		<html:hidden property="<%= SessionConstants.LESSON_OID %>"
					 value="<%= pageContext.findAttribute("lessonOID").toString() %>"/>
	</logic:present>
	<table cellspacing="0">
		<tr>
	      	<td nowrap class="formTD"><bean:message key="property.aula.weekDay"/>: </td>
	        <td nowrap class="formTD">
    	    	<html:text property="diaSemana" size="2" disabled="true"/>
				<html:hidden property="diaSemana"/>
	       	</td>
   		</tr>
	   	<tr>
    		<td nowrap="nowrap" class="formTD"><bean:message key="property.aula.time.begining"/>: </td>
        	<td nowrap="nowrap">
	          	<html:text property="horaInicio" size="2" disabled="true"/> :
    	        <html:text property="minutosInicio" size="2" disabled="true"/>
				<html:hidden property="horaInicio"/>
				<html:hidden property="minutosInicio"/>
	     	</td>
   		</tr>
	    <tr>
	        <td nowrap="nowrap" class="formTD"><bean:message key="property.aula.time.end"/>: </td>
    	    <td nowrap="nowrap">
        	 	<html:text property="horaFim"  size="2" disabled="true"/> : 
	            <html:text property="minutosFim"  size="2" disabled="true"/>
				<html:hidden property="horaFim"/>
				<html:hidden property="minutosFim"/>
    	    </td> 
	 	</tr>
	   <tr>
	        <td nowrap="nowrap" class="formTD"><bean:message key="property.aula.time.quinzenal"/>: </td>
    	    <td nowrap="nowrap">
        	 	<html:checkbox property="quinzenal" disabled="true"/>
        	 	<html:hidden property="quinzenal"/>
    	    </td> 
	 	</tr>
	 	<tr>
	        <td nowrap="nowrap" class="formTD"><bean:message key="property.aula.time.week"/>: </td>
    	    <td nowrap="nowrap">
        	 	<html:text property="week" size="1" disabled="true"/>
        	 	<html:hidden property="week"/>
    	    </td> 
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
	<html:submit property="operation" styleClass="inputbutton">
		<bean:message key="label.ok"/>
	</html:submit>
	<html:reset value="Limpar" styleClass="inputbutton">
		<bean:message key="label.clear"/>
	</html:reset>
</html:form>

<jsp:include page="shiftLessonList.jsp"/>