<%@ page language="java" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>


       <table width="100%" border="0" cellpadding="0" cellspacing="0">
          <tr>
            <td bgcolor="#FFFFFF" class="infoselected">
			  <strong><bean:message key="property.name"/></strong> <bean:write name="infoStudent" property="infoPerson.nome"/>
			  <br/>
			  <strong><bean:message key="property.number"/></strong> <bean:write name="infoStudent" property="number"/>
            </td>
          </tr>
		</table>
        <br />
	<center><font color='#034D7A' size='5'> <b> <bean:message key="title.student.view.schedule"/> </b> </font></center>
	<br/>

   	<app:gerarHorario name="<%= SessionConstants.LESSON_LIST_ATT %>"/> 
