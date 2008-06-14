<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.SessionConstants" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3.TimeTableType" %>

<em><bean:message key="title.manage.rooms"/></em>
<h2><bean:message key="title.view.room"/></h2>

<p>
	<span class="error"><!-- Error messages go here --><html:errors /></span>  
</p>


<html:form action="/viewRoom">
	<bean:define id="infoRoomOID" name="<%= SessionConstants.ROOM%>" property="idInternal" scope="request"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="execute"/>
	<html:hidden alt="<%= SessionConstants.ROOM_OID%>" property="<%= SessionConstants.ROOM_OID%>" value="<%=infoRoomOID.toString()%>"/>
	<html:hidden alt="<%=SessionConstants.EXECUTION_PERIOD_OID%>" property="<%=SessionConstants.EXECUTION_PERIOD_OID%>" value="<%= ""+request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID)%>" />				
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
	<table class="tstyle5 thlight thright">
		<tr>
		    <th><bean:message key="label.manager.executionPeriod"/></th>
		    <td>
		        <html:select bundle="HTMLALT_RESOURCES" altKey="select.selectedExecutionPeriodOID" property="selectedExecutionPeriodOID" size="1"
		        		onchange="this.form.indexWeek.value='0';this.form.submit();">
     				<html:options property="value" 
     					labelProperty="label" 
						collection="<%= SessionConstants.LABELLIST_EXECUTIONPERIOD%>" />
				</html:select>
				<html:submit styleId="javascriptButtonID" styleClass="altJavaScriptSubmitButton" bundle="HTMLALT_RESOURCES" altKey="submit.submit">
					<bean:message key="button.submit"/>
				</html:submit>
			</td>
		</tr>
		<tr>
		    <th><bean:message key="property.week"/>:</th>
		    <td>
		        <html:select bundle="HTMLALT_RESOURCES" altKey="select.indexWeek" property="indexWeek" size="1" onchange="this.form.submit();">
     				<html:options property="value" 
     					labelProperty="label" 
						collection="<%= SessionConstants.LABELLIST_WEEKS%>" />
				</html:select>
				<html:submit styleId="javascriptButtonID2" styleClass="altJavaScriptSubmitButton" bundle="HTMLALT_RESOURCES" altKey="submit.submit">
					<bean:message key="button.submit"/>
				</html:submit>
			</td>
		</tr>
	</table>
</html:form> 

<logic:present name="<%= SessionConstants.ROOM%>" scope="request">
            <table class="tstyle4 tdcenter mvert15">
                <tr>
                    <th>
                        <bean:message key="property.room.name"/>
                    </th>
					<th>
						<bean:message key="property.room.type"/>
					</th>
                    <th>
                        <bean:message key="property.room.building"/>
                    </th>
                    <th>
                        <bean:message key="property.room.floor"/>
                    </th>
					<th>
						<bean:message key="property.room.capacity.normal"/>
					</th>
					<th>
						<bean:message key="property.room.capacity.exame"/>
					</th>
                </tr>
                <tr>
					<td>
						<bean:write name="<%= SessionConstants.ROOM%>" property="nome"/>
					</td>
                    <td>
                        <bean:write name="<%= SessionConstants.ROOM%>" property="tipo"/>
                    </td>
					<td>
						<bean:write name="<%= SessionConstants.ROOM%>" property="edificio"/>
					</td>
					<td>
						<bean:write name="<%= SessionConstants.ROOM%>" property="piso"/>
					</td>
                    <td>
                         <bean:write name="<%= SessionConstants.ROOM%>" property="capacidadeNormal"/>
                    </td>
                    <td>
                        <bean:write name="<%= SessionConstants.ROOM%>" property="capacidadeExame"/>
                    </td>
                </tr>
            </table>
	<div align="center">
		<app:gerarHorario name="<%= SessionConstants.LESSON_LIST_ATT %>"
						  type="<%= TimeTableType.SOP_ROOM_TIMETABLE %>"
		/>
	</div>

	</logic:present>

	<logic:notPresent name="<%= SessionConstants.ROOM%>" scope="request">
		<p>
			<span class="error"><!-- Error messages go here --><bean:message key="message.public.notfound.room"/></span>
		</p>
	</logic:notPresent>