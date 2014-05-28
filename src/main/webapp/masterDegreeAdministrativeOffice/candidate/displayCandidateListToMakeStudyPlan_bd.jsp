<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
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
    				<bean:write name="link"/><bean:write name="candidate" property="externalId"/>
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
	
