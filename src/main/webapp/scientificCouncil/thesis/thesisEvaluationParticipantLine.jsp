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
<%@page import="net.sourceforge.fenixedu.domain.thesis.ThesisParticipationType"%>
<%@page import="net.sourceforge.fenixedu.domain.thesis.ThesisEvaluationParticipant"%>
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
		<bean:define id="url" type="java.lang.String">/publico/retrievePersonalPhoto.do?method=retrieveByUUID&amp;uuid=<bean:write name="thesisEvaluationParticipant" property="person.username"/></bean:define>
		<img src="<%= request.getContextPath() + url %>"/>
	</td>	
	<td>
		<%= participant.getPersonNameWithLogin() %>
	</td>
	<td>
		<%= participant.getCategory() %>
	</td>
	<td>
		<%= participant.getAffiliation() %>
	</td>
	<td>
		<%
			if (type == ThesisParticipationType.ORIENTATOR) {
		%>
				<%= participant.getThesis().getOrientatorCreditsDistribution() %> %
		<%
			} else if (type == ThesisParticipationType.COORIENTATOR) {
		%>
				<%= participant.getThesis().getCoorientatorCreditsDistribution() %> %
		<%
			}
		%>
	</td>
	<td>
		<bean:define id="confirm" type="java.lang.String">return confirm('<bean:message key="message.confirm.remove" bundle="SCIENTIFIC_COUNCIL_RESOURCES"/>')</bean:define>	
		<html:link page="<%= "/manageSecondCycleThesis.do?method=removeThesisEvaluationParticipant&amp;thesisEvaluationParticipantOid=" + participant.getExternalId() %>"
				style="border-bottom: none;"
				onclick="<%= confirm %>">
			<img src="<%= request.getContextPath() + "/images/transitional/error01.gif" %>"/>
		</html:link>
	</td>
</tr>
