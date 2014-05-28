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
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ page import="org.apache.struts.Globals" %>
<%@ page import="java.util.List" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>

<h2><bean:message key="title.masterDegree.administrativeOffice.chooseDegree"/></h2>

   <span class="error"><!-- Error messages go here --><html:errors /></span>
	<bean:define id="path" type="java.lang.String" scope="request" property="path" name="<%= Globals.MAPPING_KEY %>" />
    <bean:define id="masterDegreeList" name="<%= PresentationConstants.MASTER_DEGREE_LIST %>" scope="request" />
    
    <bean:define id="link"><bean:write name="path"/>.do?method=chooseMasterDegree<%= "&" %>page=0<%= "&" %>degreeID=</bean:define>

    <p><strong><%= ((List) masterDegreeList).size()%> <bean:message key="label.masterDegree.administrativeOffice.degreesFound"/></strong></p>
    <% if (((List) masterDegreeList).size() != 0) { %>

    <%--<bean:message key="label.masterDegree.chooseOne"/><br/><br/>--%>
    
	<p><bean:message key="label.manager.degrees" />:</p>
	
		<ul>
   	     	<logic:iterate id="masterDegree" name="masterDegreeList">
   	     	<li>
        	<bean:define id="masterDegreeLink">
        		<bean:write name="link"/><bean:write name="masterDegree" property="externalId"/>
        	</bean:define>
        	<html:link page='<%= pageContext.findAttribute("masterDegreeLink").toString() %>'>
				<bean:write name="masterDegree" property="nome"/> - 
				<bean:write name="masterDegree" property="sigla"/>
            </html:link>
            </li>
    		</logic:iterate>
	    </ul>
	<% } %>