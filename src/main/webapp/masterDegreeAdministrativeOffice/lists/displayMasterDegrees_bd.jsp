<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ page import="org.apache.struts.Globals" %>
<%@ page import="java.util.List" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>

   <span class="error"><!-- Error messages go here --><html:errors /></span>

	<bean:define id="path" type="java.lang.String" scope="request" property="path" name="<%= Globals.MAPPING_KEY %>" />
    <bean:define id="masterDegreeList" name="<%= PresentationConstants.MASTER_DEGREE_LIST %>" scope="request" />
    <bean:define id="link"><%= path %>.do?method=chooseMasterDegree<%= "&" %>page=0<%= "&" %>degreeID=</bean:define>
    <p>
    <h3><%= ((List) masterDegreeList).size()%> <bean:message key="label.masterDegree.administrativeOffice.degreesFound"/></h3>        
    <% if (((List) masterDegreeList).size() != 0) { %>
    </p>
    <bean:message key="label.masterDegree.chooseOne"/><br/><br/>
	<bean:message key="label.manager.degrees" /><br/>
   	     	<logic:iterate id="masterDegree" name="masterDegreeList">
        	<bean:define id="masterDegreeLink">
        		<bean:write name="link"/><bean:write name="masterDegree" property="externalId"/>
        	</bean:define>
        	<html:link page='<%= pageContext.findAttribute("masterDegreeLink").toString() %>'>
				<bean:write name="masterDegree" property="nome"/> - 
				<bean:write name="masterDegree" property="sigla"/>
				<br/>
            </html:link>
    	</logic:iterate>
	<% } %>