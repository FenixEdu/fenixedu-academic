<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<html:xhtml/>
	
	<script>
		$(document).ready( function () {
			$('#firstItem').prependTo($('#perfnav ul:eq(1)'));
			$('#perfnav ul:first').remove();
		});
	</script>

	<logic:present name="site">

		<ul>
			<li id="firstItem">
				<bean:define id="siteId" name="site" property="externalId"/>
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