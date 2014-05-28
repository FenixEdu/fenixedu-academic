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

<jsp:include page="../context.jsp"/>

<h2><bean:message key="label.action.visualize"/></h2>

<p><span class="error"><!-- Error messages go here --><html:errors /></span></p>
<p>
    <bean:define id="candidateList" name="masterDegreeCandidateList" scope="request" />
    <bean:define id="degreeCurricularPlanID" name="degreeCurricularPlanID" scope="request" />        
    <bean:define id="path" type="java.lang.String" scope="request" property="path" name="<%= Globals.MAPPING_KEY %>" />
	<bean:define id="link">
		<bean:write name="path"/>.do?method=chooseCandidate<%= "&" %>page=0<%= "&" %>degreeCurricularPlanID=<%= degreeCurricularPlanID %>
	</bean:define>
    <span class="emphasis"><%= ((List) candidateList).size()%></span> <bean:message key="label.masterDegree.administrativeOffice.candidatesFound"/>        
    <% if (((List) candidateList).size() != 0) { %>
</p>    
	    <table class="tstyle4">
    		<tr>
	    		<th><bean:message key="label.name" /></th>
				<th><bean:message key="label.masterDegree.administrativeOffice.specialization" /></th>
				<th><bean:message key="label.masterDegree.administrativeOffice.candidateSituation" /></th>
				<th><bean:message key="label.masterDegree.administrativeOffice.situationDate" /></th>
			</tr>
    		<logic:iterate id="candidate" name="candidateList" >
    			<bean:define id="candidateLink">
    				<bean:write name="link"/>&candidateID=<bean:write name="candidate" property="externalId"/>
    			</bean:define>
    			<tr>
    				<td>
      				    <html:link page='<%= pageContext.findAttribute("candidateLink").toString() %>'>
    						<bean:write name="candidate" property="infoPerson.nome" />
    					</html:link>
    				</td>
    				<td><bean:message name="candidate" property="specialization.name" bundle="ENUMERATION_RESOURCES" /></td>
    				<td><bean:write name="candidate" property="infoCandidateSituation.situation" /></td>
		            <logic:present name="candidate" property="infoCandidateSituation.date" >
	    	            <bean:define id="date" name="candidate" property="infoCandidateSituation.date" />
						<td><%= Data.format2DayMonthYear((Date) date) %></td>   
					</logic:present>
    			</tr>
    		</logic:iterate>
          </table>
        <% } %>
