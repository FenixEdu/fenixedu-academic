<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
    <br/>
        <h2><bean:message key="title.choose.discipline" /></h2>
        <br/>
        <span class="error"><html:errors/></span>        
        <html:form action="/viewExecutionCourse">
       		<input type="hidden" name="method" value="executionCourseViewerSelectedFromForm">
        	<html:hidden property="page" value="1"/>
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
     					<html:select property="courseInitials" size="1">
  	 						<option value=""><bean:message key="label.choose.executionCourse"/></option>
     						<html:options	property="sigla" labelProperty="nome" collection="<%= SessionConstants.EXECUTION_COURSE_LIST_KEY %>" />
  	 					</html:select>             
                    </td>
                </tr>
            </table>
            <br/>
       	<html:submit styleClass="inputbutton">
       		<bean:message key="label.submit"/>
       	</html:submit>
        </html:form>

