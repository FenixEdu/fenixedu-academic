<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %><html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ page import="org.apache.struts.Globals" %>
<%@ page import="java.util.List" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>


	<em><bean:message key="title.masterDegree.administrativeOffice"/></em>
	<h2><bean:message key="title.masterDegree.administrativeOffice.createCandidate"/></h2>
	
	<p>
		<span class="error"><!-- Error messages go here --><html:errors /></span>
	</p>

    <bean:define id="masterDegreeList" name="<%= PresentationConstants.MASTER_DEGREE_LIST %>" scope="request" />
    <bean:define id="link">/listMasterDegreesCandidate.do?method=chooseMasterDegree<%= "&" %>page=0<%= "&" %>degreeID=</bean:define>
    
    <p><strong><%= ((List) masterDegreeList).size()%> <bean:message key="label.masterDegree.administrativeOffice.degreesFound"/></strong></p>
    <% if (((List) masterDegreeList).size() != 0) { %>
    
    <%--<p><bean:message key="label.masterDegree.chooseOne"/></p>--%>
    
	<p><bean:message key="label.manager.degrees" />:</p>
	<ul>
		<logic:iterate id="masterDegree" name="masterDegreeList">
		<li>	
			<bean:define id="masterDegreeLink">
				<bean:write name="link"/><bean:write name="masterDegree" property="externalId"/>
			</bean:define>
			<html:link page='<%= pageContext.findAttribute("masterDegreeLink").toString() %>'>
				<bean:write name="masterDegree" property="nome"/> - <bean:write name="masterDegree" property="sigla"/>
			</html:link>
		</li>
		</logic:iterate>
	</ul>
	<% } %>