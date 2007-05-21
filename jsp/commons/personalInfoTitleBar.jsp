<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>
<%@ taglib uri="/WEB-INF/taglibs-string.tld" prefix="str" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>

Utilizador: <bean:write name="<%= SessionConstants.U_VIEW %>" property="person.nickname"/> - <dt:format pattern="dd.MM.yyyy"><dt:currentTime/></dt:format>
