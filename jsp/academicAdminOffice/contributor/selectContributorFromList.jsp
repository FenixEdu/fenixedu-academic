<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="org.apache.struts.Globals" %>
<%@ page import="java.util.List" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>

    <span class="error"><!-- Error messages go here --><html:errors /></span>
    <bean:define id="contributorList" name="<%= SessionConstants.CONTRIBUTOR_LIST %>" scope="session" />
    <bean:define id="title" name="<%= SessionConstants.CONTRIBUTOR_ACTION %>" scope="session" />
        
    <bean:define id="path" type="java.lang.String" scope="request" property="path" name="<%= Globals.MAPPING_KEY %>" />
	<bean:define id="link">
		<bean:write name="path"/>.do?method=chooseContributor<%= "&" %>page=0<%= "&" %>contributorPosition=
	</bean:define>
    <h2><bean:message name="title"/></h2>
    
    <%= ((List) contributorList).size()%> <bean:message key="label.masterDegree.administrativeOffice.contributorsFound"/>        
    <% if (((List) contributorList).size() != 0) { %>
    
	    <table class="tstyle1">
    		<tr>
				<td><bean:message key="label.masterDegree.administrativeOffice.contributorNumber" /></td>
				<td><bean:message key="label.masterDegree.administrativeOffice.contributorName" /></td>
				<td><bean:message key="label.masterDegree.administrativeOffice.contributorAddress" /></td>
				<td><bean:message key="label.person.postCode" /></td>
				<td><bean:message key="label.person.areaOfPostCode" /></td>
				<td><bean:message key="label.person.place" /></td>
				<td><bean:message key="label.person.addressParish" /></td>
				<td><bean:message key="label.person.addressMunicipality" /></td>
				<td><bean:message key="label.person.addressDistrict" /></td>
			</tr>
    		
        
    		<logic:iterate id="contributor" name="contributorList" indexId="indexContributor">
    			<bean:define id="contributorLink">
    				<bean:write name="link"/><bean:write name="indexContributor"/>
    			</bean:define>
    			<tr>
    				<td>
      				    <html:link page='<%= pageContext.findAttribute("contributorLink").toString() %>'>
    						<bean:write name="contributor" property="contributorNumber" />
    					</html:link>
    				</td>
    				<td><bean:write name="contributor" property="contributorName" /></td>
    				<td><bean:write name="contributor" property="contributorAddress" /></td>
	   				<td><bean:write name="contributor" property="areaCode" /></td>
	   				<td><bean:write name="contributor" property="areaOfAreaCode" /></td>
	   				<td><bean:write name="contributor" property="area" /></td>
	   				<td><bean:write name="contributor" property="parishOfResidence" /></td>
	   				<td><bean:write name="contributor" property="districtSubdivisionOfResidence" /></td>
	   				<td><bean:write name="contributor" property="districtOfResidence" /></td>
    			</tr>
    		</logic:iterate>
          </table>
        <% } %>
 