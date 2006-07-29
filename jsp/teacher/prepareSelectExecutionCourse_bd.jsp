<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<br />
<h2><bean:message key="title.exportGroupProperties" /></h2>
<br />
<br/>

	<table width="100%" cellpadding="0" cellspacing="0">
		<tr>
			<td class="infoop">
				<bean:message key="label.teacher.exportGroupProperties.executionCourse.description" />
			</td>
		</tr>
	</table>
	<br/>
<span class="error"><!-- Error messages go here --><html:errors /></span>        
<html:form action="/viewSiteExecutionCourse" method="get">
	<input alt="input.method" type="hidden" name="method" value="firstPage">       		
   	<%-- hide previous form for validation matters --%>
	<html:hidden alt="<%=SessionConstants.EXECUTION_PERIOD_OID%>" property="<%=SessionConstants.EXECUTION_PERIOD_OID%>" value="<%= ""+request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID)%>" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.objectCode"  property="objectCode" value="<%= pageContext.findAttribute("objectCode").toString() %>" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.groupPropertiesCode"  property="groupPropertiesCode" value="<%= request.getParameter("groupPropertiesCode") %>" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.curYear" name="chooseSearchContextForm" property="curYear"/>	
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.index" name="chooseSearchContextForm" property="index"/>
    <html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
    <table width="100%" align="center" border="0" cellpadding='0' cellspacing='0'>
    	<tr>
    		<td class="infoop"><bean:message key="message.choose.discipline" />
       		</td>
    	</tr>
    </table>
    <br />
    <table width="100%" align="center" border="0" cellpadding='0' cellspacing='0'>
    	<tr valign='center'>
        	<td class="formTD">
            	<bean:message key="property.course"/>
                <br/>
            </td>
            <td>    
 				<html:select bundle="HTMLALT_RESOURCES" altKey="select.goalExecutionCourseId" property="goalExecutionCourseId" size="1">
  	 				<option value=""><bean:message key="label.choose.executionCourse"/></option>
 					<html:options	property="idInternal" labelProperty="nome" collection="exeCourseList" />
  	 			</html:select>             
            </td>
        </tr>
	</table>
    <br/>
   	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
   		<bean:message key="label.submit"/>
   	</html:submit>
</html:form>