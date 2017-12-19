package models;

import java.sql.Date;
import java.time.LocalDate;

import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

public class Appointment {
	private String id;
	private String date, time;
	private String title, note;
	private String type;
	
	private GridPane grid;
	private Text tTitle, tsubNote;
	
	/**
	 * Constructor Appointment
	 * @param id
	 * @param date
	 * @param time
	 * @param title
	 * @param note
	 * 
	 * 2017-10-08 03:32 PM
	 */
	public Appointment(String id, String date, String time, String title, String note, String type) {
		this.id = id;
		this.date = date;
		this.time = time;
		this.title = title;
		this.note = note;
		this.type = type;
	}

	/**
	 * 
	 * GETTER AND SETTER
	 * 
	 */
	public String getId() {
		return id;
	}

	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date=date;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time=time;
	}

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title=title;
	}

	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note=note;
	}
	public String getType() {
		return type;
	}

	public GridPane getGrid() {
		return grid;
	}

	public void setGrid(GridPane grid) {
		this.grid = grid;
	}

	public Text getTTitle() {
		return tTitle;
	}

	public void setTTitle(Text tTitle) {
		this.tTitle = tTitle;
	}

	public Text getTsubNote() {
		return tsubNote;
	}

	public void setTsubNote(Text tsubNote) {
		this.tsubNote = tsubNote;
	}
	
}
