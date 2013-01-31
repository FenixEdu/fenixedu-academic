package net.sourceforge.fenixedu.domain.phd.migration.common.exceptions;

public class PhdMigrationException extends RuntimeException {

	public PhdMigrationException(String error) {
		super(error);
	}

	public PhdMigrationException() {
	}

	public String getKey() {
		return "label.phd.migration.exception." + this.getClass().getName();
	}

}
