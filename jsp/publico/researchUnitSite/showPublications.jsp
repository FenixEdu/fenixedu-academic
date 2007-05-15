<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>


<h1 class="mbottom03 cnone">
	<fr:view name="site" property="unit.nameWithAcronym"/>
</h1>

<h2 class="mtop15"><bean:message key="researcher.viewCurriculum.publicationsTitle" bundle="RESEARCHER_RESOURCES"/></h2>

<ul>
<logic:iterate id="publication" name="publications">
	<bean:define id="publicationID" name="publication" property="idInternal"/>
	
	<li>
	<fr:view name="publication" layout="values" schema="publication.TitleAndAuthors">
		<fr:destination name="view.publication" path="<%= "/showResearchResult.do?method=showPublication&amp;resultId=" +  publicationID %>"/>
	</fr:view>
	</li>
</logic:iterate>
</ul>