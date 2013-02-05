package net.sourceforge.fenixedu.presentationTier.gwt.manager.UIPlayground.listStudents.client;

import net.sourceforge.fenixedu.presentationTier.gwt.manager.UIPlayground.listStudents.client.StudentCollectionDataProvider.RowDataAcceptor;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.InvocationException;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;

public class DynaTableWidget extends Composite {

    private static class ErrorDialog extends DialogBox implements ClickHandler {

        private HTML body = new HTML("");

        public ErrorDialog() {
            setStylePrimaryName("DynaTable-ErrorDialog");
            Button closeButton = new Button("Close", this);
            VerticalPanel panel = new VerticalPanel();
            panel.setSpacing(4);
            panel.add(body);
            panel.add(closeButton);
            panel.setCellHorizontalAlignment(closeButton, VerticalPanel.ALIGN_RIGHT);
            setWidget(panel);
        }

        public String getBody() {
            return body.getHTML();
        }

        @Override
        public void onClick(ClickEvent event) {
            hide();
        }

        public void setBody(String html) {
            body.setHTML(html);
        }

    }

    private class TableHeader extends Composite implements ClickHandler {
        public final DockPanel bar = new DockPanel();
        private final Button fetcher = new Button("Listar 1º ano MEAN 09/10", this);

        public TableHeader() {
            initWidget(bar);
            bar.setStyleName("navbar");
            bar.add(fetcher, DockPanel.WEST);
            bar.setCellHorizontalAlignment(fetcher, DockPanel.ALIGN_LEFT);
        }

        @Override
        public void onClick(ClickEvent event) {
            refresh();
        }
    }

    private class RowDataAcceptorImpl implements RowDataAcceptor {

        @Override
        public void accept(String[][] data) {
            int destRowCount = data.length;
            initTable(destRowCount);
            int destColCount = grid.getCellCount(0);
            assert (data.length <= destRowCount) : "Too many rows";

            int srcRowIndex = 0;
            int srcRowCount = data.length;
            int destRowIndex = 1; // skip navbar row
            for (; srcRowIndex < srcRowCount; ++srcRowIndex, ++destRowIndex) {
                String[] srcRowData = data[srcRowIndex];
                assert (srcRowData.length == destColCount) : " Column count mismatch";
                for (int srcColIndex = 0; srcColIndex < destColCount; ++srcColIndex) {
                    String cellHTML = srcRowData[srcColIndex];
                    grid.setText(destRowIndex, srcColIndex, cellHTML);
                }
            }
        }

        @Override
        public void failed(Throwable caught) {
            if (errorDialog == null) {
                errorDialog = new ErrorDialog();
            }
            if (caught instanceof InvocationException) {
                errorDialog.setText("An RPC server could not be reached");
                errorDialog.setBody(NO_CONNECTION_MESSAGE);
            } else {
                errorDialog.setText("Unexcepted Error processing remote call");
                errorDialog.setBody(caught.getMessage());
            }
            errorDialog.center();
        }

    }

    private static final String NO_CONNECTION_MESSAGE = "<p>The DynaTable example uses a <a href=\"http://code.google.com/"
            + "webtoolkit/documentation/com.google.gwt.doc.DeveloperGuide."
            + "RemoteProcedureCalls.html\" target=\"_blank\">Remote Procedure Call</a> "
            + "(RPC) to request data from the server.  In order for the RPC to "
            + "successfully return data, the server component must be available.</p>"
            + "<p>If you are running this demo from compiled code, the server "
            + "component may not be available to respond to the RPC requests from "
            + "DynaTable.  Try running DynaTable in development mode to see the demo " + "in action.</p> "
            + "<p>Click on the Remote Procedure Call link above for more information " + "on GWT's RPC infrastructure.";

    private final RowDataAcceptor acceptor = new RowDataAcceptorImpl();

    private final Grid grid = new Grid();

    private ErrorDialog errorDialog = null;

    private final DockPanel outer = new DockPanel();

    private final TableHeader tableHeader = new TableHeader();

    private final StudentCollectionDataProvider provider;

    private String[] columns;
    private String[] columnStyles;

    public DynaTableWidget(StudentCollectionDataProvider provider, String[] columns, String[] columnStyles) {
        if (columns.length == 0) {
            throw new IllegalArgumentException("expecting a positive number of columns");
        }

        if (columnStyles != null && columns.length != columnStyles.length) {
            throw new IllegalArgumentException("expecting as many styles as columns");
        }

        setColumns(columns);
        setColumnStyles(columnStyles);
        this.provider = provider;
        initWidget(outer);
        grid.setStyleName("table");

        outer.add(tableHeader, DockPanel.NORTH);
        outer.add(grid, DockPanel.CENTER);
        initTable(1);
        setStyleName("DynaTable-DynaTableWidget");
    }

    public void refresh() {
        provider.updateRowData(acceptor);
    }

    public void setRowCount(int rows) {
        grid.resizeRows(rows);
    }

    private int getDataRowCount() {
        return grid.getRowCount() - 1;
    }

    public String[] getColumns() {
        return columns;
    }

    public void setColumns(String[] columns) {
        this.columns = columns;
    }

    public String[] getColumnStyles() {
        return columnStyles;
    }

    public void setColumnStyles(String[] columnStyles) {
        this.columnStyles = columnStyles;
    }

    private void initTable(int rowCount) {
        grid.resize(rowCount + 1, columns.length);
        for (int i = 0, n = columns.length; i < n; i++) {
            grid.setText(0, i, columns[i]);
            if (columnStyles != null) {
                grid.getCellFormatter().setStyleName(0, i, columnStyles[i] + " header");
            }
        }
    }

}
