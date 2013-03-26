<%@page import="pt.ist.fenixframework.DomainObject"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>


<logic:present role="MANAGER">

<em>Entidades Externas para Bolsas</em>
<h2>Listar Entidades</h2>
<logic:notEmpty name="externalScholarshipProviders">
<table class="tstyle1">
<tr>
    <th >
      Nome
    </th>
    <th >
      NIF
    </th>
    <th>
      Acções
    </th>
  </tr>
<logic:iterate id="provider" name="externalScholarshipProviders">
	<tr>
	<td>
		<bean:write name="provider" property="name"/>
	</td>
	<td>
		<bean:define id="partySocialSecurityNumber" name="provider" property="partySocialSecurityNumber" />
		<bean:write name="partySocialSecurityNumber" property="socialSecurityNumber"/>
	</td>
	<td>
		<logic:empty name="provider" property="phdGratuityExternalScholarshipExemption">
			<html:link action="<%= "externalScholarshipProvider.do?method=remove&provider=" + ((DomainObject)provider).getExternalId() %>">Remover Entidade Externa</html:link>
		</logic:empty>
	</td>
	</tr>
</logic:iterate>
</table>
</logic:notEmpty>
<html:link action="externalScholarshipProvider.do?method=add">Adicionar Entidade</html:link>
</logic:present>

