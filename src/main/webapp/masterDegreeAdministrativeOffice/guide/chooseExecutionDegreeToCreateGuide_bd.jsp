<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %><html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ page import="org.apache.struts.Globals" %>
<%@ page import="java.util.List" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>

<span class="error"><!-- Error messages go here --><html:errors /></span>
 

<bean:define id="link">/createGuideDispatchAction.do?method=prepare<%= "&" %>page=0<%= "&" + PresentationConstants.EXECUTION_DEGREE_OID%>=</bean:define>

<!-- Display existent execution years of execution degrees -->
<logic:present name="<%= PresentationConstants.EXECUTION_DEGREE_LIST %>" scope="request">
	<bean:define id="executionDegreeList" name="<%= PresentationConstants.EXECUTION_DEGREE_LIST %>" scope="request" />
	<p><strong><bean:message key="label.masterDegree.administrativeOffice.chooseExecutionYear" />:</strong></p>
	<logic:iterate id="executionDegree" name="executionDegreeList">
		<bean:define id="executionDegreeLink">
			<bean:write name="link"/><bean:write name="executionDegree" property="externalId"/>
 		</bean:define>
    	<html:link page='<%= pageContext.findAttribute("executionDegreeLink").toString() %>'>
			<bean:write name="executionDegree" property="infoExecutionYear.year"/> <br/>
        </html:link>
	</logic:iterate>
</logic:present>

<!-- indicate that no executions degrees were found -->
<logic:notPresent name="<%= PresentationConstants.EXECUTION_DEGREE_LIST %>" scope="request">
	<h2><bean:message key="label.masterDegree.administrativeOffice.executionYearsNotFound" /><br/><br/></h2>	
</logic:notPresent>

