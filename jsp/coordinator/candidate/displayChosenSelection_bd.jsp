<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="java.util.List" %>
<%@ page import="DataBeans.InfoMasterDegreeCandidate" %>
<%@ page import="Util.SituationName" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<span class="error"><html:errors/></span>
	<br />
	<h2>Ordenação de Candidatos Suplentes</h2>
	<br />
	<bean:define id="listOfCandidates" name="candidateList" scope="request" />
	

	<logic:present name="candidatesID">
        <bean:message key="title.masterDegree.administrativeOffice.listSubstituteCandidates" />
        <br /><br />
                
        <html:form action="/displayListToSelectCandidates.do?method=preparePresentation">
		    <table>	     
		    <logic:iterate id="candidate" name="candidatesID" indexId="indexCandidate" >
    	        <html:hidden property='<%= "candidatesID[" + indexCandidate + "]" %>' />					
    	        <html:hidden property='<%= "situations[" + indexCandidate + "]" %>' />					
    	        <html:hidden property='<%= "remarks[" + indexCandidate + "]" %>' />					
    	        <bean:define id="situations" name="situations"/>
    	        <bean:define id="substitutes" name="substitutes"/>    					 

				<%  if (SituationName.checkIfSubstitute( ((String [])situations)[indexCandidate.intValue()])) { %>
    				<tr>
    					<td>
    						<%= ((InfoMasterDegreeCandidate) ((List) listOfCandidates).get(indexCandidate.intValue())).getInfoPerson().getNome() %>
    					</td>
    					<td>
    			          <html:text property='<%= "substitutes[" + indexCandidate + "]" %>' size="2"/>
    					</td>
    				</tr>
				<% } %>
			</logic:iterate>       
        	</table>	
        
		   <html:submit value="Seguinte" styleClass="inputbutton" property="ok"/>
        </html:form>	
   </logic:present>     
