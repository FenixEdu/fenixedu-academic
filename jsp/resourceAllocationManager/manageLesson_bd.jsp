<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.SessionConstants" %>

<em><bean:message key="title.resourceAllocationManager.management"/></em>
<h2><bean:message key="link.manage.turnos"/></h2>

<p class="mbottom05">O curso seleccionado &eacute;:</p>
<jsp:include page="context.jsp"/>

<h3><bean:message key="title.manage.aulas"/></h3>

<p>
	<span class="error"><!-- Error messages go here --><html:errors /></span>
</p>


<bean:message key="message.weekdays"/>

<html:form action="/manageLesson" focus="diaSemana">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="chooseRoom"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
	<logic:present name="action">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.action" property="action"
					 value="<%= pageContext.findAttribute("action").toString() %>"/>
	</logic:present>
	<html:hidden alt="<%= SessionConstants.EXECUTION_PERIOD_OID %>" property="<%= SessionConstants.EXECUTION_PERIOD_OID %>"
				 value="<%= pageContext.findAttribute("executionPeriodOID").toString() %>"/>
	<html:hidden alt="<%= SessionConstants.EXECUTION_DEGREE_OID %>" property="<%= SessionConstants.EXECUTION_DEGREE_OID %>"
				 value="<%= pageContext.findAttribute("executionDegreeOID").toString() %>"/>
	<html:hidden alt="<%= SessionConstants.CURRICULAR_YEAR_OID %>" property="<%= SessionConstants.CURRICULAR_YEAR_OID %>"
				 value="<%= pageContext.findAttribute("curricularYearOID").toString() %>"/>
	<html:hidden alt="<%= SessionConstants.EXECUTION_COURSE_OID %>" property="<%= SessionConstants.EXECUTION_COURSE_OID %>"
				 value="<%= pageContext.findAttribute("executionCourseOID").toString() %>"/>
	<html:hidden alt="<%= SessionConstants.SHIFT_OID %>" property="<%= SessionConstants.SHIFT_OID %>"
				 value="<%= pageContext.findAttribute("shiftOID").toString() %>"/>
	<logic:present name="lessonOID">
		<html:hidden alt="<%= SessionConstants.LESSON_OID %>" property="<%= SessionConstants.LESSON_OID %>"
					 value="<%= pageContext.findAttribute("lessonOID").toString() %>"/>
	</logic:present>

	<table class="tstyle5 thlight thright">
		<tr>
	      	<th><bean:message key="property.aula.weekDay"/>:</th>
	        <td>
    	    	<html:text bundle="HTMLALT_RESOURCES" altKey="text.diaSemana" property="diaSemana" size="2"/>
	       	</td>
   		</tr>
	   	<tr>
    		<th><bean:message key="property.aula.time.begining"/>:</th>
        	<td>
	          	<html:text bundle="HTMLALT_RESOURCES" altKey="text.horaInicio" property="horaInicio"  size="2"/> :
    	        <html:text bundle="HTMLALT_RESOURCES" altKey="text.minutosInicio" property="minutosInicio" size="2"/>
	     	</td>
   		</tr>
	    <tr>
	        <th><bean:message key="property.aula.time.end"/>:</th>
    	    <td>
        	 	<html:text bundle="HTMLALT_RESOURCES" altKey="text.horaFim" property="horaFim"  size="2"/>
            		:
	            <html:text bundle="HTMLALT_RESOURCES" altKey="text.minutosFim" property="minutosFim"  size="2"/>
    	    </td> 
	 	</tr> 
	 	<tr>
	        <th><bean:message key="property.aula.time.quinzenal"/>:</th>
    	    <td>
        	 	<html:checkbox bundle="HTMLALT_RESOURCES" altKey="checkbox.quinzenal" property="quinzenal"/>
    	    </td> 
	 	</tr>	 	
	 	<tr>
	        <th><bean:message key="property.lesson.new.begin.date"/>:</th>
    	    <td>
        	 	<html:text bundle="HTMLALT_RESOURCES" altKey="text.week" property="newBeginDate" size="8" maxlength="10"/>        	 	
        	 	<b><bean:message key="label.until"/></b>
        	 	<html:text bundle="HTMLALT_RESOURCES" altKey="text.week" property="newEndDate" size="8" maxlength="10"/>
        	 	<bean:message key="property.lesson.new.begin.date.format"/>        	 	        	 
    	    </td> 
	 	</tr>
	 	<tr>
	        <th><bean:message key="property.lesson.period.dates"/>:</th>
    	    <td>
        	 	<b><bean:write name="executionDegreeLessonsStartDate"/> - <bean:write name="executionDegreeLessonsEndDate"/></b>
    	    </td> 
	 	</tr>	 		 	
	</table>

	<p>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
			<bean:message key="lable.chooseRoom"/>
		</html:submit>
		<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset" styleClass="inputbutton">
			<bean:message key="label.clear"/>
		</html:reset>
	</p>
</html:form>

<jsp:include page="shiftLessonList.jsp"/>
