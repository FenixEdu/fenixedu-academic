<%@page import="net.sourceforge.fenixedu.presentationTier.servlets.filters.functionalities.FilterFunctionalityContext"%>
<%@ page language="java" %>

<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>


<logic:present name="<%= net.sourceforge.fenixedu.domain.functionalities.FunctionalityContext.CONTEXT_KEY %>">
	<fr:view name="<%= net.sourceforge.fenixedu.domain.functionalities.FunctionalityContext.CONTEXT_KEY %>" property="selectedContainer" 
		layout="unit-side-menu">
            <fr:layout>
		        <fr:property name="sectionUrl" value="<%= request.getContextPath() + "/publico/department/departmentSite.do?method=section" %>"/>
            </fr:layout>
	</fr:view>
</logic:present>

<div style="margin-top: 50px;">
    <fr:view name="site" property="sideBanner" layout="html" type="pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString"/>
</div>