<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/ganttDiagrams.tld" prefix="gd" %>
<html:xhtml/>

<h2><bean:message key="rooms.punctual.scheduling.title" bundle="SOP_RESOURCES"/></h2>

<logic:present role="TIME_TABLE_MANAGER">

	<style>
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
	background-color: #fed;
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
				<html:messages id="message" message="true" bundle="SOP_RESOURCES">
					<bean:write name="message"/>
				</html:messages>
			</span>
		<p>
	</logic:messagesPresent>	
	
	
	<html:link page="/roomsPunctualScheduling.do?method=prepareCreate">		
		<bean:message bundle="SOP_RESOURCES" key="label.create.room.punctual.scheduling"/>
	</html:link>
	
	<logic:notEmpty name="ganttDiagram">
		<logic:notEmpty name="ganttDiagram" property="events">
			<p>
				<gd:ganttDiagram 
					 ganttDiagram="ganttDiagram" 
					 eventParameter="genericEventID"			
					 eventUrl="/sop/roomsPunctualScheduling.do?method=prepareView" 					 
					 firstDayParameter="firstDay" 					 
					 weeklyViewUrl="/sop/roomsPunctualScheduling.do?method=prepare" 
					 dailyViewUrl="/sop/roomsPunctualScheduling.do?method=prepareViewDailyView" 
					 monthlyViewUrl="/sop/roomsPunctualScheduling.do?method=prepareViewMonthlyView"
					 bundle="SOP_RESOURCES" 
				/>		
			</p>
			
			<p><bean:message bundle="SOP_RESOURCES" key="label.legend"/>:</p>
			[C] -> <bean:message bundle="SOP_RESOURCES" key="label.continuous"/>
			[D] -> <bean:message bundle="SOP_RESOURCES" key="label.daily"/>
			[S] -> <bean:message bundle="SOP_RESOURCES" key="label.weekly"/>
			[Q] -> <bean:message bundle="SOP_RESOURCES" key="label.biweekly"/>
						
		</logic:notEmpty>			
	</logic:notEmpty>	
	
</logic:present>


