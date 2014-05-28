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
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/enum" prefix="e" %>

<jsp:include page="/commons/contextLessonAndShiftAndExecutionCourseAndExecutionDegreeAndCurricularYear.jsp" />

<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>

<h2><bean:message key="title.manage.aulas"/></h2>

<p class="mbottom05">O curso seleccionado &eacute;:</p>
<jsp:include page="context.jsp"/>

<p>
<span class="error"><!-- Error messages go here -->
	<html:errors/>
</span>
</p>

<html:form action="/manageLesson" focus="nomeSala">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="createEditLesson"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="2"/>
	
	<logic:present name="action">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.action" property="action"
					 value="<%= pageContext.findAttribute("action").toString() %>"/>
	</logic:present>
    <html:hidden alt="<%= PresentationConstants.ACADEMIC_INTERVAL %>" property="<%= PresentationConstants.ACADEMIC_INTERVAL %>"
                 value="<%= pageContext.findAttribute(PresentationConstants.ACADEMIC_INTERVAL).toString() %>"/>
	<html:hidden alt="<%= PresentationConstants.EXECUTION_DEGREE_OID %>" property="<%= PresentationConstants.EXECUTION_DEGREE_OID %>"
				 value="<%= pageContext.findAttribute("executionDegreeOID").toString() %>"/>
	<html:hidden alt="<%= PresentationConstants.CURRICULAR_YEAR_OID %>" property="<%= PresentationConstants.CURRICULAR_YEAR_OID %>"
				 value="<%= pageContext.findAttribute("curricularYearOID").toString() %>"/>
	<html:hidden alt="<%= PresentationConstants.EXECUTION_COURSE_OID %>" property="<%= PresentationConstants.EXECUTION_COURSE_OID %>"
				 value="<%= pageContext.findAttribute("executionCourseOID").toString() %>"/>
	<html:hidden alt="<%= PresentationConstants.SHIFT_OID %>" property="<%= PresentationConstants.SHIFT_OID %>"
				 value="<%= pageContext.findAttribute("shiftOID").toString() %>"/>
	<logic:present name="lessonOID">
		<html:hidden alt="<%= PresentationConstants.LESSON_OID %>" property="<%= PresentationConstants.LESSON_OID %>"
					 value="<%= pageContext.findAttribute("lessonOID").toString() %>"/>
	</logic:present>
	
	<p class="mbottom05"><strong><bean:message key="label.lesson.choose.room"/></strong></p>
	
	<table class="tstyle5 thlight thright mtop05">
		<tr>
	      	<th><bean:message key="property.aula.weekDay"/>:</th>
	        <td>
    	    	<html:text bundle="HTMLALT_RESOURCES" altKey="text.diaSemana" property="diaSemana" size="2" disabled="true"/>
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.diaSemana" property="diaSemana"/>
	       	</td>
   		</tr>
	   	<tr>
    		<th><bean:message key="property.aula.time.begining"/>: </th>
        	<td>
	          	<html:text bundle="HTMLALT_RESOURCES" altKey="text.horaInicio" property="horaInicio" size="2" disabled="true"/> :
    	        <html:text bundle="HTMLALT_RESOURCES" altKey="text.minutosInicio" property="minutosInicio" size="2" disabled="true"/>
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.horaInicio" property="horaInicio"/>
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.minutosInicio" property="minutosInicio"/>
	     	</td>
   		</tr>
	    <tr>
	        <th><bean:message key="property.aula.time.end"/>: </th>
    	    <td>
        	 	<html:text bundle="HTMLALT_RESOURCES" altKey="text.horaFim" property="horaFim"  size="2" disabled="true"/> : 
	            <html:text bundle="HTMLALT_RESOURCES" altKey="text.minutosFim" property="minutosFim"  size="2" disabled="true"/>
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.horaFim" property="horaFim"/>
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.minutosFim" property="minutosFim"/>
    	    </td> 
	 	</tr>
	   <tr>
	        <th><bean:message key="property.aula.time.quinzenal"/>: </th>
    	    <td>
        	 	<html:checkbox bundle="HTMLALT_RESOURCES" altKey="checkbox.quinzenal" property="quinzenal" disabled="true"/>
        	 	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.quinzenal" property="quinzenal"/>
    	    </td> 
	 	</tr>	 	
	 	<tr>
	        <th><bean:message key="property.lesson.new.begin.date"/>: </th>
    	    <td>
        	 	<html:text bundle="HTMLALT_RESOURCES" altKey="text.new.begin.date" property="newBeginDate" size="8" disabled="true"/>
        	 	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.week" property="newBeginDate"/>
        	 	<bean:message key="label.until"/>
        	 	<html:text bundle="HTMLALT_RESOURCES" altKey="text.new.end.date" property="newEndDate" size="8" disabled="true"/>
        	 	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.week" property="newEndDate"/>
        	 	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.week" property="createLessonInstances"/>
    	    </td> 
	 	</tr>	   
	 	<tr>
    		<th><bean:message key="property.aula.sala"/>: </th> 
       		<td>
     			<html:select bundle="HTMLALT_RESOURCES" altKey="select.nomeSala" property="nomeSala" size="1" >
            	   	<html:options collection="listaSalas" property="value" labelProperty="label"/>
	           	</html:select>
    	  	</td>
	  	</tr>
	</table>
	
	<p>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.operation" property="operation" styleClass="inputbutton">
			<bean:message key="label.ok"/>
		</html:submit>
		<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset" value="Limpar" styleClass="inputbutton">
			<bean:message key="label.clear"/>
		</html:reset>
	</p>
</html:form>

<jsp:include page="shiftLessonList.jsp"/>