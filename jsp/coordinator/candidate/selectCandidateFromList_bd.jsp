<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="org.apache.struts.Globals" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Date" %>
<%@ page import="Util.Data" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<jsp:include page="../context.jsp"/>
<p><span class="error"><html:errors/></span></p>
<p>
    <bean:define id="candidateList" name="<%= SessionConstants.MASTER_DEGREE_CANDIDATE_LIST %>" scope="session" />        
    <bean:define id="path" type="java.lang.String" scope="request" property="path" name="<%= Globals.MAPPING_KEY %>" />
	<bean:define id="link">
		<bean:write name="path"/>.do?method=chooseCandidate<%= "&" %>page=0<%= "&" %>candidatePosition= 
	</bean:define>
    <span class="emphasis"><%= ((List) candidateList).size()%></span> <bean:message key="label.masterDegree.administrativeOffice.candidatesFound"/>        
    <% if (((List) candidateList).size() != 0) { %>
</p>    
	    <table>
    		<tr>
	    		<td class="listClasses-header"><bean:message key="label.name" /></td>
				<td class="listClasses-header"><bean:message key="label.masterDegree.administrativeOffice.specialization" /></td>
				<td class="listClasses-header"><bean:message key="label.masterDegree.administrativeOffice.candidateSituation" /></td>
				<td class="listClasses-header"><bean:message key="label.masterDegree.administrativeOffice.situationDate" /></td>
			</tr>
    		<logic:iterate id="candidate" name="candidateList" indexId="indexCandidate">
    			<bean:define id="candidateLink">
    				<bean:write name="link"/><bean:write name="indexCandidate"/>
    			</bean:define>
    			<tr>
    				<td class="listClasses">
      				    <html:link page='<%= pageContext.findAttribute("candidateLink").toString() %>'>
    						<bean:write name="candidate" property="infoPerson.nome" />
    					</html:link>
    				</td>
    				<td class="listClasses"><bean:write name="candidate" property="specialization" /></td>
    				<td class="listClasses"><bean:write name="candidate" property="infoCandidateSituation.situation" /></td>
		            <logic:present name="candidate" property="infoCandidateSituation.date" >
	    	            <bean:define id="date" name="candidate" property="infoCandidateSituation.date" />
						<td class="listClasses"><%= Data.format2DayMonthYear((Date) date) %></td>   
					</logic:present>
    			</tr>
    		</logic:iterate>
          </table>
        <% } %>
