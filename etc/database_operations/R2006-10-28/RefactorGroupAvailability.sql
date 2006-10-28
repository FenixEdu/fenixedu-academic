UPDATE `AVAILABILITY_POLICY`
	SET `OJB_CONCRETE_CLASS` = 'net.sourceforge.fenixedu.domain.functionalities.ExpressionGroupAvailability';
	
ALTER TABLE `AVAILABILITY_POLICY`
	ADD COLUMN `TARGET_GROUP` BLOB;
