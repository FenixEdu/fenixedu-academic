<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<html:errors/>
<logic:iterate id="sitio" name="Sitios">
  <html:link page="/obterSitio.do" paramId="nomeSitio" paramName="sitio"><%= sitio %></html:link><br/>
</logic:iterate>
