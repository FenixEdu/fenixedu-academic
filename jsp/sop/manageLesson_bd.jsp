<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<table width="100%" cellspacing="0">
	<tr>
    	<td class="infoselected"><p>O curso seleccionado &eacute;:</p>
			<strong><jsp:include page="context.jsp"/></strong>
        </td>
  	</tr>
</table>
<h2><bean:message key="title.manage.aulas"/></h2>
<br />
<span class="error"><html:errors/></span>

<br />
<bean:message key="message.weekdays"/>
<br />
<html:form action="/manageLesson" focus="diaSemana">
	<html:hidden property="method" value="chooseRoom"/>
	<html:hidden property="page" value="1"/>
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
    	    	<html:text property="diaSemana"  size="2"/>
	       	</td>
   		</tr>
	   	<tr>
    		<td nowrap="nowrap" class="formTD"><bean:message key="property.aula.time.begining"/>: </td>
        	<td nowrap="nowrap">
	          	<html:text property="horaInicio"  size="2"/> :
    	        <html:text property="minutosInicio" size="2"/>
	     	</td>
   		</tr>
	    <tr>
	        <td nowrap="nowrap" class="formTD"><bean:message key="property.aula.time.end"/>: </td>
    	    <td nowrap="nowrap">
        	 	<html:text property="horaFim"  size="2"/>
            		:
	            <html:text property="minutosFim"  size="2"/>
    	    </td> 
	 	</tr> 
	 	<tr>
	        <td nowrap="nowrap" class="formTD"><bean:message key="property.aula.time.quinzenal"/>: </td>
    	    <td nowrap="nowrap">
        	 	<html:checkbox property="quinzenal"/>
    	    </td> 
	 	</tr>
	 	<tr>
	        <td nowrap="nowrap" class="formTD"><bean:message key="property.aula.time.week"/>: </td>
    	    <td nowrap="nowrap">
        	 	<html:text property="week" size="1"/>
    	    </td> 
	 	</tr>
	</table>
	<br />
	<html:submit styleClass="inputbutton">
		<bean:message key="lable.chooseRoom"/>
	</html:submit>
	<html:reset styleClass="inputbutton">
		<bean:message key="label.clear"/>
	</html:reset>
</html:form>

<jsp:include page="shiftLessonList.jsp"/>
