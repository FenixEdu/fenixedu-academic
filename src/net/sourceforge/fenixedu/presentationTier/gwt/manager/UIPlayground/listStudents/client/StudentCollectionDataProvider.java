package net.sourceforge.fenixedu.presentationTier.gwt.manager.UIPlayground.listStudents.client;

public interface StudentCollectionDataProvider {

	interface RowDataAcceptor {
		void accept(String[][] rows);

		void failed(Throwable caught);
	}

	void updateRowData(RowDataAcceptor acceptor);

}
