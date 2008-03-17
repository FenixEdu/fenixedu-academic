delete from RESOURCE_ALLOCATION
where RESOURCE_ALLOCATION.OJB_CONCRETE_CLASS = 'net.sourceforge.fenixedu.domain.space.PersonSpaceOccupation'
and RESOURCE_ALLOCATION.BEGIN is null;
