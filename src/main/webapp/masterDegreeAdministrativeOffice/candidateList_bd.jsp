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
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
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