<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<br />
<br />
<div align="right">Chefe de Secção,</div>
<br />
<div align="right"><p>(Josefina Miranda)</p></div>
<br />
<p><bean:write name="<%= SessionConstants.DATE %>" /></p>