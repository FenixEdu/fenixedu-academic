<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
    <br/>
        <center><font color='#034D7A' size='5'> <b> <bean:message key="title.choose.discipline"/> </b> </font></center>
        <br/>
        <br/>
        <span class="error"><html:errors/></span>        
        <html:form action="/viewExecutionCourse">
       		<input type="hidden" name="method" value="executionCourseViewerSelectedFromForm">
        
        	<html:hidden property="page" value="1"/>
            <table align="center" border="5" cellpadding='20' cellspacing='10'>
                <tr align="center" valign='center'>
                    <td>
                        <bean:message key="property.course"/>
	                    <br/>
    					<html:select property="courseInitials" size="1">
  	 <option value=""><bean:message key="label.choose.executionCourse"/></option>
     <html:options	property="sigla" 
     				labelProperty="nome" 
					collection="<%= SessionConstants.EXECUTION_COURSE_LIST_KEY %>" />
  </html:select>             
                    </td>
                </tr>
            </table>
            <br/>
            <table align="center">
                <tr align="center">
                    <td width="100" height="50">
                        <html:submit>
                            <bean:message key="label.search"/>
                        </html:submit>
                    </td>
                </tr>
            </table>
        </html:form>

