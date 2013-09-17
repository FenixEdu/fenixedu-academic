<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>

<h2><bean:message key="link.coordinator.approveCandidates" /></h2>

<p>
	<span class="error"><!-- Error messages go here --><html:errors /></span>
</p>

	<bean:define id="situationList" name="<%= PresentationConstants.CANDIDATE_SITUATION_LIST %>" scope="request" />
	<bean:define id="executionDegreeID" name="<%= PresentationConstants.EXECUTION_DEGREE %>" scope="request" />

<logic:present name="jspTitle">
	<h2><bean:write name="jspTitle" /></h2>
</logic:present>

	<p><bean:message key="label.numerusClausus" /> <span class="emphasis"><bean:write name="numerusClausus" /></span></p>
    <logic:present name="candidateList">
            <bean:message key="title.masterDegree.administrativeOffice.listCandidates" />
        <br /><br />
        <html:form action="/displayListToSelectCandidates.do?method=selectCandidates">
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
        	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeCurricularPlanID" property="degreeCurricularPlanID" />
		   <html:submit bundle="HTMLALT_RESOURCES" altKey="submit.ok" value="Seguinte" styleClass="inputbutton" property="ok"/>
        </html:form>	
   </logic:present>