<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<%@ taglib uri="/WEB-INF/struts-faces.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<f:view>
	<bean:define id="body" type="java.lang.String">
			<h:outputText value="Hello World :o0)"/>
			<br />
			<h:outputText value="#{infoExecutionPeriod.name}"/>
			<br />
			<br />
			<h:form>
				<h:commandButton value="List Execution Periods" action="listExecutionPeriods"/>
			</h:form>
	</bean:define>

	<tiles:insert definition="definition.manager.masterPage" flush="false">
		<tiles:put name="body-inline" value="<%= body %>"/>
	</tiles:insert>
</f:view>