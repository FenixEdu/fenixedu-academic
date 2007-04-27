<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<html:xhtml/>

	<logic:present name="site">

        <fr:view name="site" layout="unit-top-menu">
            <fr:layout>
                <fr:property name="sectionUrl" value="/researchSite/viewResearchUnitSite.do?method=section"/>
                <fr:property name="contextParam" value="siteID"/>
                
                <fr:property name="empty">
                	<ul>
                		<li>
                			<a href="#" style="visibility: hidden;">&nbps;</a>
                		</li>
                	</ul>
                </fr:property>
            </fr:layout>
        </fr:view>
               
    </logic:present>