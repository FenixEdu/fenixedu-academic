<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="org.apache.struts.action.Action" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Date" %>
<%@ page import="Util.Data" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>


    <table width="100%" border="0" cellpadding="0" cellspacing="0">
    	<tr align="center">
    		<td bgcolor="#FFFFFF" class="infoselected"><p>
    			<strong><jsp:include page="../context.jsp"/></strong>
             </td>
        </tr>
    </table>


    <br> <br>
    <span class="error"><html:errors/></span>
       
    <span class="error"><html:errors/></span>
    <bean:define id="candidateList" name="<%= SessionConstants.MASTER_DEGREE_CANDIDATE_LIST %>" scope="session" />
        
    <bean:define id="path" type="java.lang.String" scope="request" property="path" name="<%= Action.MAPPING_KEY %>" />
	<bean:define id="link">
		<bean:write name="path"/>.do?method=chooseCandidate<%= "&" %>page=0<%= "&" %>candidatePosition=
	</bean:define>
    
    <%= ((List) candidateList).size()%> <bean:message key="label.masterDegree.administrativeOffice.candidatesFound"/>        
    <% if (((List) candidateList).size() != 0) { %>
    
	    <table>
    		<tr>
				<td><bean:message key="label.name" /></td>
				<td><bean:message key="label.masterDegree.administrativeOffice.specialization" /></td>
				<td><bean:message key="label.masterDegree.administrativeOffice.candidateSituation" /></td>
				<td><bean:message key="label.masterDegree.administrativeOffice.situationDate" /></td>
			</tr>
    		
        
    		<logic:iterate id="candidate" name="candidateList" indexId="indexCandidate">
    			<bean:define id="candidateLink">
    				<bean:write name="link"/><bean:write name="indexCandidate"/>
    			</bean:define>
    			<tr>
    				<td>
      				    <html:link page='<%= pageContext.findAttribute("candidateLink").toString() %>'>
    						<bean:write name="candidate" property="infoPerson.nome" />
    					</html:link>
    				</td>
    				<td><bean:write name="candidate" property="specialization" /></td>
    				<td><bean:write name="candidate" property="infoCandidateSituation.situation" /></td>
		            <logic:present name="candidate" property="infoCandidateSituation.date" >
	    	            <bean:define id="date" name="candidate" property="infoCandidateSituation.date" />
						<td><%= Data.format2DayMonthYear((Date) date) %></td>   
					</logic:present>
    			</tr>
    		</logic:iterate>
          </table>
        <% } %>
