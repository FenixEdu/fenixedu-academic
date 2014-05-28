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

<h2><bean:message key="title.masterDegree.administrativeOffice.curricularPlan"/></h2>

   <span class="error"><!-- Error messages go here --><html:errors /></span>
  <bean:define id="path" type="java.lang.String" scope="request" property="path" name="<%= Globals.MAPPING_KEY %>" />
  <bean:define id="masterDegreeCPlanList" name="<%= PresentationConstants.MASTER_DEGREE_CURRICULAR_PLAN_LIST %>" scope="request" />
  <bean:define id="link"><bean:write name="path"/>.do?method=prepareChooseExecutionYear<%= "&" %>page=0<%= "&" %>curricularPlanID=</bean:define>

    <p><strong><%= ((List) masterDegreeCPlanList).size()%> <bean:message key="label.masterDegree.administrativeOffice.curricularPlanFound"/></strong></p>
    <% if (((List) masterDegreeCPlanList).size() != 0) { %>

    <%--<bean:message key="label.masterDegree.chooseOne"/><br/><br/>--%>
    
	<p><bean:message key="label.masterDegree.administrativeOffice.curricularPlans" /></p>
  	<% } %>
  
  	<ul>
  	<logic:iterate id="degreeCurricularPlan" name="masterDegreeCPlanList">
  		<li>
    	<bean:define id="degreeCurricularPlanLink">
    		<bean:write name="link"/><bean:write name="degreeCurricularPlan" property="externalId"/>&degreeID=<bean:write name="degreeCurricularPlan" property="infoDegree.externalId"/>
    	</bean:define>
    	<html:link page='<%= pageContext.findAttribute("degreeCurricularPlanLink").toString() %>'>
			<bean:write name="degreeCurricularPlan" property="infoDegree.nome"/> - 
			<bean:write name="degreeCurricularPlan" property="name"/>
        </html:link>
        </li>
	</logic:iterate>
	</ul>

