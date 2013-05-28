<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3.TimeTableType" %>

<em><bean:message key="title.manage.rooms"/></em>
<h2><bean:message key="title.view.room"/></h2>

<p>
	<span class="error"><!-- Error messages go here --><html:errors /></span>  
</p>

<fr:form action="/viewRoom.do?method=academicIntervalPostBack" >
    <fr:edit id="roomOccupationWeekBean" schema="roomOccupationWeek.choose" name="roomOccupationWeekBean">
        <fr:layout name="tabular">
            <fr:property name="classes" value="tstyle5 thlight thright mtop05"/>
            <fr:property name="columnClasses" value=",,tdclear tderror1"/>
        </fr:layout>
        <fr:destination name="academicIntervalPostBack" path="/viewRoom.do?method=academicIntervalPostBack"/> 
        <fr:destination name="weekPostBack" path="/viewRoom.do?method=weekPostBack"/>
    </fr:edit>
</fr:form>

<bean:define id="execution_period_oid" name="roomOccupationWeekBean" property="executionSemester.externalId" scope="request" />

<logic:present name="<%= PresentationConstants.ROOM%>" scope="request">
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
						<bean:write name="<%= PresentationConstants.ROOM%>" property="nome"/>
					</td>
                    <td>
                        <bean:write name="<%= PresentationConstants.ROOM%>" property="tipo"/>
                    </td>
					<td>
						<bean:write name="<%= PresentationConstants.ROOM%>" property="edificio"/>
					</td>
					<td>
						<bean:write name="<%= PresentationConstants.ROOM%>" property="piso"/>
					</td>
                    <td>
                         <bean:write name="<%= PresentationConstants.ROOM%>" property="capacidadeNormal"/>
                    </td>
                    <td>
                        <bean:write name="<%= PresentationConstants.ROOM%>" property="capacidadeExame"/>
                    </td>
                </tr>
            </table>
	<div align="center">
		<app:gerarHorario name="<%= PresentationConstants.LESSON_LIST_ATT %>"
						  type="<%= TimeTableType.SOP_ROOM_TIMETABLE %>"
		/>
	</div>

</logic:present>

<logic:notPresent name="<%= PresentationConstants.ROOM%>" scope="request">
	<p>
		<span class="error"><!-- Error messages go here --><bean:message key="message.public.notfound.room"/></span>
	</p>
</logic:notPresent>
 