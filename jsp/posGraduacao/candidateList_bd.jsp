<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="org.apache.struts.action.Action" %>
<%@ page import="java.util.List" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>

<html>
  <head>
    <title><bean:message key="title.masterDegree.administrativeOffice.listCandidates" /></title>
  </head>
  <body>
   
   
    <span class="error"><html:errors/></span>
    <bean:define id="candidateList" name="<%= SessionConstants.MASTER_DEGREE_CANDIDATE_LIST %>" scope="session" />
    <bean:define id="findQuery" name="<%= SessionConstants.MASTER_DEGREE_CANDIDATE_QUERY %>" scope="session" />
    <bean:define id="title" name="<%= SessionConstants.MASTER_DEGREE_CANDIDATE_ACTION %>" scope="session" />
        
    <bean:define id="path" type="java.lang.String" scope="request" property="path" name="<%= Action.MAPPING_KEY %>" />
	<bean:define id="link">
		<bean:write name="path"/>.do?method=chooseCandidate<%= "&" %>candidatePosition=
	</bean:define>
    <h2><bean:message name="title"/></h2>
    
    
    Critérios de procura:<br><bean:write name="findQuery" filter="false"/><br><br>
    
    <%= ((List) candidateList).size()%> <bean:message key="label.masterDegree.administrativeOffice.candidatesFound"/>        
    <% if (((List) candidateList).size() != 0) { %>
    
	    <table>
    		<tr>
				<td><bean:message key="label.person.name" /></td>
				<td><bean:message key="label.candidate.candidateNumber" /></td>
				<td><bean:message key="label.candidate.degree" /></td>
				<td><bean:message key="label.candidate.specialization" /></td>
				<td><bean:message key="label.candidate.infoCandidateSituation" /></td>
				<td><bean:message key="label.candidate.infoCandidateSituationDate" /></td>
				
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
    				<td><bean:write name="candidate" property="candidateNumber" /></td>
    				<td><bean:write name="candidate" property="infoExecutionDegree.infoDegreeCurricularPlan.infoDegree.sigla" /></td>
    				<td><bean:write name="candidate" property="specialization" /></td>
    				<td><bean:write name="candidate" property="infoCandidateSituation.situation" /></td>
    				<td><bean:write name="candidate" property="infoCandidateSituation.date" /></td>
    			</tr>
    		</logic:iterate>
          </table>
        <% } %>


  </body>
</html>
