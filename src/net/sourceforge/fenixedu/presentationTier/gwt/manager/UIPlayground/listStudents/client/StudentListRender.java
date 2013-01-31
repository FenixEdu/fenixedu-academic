package net.sourceforge.fenixedu.presentationTier.gwt.manager.UIPlayground.listStudents.client;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;

@Deprecated
public class StudentListRender extends Composite {

	private final RetrieveSpecificStudentSetAsync service;
	private final DockPanel panel = new DockPanel();
	private final Button button = new Button("Mostrar Lista");
	private final Label label = new Label("-- Mensagens de erro aqui --");
	private Grid table = new Grid(1, 1);
	private List<String> pupilsList;

	public StudentListRender() {
		service = (RetrieveSpecificStudentSetAsync) GWT.create(RetrieveSpecificStudentSet.class);
		ServiceDefTarget endpoint = (ServiceDefTarget) service;
		endpoint.setServiceEntryPoint(GWT.getModuleBaseURL() + "RetrieveSpecificStudentSet.gwt");

		table.setTitle("Lista dos alunos");

		initWidget(panel);
		panel.add(button, DockPanel.NORTH);
		panel.add(label, DockPanel.CENTER);
		panel.add(table, DockPanel.SOUTH);

		button.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				service.getStudentNames(new AsyncCallback() {
					@Override
					public void onFailure(Throwable e) {
						label.setText("Server call failed");
					}

					@Override
					public void onSuccess(Object obj) {
						if (obj != null) {
							pupilsList = (List<String>) obj;
							displayStudents();
						} else {
							label.setText("Server call returned nothing");
						}
					}
				});
			}
		});
	}

	private void displayStudents() {
		int iter = 0;
		table.resizeRows(pupilsList.size());
		for (String student : pupilsList) {
			iter++;
			table.setText(iter, 1, student);
		}
	}

}
