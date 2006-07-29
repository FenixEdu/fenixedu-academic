<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>

<span class="error"><!-- Error messages go here --><html:errors /></span>
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
        	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeCurricularPlanID" property="degreeCurricularPlanID"/>
		    <table>	        
        	<!-- Candidate List -->
        	<logic:iterate id="candidate" name="candidateList" indexId="indexCandidate">
				<tr>
					<td>
				        <html:hidden alt='<%= "candidatesID[" + indexCandidate + "]" %>' property='<%= "candidatesID[" + indexCandidate + "]" %>' />					
					<bean:write name="candidate" property="infoPerson.nome"/></td>
					<td>
                        <html:select alt='<%= "situations[" + indexCandidate + "]" %>' property='<%= "situations[" + indexCandidate + "]" %>' >
                            <html:options collection="situationList" property="value" labelProperty="label"/> 
                        </html:select> 
					</td>
					<td><bean:message key="label.masterDegree.administrativeOffice.remarks" />
					<html:textarea alt='<%= "remarks[" + indexCandidate + "]" %>' property='<%= "remarks[" + indexCandidate + "]" %>'/></td>
        		</tr>
        	</logic:iterate>
        	</table>	
		   <html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionDegreeID" property="executionDegreeID" value="<%= pageContext.findAttribute("executionDegreeID").toString() %>" />
		   <html:submit bundle="HTMLALT_RESOURCES" altKey="submit.ok" value="Seguinte" styleClass="inputbutton" property="ok"/>
        </html:form>	
   </logic:present>