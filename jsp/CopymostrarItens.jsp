<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

    <logic:present name="Seccao">
      <h1><bean:write name="Seccao" property="nome"/></h1>
      <logic:iterate id="item" name="Seccao" property="itens">
        <logic:equal name="item" property="urgente" value="true"><font color="red"></logic:equal>
        <h3><bean:write name="item" property="nome"/></h3>
        <bean:write name="item" property="informacao"/><br/>
        <logic:equal name="item" property="urgente" value="true"></font></logic:equal>
      </logic:iterate>
	</logic:present>