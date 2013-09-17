<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>

<p class="infoop"><span><h2 class="inline"><bean:message key="label.candidacy.title.detail" /></h2></span></p>
<fr:view name="candidacy" schema="candidacy.short" >
    <fr:layout name="tabular">
    </fr:layout>
</fr:view>

<p class="infoop"><span><h2 class="inline"><bean:message key="label.candidacy.title.activeSituation" /></h2></span></p>
<fr:view name="candidacy" property="activeCandidacySituation" schema="candidacySituation.candidate.view" >
    <fr:layout name="tabular">
    </fr:layout>
</fr:view>

<br/>

<bean:define id="candidacyID" name="candidacy" property="externalId" />
<logic:equal name="canChangePersonalData" value="true">
	<html:link action="<%="/changePersonalData.do?method=prepare&amp;candidacyID=" + candidacyID%>"><bean:message  key="link.changePersonalData"/></html:link>
	<br/>
</logic:equal>

<html:link action="<%="/viewCandidacies.do?method=prepareUploadDocuments&amp;candidacyID=" + candidacyID%>"><bean:message  key="link.candidacyDocuments"/></html:link>

	