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
   
   <table>
    <span class="error"><html:errors/></span>
    <bean:define id="candidateList" name="<%= SessionConstants.MASTER_DEGREE_CANDIDATE_LIST %>" scope="session" />
    <bean:define id="findQuery" name="<%= SessionConstants.MASTER_DEGREE_CANDIDATE_QUERY %>" scope="session" />
    
    <bean:define id="path" type="java.lang.String" scope="request" property="path" name="<%= Action.MAPPING_KEY %>" />
        <html:form action="<%=path%>">
        <input type="hidden" value="chooseCandidate" name="method"/>
        
        Critérios de procura:<br><bean:write name="findQuery" /><br><br>
        
        <%= ((List) candidateList).size()%> <bean:message key="label.masterDegree.administrativeOffice.candidatesFound"/>
        <% if (((List) candidateList).size() != 0) { %>
        
insert list here











        <% } %>
    </html:form>
   </table>
  </body>
</html>
