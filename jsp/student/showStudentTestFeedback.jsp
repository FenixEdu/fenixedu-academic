<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<tiles:insert page="/fenixLayout_2col.jsp" flush="true">
  <tiles:put name="title" value=".IST - Portal do Estudante" />
  <tiles:put name="serviceName" value="Portal do Estudante" />
  <tiles:put name="navGeral" value="/student/commons/commonNavGeralStudent.jsp" />
  <tiles:put name="navLocal" value="/student/testsNavbar.jsp" type="page"/>
  <tiles:put name="body" value="/student/showStudentTestFeedback_bd.jsp" />
  <tiles:put name="footer" value="/student/commons/commonFooterStudent.jsp" />
</tiles:insert>