package org.fenixedu.parking.ui.Action;

import org.apache.struts.actions.ForwardAction;

import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "parkingManager", path = "/index", parameter = "parkingManagerIndex")
public class VoidActionForParkingManager extends ForwardAction {
}