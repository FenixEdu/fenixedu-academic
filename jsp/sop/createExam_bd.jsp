<%@ page language="java" %>
<%@ page import="org.apache.struts.action.Action" %>
<%@ page import ="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td class="infoselected"><p>A licenciatura seleccionada &eacute;:</p>
			<strong><jsp:include page="context.jsp"/></strong>
        </td>
    </tr>
</table>
<br/>
<h2><bean:message key="title.exam.create"/></h2>
<span class="error"><html:errors /></span>
<html:form action="/createExam">
	<html:hidden property="page" value="1"/>
	<table cellpadding="0" cellspacing="0">
    	<tr>
        	<td width="50">
            	<bean:message key="property.exam.year"/>:
            </td>
            <td width="25">
            	<html:text property="year" size="5" maxlength="5" />
            </td>
            <td width="50">
            	<bean:message key="property.exam.month"/>:
            </td>
            <td width="125">
            	<html:select property="month">
		            <option value="" selected="selected"></option>
		            <html:options collection="<%= SessionConstants.LABLELIST_MONTHSOFYEAR %>" property="value" labelProperty="label"/>
	            </html:select>
            </td>
            <td width="50">
            	<bean:message key="property.exam.day"/>:
            </td>
            <td width="100">
            	<html:select property="day">
                	<option value="" selected="selected"></option>
                    <html:options name="<%= SessionConstants.LABLELIST_DAYSOFMONTH %>"/>
                </html:select>
            </td>
		</tr>
        <tr>
            <td width="50">
                <bean:message key="property.exam.beginning"/>:
            </td>
            <td class="formTD">
            	<html:select property="beginning">
                  	<option value="" selected="selected"></option>                        
                    <html:options name="<%= SessionConstants.LABLELIST_HOURS %>"/>
                </html:select>
            </td>
       	</tr>
        <tr>
            <td width="50">
                <bean:message key="property.exam.season"/>:
            </td>
            <td class="formTD">
            	<html:select property="season">
                  	<option value="" selected="selected"></option>           
		            <html:options collection="<%= SessionConstants.LABLELIST_SEASONS %>" property="value" labelProperty="label"/>                  	             
                </html:select>
            </td>
       	</tr>
	</table>
	<br />
<html:hidden property="method" value="create"/>
<html:submit styleClass="inputbutton">
   <bean:message key="label.create"/>
</html:submit>
<html:reset value="Limpar" styleClass="inputbutton">
   <bean:message key="label.clear"/>
</html:reset>
</html:form>