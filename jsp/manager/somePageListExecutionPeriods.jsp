<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<%@ taglib uri="/WEB-INF/struts-faces.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<ft:tilesView definition="definition.manager.masterPage" attributeName="body-inline">
		<h:outputText value="Execution Periods: "/>
		<br />
		<h:dataTable value="#{executionPeriods.executionPeriods}" var="executionPeriod">
			<h:column>
				<h:outputText value="#{executionPeriod.name}"/>
			</h:column>
			<h:column>
				<h:outputText value="#{executionPeriod.infoExecutionYear.year}"/>
			</h:column>
		</h:dataTable>
</ft:tilesView>