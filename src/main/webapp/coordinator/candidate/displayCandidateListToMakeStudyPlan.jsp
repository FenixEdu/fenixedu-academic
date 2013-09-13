<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<tiles:insert definition="df.layout.two-column" beanName="" flush="true">
  <tiles:put name="title" value="Intranet" />
  <tiles:put name="serviceName" value="Portal de Coordenador" />
  <tiles:put name="navLocal" value="/coordinator/coordinatorMainMenu.jsp" />
  <tiles:put name="navGeral" value="/coordinator/commonNavGeralCoordinator.jsp" />
  <tiles:put name="body-context" value="/coordinator/context.jsp"/>  
  <tiles:put name="body" value="/coordinator/candidate/displayCandidateListToMakeStudyPlan_bd.jsp" />
  <tiles:put name="footer" value="/copyright.jsp" />
</tiles:insert>