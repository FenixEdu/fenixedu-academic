<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="org.apache.struts.Globals" %>


<span class="error"><!-- Error messages go here --><html:errors /></span>
		<bean:define id="path" type="java.lang.String" scope="request" property="path" name="<%= Globals.MAPPING_KEY %>" />
		<bean:define id="link">
		<bean:write name="path"/>.do?method=prepareSecondChooseMasterDegree<%= "&" %>candidateID=
		</bean:define>
	<br />
	
<logic:present name="jspTitle">
	<h2><bean:write name="jspTitle" /></h2>
	<br />
</logic:present>

	<logic:present name="candidateList">
            <bean:message key="title.masterDegree.administrativeOffice.listCandidates" />
        <br /><br />
            <table>	        
            <tr>
            	<th class="listClasses-header"><bean:message key="label.person.name" /></th>
            </tr>
        	<!-- Candidate List -->
        	<logic:iterate id="candidate" name="candidateList" indexId="indexCandidate">
        		<bean:define id="candidateLink">
    				<bean:write name="link"/><bean:write name="candidate" property="idInternal"/>
    			</bean:define>
    		<tr>
    		<td class="listClasses">
      			<html:link page="<%= pageContext.findAttribute("candidateLink").toString() + "&amp;executionYear=" + pageContext.findAttribute("executionYear") %>">
    				<bean:write name="candidate" property="infoPerson.nome" />
    			</html:link>
    		</td>
    		</tr>
    		
    		</logic:iterate>
        	</table>
	</logic:present> 
	
