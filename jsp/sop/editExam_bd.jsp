<%@ page language="java" %>
<%@ page import="org.apache.struts.action.Action" %>
<%@ page import ="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<table width="100%" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td bgcolor="#FFFFFF" class="infoselected"><p>A disciplina
        	&eacute;:<br/>
			<strong><jsp:include page="context.jsp"/></strong>
         </td>
    </tr>
</table>
<br/>

<h2><bean:message key="title.exam.edit"/></h2>
<span class="error"><html:errors /></span>

<html:form action="/editExam">
	<html:hidden property="page" value="1"/>
	<table cellpadding="0" cellspacing="2">
    	<tr>
        	<td nowrap class="formTD" align="right">
            	<bean:message key="property.exam.year"/>
            </td>
            <td nowrap class="formTD" align="left">
            	<html:text property="year"/>
            </td>
            <td nowrap class="formTD" align="right">
            	<bean:message key="property.exam.month"/>
            </td>
            <td nowrap class="formTD" align="left">
            	<html:select property="month">
		            <option value="" selected="selected">[Mês]</option>
		            <html:options collection="<%= SessionConstants.LABLELIST_MONTHSOFYEAR %>" property="value" labelProperty="label"/>
	            </html:select>
            </td>
            <td nowrap class="formTD" align="right">
            	<bean:message key="property.exam.day"/>
            </td>
            <td nowrap class="formTD" align="left">
            	<html:select property="day">
                	<option value="" selected="selected">[Dia]</option>
                    <html:options name="<%= SessionConstants.LABLELIST_DAYSOFMONTH %>"/>
                </html:select>
            </td>
		</tr>
        <tr>
            <td nowrap="nowrap" class="formTD" align="right">
                <bean:message key="property.exam.beginning"/>
            </td>
            <td nowrap="nowrap" align="left">
            	<html:select property="beginning">
                  	<option value="" selected="selected">[Turno]</option>                        
                    <html:options name="<%= SessionConstants.LABLELIST_HOURS %>"/>
                </html:select>
            </td>
       	</tr>
        <tr>
            <td nowrap="nowrap" class="formTD" align="right">
                <bean:message key="property.exam.season"/>
            </td>
            <td nowrap="nowrap" align="left">
            	<html:select property="season">
                  	<option value="" selected="selected">[Época]</option>           
		            <html:options collection="<%= SessionConstants.LABLELIST_SEASONS %>" property="value" labelProperty="label"/>                  	             
                </html:select>
            </td>
       	</tr>
	</table>

	<br/>
    <table align="lef">
    	<tr align="center">
        	<td>
        		<html:hidden property="method" value="create"/>
            	<html:submit styleClass="inputbutton">
              		<bean:message key="label.create"/>
             	</html:submit>
            </td>
            <td width="20"> </td>
            <td>
            	<html:reset value="Limpar" styleClass="inputbutton">
                	<bean:message key="label.clear"/>
                </html:reset>
            </td>
		</tr>
	</table>
</html:form>