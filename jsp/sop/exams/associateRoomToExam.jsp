<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="org.apache.struts.action.Action" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>

Exame de
	<logic:present name="<%=SessionConstants.LIST_EXECUTION_COURSE_NAMES%>">		
		<bean:define id="executionCourses" name="<%=SessionConstants.LIST_EXECUTION_COURSE_NAMES%>"/>	
		<bean:size id="lenght" name="executionCourses"/>
	
				<logic:iterate indexId="index" id ="executionCourseName" name="executionCourses">
					<bean:write name="executionCourseName"/>
					<logic:notEqual name="lenght" value="<%= "" + (index.intValue() + 1) %>"> 
					/
					</logic:notEqual>
				</logic:iterate>
	</logic:present>
dia <bean:write name="<%=SessionConstants.DATE%>"/>
 das <bean:write name="<%=SessionConstants.START_TIME%>"/>
 às <bean:write name="<%=SessionConstants.END_TIME%>"/>
<br/>

<html:form action="/associateRoomToExam">
	<span class="error"><html:errors /></span>
	Ordenar por: 
	<html:radio property="sortBy" value="capacity" onclick="this.form.method.value='prepare';this.form.submit();"/> Capacidade
	<html:radio property="sortBy" value="building" onclick="this.form.method.value='prepare';this.form.submit();"/> Edificio
	<html:radio property="sortBy" value="tipoSala" onclick="this.form.method.value='prepare';this.form.submit();"/> Tipo de Sala
	
	<html:hidden property="page" value="1"/>
	<html:hidden property="method" value="choose"/>
	
	<html:hidden property="<%= SessionConstants.EXECUTION_PERIOD_OID %>"
					 value="<%= pageContext.findAttribute(SessionConstants.EXECUTION_PERIOD_OID).toString() %>"/>
	<html:hidden property="<%= SessionConstants.EXECUTION_DEGREE_OID %>"
					 value="<%= pageContext.findAttribute(SessionConstants.EXECUTION_DEGREE_OID).toString() %>"/>
	<html:hidden property="<%= SessionConstants.CURRICULAR_YEAR_OID %>"
					 value="<%= pageContext.findAttribute(SessionConstants.CURRICULAR_YEAR_OID).toString() %>"/>
	<html:hidden property="<%= SessionConstants.EXECUTION_COURSE_OID %>"
					 value="<%= pageContext.findAttribute("executionCourseOID").toString() %>"/>
	
	<html:hidden property="day" />
	<html:hidden property="month" />
	<html:hidden property="year" />
	<html:hidden property="beginningHour" />
	<html:hidden property="beginningMinute" />
	<html:hidden property="endHour" />
	<html:hidden property="endMinute" />
	<html:hidden property="season" />
	
	<h2>Salas</h2>
	<logic:present name="<%=SessionConstants.AVAILABLE_ROOMS%>">		
		<bean:define id="availableRooms" name="<%=SessionConstants.AVAILABLE_ROOMS%>"/>
				<logic:iterate id ="infoRoom" name="availableRooms">
					<html:multibox property="rooms">
						<bean:write name="infoRoom" property="idInternal"/>
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
			<html:hidden property="scopes" value="<%= pageContext.findAttribute("scopeID").toString() %>" />
	</logic:iterate>
	
	<logic:iterate indexId="i" id="executionCourseID" name="executionCoursesArray">
			<html:hidden property="executionCourses" value="<%= pageContext.findAttribute("executionCourseID").toString() %>" />
	</logic:iterate>
	
	<logic:notPresent name="<%=SessionConstants.AVAILABLE_ROOMS%>">
		Não existem salas disponíveis.
	</logic:notPresent>

<logic:present name="<%= SessionConstants.EXAM_OID %>">
	<html:hidden property="<%= SessionConstants.EXAM_OID %>"
		value="<%= pageContext.findAttribute(SessionConstants.EXAM_OID).toString() %>"/>
</logic:present>

<html:submit styleClass="inputbutton">
	<bean:message key="label.choose"/>
</html:submit>
<html:reset value="Repor" styleClass="inputbutton">
	<bean:message key="label.repor"/>
</html:reset>
</html:form>


