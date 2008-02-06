<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<html:xhtml/>

<h2>
	<bean:message key="link.manage.card.generation.consult.category.codes"/>
</h2>

<br/>

<table class="tstyle4 thlight mtop05">
  <tr>
    <th><bean:message bundle="CARD_GENERATION_RESOURCES" key="label.category.code"/></th>
    <th><bean:message bundle="CARD_GENERATION_RESOURCES" key="label.line.format"/></th>
    <th><bean:message bundle="CARD_GENERATION_RESOURCES" key="label.category.description"/></th>
    <th><bean:message bundle="CARD_GENERATION_RESOURCES" key="label.degree.type"/></th>
  </tr>
	<logic:iterate id="category" name="categories">
	  	<tr>
    		<td><bean:write name="category" property="code"/></td>
    		<td><bean:message bundle="ENUMERATION_RESOURCES" name="category" property="lineLayout.name"/></td>
    		<td><bean:message bundle="ENUMERATION_RESOURCES" name="category" property="name"/></td>
    		<td>
   				<logic:iterate id="degreeType" indexId="degreeTypeIndex" name="category" property="degreeTypes">
   					<logic:notEqual name="degreeTypeIndex" value="0">
   						<br/>
   					</logic:notEqual>
					<html:link page="/manageCardGeneration.do?method=showDegreeCodesAndLabels" paramId="degreeType" paramName="degreeType" paramProperty="name">
	   					<bean:message bundle="ENUMERATION_RESOURCES" name="degreeType" property="name"/>
					</html:link>
					<bean:define id="problemsMap" name="problemsMap" type="java.util.Map"/>
					<% if (((Boolean) problemsMap.get(degreeType)).booleanValue()) { %>
						<font color="red">
							<bean:message bundle="CARD_GENERATION_RESOURCES" key="message.manage.card.generation.degree.type.has.problems"/>
						</font>
					<% } %>
   				</logic:iterate>
    		</td>
  		</tr>
	</logic:iterate>
</table>
