<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<h2><bean:message key="title.candidacies" /></h2>

<fr:view name="UserView" property="person.candidacies" schema="candidacy.short" >
    <fr:layout name="tabular">
		<fr:property name="linkFormat(viewCandidacyDetails)" value="<%="/viewCandidacies.do?method=viewDetail&candidacyID=${idInternal}" %>"/>
		<fr:property name="key(viewCandidacyDetails)" value="link.viewCandidacyDetails"/>
    </fr:layout>
</fr:view>
	