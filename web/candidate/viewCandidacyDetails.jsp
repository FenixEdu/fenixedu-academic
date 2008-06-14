<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<p class="infoop"><span><h2 class="inline"><bean:message key="label.candidacy.title.detail" /></h2></span></p>
<fr:view name="candidacy" schema="candidacy.short" >
    <fr:layout name="tabular">
    </fr:layout>
</fr:view>

<p class="infoop"><span><h2 class="inline"><bean:message key="label.candidacy.title.activeSituation" /></h2></span></p>
<fr:view name="candidacy" property="activeCandidacySituation" schema="candidacySituation.full" >
    <fr:layout name="tabular">
    </fr:layout>
</fr:view>

<br/>

<bean:define id="candidacyID" name="candidacy" property="idInternal" />
<logic:equal name="canChangePersonalData" value="true">
	<html:link action="<%="/changePersonalData.do?method=prepare&amp;candidacyID=" + candidacyID%>"><bean:message  key="link.changePersonalData"/></html:link>
	<br/>
</logic:equal>

<html:link action="<%="/viewCandidacies.do?method=prepareUploadDocuments&amp;candidacyID=" + candidacyID%>"><bean:message  key="link.candidacyDocuments"/></html:link>

	