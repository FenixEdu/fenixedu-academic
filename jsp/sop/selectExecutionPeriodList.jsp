<%@ page language="java" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
  <html:select property="index" size="1">
     <html:options	property="value" 
     				labelProperty="label" 
					collection="<%= SessionConstants.LABELLIST_EXECUTIONPERIOD%>" />
  </html:select>
