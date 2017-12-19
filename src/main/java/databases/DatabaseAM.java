package databases;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import models.Appointment;

public class DatabaseAM {
	private Connection conn;

	/**
	 * Opening Calendar database.
	 * 
	 * 2017-10-08 03:32 PM
	 */
	public void open() {
		try {
			Class.forName("org.sqlite.JDBC");
			String dbURL = "jdbc:sqlite:Calendar.db";
			conn = DriverManager.getConnection(dbURL);
			if (conn != null) {
				System.out.println("Connect to database!!!");
			}
		} catch (ClassNotFoundException ex) {
			System.err.println("ClassNotFoundException");
		} catch (SQLException ex) {
			System.err.println("DatabaseAM open() SQLException");
		}
	}

	/**
	 * Get values from RootController.addToDB(...)
	 * @param id Generate from year-month-day-hours-minutes-seconds
	 * @param date from datePicker
	 * @param time from TextField
	 * @param title from Textfield
	 * @param note from TextArea
	 * 
	 * 2017-10-08 03:32 PM
	 */
	public void insert(String id, String date, String time, String title, String note, String type) {
		try {
			String query = "INSERT INTO Appointment ('id','date','time','title','note','type') VALUES "
					+ "(" + "'" + id + "','" + date + "','"
					+ time + "','" + title + "','" + note + "','" + type + "')";
			Statement statement = conn.createStatement();
			statement.executeUpdate(query);
		} catch (SQLException ex) {
			System.err.println("DatabaseAM insert() SQLException");
		}
	}

	public void update(String id, String date, String time, String title, String note) {
		try {
			String query = "UPDATE Appointment SET 'date' = '" + date + "', 'time' = '" + time + "','title' = '"
					+ title + "', 'note' = '" + note + "' WHERE id = '" + id + "'";
			Statement statement = conn.createStatement();
			statement.executeUpdate(query);
		} catch (SQLException ex) {
			System.err.println("DatabaseAM update() SQLException");
		}
	}

	/**
	 * Get appointments
	 * @return list of any appointment
	 * 
	 * 2017-10-08 08:24 PM
	 */
	public ArrayList<Appointment> getAll() {
		// ObservableList<LineItem> listItem = FXCollections.observableArrayList();
		ArrayList<Appointment> ams = new ArrayList<Appointment>();
		
		try {
			String query = "SELECT * FROM Appointment";
			Statement statement = conn.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			while (resultSet.next()) {
				String id = resultSet.getString(1);
				String date = resultSet.getString(2);
				String time = resultSet.getString(3);
				String title = resultSet.getString(4);
				String note = resultSet.getString(5);
				String type = resultSet.getString(6);

				ams.add(new Appointment(id, date, time, title, note, type));
				// listItem.add(new LineItem(new Product(id,name,price),quantity));
				// System.out.println("ID: "+id+"Name: "+name+"Price: "+price+"Quantity:
				// "+quantity);
			}
		} catch (SQLException ex) {
			System.err.println("DatabaseAM getAll() SQLException");
		}
		return ams;
	}

	public void delete(String id) {
		try {
			String query = "DELETE FROM Appointment WHERE id = '" + id + "'";
			Statement statement = conn.createStatement();
			statement.executeUpdate(query);
		} catch (SQLException ex) {
			System.err.println("DatabaseAM delete() SQLException");
		}
	}

	/**
	 * Closing Calendar database
	 * 
	 * 2017-10-08 03:32 PM
	 */
	public void close() {
		try {
			conn.close();
			System.out.println("Close Database!!!");
		} catch (SQLException e) {
			System.err.println("DatabaseAM close() SQLException");
		}
	}
}
