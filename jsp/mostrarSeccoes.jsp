<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

  <logic:iterate id="seccao" name="Sitio" property="seccoes">
<%--    <logic:equal name="Seccao" property="nome" value="<%= seccao %>">
      <%= seccao %>
    </logic:equal>
    <logic:notEqual name="Seccao" property="nome" value="<%= seccao %>">--%>
      <html:link page="/obterSeccao.do" paramId="nomeSeccao" paramName="seccao"><%= seccao %></html:link>
<%--    </logic:notEqual>--%>
    <br/>
  </logic:iterate>
