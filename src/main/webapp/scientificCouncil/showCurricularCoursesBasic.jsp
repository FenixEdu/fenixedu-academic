<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<tiles:insert definition="df.layout.two-column.teacher" beanName="" flush="true">
  <tiles:put name="title" value="Conselho Científico" />
  <tiles:put name="serviceName" value="Portal do Conselho Científico" />
  <tiles:put name="navLocal" value="/scientificCouncil/navigation.jsp" />
  <tiles:put name="navGeral" value="/scientificCouncil/commonNavGeral.jsp" />
  <tiles:put name="body" value="/scientificCouncil/showCurricularCoursesBasic_bd.jsp" />
  <tiles:put name="footer" value="/copyright.jsp" />
</tiles:insert>
