<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="org.apache.struts.Globals" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Date" %>
<%@ page import="net.sourceforge.fenixedu.util.Data" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
    <span class="error"><html:errors/></span>
    	<bean:define id="candidateList" name="<%= SessionConstants.MASTER_DEGREE_CANDIDATE_LIST %>" scope="session" />
    	<bean:define id="findQuery" name="<%= SessionConstants.MASTER_DEGREE_CANDIDATE_QUERY %>" scope="session" />
    	<bean:define id="title" name="<%= SessionConstants.MASTER_DEGREE_CANDIDATE_ACTION %>" scope="session" /> 
    	<bean:define id="path" type="java.lang.String" scope="request" property="path" name="<%= Globals.MAPPING_KEY %>" />
		<bean:define id="link">
		<bean:write name="path"/>.do?method=chooseCandidate<%= "&" %>personID=
		</bean:define>
	<h2><bean:message name="title"/></h2>
    	<p><b>Critérios de procura:</b><br /><br /><bean:write name="findQuery" filter="false"/></p>
    	<br /> 
    	<p>
    		<h3>   
    		<%= ((List) candidateList).size()%> <bean:message key="label.masterDegree.administrativeOffice.candidatesFound"/>      
    		<% if (((List) candidateList).size() != 0) { %>
    		</h3>
    	</p>  
    <table>
    	<tr>
			<td class="listClasses-header"><bean:message key="label.person.name" /></td>
			<td class="listClasses-header"><bean:message key="label.candidate.number" /></td>
			<td class="listClasses-header"><bean:message key="label.candidate.degree" /></td>
			<td class="listClasses-header"><bean:message key="label.candidate.specialization" /></td>
			<td class="listClasses-header"><bean:message key="label.candidate.infoCandidateSituation" /></td>
			<td class="listClasses-header"><bean:message key="label.candidate.infoCandidateSituationDate" /></td>	
		</tr>
    		<logic:iterate id="candidate" name="candidateList" indexId="indexCandidate">
    			<bean:define id="candidateLink">
    				<bean:write name="link"/><bean:write name="candidate" property="infoPerson.idInternal"/><%= "&" %>candidateID=<bean:write name="candidate" property="idInternal"/>
    			</bean:define>
    	<tr>
    		<td class="listClasses">
      			<html:link page='<%= pageContext.findAttribute("candidateLink").toString() %>'>
    				<bean:write name="candidate" property="infoPerson.nome" />
    			</html:link>
    		</td>
    		<td class="listClasses"><bean:write name="candidate" property="candidateNumber" /></td>
    		<td class="listClasses"><bean:write name="candidate" property="infoExecutionDegree.infoDegreeCurricularPlan.infoDegree.nome" /></td>
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