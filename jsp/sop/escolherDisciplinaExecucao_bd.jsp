<%@ page language="java" %>
<%@ page import="org.apache.struts.action.Action" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
	   	<table width="100%" border="0" cellpadding="0" cellspacing="0">
          <tr>
            <td class="infoselected"><p>A licenciatura seleccionada
              &eacute;:</p>
			  <strong><jsp:include page="context.jsp"/></strong>
            </td>
          </tr>
        </table>
        <br/>
        <h2><bean:message key="title.choose.discipline"/></h2>
        <span class="error"><html:errors /></span>
        <bean:define id="path" type="java.lang.String" scope="request" property="path" name="<%= Action.MAPPING_KEY %>" />
        <html:form action="<%= path %>">        
        	<html:hidden property="page" value="1"/>
    	<table border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td nowrap="nowrap" class="formTD"><bean:message key="property.course"/></td>
            <td nowrap="nowrap" class="formTD"><jsp:include page="selectExecutionCourseList.jsp"/></td>
          </tr>
        </table>
          <html:submit value="Procurar" styleClass="inputbutton">
           	  <bean:message key="label.search"/>
          </html:submit>
        </html:form>    
