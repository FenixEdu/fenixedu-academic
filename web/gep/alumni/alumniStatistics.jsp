<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<html:xhtml/>

<h2><bean:message key="label.alumni" bundle="GEP_RESOURCES" /></h2>

<bean:write name="statistics1" />
<br/>
<bean:write name="statistics2" />
<br/>
<bean:write name="statistics3" />
<br/>
<bean:write name="statistics4" />
<br/>
<bean:write name="statistics5" />

