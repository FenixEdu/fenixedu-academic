update CONTENT SET WRITERS = CONCAT(WRITERS,' || unitEmployees($I(61070,\'organizationalStructure.Unit\'))') WHERE ID_INTERNAL IN (28366, 28365, 28362, 28361, 28360);

update CONTENT SET READERS = CONCAT(READERS,' || unitEmployees($I(61070,\'organizationalStructure.Unit\'))') WHERE ID_INTERNAL IN (28366, 28365, 28362, 28361, 28360);

