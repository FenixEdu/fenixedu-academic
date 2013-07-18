package net.sourceforge.fenixedu.applicationTier.Servico.teacher.tests;


import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.tests.Positionable;
import pt.ist.fenixWebFramework.services.Service;

public class SwitchPosition {
    @Service
    public static void run(Positionable positionable, Integer relativePosition) throws FenixServiceException {
        positionable.switchPosition(relativePosition);
    }
}