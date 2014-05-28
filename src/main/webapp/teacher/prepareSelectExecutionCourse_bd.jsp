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
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
<br />
<h2><bean:message key="title.exportGroupProperties" /></h2>
<br />
<br/>

	<table width="98%" cellpadding="0" cellspacing="0">
		<tr>
			<td class="infoop">
				<bean:message key="label.teacher.exportGroupProperties.executionCourse.description" />
			</td>
		</tr>
	</table>
	<br/>
<span class="error"><!-- Error messages go here --><html:errors /></span>        
<html:form action="/studentGroupManagement" >
	<input alt="input.method" type="hidden" name="method" value="firstPage">       		
   	<%-- hide previous form for validation matters --%>
	<html:hidden alt="<%=PresentationConstants.EXECUTION_PERIOD_OID%>" property="<%=PresentationConstants.EXECUTION_PERIOD_OID%>" value="<%= ""+pageContext.findAttribute(PresentationConstants.EXECUTION_PERIOD_OID)%>" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionCourseID"  property="executionCourseID" value="<%= pageContext.findAttribute("executionCourseID").toString() %>" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.groupPropertiesCode"  property="groupPropertiesCode" value="<%= request.getParameter("groupPropertiesCode") %>" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.curYear" name="chooseSearchContextForm" property="curYear"/>	
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.index" name="chooseSearchContextForm" property="index"/>
    <html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
    <table width="98%" align="center" border="0" cellpadding='0' cellspacing='0'>
    	<tr>
    		<td class="infoop"><bean:message key="message.choose.discipline" />
       		</td>
    	</tr>
    </table>
    <br />
    <table width="98%" align="center" border="0" cellpadding='0' cellspacing='0'>
    	<tr valign='center'>
        	<td class="formTD">
            	<bean:message key="property.course"/>
                <br/>
            </td>
            <td>    
 				<html:select bundle="HTMLALT_RESOURCES" altKey="select.goalExecutionCourseId" property="goalExecutionCourseId" size="1">
  	 				<option value=""><bean:message key="label.choose.executionCourse"/></option>
 					<html:options	property="externalId" labelProperty="nome" collection="exeCourseList" />
  	 			</html:select>             
            </td>
        </tr>
	</table>
    <br/>
   	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
   		<bean:message key="label.submit"/>
   	</html:submit>
</html:form>