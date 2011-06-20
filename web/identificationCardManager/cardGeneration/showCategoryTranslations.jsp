<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<html:xhtml/>

<em>Cartões de Identificação</em>
<h2><bean:message key="link.manage.card.generation.consult.category.codes"/></h2>

<p><html:link page="/manageCardGeneration.do?method=firstPage">« Voltar</html:link></p>

<table class="tstyle4 tdtop thlight mtop05">
  <tr>
    <th><bean:message bundle="CARD_GENERATION_RESOURCES" key="label.category.code"/></th>
    <th><bean:message bundle="CARD_GENERATION_RESOURCES" key="label.line.format"/></th>
    <th><bean:message bundle="CARD_GENERATION_RESOURCES" key="label.category.description"/></th>
    <th><bean:message bundle="CARD_GENERATION_RESOURCES" key="label.degree.type"/></th>
    <th><bean:message bundle="CARD_GENERATION_RESOURCES" key="label.subcategory.code"/></th>
  </tr>
	<logic:iterate id="category" name="categories">
	  	<tr>
    		<td class="acenter"><bean:write name="category" property="code"/></td>
    		<td><bean:message bundle="ENUMERATION_RESOURCES" name="category" property="lineLayout.name"/></td>
    		<td><bean:message bundle="ENUMERATION_RESOURCES" name="category" property="name"/></td>
    		<td>
   				<logic:iterate id="degreeType" indexId="degreeTypeIndex" name="category" property="degreeTypes">
   					<logic:notEqual name="degreeTypeIndex" value="0">
   						<br/>
   					</logic:notEqual>
   					<logic:notEqual name="degreeType" property="name" value="EMPTY">
						<html:link page="/manageCardGeneration.do?method=showDegreeCodesAndLabels" paramId="degreeType" paramName="degreeType" paramProperty="name">
	   						<bean:message bundle="ENUMERATION_RESOURCES" name="degreeType" property="name"/>
						</html:link>
						<bean:define id="problemsMap" name="problemsMap" type="java.util.Map"/>
						<% if (((Boolean) problemsMap.get(degreeType)).booleanValue()) { %>
							<font color="red">
								<bean:message bundle="CARD_GENERATION_RESOURCES" key="message.manage.card.generation.degree.type.has.problems"/>
							</font>
						<% } %>
					</logic:notEqual>
   					<logic:equal name="degreeType" property="name" value="EMPTY">
						<html:link page="/manageCardGeneration.do?method=showDegreeCodesAndLabels&degreeType=EMPTY">
	   						<bean:message bundle="ENUMERATION_RESOURCES" key="EMPTY.desc"/>
						</html:link>
						<bean:define id="problemsMap" name="problemsMap" type="java.util.Map"/>
						<% if (((Boolean) problemsMap.get(degreeType)).booleanValue()) { %>
							<font color="red">
								<bean:message bundle="CARD_GENERATION_RESOURCES" key="message.manage.card.generation.degree.type.has.problems"/>
							</font>
						<% } %>
					</logic:equal>
   				</logic:iterate>
    		</td>
    		<td>
    			<logic:equal name="category" property="code" value="71">
    				00 
    			</logic:equal>
    			<logic:equal name="category" property="code" value="72">
    				01 - Avençados
    				<br/>
    				05 - AEIST
    				<br/>
    				06 - ADIST
    			</logic:equal>
    			<logic:equal name="category" property="code" value="73">
    				00
    			</logic:equal>
    			<logic:equal name="category" property="code" value="81">
    				00
    				<br/>
    				01 - Docentes &lt; 50%
    			</logic:equal>
    			<logic:equal name="category" property="code" value="82">
    				00
    			</logic:equal>
    			<logic:equal name="category" property="code" value="83">
    				00
    				<br/>
    				01 - Docentes
    			</logic:equal>
    			<logic:equal name="category" property="code" value="92">
    				00
    				<br/>
    				01 - Acesso especial
    			</logic:equal>
    			<logic:equal name="category" property="code" value="94">
    				00
    			</logic:equal>
    			<logic:equal name="category" property="code" value="95">
    				00
    				<br/>
    				01 - Pós-graduação
    			</logic:equal>
    			<logic:equal name="category" property="code" value="96">
    				01 - IST
    				<br/>
    				03 - Bolseiro ou Investigador de um Centro de Investigação
    			</logic:equal>
    			<logic:equal name="category" property="code" value="97">
    				00
    			</logic:equal>
    			<logic:equal name="category" property="code" value="98">
    				00
    			</logic:equal>
    			<logic:equal name="category" property="code" value="99">
    				00
    			</logic:equal>
    		</td>
  		</tr>
	</logic:iterate>
</table>
