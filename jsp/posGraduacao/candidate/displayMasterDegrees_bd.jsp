<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="org.apache.struts.Globals" %>
<%@ page import="java.util.List" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>

   <span class="error"><html:errors/></span>

    <bean:define id="masterDegreeList" name="<%= SessionConstants.MASTER_DEGREE_LIST %>" scope="request" />
    <bean:define id="link">/listMasterDegreesCandidate.do?method=chooseMasterDegree<%= "&" %>page=0<%= "&" %>degreeID=</bean:define>
    <p>
    <h3><%= ((List) masterDegreeList).size()%> <bean:message key="label.masterDegree.administrativeOffice.degreesFound"/></h3>        
    <% if (((List) masterDegreeList).size() != 0) { %>
    </p>
    <bean:message key="label.masterDegree.chooseOne"/><br><br>
	<bean:message key="label.manager.degrees" /><br>
   	     	<logic:iterate id="masterDegree" name="masterDegreeList">
        	<bean:define id="masterDegreeLink">
        		<bean:write name="link"/><bean:write name="masterDegree" property="idInternal"/>
        	</bean:define>
        	<html:link page='<%= pageContext.findAttribute("masterDegreeLink").toString() %>'>
				<bean:write name="masterDegree" property="nome"/> - 
				<bean:write name="masterDegree" property="sigla"/>
				<br>
            </html:link>
    	</logic:iterate>
	<% } %>