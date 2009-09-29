package net.sourceforge.fenixedu.presentationTier.Action.manager.ectsComparabilityTables;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.degreeStructure.EctsConversionTable;

public class ECTSTableImportStatusLine implements Serializable {
    public static enum TableImportStatus {
	SUCCESS, DUPLICATE_EQUAL, DUPLICATE_DIFFERENT;
    }

    private static final long serialVersionUID = -4819713337084034919L;

    private final EctsConversionTable table;

    private final TableImportStatus status;

    public ECTSTableImportStatusLine(EctsConversionTable table, TableImportStatus status) {
	this.table = table;
	this.status = status;
    }

    public EctsConversionTable getTable() {
	return table;
    }

    public TableImportStatus getStatus() {
	return status;
    }
}
