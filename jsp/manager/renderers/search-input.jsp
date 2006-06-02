<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<fr:form action="/renderers/searchPersons.do?method=search" encoding="multipart/form-data">
    <fr:edit name="bean" schema="xxx">
        <fr:hidden slot="number" name="bean" property="personName"/>
        <fr:destination name="cancel" path="/renderers/index.do" redirect="true"/>
    </fr:edit>
    
    <html:submit>Submeterae</html:submit>
    <html:cancel>Vamos lá cancelar istoae</html:cancel>
</fr:form>
