<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>
<%@ taglib uri="/WEB-INF/taglibs-string.tld" prefix="str" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<div id="logged">
	<div class="user">
		<strong>Utilizador:</strong> 
		<str:getPrechomp delimiter=" ">
			<bean:write name="<%= SessionConstants.U_VIEW %>" property="fullName"/>
		</str:getPrechomp> 
		<str:getChomp delimiter=" ">
			<bean:write name="<%= SessionConstants.U_VIEW %>" property="fullName"/>
		</str:getChomp>
	</div>
	<div class="info"><dt:format pattern="dd.MM.yyyy"><dt:currentTime/></dt:format></div>
	<div class="clear"></div>
</div>