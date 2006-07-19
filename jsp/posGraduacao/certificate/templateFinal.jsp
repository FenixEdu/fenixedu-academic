<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<br />
<br />
<div align="right" style="margin-right: 100px;">Dr. Nuno Riscado</div>
<div align="right" style="margin-right: 100px;">(Coordenador do Núcleo de Pós-Graduação e FORMAÇÃO CONTÍNUA)</div>
<br />
<p><bean:write name="<%= SessionConstants.DATE %>" /></p>