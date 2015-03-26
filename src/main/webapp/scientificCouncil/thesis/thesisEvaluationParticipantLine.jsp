<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Academic.

    FenixEdu Academic is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Academic is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@page import="org.fenixedu.academic.domain.thesis.ThesisParticipationType"%>
<%@page import="org.fenixedu.academic.domain.thesis.ThesisEvaluationParticipant"%>
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<html:xhtml/>

<%
	final ThesisEvaluationParticipant participant = (ThesisEvaluationParticipant) request.getAttribute("thesisEvaluationParticipant");
	final ThesisParticipationType type = participant.getType();
%>
<tr>
	<td>
		<html:link action="<%= "/manageSecondCycleThesis.do?method=editThesisEvaluationParticipant&amp;thesisEvaluationParticipantOid=" + participant.getExternalId() %>">
			<bean:message bundle="ENUMERATION_RESOURCES" key="<%= "ThesisParticipationType." + participant.getType().getName() %>"/>
		</html:link>
	</td>
	<td>
		<logic:notEmpty name="thesisEvaluationParticipant" property="person">
			<bean:define id="url" type="java.lang.String">/user/photo/<bean:write name="thesisEvaluationParticipant" property="person.username"/></bean:define>
			<img src="<%= request.getContextPath() + url %>"/>
		</logic:notEmpty>
	</td>	
	<td>
		<%= participant.getName() %> <logic:notEmpty name="thesisEvaluationParticipant" property="person">( <%= participant.getPerson().getUsername() %> )</logic:notEmpty>
	</td>
	<td>
		<%= participant.getCategory() %>
	</td>
	<td>
		<%= participant.getAffiliation() %>
	</td>
	<td>
		<%= participant.getPercentageDistribution() %> %
	</td>
	<td>
		<bean:define id="confirm" type="java.lang.String">return confirm('<bean:message key="message.confirm.remove" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/>')</bean:define>
		<html:link page="<%= "/manageSecondCycleThesis.do?method=removeThesisEvaluationParticipant&amp;thesisEvaluationParticipantOid=" + participant.getExternalId() %>"
				style="border-bottom: none;"
				onclick="<%= confirm %>">
			<img src="<%=request.getContextPath()%>/images/delete.gif"/>
		</html:link>
	</td>
</tr>
