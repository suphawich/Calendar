package controllers;

import java.net.URL;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.ResourceBundle;

import databases.DatabaseAM;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.*;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.Node;
import models.Appointment;

public class RootController implements Initializable {
	@FXML
	private VBox vboxAM;
	@FXML
	private VBox vboxMenu;
	@FXML
	private VBox vboxBGAM;
	@FXML
	private VBox vboxBGNOTE;
	@FXML
	private Label labelTitle;
	@FXML
	private Label labelNote;
	
	
	private DatabaseAM dbam;
	private HashMap<String, Appointment> hams;
	
	/**
	 * Setting and make object
	 * 
	 * 2017-10-08 03:32 PM
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		System.out.println("Welcome to RootCOntroller");
		hams = new HashMap<String, Appointment>();
		dbam = new DatabaseAM();
		dbam.open();
		initVBoxAM();
		initVBoxMenu();
		
//		ScrollPane spNote = new ScrollPane();
//		spNote.setContent(labelNote);
		
//		vboxBGNOTE.getChildren().add(spNote);
	}

	/**
	 * Initialize object in vboxAM
	 * 
	 * 2017-10-08 03:32 PM
	 */
	private void initVBoxAM() {
		ScrollPane sp = new ScrollPane();
//		sp.setFitToWidth(true);
		sp.setContent(vboxAM);
		vboxBGAM.getChildren().add(sp);
//		sp.setFitToWidth(true);
		ArrayList<Appointment> ams = dbam.getAll();
		for (Appointment am : ams) {
			addToVBoxAM(am);
		}
	}

	
	
	/**
	 * Method for add new appointment to vboxAM There are many method have to call
	 * this method, so adding object to vboxAM have to seperate to new method.
	 * 
	 * @param am
	 * Appointment class
	 * 
	 * 2017-10-08 03:53 PM
	 * 
	 * fix
	 * change label to grid
	 * 2017-10-09 04:38 AM
	 */
	private void addToVBoxAM(Appointment am) {
		//add to temp data.
		hams.put(am.getId(), am);
		ContextMenu cm = new ContextMenu();
		MenuItem mi1 = new MenuItem("Edit");
		cm.getItems().add(mi1);
		MenuItem mi2 = new MenuItem("Delete");
		cm.getItems().add(mi2);
		mi1.setOnAction(event -> actionEdit(am));
		mi2.setOnAction(event -> actionDelete(am));
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		GridPane subgrid = new GridPane();
		Text tTitle = new Text(am.getTitle());
		tTitle.setFont(Font.font("System", FontWeight.BOLD, 16));
		tTitle.setFill(Paint.valueOf("#3B4048"));
		Text tsubNote = new Text();
		tsubNote.setFont(Font.font("System", 16));
		tsubNote.setFill(Paint.valueOf("#828795"));
//		subgrid.setValignment(tsubNote, VPos.TOP);
		if (am.getNote().length() <= 20) {
			tsubNote.setText(am.getNote());
		} else {
			tsubNote.setText(am.getNote().substring(0, 20) + "...");
		}
		subgrid.add(tTitle, 0, 0);
		subgrid.add(tsubNote, 0, 1);
		grid.add(subgrid, 1, 0);
		
		grid.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				if (e.getButton() == MouseButton.PRIMARY) {
//					grid.setStyle("-fx-background-color: #E9EAEE;");
					
					/**
					 * SETTING COLOR OF GRIDS
					 * Get grid from vboxAM.
					 * 
					 * appointment-select = that grid get clicked.
					 * appointment = original grid.
					 */
					for (Node n : vboxAM.getChildren()) {
						
						//convert Node to GridPane
						GridPane ogrid = (GridPane) n;
						
						//clicked grid.
						if (ogrid==grid) {
							grid.setId("appointment-select");
							
						} else { //set grid isn't clicked to original grid color.
							ogrid.setId("appointment");
						}
					}
					labelTitle.setText(am.getTitle());
					labelNote.setText(am.getNote());
				}
				if (e.getButton() == MouseButton.SECONDARY) {
					cm.show(grid, e.getScreenX(), e.getScreenY());
				} else {
					cm.hide();
//					grid.setStyle("-fx-background-color: #FFFFFF");
				}
			}
		});
		grid.setMinSize(350, 80);
