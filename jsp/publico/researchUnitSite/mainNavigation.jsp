<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<html:xhtml/>


<logic:present name="<%= net.sourceforge.fenixedu.domain.functionalities.FunctionalityContext.CONTEXT_KEY %>">
	<fr:view name="<%= net.sourceforge.fenixedu.domain.functionalities.FunctionalityContext.CONTEXT_KEY %>" property="selectedContainer" 
		layout="unit-side-menu">
            <fr:layout>
                <fr:property name="sectionUrl" value="/researchSite/viewResearchUnitSite.do?method=section"/>
                <fr:property name="contextParam" value="siteID"/>
            </fr:layout>
        </fr:view>
    </logic:present>
    
<div class="usitebannerlat">
	<fr:view name="site" property="sideBanner" layout="html" type="net.sourceforge.fenixedu.util.MultiLanguageString"/>
</div>