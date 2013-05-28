<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="org.apache.struts.Globals" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Date" %>
<%@ page import="net.sourceforge.fenixedu.util.Data" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
    <span class="error"><!-- Error messages go here --><html:errors /></span>
    	<bean:define id="candidateList" name="<%= PresentationConstants.MASTER_DEGREE_CANDIDATE_LIST %>" />
    	<bean:define id="findQuery" name="<%= PresentationConstants.MASTER_DEGREE_CANDIDATE_QUERY %>" />
    	<bean:define id="title" name="<%= PresentationConstants.MASTER_DEGREE_CANDIDATE_ACTION %>" /> 
    	<bean:define id="path" type="java.lang.String" scope="request" property="path" name="<%= Globals.MAPPING_KEY %>" />
		<bean:define id="link">
		<bean:write name="path"/>.do?method=chooseCandidate<%= "&" %>personID=
		</bean:define>
		
	<em><bean:message key="title.masterDegree.administrativeOffice"/></em>
	<h2><bean:message name="title"/></h2>
	
    	<p class="mtop15"><b>Critérios de procura:</b>
    	<p><bean:write name="findQuery" filter="false"/></p>

    	<p class="mtop2 mbottom05">
    		<%= ((List) candidateList).size()%> <bean:message key="label.masterDegree.administrativeOffice.candidatesFound"/>      
    		<% if (((List) candidateList).size() != 0) { %>
    	</p>  

    <table class="tstyle2">
    	<tr>
			<th><bean:message key="label.candidate.name" /></th>
			<th><bean:message key="label.candidate.number" /></th>
			<th><bean:message key="label.candidate.degree" /></th>
			<th><bean:message key="label.candidate.specialization" /></th>
			<th><bean:message key="label.candidate.infoCandidateSituation" /></th>
			<th><bean:message key="label.candidate.infoCandidateSituationDate" /></th>	
		</tr>
    		<logic:iterate id="candidate" name="candidateList" indexId="indexCandidate">
    			<bean:define id="candidateLink">
    				<bean:write name="link"/><bean:write name="candidate" property="infoPerson.externalId"/><%= "&" %>candidateID=<bean:write name="candidate" property="externalId"/>
    			</bean:define>
    	<tr>
    		<td>
      			<html:link page='<%= pageContext.findAttribute("candidateLink").toString() %>'>
    				<bean:write name="candidate" property="infoPerson.nome" />
    			</html:link>
    		</td>
    		<td><bean:write name="candidate" property="candidateNumber" /></td>
    		<td><bean:write name="candidate" property="infoExecutionDegree.infoDegreeCurricularPlan.infoDegree.nome" /></td>
    		<td><bean:message name="candidate" property="specialization.name" bundle="ENUMERATION_RESOURCES"/></td>
    		<td><bean:write name="candidate" property="infoCandidateSituation.situation" /></td>
		    	<logic:present name="candidate" property="infoCandidateSituation.date" >
	   	         	<bean:define id="date" name="candidate" property="infoCandidateSituation.date" />
			<td><%= Data.format2DayMonthYear((Date) date) %></td>  
				</logic:present>
    	</tr>
    		</logic:iterate>
  	</table>
        <% } %>