//		grid.setPrefSize(200, 90);
		grid.setId("appointment");
		vboxAM.getChildren().add(grid);
//		sbAM
		am.setGrid(grid);
		am.setTTitle(tTitle);
		am.setTsubNote(tsubNote);
	}

	
	
	
	/**
	 * Initialize object in vboxMenu
	 * 
	 * 2017-10-08 03:32 PM
	 */
	private void initVBoxMenu() {
		Button addBtn = new Button("A");
		addBtn.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				if (e.getButton() == MouseButton.PRIMARY) {
					makeAddDialog();
				}
			}
		});
		vboxMenu.setStyle("-fx-background-color: #2D364B;");
		vboxMenu.getChildren().add(addBtn);
	}

	
	
	
	/**
	 * Make add appointment dialog.
	 * 
	 * 2017-10-08 03:32 PM.
	 */
	private void makeAddDialog() {
		
		//Make Dialog
		Dialog<Integer> dialog = new Dialog<>();
		dialog.setTitle("Add");
		dialog.setHeaderText("ใส่ข้อมูลนัดหมายให้ครบถ้วน");
		ButtonType logButton = new ButtonType("Add", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().addAll(logButton, ButtonType.CANCEL);

		
		//Make main grid
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));

		
		//Add form Title.
		grid.add(new Label("Title:"), 0, 0);
		TextField tfTitle = new TextField();
		tfTitle.setPrefSize(100, 30);
		tfTitle.setPromptText("--- Title ---");
		grid.add(tfTitle, 1, 0);

		
		//Add form Note
		Label lNote = new Label("Note:");
		grid.setValignment(lNote, VPos.TOP);
		grid.add(lNote, 0, 1);
		TextArea taNote = new TextArea();
		taNote.setPromptText("--- Input Message ---");
		taNote.setPrefSize(300, 100);
		taNote.setWrapText(true);
		grid.add(taNote, 1, 1);

		
		//Add form Date
		Label ldp = new Label("Date:");
		grid.add(ldp, 0, 2);
		DatePicker dp = new DatePicker();
		dp.setPromptText("mm/dd/yyyy");
		grid.add(dp, 1, 2);

		
		//Add form Time
		grid.add(new Label("Time:"), 0, 3);
		Date dateCurrent = new Date();
		GridPane gridTime = new GridPane();
		TextField tfHours = new TextField(String.valueOf(dateCurrent.getHours()));
		tfHours.setPrefSize(40, 30);
		TextField tfMinutes = new TextField(String.valueOf(dateCurrent.getMinutes()));
		tfMinutes.setPrefSize(40, 30);
		gridTime.add(tfHours, 0, 0);
		gridTime.add(new Label(" : "), 1, 0);
		gridTime.add(tfMinutes, 2, 0);
		grid.add(gridTime, 1, 3);

		//Add form Type
		MenuButton mb = new MenuButton("No");
		MenuItem mi1 = new MenuItem("No");
		MenuItem mi2 = new MenuItem("Day");
		MenuItem mi3 = new MenuItem("Week");
		MenuItem mi4 = new MenuItem("Month");
		mi1.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				mb.setText("No");
			}
			
		});
		mi2.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				mb.setText("Day");
			}
			
		});
		mi3.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				mb.setText("Week");
			}
			
		});
		mi4.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				mb.setText("Month");
			}
			
		});
		mb.getItems().addAll(mi1, mi2, mi3, mi4);
		
		grid.add(new Label("Every "), 0, 4);
		grid.add(mb, 1, 4);
		dialog.getDialogPane().setContent(grid);
		
		//Point of begin
		Platform.runLater(() -> tfTitle.requestFocus());

		
		/**
		 * CORE FUNCTION FOR ADD DATA
		 * TO DATABASE AND VBOXAM.
		 */
		dialog.setResultConverter(dialogButton -> {
			if (dialogButton == logButton) {
				String title = tfTitle.getText();
				String note = taNote.getText();
				String date = String.valueOf(dp.getValue());
				String time = tfHours.getText() + "-" + tfMinutes.getText();
				String type = mb.getText();
				addToDB(date, time, title, note, type);
				return 200;
			} else {
				return null;
			}
		});
		dialog.show();
	}

	
	
	
	/**
	 * get values from makeAddDialog() after apply.
	 * 
	 * @param date
	 *            from Datepicker
	 * @param time
	 *            from TextField
	 * @param title
	 *            from TextField
	 * @param note
	 *            from TextArea
	 * 
	 * 
	 *            2017-10-08 03:32 PM
	 */
	private void addToDB(String date, String time, String title, String note, String type) {

		// generate new id for new appointment.
		Date idDate = new Date();
		LocalDate localDate = idDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		int year = localDate.getYear();
		int month = localDate.getMonthValue();
		int day = localDate.getDayOfMonth();
		String id = String.format("%d%02d%02d%02d%02d%02d", year, month, day, idDate.getHours(), idDate.getMinutes(),
				idDate.getSeconds());

		int n = -1;
		if (type.equalsIgnoreCase("No")) {
			// insert appointment to database.
			dbam.insert(id, date, time, title, note, type);
			
			// make new appointment to vboxAM.
			addToVBoxAM(new Appointment(id, date, time, title, note, type));
		} else if (type.equalsIgnoreCase("Day")) {
			n = 1;
		} else if (type.equalsIgnoreCase("Week")) {
			n = 7;
		} else if (type.equalsIgnoreCase("Month")) {
			n = 30;
		}
		if (n!=-1) {
			for (int i=1, c=1; i<=365; i+=n,c++) {
				String newDate = nextDate(date, n);
				dbam.insert(id+String.valueOf(c), newDate, time, title, note, type);
				
				// make new appointment to vboxAM.
				addToVBoxAM(new Appointment(id+String.valueOf(c), newDate, time, title, note, type));
				date = newDate;
			}	
		}
		
	}

	private String nextDate(String date, int n) {
		String[] ldate = date.split("-");
		int y = Integer.parseInt(ldate[0]);
		int m = Integer.parseInt(ldate[1]);
		int d = Integer.parseInt(ldate[2]);
		if ((d+n)>31 && (m==1||m==3||m==5||m==7||m==8||m==10||m==12)) {
			if ((m+1)>12) {
				y++;
				m=1;
				d=(d+n)-31;
			} else {
				m++;
				d=(d+n)-31;
			}
			return String.format("%d-%02d-%02d", y, m, d);
		}
		if ((d+n)>30 && (m==4||m==6||m==9||m==11)) {
			m++;
			d=(d+n)-30;
			return String.format("%d-%02d-%02d", y, m, d);
		}
		if ((d+n)>28 && (m==2)) {
			m++;
			d=(d+n)-28;
			return String.format("%d-%02d-%02d", y, m, d);
		}
		return String.format("%d-%02d-%02d", y, m, d+n);
	}
	
	
	/**
	 * update data in database then update in appointment
	 * and the last update onject in vboxAM.
	 * @param id
	 * @param date
	 * @param time
	 * @param title
	 * @param note
	 * @param am
	 * 
	 * fix
	 * change label to grid
	 * 
	 * 
	 * 2017-10-09 04:38 AM
	 */
	private void updateItemVBoxAM(String id, String date, String time, String title, String note) {
		dbam.update(id, date, time, title, note);
		Appointment am = hams.get(id);
		am.setDate(date);
		am.setTime(time);
		am.setTitle(title);
		am.setNote(note);

		am.getTTitle().setText(am.getTitle());
		if (am.getNote().length() <= 20) {
			am.getTsubNote().setText(am.getNote());
		} else {
			am.getTsubNote().setText(am.getNote().substring(0, 20) + "...");
		}
//		labelTitle.setText(am.getTitle());
//		labelNote.setText(am.getNote());
	}

	
	
	
	/**
	 * Action edit when right click on that object.
	 * @param am
	 * 
	 * 
	 * 2017-10-09 04:38 AM
	 */
	private void actionEdit(Appointment am) {
		System.out.println("to actionEdit(), " + am.getTitle());
		Dialog<Integer> dialog = new Dialog<>();
		dialog.setTitle("Add");
		dialog.setHeaderText("ใส่ข้อมูลนัดหมายให้ครบถ้วน");
		ButtonType logButton = new ButtonType("Edit", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().addAll(logButton, ButtonType.CANCEL);

		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));

		grid.add(new Label("Title:"), 0, 0);
		TextField tfTitle = new TextField();
		tfTitle.setPrefSize(100, 30);
		tfTitle.setPromptText("--- Title ---");
		tfTitle.setText(am.getTitle());
		grid.add(tfTitle, 1, 0);

		Label lNote = new Label("Note:");
		grid.setValignment(lNote, VPos.TOP);
		grid.add(lNote, 0, 1);
		TextArea taNote = new TextArea();
		taNote.setPromptText("--- Input Message ---");
		taNote.setPrefSize(300, 100);
		taNote.setWrapText(true);
		taNote.setText(am.getNote());
		grid.add(taNote, 1, 1);

		Label ldp = new Label("Date:");
		grid.add(ldp, 0, 2);
		DatePicker dp = new DatePicker();
		dp.setPromptText("mm/dd/yyyy");
		String[] dates = am.getDate().split("-");
		String sodate = String.format("%s/%s/%s", dates[1], dates[2], dates[0]);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		LocalDate lcd = LocalDate.parse(sodate, formatter);
		dp.setValue(lcd);
		grid.add(dp, 1, 2);

		grid.add(new Label("Time:"), 0, 3);
		Date dateCurrent = new Date();
		GridPane gridTime = new GridPane();
		String[] times = am.getTime().split("-");
		TextField tfHours = new TextField(String.valueOf(dateCurrent.getHours()));
		tfHours.setPrefSize(40, 30);
		tfHours.setText(times[0]);
		TextField tfMinutes = new TextField(String.valueOf(dateCurrent.getMinutes()));
		tfMinutes.setPrefSize(40, 30);
		tfMinutes.setText(times[1]);
		gridTime.add(tfHours, 0, 0);
		gridTime.add(new Label(" : "), 1, 0);
		gridTime.add(tfMinutes, 2, 0);
		grid.add(gridTime, 1, 3);
		
		grid.add(new Label("Every"), 0, 4);
		grid.add(new Label(am.getType()), 1, 4);

		dialog.getDialogPane().setContent(grid);
		Platform.runLater(() -> tfTitle.requestFocus());

		dialog.setResultConverter(dialogButton -> {
			if (dialogButton == logButton) {
				String id = am.getId();
				id = id.substring(0, 14);
				String title = tfTitle.getText();
				String note = taNote.getText();
				String date = String.valueOf(dp.getValue());
				String time = tfHours.getText() + "-" + tfMinutes.getText();
				String type = am.getType();
				int n = -1;
				if (type.equalsIgnoreCase("No")) {
					updateItemVBoxAM(id, date, time, title, note);
				} else if (type.equalsIgnoreCase("Day")) {
					n = 1;
				} else if (type.equalsIgnoreCase("Week")) {
					n = 7;
				} else if (type.equalsIgnoreCase("Month")) {
					n = 30;
				}
				if (n!=-1) {
					for (int i=1, c=1; i<=365; i+=n,c++) {
						updateItemVBoxAM(id+String.valueOf(c), date, time, title, note);
					}	
				}
				return 200;
			} else {
				return null;
			}
		});
		dialog.show();
	}

	
	
	/**
	 * Action delete when right click on that object.
	 * @param am
	 * 
	 * 
	 * 2017-10-09 04:38 AM
	 */
	private void actionDelete(Appointment am) {
		System.out.println("to actionDelete(), " + am.getTitle());
		String type = am.getType();
		int n = -1;
		if (type.equalsIgnoreCase("No")) {
			dbam.delete(am.getId());
			vboxAM.getChildren().remove(am.getGrid());
		} else if (type.equalsIgnoreCase("Day")) {
			n = 1;
		} else if (type.equalsIgnoreCase("Week")) {
			n = 7;
		} else if (type.equalsIgnoreCase("Month")) {
			n = 30;
		}
		if (n!=-1) {
			String id = am.getId().substring(0, 14);
			for (int i=1, c=1; i<=365; i+=n,c++) {
				am = hams.get(id+String.valueOf(c));
				dbam.delete(id+String.valueOf(c));
				vboxAM.getChildren().remove(am.getGrid());
			}	
		}
		
	}
}


