<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
<br />
<br />
<div align="right" style="margin-right: 100px; color: #000;">Dr. Nuno Riscado</div>
<div align="right" style="margin-right: 100px; color: #000;">(Coordenador do Núcleo de Pós-Graduação e Formação Contínua)</div>
<br />
<p><bean:write name="<%= PresentationConstants.DATE %>" /></p>