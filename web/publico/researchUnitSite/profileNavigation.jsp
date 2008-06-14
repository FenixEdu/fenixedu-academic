<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<html:xhtml/>

	<logic:present name="site">

		<ul>
			<li>
				<bean:define id="siteId" name="site" property="idInternal"/>
				<html:link page="<%= "/researchSite/viewResearchUnitSite.do?method=frontPage&amp;siteID=" + siteId %>">
					<bean:message key="label.siteUnit.section.start" bundle="MESSAGING_RESOURCES"/>
				</html:link>
			</li>
		</ul>
		
        <fr:view name="site" layout="unit-top-menu">
            <fr:layout>
                <fr:property name="sectionUrl" value="/researchSite/viewResearchUnitSite.do?method=section"/>
                <fr:property name="contextParam" value="siteID"/>
            </fr:layout>
        </fr:view>
               
    </logic:present>