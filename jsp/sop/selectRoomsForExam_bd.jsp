<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<table width="100%" cellspacing="0">
	<tr>
		<td class="infoselected"><br /><br /><jsp:include page="examContext.jsp"/></td>
    </tr>
</table>
<br/>
<h2><bean:message key="title.exam.setRooms"/></h2>
<span class="error"><html:errors /></span>
<html:form action="/editExamRooms">
	<html:hidden property="page" value="1"/>

		<logic:present name="examDateAndTime">
			<html:hidden property="<%= SessionConstants.EXAM_DATEANDTIME %>"
						 value="<%= pageContext.findAttribute("examDateAndTime").toString() %>"/>
		</logic:present>

		<logic:present name="executionDegreeOID">
			<html:hidden property="<%= SessionConstants.EXECUTION_DEGREE_OID %>"
						 value="<%= pageContext.findAttribute("executionDegreeOID").toString() %>"/>
		</logic:present>
		<logic:present name="executionCourseOID">
			<html:hidden property="<%= SessionConstants.EXECUTION_COURSE_OID %>"
						 value="<%= pageContext.findAttribute("executionCourseOID").toString() %>"/>
		</logic:present>

		<bean:define id="oldExamSeason"
					 name="<%= SessionConstants.INFO_EXAMS_KEY %>"
					 property="infoExam.season.season"/>
		<html:hidden property="oldExamSeason"
					 value="<%= pageContext.findAttribute("oldExamSeason").toString() %>"/>

		<logic:present name="nextPage">
			<html:hidden property="input"
						 value="<%= pageContext.findAttribute(SessionConstants.NEXT_PAGE).toString() %>"/>
		</logic:present>

	<logic:present name="<%= SessionConstants.CURRICULAR_YEARS_LIST %>" scope="request">
		<logic:iterate id="year" name="<%= SessionConstants.CURRICULAR_YEARS_LIST %>" scope="request">
			<logic:equal name="year" value="1">
				<html:hidden property="<%= SessionConstants.CURRICULAR_YEARS_1 %>"
							 value="1"/>
			</logic:equal>
			<logic:equal name="year" value="2">
				<html:hidden property="<%= SessionConstants.CURRICULAR_YEARS_2 %>"
							 value="2"/>
			</logic:equal>
			<logic:equal name="year" value="3">
				<html:hidden property="<%= SessionConstants.CURRICULAR_YEARS_3 %>"
							 value="3"/>
			</logic:equal>
			<logic:equal name="year" value="4">
				<html:hidden property="<%= SessionConstants.CURRICULAR_YEARS_4 %>"
							 value="4"/>
			</logic:equal>
			<logic:equal name="year" value="5">
				<html:hidden property="<%= SessionConstants.CURRICULAR_YEARS_5 %>"
							 value="5"/>
			</logic:equal>
		</logic:iterate>
	</logic:present>

	<logic:present name="<%=SessionConstants.AVAILABLE_ROOMS%>">
		<bean:define id="roomsHashTable" name="<%=SessionConstants.AVAILABLE_ROOMS%>"/>
			<logic:iterate id="infoRoomsOfBuilding" name="roomsHashTable">
				<%int i=0;%>
				<logic:iterate id ="infoRoom" name="infoRoomsOfBuilding">
					<%if(i==0) {%>
					<strong><bean:write name="infoRoom" property="edificio"/></strong><br/>
					<%};%>
					<html:multibox property="selectedRooms">
						<bean:write name="infoRoom" property="idInternal"/>
					</html:multibox>
					<bean:write name="infoRoom" property="nome"/>&nbsp;&nbsp;
					(<bean:write name="infoRoom" property="capacidadeExame"/> lugares)<br/>
					<%i++;%>
				</logic:iterate>
				</br>
			</logic:iterate>
	</logic:present>
	
	<logic:notPresent name="<%=SessionConstants.AVAILABLE_ROOMS%>">
		No rooms available.
	</logic:notPresent>	
<br />
<html:hidden property="method" value="select"/>
<html:submit styleClass="inputbutton">
	<bean:message key="label.choose"/>
</html:submit>
<html:reset value="Limpar" styleClass="inputbutton">
	<bean:message key="label.clear"/>
</html:reset>
</html:form>