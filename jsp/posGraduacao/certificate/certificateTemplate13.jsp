<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<bean:define id="conclusiondate" name="<%= SessionConstants.CONCLUSION_DATE %>" />
<bean:define id="finalResult" name="<%= SessionConstants.FINAL_RESULT%>" />
<p>
Da acta da prova consta o seguinte resultado atribuído pelo júri legalmente constituído: <b><bean:write name="finalResult" /></b> pelo que<bean:write name="notString"/>tem direito
ao grau académico de MESTRE.
</p>