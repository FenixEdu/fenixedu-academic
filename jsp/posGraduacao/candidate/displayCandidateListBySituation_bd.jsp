<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>

<span class="error"><html:errors/></span>
	<br />
	<bean:define id="situationList" name="<%= SessionConstants.CANDIDATE_SITUATION_LIST %>" scope="request" />
	<bean:define id="executionDegreeID" name="<%= SessionConstants.EXECUTION_DEGREE %>" scope="request" />
	
<logic:present name="jspTitle">
	<h2><bean:write name="jspTitle" /></h2>
	<br />
</logic:present>

	<h2><bean:message key="label.numerusClausus" /> <bean:write name="numerusClausus" /></h2>

    <logic:present name="candidateList">
            <bean:message key="title.masterDegree.administrativeOffice.listCandidates" />
        <br /><br />
        <html:form action="/displayListToSelectCandidates.do?method=selectCandidates">
		    <table>	        
        	<!-- Candidate List -->
        	<logic:iterate id="candidate" name="candidateList" indexId="indexCandidate">
				<tr>
					<td>
				        <html:hidden property='<%= "candidatesID[" + indexCandidate + "]" %>' />					
					<bean:write name="candidate" property="infoPerson.nome"/></td>
					<td>
                        <html:select property='<%= "situations[" + indexCandidate + "]" %>' >
                            <html:options collection="situationList" property="value" labelProperty="label"/> 
                        </html:select> 
					</td>
					<td><bean:message key="label.masterDegree.administrativeOffice.remarks" />
					<html:textarea property='<%= "remarks[" + indexCandidate + "]" %>'/></td>
        		</tr>
        	</logic:iterate>
        	</table>	
		   <html:submit value="Seguinte" styleClass="inputbutton" property="ok"/>
		   <html:hidden property="executionDegreeID" value="<%= pageContext.findAttribute("executionDegreeID").toString() %>" />
        </html:form>	
   </logic:present>