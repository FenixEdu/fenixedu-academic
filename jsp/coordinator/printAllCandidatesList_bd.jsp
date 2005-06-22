<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="java.util.List" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.masterDegree.coordinator.PrintAllCandidatesListDispatchAction" %>
<h2>Imprimir lista de todos os candidatos</h2>
<br/>
<p>Esta opção permite-lhe imprimir uma lista de todos os candidatos associados ao plano curricular escolhido</p>
<p>Mostrar só : 
<!-- first option-->
<logic:notPresent name="filterNextSelectOptions">
<html:form action="/printAllCandidatesList.do?method=prepareSecondFilter" method="POST">
	<html:hidden property="degreeCurricularPlanID" value="<%= request.getParameter("degreeCurricularPlanID") %>"/>
	<html:select property="filterBy">
		<html:optionsCollection name="filterSelectOptions"/>
	</html:select>
		<html:submit styleClass="inputbutton" value="Continuar"/>
		<html:submit styleClass="inputbutton" property="export" value="Excel"/>
</html:form>
</logic:notPresent>

<!-- was anything shown yet ? -->
<logic:present name="filterNextSelectOptions">
<html:form action="/printAllCandidatesList.do?method=printAllCandidatesList">
	<html:hidden property="degreeCurricularPlanID" value="<%= request.getAttribute("degreeCurricularPlanID").toString() %>"/>
	<html:hidden property="filterBy" value="<%= request.getAttribute("filterBy").toString() %>"/>
	<logic:present name="chosenFilter">
		<i><%= request.getAttribute("chosenFilter").toString() %></i> - 
	</logic:present>
	<html:select property="filterWithValue">
		<html:optionsCollection name="filterNextSelectOptions"/>
	</html:select>
		<html:submit styleClass="inputbutton" value="Continuar"/>
		<html:submit styleClass="inputbutton" property="export" value="Excel"/>
</html:form>
</logic:present>