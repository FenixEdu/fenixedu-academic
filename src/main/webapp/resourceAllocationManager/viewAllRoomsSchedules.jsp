<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3.TimeTableType" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/struts-example-1.0" prefix="app" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<link href="${pageContext.request.contextPath}/CSS/dotist_timetables.css" rel="stylesheet" type="text/css" />

<c:forEach var="bean" items="${beans}">

	<div class="alert alert-warning">
		<strong>${academicInterval.pathName}</strong>
	</div>

	<table class="table table-bordered">
		<thead>
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
		</thead>
		<tr>
			<td class="listClasses">
				${bean.room.nome}
			</td>
			<td class="listClasses">
				${bean.room.roomClassification.name}
			</td>
			<td class="listClasses">
				${bean.room.spaceBuilding.name}
			</td>
			<td class="listClasses">
				${bean.room.piso}
			</td>
			<td class="listClasses">
				${bean.room.capacidadeNormal}
			</td>
			<td class="listClasses">
				${bean.room.capacidadeExame}
			</td>
		</tr>
	</table>
	<c:set var="lessons" value="${bean.lessons}" />
	<div align="center"><app:gerarHorario name="lessons" type="<%= TimeTableType.SOP_ROOM_TIMETABLE %>"/></div>

	<br />
	<br />
</c:forEach>

<c:if test="${beans.size() == 0}">
	<span class="error"><bean:message key="message.rooms.notExisting"/></span>
</c:if>

