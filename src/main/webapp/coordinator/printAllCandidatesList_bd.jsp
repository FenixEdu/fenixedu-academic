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
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ page import="java.util.List" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.masterDegree.coordinator.PrintAllCandidatesListDispatchAction" %>

<jsp:include page="/coordinator/context.jsp" />

<h2>Imprimir Lista de Candidatos</h2>


<p class="mvert15">Esta opção permite-lhe imprimir uma lista de todos os candidatos associados ao plano curricular escolhido</p>

<!-- first option-->
<logic:notPresent name="filterNextSelectOptions">
<html:form action="/printAllCandidatesList.do?method=prepareSecondFilter" method="post">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeCurricularPlanID" property="degreeCurricularPlanID" value="<%= request.getParameter("degreeCurricularPlanID") %>"/>
	<html:select bundle="HTMLALT_RESOURCES" altKey="select.filterBy" property="filterBy">
		<html:optionsCollection name="filterSelectOptions"/>
	</html:select>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton" value="Continuar"/>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.export" styleClass="inputbutton" property="export" value="Excel"/>
</html:form>
</logic:notPresent>

<!-- was anything shown yet ? -->
<logic:present name="filterNextSelectOptions">
<html:form action="/printAllCandidatesList.do?method=printAllCandidatesList">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeCurricularPlanID" property="degreeCurricularPlanID" value="<%= request.getAttribute("degreeCurricularPlanID").toString() %>"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.filterBy" property="filterBy" value="<%= request.getAttribute("filterBy").toString() %>"/>
	<logic:present name="chosenFilter">
		<i><%= request.getAttribute("chosenFilter").toString() %></i> - 
	</logic:present>
	<html:select bundle="HTMLALT_RESOURCES" altKey="select.filterWithValue" property="filterWithValue">
		<html:optionsCollection name="filterNextSelectOptions"/>
	</html:select>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton" value="Continuar"/>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.export" styleClass="inputbutton" property="export" value="Excel"/>
</html:form>
</logic:present>