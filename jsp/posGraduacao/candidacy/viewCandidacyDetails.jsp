<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<p class="infoop"><span><h2 class="inline"><bean:message key="label.candidacy.title.detail" bundle="ADMIN_OFFICE_RESOURCES"/></h2></span></p>
<fr:view name="candidacy" schema="candidacy.short" >
    <fr:layout name="tabular">
    </fr:layout>
</fr:view>

<p class="infoop"><span><h2 class="inline"><bean:message key="label.candidacy.title.activeSituation" bundle="ADMIN_OFFICE_RESOURCES"/></h2></span></p>
<fr:view name="candidacy" property="activeCandidacySituation" schema="candidacySituation.full" >
    <fr:layout name="tabular">
    </fr:layout>
</fr:view>
