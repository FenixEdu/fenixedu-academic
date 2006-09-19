<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>

<h2><bean:message bundle="MANAGER_RESOURCES" key="title.transaction.logs"/></h2>
<br />

<bean:define id="url"><%= request.getContextPath() %>/manager/transactionSystem.do?method=viewChart</bean:define>
<html:img align="middle" src="<%= url %>" altKey="clip_image002" bundle="IMAGE_RESOURCES" />