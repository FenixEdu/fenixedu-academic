UPDATE FUNCTIONALITY SET PATH = "/sop/paginaPrincipal.do" 
	WHERE UUID = "6a97cab2-491d-4e9f-a876-3a15d1d1033f";
	
UPDATE FUNCTIONALITY SET PATH = "/departmentMember/index.do", PREFIX = "/departmentMember"
	WHERE UUID = "123c9a8e-de47-4b4f-9543-a38116c03b54";

UPDATE FUNCTIONALITY SET PARAMETERS = "degreeId,degreeCurricularPlanId,curricularCourseId,executionCourseId,professorShipTeachersIds,responsibleTeachersIds" 
	WHERE UUID = "81bcaf76-48d3-46e4-9698-87a5eb5b2a0d";