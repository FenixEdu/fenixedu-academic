<%@ page language="java" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>


	<center><font color='#034D7A' size='5'> <b> <bean:message key="title.room"/> 
																	  <bean:write name="publico.infoRoom" property="nome"/> </b> </font></center>
	<br/>

   	<app:gerarHorario name="<%= SessionConstants.LESSON_LIST_ATT %>"/> 
