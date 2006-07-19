<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<bean:define id="conclusiondate" name="<%= SessionConstants.CONCLUSION_DATE %>" />
<bean:define id="finalResult" name="<%= SessionConstants.FINAL_RESULT%>" />
<p>
Da acta da prova consta o seguinte resultado atribuído pelo júri legalmente constituído: <b><bean:message name="finalResult" bundle="ENUMERATION_RESOURCES"/></b> pelo que<bean:write name="notString"/>tem direito
ao grau académico de MESTRE.
</p>