package net.sourceforge.fenixedu.presentationTier.gwt.manager.UIPlayground.listStudents.client;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Composite;

public class StudentCollectionWidget extends Composite {

    public class CollectionProvider implements StudentCollectionDataProvider {

        private final RetrieveSpecificStudentSetAsync studentListService;

        private List<String> studentNames;

        public CollectionProvider() {

            studentListService = (RetrieveSpecificStudentSetAsync) GWT.create(RetrieveSpecificStudentSet.class);

            ServiceDefTarget target = (ServiceDefTarget) studentListService;

            String moduleRelativeURL = GWT.getModuleBaseURL() + "RetrieveSpecificStudentSet.gwt";
            target.setServiceEntryPoint(moduleRelativeURL);
        }

        @Override
        public void updateRowData(final RowDataAcceptor acceptor) {

            studentListService.getStudentNames(new AsyncCallback<List<String>>() {

                @Override
                public void onFailure(Throwable caught) {
                    acceptor.failed(caught);
                }

                @Override
                public void onSuccess(List<String> pupilsList) {
                    studentNames = pupilsList;
                    pushResults(acceptor, studentNames);
                }
            });

        }

        private void pushResults(RowDataAcceptor acceptor, List<String> pupilsList) {

            String[][] rows = new String[pupilsList.size()][];
            int i = 0;
            for (String pupil : pupilsList) {
                rows[i] = new String[1];
                rows[i][0] = pupil;
                i++;
            }
            acceptor.accept(rows);
        }
    }

    private final CollectionProvider colProvider = new CollectionProvider();

    private final DynaTableWidget dynaTable;

    public StudentCollectionWidget() {
        String[] columns = new String[] { "Name" };
        String[] styles = new String[] { "name" };
        dynaTable = new DynaTableWidget(colProvider, columns, styles);
        initWidget(dynaTable);
    }

}
