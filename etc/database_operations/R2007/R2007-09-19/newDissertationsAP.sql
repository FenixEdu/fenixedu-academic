INSERT INTO `AVAILABILITY_POLICY` (`OJB_CONCRETE_CLASS`, `KEY_ROOT_DOMAIN_OBJECT`, `KEY_ACCESSIBLE_ITEM`, `EXPRESSION`, `TARGET_GROUP`) 
SELECT 'net.sourceforge.fenixedu.domain.functionalities.ExpressionGroupAvailability', 1, `ID_INTERNAL`, 'ifTrue($I(degreeID, Degree).anyThesisAvailable)', 'ifTrue($I(degreeID, Degree).anyThesisAvailable)' FROM `ACCESSIBLE_ITEM` 
WHERE `UUID` = '72551ac3-ed0a-4e5a-8d1e-6d49dd60eb28';
