<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ page import="java.util.List" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidate" %>
<%@ page import="net.sourceforge.fenixedu.util.SituationName" %>


<span class="error"><!-- Error messages go here --><html:errors /></span>
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
    	        <html:hidden alt='<%= "candidatesID[" + indexCandidate + "]" %>' property='<%= "candidatesID[" + indexCandidate + "]" %>' />					
    	        <html:hidden alt='<%= "situations[" + indexCandidate + "]" %>' property='<%= "situations[" + indexCandidate + "]" %>' />					
    	        <html:hidden alt='<%= "remarks[" + indexCandidate + "]" %>' property='<%= "remarks[" + indexCandidate + "]" %>' />					
    	        <bean:define id="situations" name="situations"/>
    	        <bean:define id="substitutes" name="substitutes"/>    					 

				<%  if (SituationName.checkIfSubstitute( ((String [])situations)[indexCandidate.intValue()])) { %>
    				<tr>
    					<td>
    						<%= ((InfoMasterDegreeCandidate) ((List) listOfCandidates).get(indexCandidate.intValue())).getInfoPerson().getNome() %>
    					</td>
    					<td>
    			          <html:text alt='<%= "substitutes[" + indexCandidate + "]" %>' property='<%= "substitutes[" + indexCandidate + "]" %>' size="2"/>
    					</td>
    				</tr>
				<% } %>
			</logic:iterate>       
        	</table>	
		   <html:submit bundle="HTMLALT_RESOURCES" altKey="submit.ok" value="Seguinte" styleClass="inputbutton" property="ok"/>
        </html:form>	
   </logic:present>     
