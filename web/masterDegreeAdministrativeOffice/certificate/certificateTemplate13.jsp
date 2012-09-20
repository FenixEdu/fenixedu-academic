<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
<bean:define id="conclusiondate" name="<%= PresentationConstants.CONCLUSION_DATE %>" />
<bean:define id="finalResult" name="<%= PresentationConstants.FINAL_RESULT%>" />
<p>
Da acta da prova consta o seguinte resultado atribuído pelo júri legalmente constituído: <b><bean:message name="finalResult" bundle="ENUMERATION_RESOURCES"/></b> pelo que<bean:write name="notString"/>tem direito
ao grau académico de MESTRE, ao abrigo do D.L. 216/92 de 13 de Outubro.
</p>