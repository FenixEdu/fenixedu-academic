<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>

<style>@import url(<%= request.getContextPath() %>/CSS/dotist_degreeStructure.css);</style>

<ft:tilesView definition="bolonhaManager.masterPage" attributeName="body-inline">
	<f:loadBundle basename="ServidorApresentacao/BolonhaManagerResources" var="bolonhaBundle"/>
	<f:loadBundle basename="ServidorApresentacao/EnumerationResources" var="enumerationBundle"/>
	
	<h:outputText value="<i>#{CurricularCourseManagement.degreeCurricularPlan.name}" escape="false"/>
	<h:outputText value=" (#{enumerationBundle[CurricularCourseManagement.degreeCurricularPlan.curricularStage.name]})</i>" escape="false"/>
	<h:outputText value="<h2>#{bolonhaBundle['buildCurricularPlan']}</h2>" escape="false"/>

	<h:outputText value="#{bolonhaBundle['view.structure.organized.by']}: " escape="false"/>
	<h:outputLink value="buildCurricularPlan.faces">
		<h:outputText value="#{bolonhaBundle['groups']}" />
		<f:param name="degreeCurricularPlanID" value="#{CurricularCourseManagement.degreeCurricularPlanID}"/>
		<f:param name="organizeBy" value="groups"/>
	</h:outputLink>
	<h:outputText value=" | " escape="false"/>
	<h:outputLink value="buildCurricularPlan.faces">
		<h:outputText value="#{bolonhaBundle['curricularYear']}/#{bolonhaBundle['semester']}" />
		<f:param name="degreeCurricularPlanID" value="#{CurricularCourseManagement.degreeCurricularPlanID}"/>
		<f:param name="organizeBy" value="years"/>		
	</h:outputLink>

	<h:outputText value="</br></br>* " escape="false"/>
	<h:outputLink value="editCurricularPlanStructure.faces">
		<h:outputText value="#{bolonhaBundle['edit.curricularPlan.structure']}" />
		<f:param name="degreeCurricularPlanID" value="#{CurricularCourseManagement.degreeCurricularPlanID}"/>
	</h:outputLink>
	<h:outputText value="</br>" escape="false"/>

	<h:outputText escape="false" value="<input id='degreeCurricularPlanID' name='degreeCurricularPlanID' type='hidden' value='#{CurricularCourseManagement.degreeCurricularPlanID}'"/><br/>
	<fc:degreeCurricularPlanRender dcp="#{CurricularCourseManagement.degreeCurricularPlan}" onlyStructure="false" toEdit="true" organizeBy="<%=request.getParameter("organizeBy")%>"/>

	<h:outputText value="</br></br>" escape="false"/>
	<h:form>
		<h:outputText escape="false" value="<input id='degreeCurricularPlanID' name='degreeCurricularPlanID' type='hidden' value='#{CurricularCourseManagement.degreeCurricularPlanID}'"/><br/>
		<h:commandButton styleClass="inputbutton" value="#{bolonhaBundle['return']}"
			action="curricularPlansManagement"/>
	</h:form>
	
</ft:tilesView>

<table class="style2">
	<tr>
		<th class="aright" width="180px">(<a
			href="createCourseGroup.faces?degreeCurricularPlanID=402&amp;parentCourseGroupID=16302">Criar
		Grupo</a>)</th>
	</tr>
</table>
<table class="style2">
	<tr>
		<th colspan="3" class="aleft"><strong>Arquitectura e Sistemas
		Distribuídos </strong></th>
		<th class="aright" width="170px">(<a
			href="createCourseGroup.faces?degreeCurricularPlanID=402&amp;parentCourseGroupID=16309">Criar
		Grupo</a> , <a
			href="editCourseGroup.faces?degreeCurricularPlanID=402&amp;courseGroupID=16309">Editar</a>
		, <a
			href="deleteCourseGroup.faces?degreeCurricularPlanID=402&amp;courseGroupID=16309">Apagar</a>)
		</th>
	</tr>
</table>
<table class="style2 indent1">
	<tr>
		<th colspan="3" class="aleft"><strong>Arquitectura e Sistemas
		Operativos em ASD </strong></th>
		<th class="aright" width="150px">(<a
			href="createCourseGroup.faces?degreeCurricularPlanID=402&amp;parentCourseGroupID=16310">Criar
		Grupo</a> , <a
			href="editCourseGroup.faces?degreeCurricularPlanID=402&amp;courseGroupID=16310">Editar</a>
		, <a
			href="deleteCourseGroup.faces?degreeCurricularPlanID=402&amp;courseGroupID=16310">Apagar</a>)
		</th>
	</tr>
</table>
