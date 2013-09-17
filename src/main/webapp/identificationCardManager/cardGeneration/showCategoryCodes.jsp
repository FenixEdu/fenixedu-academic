<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<html:xhtml/>

<em><bean:message key="link.manage.card.generation.header"/></em>
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

<br/>

<h2><bean:message bundle="CARD_GENERATION_RESOURCES" key="link.manage.card.generation.edit.professionalCategories"/></h2>

<table class="tstyle4 tdtop thlight mtop05">
  <tr>
    <th><bean:message bundle="CARD_GENERATION_RESOURCES" key="label.professionalCategory.giafId"/></th>
    <th><bean:message bundle="CARD_GENERATION_RESOURCES" key="label.professionalCategory.name"/></th>
    <th><bean:message bundle="CARD_GENERATION_RESOURCES" key="label.professionalCategory.categoryType"/></th>
    <th><bean:message bundle="CARD_GENERATION_RESOURCES" key="label.professionalCategory.weight"/></th>
    <th></th>
    <th><bean:message bundle="CARD_GENERATION_RESOURCES" key="label.professionalCategory.identificationCardLabel"/></th>
    <th></th>
  </tr>
	<logic:iterate id="professionalCategory" name="professionalCategories">
		<bean:size id="count" name="professionalCategory" property="giafProfessionalDataSet"/>
		<logic:greaterThan name="count" value="0">
			<tr>
				<td>
					<bean:write name="professionalCategory" property="giafId"/>
				</td>
				<td>
					<logic:empty name="professionalCategory" property="identificationCardLabel">
						<font color="red">
							<bean:write name="professionalCategory" property="name.content"/>
						</font>
					</logic:empty>
					<logic:notEmpty name="professionalCategory" property="identificationCardLabel">
						<bean:write name="professionalCategory" property="name.content"/>
					</logic:notEmpty>
				</td>
				<td>
					<bean:write name="professionalCategory" property="categoryType"/>
				</td>
				<td>
					<bean:write name="professionalCategory" property="weight"/>
				</td>
				<td>
					<bean:write name="professionalCategory" property="giafProfessionalDataCount"/>
				</td>
				<td>
					<bean:write name="professionalCategory" property="identificationCardLabel"/>
				</td>
    			<td>
	    			<html:link page="/manageCardGeneration.do?method=editProfessionalCategory" paramId="professionalCategoryID" paramName="professionalCategory" paramProperty="externalId">
		    			<bean:message bundle="CARD_GENERATION_RESOURCES" key="link.edit"/>
   					</html:link>
    			</td>
			</tr>
		</logic:greaterThan>
	</logic:iterate>
</table>

