<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ page import="java.util.List" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>

<span class="error"><!-- Error messages go here --><html:errors /></span>

<bean:define id="candidateList" name="<%= PresentationConstants.MASTER_DEGREE_CANDIDATE_LIST %>"  />

<%= ((List) candidateList).size() %> <bean:message key="label.masterDegree.administrativeOffice.candidatesFound"/>        

<br/><bean:message key="label.masterDegree.chooseOne"/><br/>

<logic:iterate id="candidate" name="candidateList" indexId="indexCandidate">
	<bean:define id="candidateLink">
		/chooseCandidate.do?candidateID=<bean:write name="candidate" property="externalId"/>
	</bean:define>
	<html:link page='<%= pageContext.findAttribute("candidateLink").toString() %>'>
        <bean:write name="candidate" property="infoExecutionDegree.infoDegreeCurricularPlan.infoDegree.nome" /> - 
		<bean:message name="candidate" property="specialization.name" bundle="ENUMERATION_RESOURCES"/>
    </html:link>
    <br/>
</logic:iterate>
