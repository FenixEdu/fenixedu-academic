<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<span class="error"><html:errors/></span>
	<br />
	<h2>Listas finais de Candidatos</h2>
	<br />
<bean:define id="executionDegreeID" name="<%= SessionConstants.EXECUTION_DEGREE %>" scope="request" />
<html:hidden property="executionDegreeID" value="<%= pageContext.findAttribute("executionDegreeID").toString() %>" />
	<logic:iterate id="group" name="infoGroup" >
		<h2><bean:write name="group" property="situationName"/></h2>
  		<table>
        	<logic:iterate id="candidate" name="group" property="candidates">
        		<tr>
        			<td><bean:write name="candidate" property="candidateName"/></td>
        			<td><bean:write name="candidate" property="situationName"/></td>
        			<td><bean:write name="candidate" property="remarks"/></td>
        		    <logic:present name="candidate" property="orderPosition">
        				<td><bean:write name="candidate" property="orderPosition"/></td>
        			</logic:present>
        		</tr>
        	</logic:iterate> 
   		</table>
	</logic:iterate> 

	
    <logic:equal name="confirmation" value="YES">
        <html:form action="/displayListToSelectCandidates.do?method=aprove">
        <html:hidden property="executionDegreeID" value="<%= pageContext.findAttribute("executionDegreeID").toString() %>" />
        	<logic:iterate id="candidate" name="candidatesID" indexId="indexCandidate">
                <html:hidden property='<%= "candidatesID[" + indexCandidate + "]" %>' />					
                <html:hidden property='<%= "situations[" + indexCandidate + "]" %>' />					
                <html:hidden property='<%= "remarks[" + indexCandidate + "]" %>' />					
                <html:hidden property='<%= "substitutes[" + indexCandidate + "]" %>' />	
                <html:hidden property="executionDegreeID" value="<%= pageContext.findAttribute("executionDegreeID").toString() %>" />				
                <html:hidden property="degreeCurricularPlanID"/>
        	</logic:iterate> 
    	    <html:submit value="Confirmar" styleClass="inputbutton" property="OK"/>
    	    <html:submit value="Cancelar" styleClass="inputbutton" property="NotOK"/>
    	</html:form>
	</logic:equal>
	<bean:define id="link">/displayListToSelectCandidates.do?method=print&executionDegreeID=<%= pageContext.findAttribute("executionDegreeID").toString() %></bean:define>
	<logic:equal name="confirmation" value="NO">
	<html:hidden property="executionDegreeID" value="<%= pageContext.findAttribute("executionDegreeID").toString() %>" />
		<h2><bean:message key="label.candidateApprovalDone" /> </h2><br />
		<br />
		<br />
		<h2>
		   <html:link page='<%= pageContext.findAttribute("link").toString() %>' target="_blank">   
		   		<bean:message key="link.masterDegree.printCandidateApprovalList" />
		   </html:link>
		</h2>
	</logic:equal>
	
	<logic:equal name="confirmation" value="PRINT_PAGE">
		<table>
			<tr align="left">
				<td>
					O Coordenador
				</td>
			</tr>
		</table>
	</logic:equal>
	
	