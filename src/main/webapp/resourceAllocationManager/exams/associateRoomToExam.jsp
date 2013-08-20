<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="org.apache.struts.Globals" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>

Exame de
	<logic:present name="<%=PresentationConstants.LIST_EXECUTION_COURSE_NAMES%>">		
		<bean:define id="executionCourses" name="<%=PresentationConstants.LIST_EXECUTION_COURSE_NAMES%>"/>	
		<bean:size id="lenght" name="executionCourses"/>
	
				<logic:iterate indexId="index" id ="executionCourseName" name="executionCourses">
					<bean:write name="executionCourseName"/>
					<logic:notEqual name="lenght" value="<%= "" + (index.intValue() + 1) %>"> 
					/
					</logic:notEqual>
				</logic:iterate>
	</logic:present>
dia <bean:write name="<%=PresentationConstants.EXAM_DATEANDTIME_STR%>"/>
 <br/>

<html:form action="/associateRoomToExam">
	<span class="error"><!-- Error messages go here --><html:errors /></span>
	Ordenar por: 
	<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.sortBy" property="sortBy" value="capacity" onclick="this.form.method.value='prepare';this.form.submit();"/> Capacidade
	<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.sortBy" property="sortBy" value="building" onclick="this.form.method.value='prepare';this.form.submit();"/> Edificio
	<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.sortBy" property="sortBy" value="tipoSala" onclick="this.form.method.value='prepare';this.form.submit();"/> Tipo de Sala
	
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="choose"/>
	
	<html:hidden alt="<%= PresentationConstants.EXECUTION_PERIOD_OID %>" property="<%= PresentationConstants.EXECUTION_PERIOD_OID %>"
					 value="<%= pageContext.findAttribute(PresentationConstants.EXECUTION_PERIOD_OID).toString() %>"/>
	<html:hidden alt="<%= PresentationConstants.EXECUTION_DEGREE_OID %>" property="<%= PresentationConstants.EXECUTION_DEGREE_OID %>"
					 value="<%= pageContext.findAttribute(PresentationConstants.EXECUTION_DEGREE_OID).toString() %>"/>
	<html:hidden alt="<%= PresentationConstants.CURRICULAR_YEAR_OID %>" property="<%= PresentationConstants.CURRICULAR_YEAR_OID %>"
					 value="<%= pageContext.findAttribute(PresentationConstants.CURRICULAR_YEAR_OID).toString() %>"/>
	<html:hidden alt="<%= PresentationConstants.EXECUTION_COURSE_OID %>" property="<%= PresentationConstants.EXECUTION_COURSE_OID %>"
					 value="<%= pageContext.findAttribute("executionCourseOID").toString() %>"/>
	<logic:present name="<%= PresentationConstants.DATE %>">
		<html:hidden alt="<%= PresentationConstants.DATE %>" property="<%= PresentationConstants.DATE %>"
						 value="<%= pageContext.findAttribute("date").toString() %>"/>
		<html:hidden alt="<%= PresentationConstants.START_TIME %>" property="<%= PresentationConstants.START_TIME %>"
						 value="<%= pageContext.findAttribute("start_time").toString() %>"/>
		<html:hidden alt="<%= PresentationConstants.END_TIME %>" property="<%= PresentationConstants.END_TIME %>"
						 value="<%= pageContext.findAttribute("end_time").toString() %>"/>
	</logic:present>
	
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.day" property="day" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.month" property="month" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.year" property="year" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.beginningHour" property="beginningHour" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.beginningMinute" property="beginningMinute" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.endHour" property="endHour" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.endMinute" property="endMinute" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.season" property="season" />
	
	<h2>Salas</h2>
	<logic:present name="<%=PresentationConstants.AVAILABLE_ROOMS%>">		
		<bean:define id="availableRooms" name="<%=PresentationConstants.AVAILABLE_ROOMS%>"/>
				<logic:iterate id ="infoRoom" name="availableRooms">
					<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.rooms" property="rooms">
						<bean:write name="infoRoom" property="externalId"/>
					</html:multibox>
					<bean:write name="infoRoom" property="nome"/>&nbsp;&nbsp;
					( <bean:write name="infoRoom" property="capacidadeExame"/> lugares,
					 <bean:write name="infoRoom" property="edificio"/>,
					 <bean:define id="tipo" name="infoRoom" property="tipo"/>
					 <logic:equal name="tipo" value="A">Anfiteatro</logic:equal>
					 <logic:equal name="tipo" value="P">Plana</logic:equal>
					 <logic:equal name="tipo" value="L">Laboratório</logic:equal>)<br/>
				</logic:iterate>
				<br/>
	</logic:present>

	<logic:iterate indexId="i" id="scopeID" name="scopes">
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.scopes" property="scopes" value="<%= pageContext.findAttribute("scopeID").toString() %>" />
	</logic:iterate>
	
	<logic:iterate indexId="i" id="executionCourseID" name="executionCoursesArray">
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionCourses" property="executionCourses" value="<%= pageContext.findAttribute("executionCourseID").toString() %>" />
	</logic:iterate>
	
	<logic:notPresent name="<%=PresentationConstants.AVAILABLE_ROOMS%>">
		Não existem salas disponíveis.
	</logic:notPresent>

<logic:present name="<%= PresentationConstants.EXAM_OID %>">
	<html:hidden alt="<%= PresentationConstants.EXAM_OID %>" property="<%= PresentationConstants.EXAM_OID %>"
		value="<%= pageContext.findAttribute(PresentationConstants.EXAM_OID).toString() %>"/>
</logic:present>

<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
	<bean:message key="label.choose"/>
</html:submit>
<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset" value="Repor" styleClass="inputbutton">
	<bean:message key="label.repor"/>
</html:reset>
</html:form>


