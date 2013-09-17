<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
<span class="error"><!-- Error messages go here --><html:errors /></span>
	<br />
	<h2>Listas finais de Candidatos</h2>
	<br />
<bean:define id="executionDegreeID" name="<%= PresentationConstants.EXECUTION_DEGREE %>" scope="request" />
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionDegreeID" property="executionDegreeID" value="<%= pageContext.findAttribute("executionDegreeID").toString() %>" />
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
        <html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionDegreeID" property="executionDegreeID" value="<%= pageContext.findAttribute("executionDegreeID").toString() %>" />
        	<logic:iterate id="candidate" name="candidatesID" indexId="indexCandidate">
                <html:hidden alt='<%= "candidatesID[" + indexCandidate + "]" %>' property='<%= "candidatesID[" + indexCandidate + "]" %>' />					
                <html:hidden alt='<%= "situations[" + indexCandidate + "]" %>' property='<%= "situations[" + indexCandidate + "]" %>' />					
                <html:hidden alt='<%= "remarks[" + indexCandidate + "]" %>' property='<%= "remarks[" + indexCandidate + "]" %>' />					
                <html:hidden alt='<%= "substitutes[" + indexCandidate + "]" %>' property='<%= "substitutes[" + indexCandidate + "]" %>' />	
                <html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionDegreeID" property="executionDegreeID" value="<%= pageContext.findAttribute("executionDegreeID").toString() %>" />				
                <html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeCurricularPlanID" property="degreeCurricularPlanID"/>
        	</logic:iterate> 
    	    <html:submit bundle="HTMLALT_RESOURCES" altKey="submit.OK" value="Confirmar" styleClass="inputbutton" property="OK"/>
    	    <html:submit bundle="HTMLALT_RESOURCES" altKey="submit.NotOK" value="Cancelar" styleClass="inputbutton" property="NotOK"/>
    	</html:form>
	</logic:equal>
	<bean:define id="link">/displayListToSelectCandidates.do?method=print&executionDegreeID=<%= pageContext.findAttribute("executionDegreeID").toString() %></bean:define>
	<logic:equal name="confirmation" value="NO">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionDegreeID" property="executionDegreeID" value="<%= pageContext.findAttribute("executionDegreeID").toString() %>" />
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
	
	