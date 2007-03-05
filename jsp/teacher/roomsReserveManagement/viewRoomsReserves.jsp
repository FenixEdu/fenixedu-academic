<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/collectionPager.tld" prefix="cp"%>
<%@ taglib uri="/WEB-INF/ganttDiagrams.tld" prefix="gd" %>
<html:xhtml/>

<em><bean:message key="label.teacherPortal" bundle="APPLICATION_RESOURCES"/></em>
<h2><bean:message key="rooms.reserve.title" bundle="APPLICATION_RESOURCES"/></h2>

<logic:present role="TEACHER">


	<style type="text/css">
		.tcalendar {
		border-collapse: collapse;
		/*border: 1px solid #ccc;*/
		}
		.tcalendar th {
		border: 1px solid #ccc;
		overflow: hidden;
		}
		.tcalendar td {
		border: 1px solid #ccc;
		}
		
		.tcalendar th {
		text-align: center;
		background-color: #f5f5f5;
		background-color: #f5f5f5;
		padding: 3px 4px;
		}
		.tcalendar td {
		background-color: #fff;
		padding: 0;
		}
		.tcalendar td.padded {
		padding: 2px 6px;
		border: 1px solid #ccc;
		}
		td.padded { }
		.tdbar {
		background-color: #a3d1d9;
		}
		tr.active td {
		background-color: #fefeea;
		}
		.color555 {
		color: #555;
		}
		tr.selected td {
		background-color: #fdfdde;
		}
		td.tcalendarlinks {
		padding: 0.5em 0;
		border-bottom: none;
		border-left: none;
		border-right: none;
		}
		td.tcalendarlinks span { color: #888; }
		td.tcalendarlinks span a { color: #888; }
	</style>

	<logic:messagesPresent message="true">
		<p>
			<span class="error0"><!-- Error messages go here -->
				<html:messages id="message" message="true" bundle="APPLICATION_RESOURCES">
					<bean:write name="message"/>
				</html:messages>
			</span>
		<p>
	</logic:messagesPresent>	
	
	<div class="infoop2 mtop15">
		<p><bean:message key="label.rooms.reserve.teacher.instructions" bundle="APPLICATION_RESOURCES"/></p>
	</div>
	
	<bean:define id="person" name="UserView" property="person" type="net.sourceforge.fenixedu.domain.Person"/>

	<ul class="mvert15">
		<li>
			<html:link page="/roomsReserveManagement.do?method=prepareCreateNewReserve">		
				<bean:message bundle="APPLICATION_RESOURCES" key="label.create.rooms.reserve"/>
			</html:link>
		</li>
	</ul>	

	<logic:notEmpty name="requests">	
				
		<p class="mbottom05"><b><bean:message key="label.rooms.reserve.list" bundle="APPLICATION_RESOURCES"/>:</b></p>
		<logic:notEqual name="numberOfPages" value="1">
			<p>
				<bean:message key="label.pages" bundle="APPLICATION_RESOURCES"/>:
				<cp:collectionPages url="/teacher/roomsReserveManagement.do?method=viewReserves" numberOfVisualizedPages="11" pageNumberAttributeName="pageNumber" numberOfPagesAttributeName="numberOfPages"/>
			</p>
		</logic:notEqual>
		<table class="tstyle1 thlight">			
			<tr>
				<th><bean:message key="label.rooms.reserve.instant" bundle="APPLICATION_RESOURCES"/></th>
				<th><bean:message key="label.rooms.reserve.order" bundle="APPLICATION_RESOURCES"/></th>
				<th><bean:message key="label.rooms.reserve.state" bundle="APPLICATION_RESOURCES"/></th>
				<th><bean:message key="label.rooms.reserve.periods" bundle="APPLICATION_RESOURCES"/></th>	
				<th><bean:message key="label.rooms.reserve.number.of.new.comments" bundle="APPLICATION_RESOURCES"/></th>	
			</tr>
			<logic:iterate id="punctualRequest" name="requests" type="net.sourceforge.fenixedu.domain.PunctualRoomsOccupationRequest">					
				<tr>
					<td class="nowrap smalltxt">
						<bean:write name="punctualRequest" property="presentationInstant"/>						
					</td>
					<td>
						<bean:define id="seeReserveURL">/roomsReserveManagement.do?method=seeSpecifiedRoomsReserve&amp;punctualReserveID=<bean:write name="punctualRequest" property="idInternal"/></bean:define>
						<html:link page="<%= seeReserveURL %>">
							<bean:write name="punctualRequest" property="subject"/>
						</html:link>										
					</td>	
					<td class="acenter smalltxt"><bean:message name="punctualRequest" property="currentState.name" bundle="APPLICATION_RESOURCES"/></td>					
					<td class="acenter smalltxt">											
						<logic:notEmpty name="punctualRequest" property="genericEvents">
							<bean:message key="label.yes.capitalized" bundle="SOP_RESOURCES"/>
						</logic:notEmpty>
						<logic:empty name="punctualRequest" property="genericEvents">
							-
						</logic:empty>					
					</td>	
					<td class="acenter">
						<% Integer numOfNewComments = punctualRequest.getNumberOfNewComments(person);	%>
						<%= numOfNewComments.toString() %>
					</td>					
				</tr>
			</logic:iterate>		
		</table>		
		
		<logic:notEqual name="numberOfPages" value="1">
			<bean:message key="label.pages" bundle="APPLICATION_RESOURCES"/>:
			<cp:collectionPages url="/teacher/roomsReserveManagement.do?method=viewReserves" numberOfVisualizedPages="11" pageNumberAttributeName="pageNumber" numberOfPagesAttributeName="numberOfPages"/>	
		</logic:notEqual>						
			
		<logic:notEmpty name="ganttDiagram">
			<logic:notEmpty name="ganttDiagram" property="events">			
				<p class="mbottom05"><b><bean:message key="label.rooms.reserve.gantt.diagram" bundle="APPLICATION_RESOURCES"/>:</b></p>
				<p>					
					<gd:ganttDiagram 
						 ganttDiagram="ganttDiagram" 						 
						 eventParameter="punctualReserveID"			
						 eventUrl="/teacher/roomsReserveManagement.do?method=seeSpecifiedRoomsReserve" 					 						 
						 firstDayParameter="firstDay" 					 						 
						 weeklyViewUrl="/teacher/roomsReserveManagement.do?method=viewReserves" 
						 dailyViewUrl="/teacher/roomsReserveManagement.do?method=viewReservesDailyView" 
						 monthlyViewUrl="/teacher/roomsReserveManagement.do?method=viewReservesMonthlyView"						 
						 bundle="APPLICATION_RESOURCES" 
					/>		
				</p>
				<jsp:include page="../../sop/roomsPunctualScheduling/legend.jsp" />
			</logic:notEmpty>
		</logic:notEmpty>
							
	</logic:notEmpty>
		
</logic:present>

