<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="org.apache.struts.Globals" %>
<%@ page import="java.util.List" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>

<span class="error"><html:errors/></span>
 

<bean:define id="link">/createGuideDispatchAction.do?method=prepare<%= "&" %>page=0<%= "&" + SessionConstants.EXECUTION_DEGREE_OID%>=</bean:define>

<!-- Display existent execution years of execution degrees -->
<logic:present name="<%= SessionConstants.EXECUTION_DEGREE_LIST %>" scope="request">
	<bean:define id="executionDegreeList" name="<%= SessionConstants.EXECUTION_DEGREE_LIST %>" scope="request" />
	<h2><bean:message key="label.masterDegree.administrativeOffice.chooseExecutionYear" />:<br/><br/></h2>
	<logic:iterate id="executionDegree" name="executionDegreeList">
		<bean:define id="executionDegreeLink">
			<bean:write name="link"/><bean:write name="executionDegree" property="idInternal"/>
 		</bean:define>
    	<html:link page='<%= pageContext.findAttribute("executionDegreeLink").toString() %>'>
			<bean:write name="executionDegree" property="infoExecutionYear.year"/> <br/>
        </html:link>
	</logic:iterate>
</logic:present>

<!-- indicate that no executions degrees were found -->
<logic:notPresent name="<%= SessionConstants.EXECUTION_DEGREE_LIST %>" scope="request">
	<h2><bean:message key="label.masterDegree.administrativeOffice.executionYearsNotFound" /><br/><br/></h2>	
</logic:notPresent>

