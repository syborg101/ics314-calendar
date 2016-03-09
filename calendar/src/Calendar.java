import java.awt.event.ActionListener;
import java.io.PrintWriter;

import javax.management.InvalidAttributeValueException;
import javax.sound.sampled.*;
import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/*
 * Created by Sy on 2/22/2016.
 */

public class Calendar implements ActionListener {

    /* Set the DateFormat and create a Date variable */
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    Date date = new Date();
    //System.out.println(dateFormat.format(date)); //2014/08/06 15:59:48

    /* Text fields */
    JTextField eventTitle = null;
    JTextField dateTimeStart = null;
    JTextField dateTimeEnd = null;
    JTextField location = null;
    JTextField description = null;
    JTextField status = null;
    JTextField latitude = null;
    JTextField longitude = null;
    //@author: Ming
    JComboBox cb = null;
    
    /*@author Kalen
     * I added in this boolean to use to test whether or not the user is adding geographic coordinates. I also added a second boolean to test whether or not
     * the user made a error in entering the geographic coordinates.
     */
    boolean useGeo = false;
    boolean geoError = false;

    /* Buttons */
    JButton submit = null;
    JButton quit = null;

    /* Panel */
    Panel panel = null;


    /* Constructor */
    public Calendar() {

        /* Create the text fields */
        eventTitle = new JTextField("Untitled Event");
        dateTimeStart = new JTextField(dateFormat.format(date));
        dateTimeEnd = new JTextField(dateFormat.format(date));
        location = new JTextField("University of Hawaii at Manoa, 2500 Campus Rd, " +
                "Honolulu, HI 96822, United States");
        description = new JTextField("");
        status = new JTextField("Confirmed");
        latitude = new JTextField("");
        longitude = new JTextField("");
        //@author: Ming
        String[] choices = { "PUBLIC","PRIVATE", "CONFIDENTIAL"};
        cb = new JComboBox(choices);

        /* Create the buttons with action listeners on these objects*/
        submit = new JButton("Submit");
        submit.addActionListener(this);
        quit = new JButton("Quit");
        quit.addActionListener(this);

        /* Create a panel implemented in the Panel class at the bottom. */
        panel = new Panel();

        /* Create a Swing frame */
        JFrame frame = new JFrame();

        /* Setting a simple layout for the frame*/
        frame.setLayout(new GridLayout(0, 2));

        /* Add all the above widgets to the frame, one after the other */
        //text fields
        frame.add(new JLabel("Event Title: "));
        frame.add(eventTitle);
        frame.add(new JLabel("From: "));
        frame.add(dateTimeStart);
        frame.add(new JLabel("To: "));
        frame.add(dateTimeEnd);
        frame.add(new JLabel("Where: "));
        frame.add(location);
        frame.add(new JLabel("Latitude: "));
        frame.add(latitude);
        frame.add(new JLabel("Longitude: "));
        frame.add(longitude);
        frame.add(new JLabel("Description: "));
        frame.add(description);
        frame.add(new JLabel("Status: "));
        frame.add(status);
        //@author: Ming
        frame.add(new JLabel("Classification"));
        frame.add(cb);

        //buttons
        frame.add(submit);
        frame.add(quit);

        /* Pack then render the frame */
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new Calendar();
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == this.quit) {
            // If the quit button is clicked, then quit

            System.exit(0);
        } else if (event.getSource() == this.submit) {
            // If the submit button is clicked, then...


            // Create a new Date and DateFormat(with seconds) for datetime at submit click
            Date date2 = new Date();
            DateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            System.out.println("Submitted");
            try {
                // For DTSTART
                String startYear = dateTimeStart.getText().substring(0, 4);
                String startMonth = dateTimeStart.getText().substring(5, 7);
                String startDay = dateTimeStart.getText().substring(8, 10);
                String startHour = dateTimeStart.getText().substring(11, 13);
                int startHr = Integer.parseInt(startHour);

                /*if(startHr > 12){
                    startHr -= 2;  //if pm
                                   // if am += 10
                    System.out.println(startHr);
                }
                */
                String startMinute = dateTimeStart.getText().substring(14, 16);

                // For DTEND
                String endYear = dateTimeEnd.getText().substring(0, 4);
                String endMonth = dateTimeEnd.getText().substring(5, 7);
                String endDay = dateTimeEnd.getText().substring(8, 10);
                String endHour = dateTimeEnd.getText().substring(11, 13);
                int endHr = Integer.parseInt(endHour);
                String endMinute = dateTimeEnd.getText().substring(14, 16);

                // For DTSTAMP ***fix this. what's the difference between this and CREATED??***
                String currentYear = dateFormat.format(date).substring(0, 4);
                String currentMonth = dateFormat.format(date).substring(5, 7);
                String currentDay = dateFormat.format(date).substring(8, 10);
                String currentHour = dateFormat.format(date).substring(11, 13);
                int currentHr = Integer.parseInt(currentHour);
                String currentMinute = dateFormat.format(date).substring(14, 16);
                String currentSecond = dateFormat2.format(date).substring(17, 19);

                // For CREATED and LAST-MODIFIED ***maybe separate these two later***
                String submitYear = dateFormat2.format(date2).substring(0, 4);
                String submitMonth = dateFormat2.format(date2).substring(5, 7);
                String submitDay = dateFormat2.format(date2).substring(8, 10);
                String submitHour = dateFormat2.format(date2).substring(11, 13);
                int submitHr = Integer.parseInt(submitHour);
                String submitMinute = dateFormat2.format(date2).substring(14, 16);
                String submitSecond = dateFormat2.format(date2).substring(17, 19);


                // Variables to be written into .ics file
                String startText = "BEGIN:VCALENDAR\nPRODID:-//Google Inc//Google Calendar 70.9054//EN\n" +
                        "VERSION:2.0\nCALSCALE:GREGORIAN\nMETHOD:PUBLISH\n" +
                        "X-WR-CALNAME:symn@hawaii.edu\n" + //need to fix so it takes the user's email instead
                        "X-WR-TIMEZONE:Pacific/Honolulu\nBEGIN:VEVENT\n";

                // Hours are +10 because that's how it's recorded from Google calendar
                String DTSTART = startYear + startMonth + startDay + "T" +
                        (startHr + 10) + startMinute + "00Z";

                String DTEND = endYear + endMonth + endDay + "T" +
                        (endHr + 10) + endMinute + "00Z";

                String DTSTAMP = currentYear + currentMonth + currentDay + "T" +
                        (currentHr + 10) + currentMinute + currentSecond + "Z";

                //***fix this, don't know what it is***
                String UID = "rp6v6nppa2nm9gqfh1ais4k3mo@google.com";

                String CREATED = submitYear + submitMonth + submitDay + "T" +
                        (submitHr + 10) + submitMinute + submitSecond + "Z";

                String DESCRIPTION = this.description.getText();

                String LASTMODIFIED = submitYear + submitMonth + submitDay + "T" +
                        (submitHr + 10) + submitMinute + submitSecond + "Z";

                String LOCATION = location.getText().replaceAll(",", "\\\\,");
                
                //@author: Ming
                //Getting choice of CLASSIFICATION
                String choice = (String) cb.getSelectedItem();

                //***should be zero. Sequence is incremented when changes are made to a existing event in iCal application.***
                String SEQUENCE = "0";

                /* @author Kalen
                 * Status is used for group events to indicate whether the event is Confirmed (definitely happening), tentative (might not happen), or
                 * cancelled (not happening).
                 */
                String STATUS = this.status.getText();
                if (!(STATUS.equalsIgnoreCase("Tentative") || STATUS.equalsIgnoreCase("Confirmed") || STATUS.equalsIgnoreCase("Cancelled"))) {
                	throw new InvalidAttributeValueException("wrong");
                }
                STATUS = STATUS.toUpperCase();
                
                /*@author Kalen
                 * This block of code is for my implementation of the geographic coordinate thing. First it gets the text from the latitude and longitude
                 * input. It checks whether or not the length of either is 0, which means the user didn't enter anything, and sets a the boolean useGeo
                 * accordingly. This boolean is used to determine whether or not the code for adding the geo coordinates will be used. If the user did enter
                 * something, it tests to see whether or not it is valid input and sets another boolean, geoError, accordingly. If the input was invalid, the 
                 * block catches it, sets the geoError boolean to true, and throws another exception to be caught by the main catch block for the program.
                 */
                String lat = latitude.getText();
                String lon = longitude.getText();
                Float latAsFloat = null;
                Float lonAsFloat = null;
                if (lat.length() > 0 && lon.length() > 0) {
                	useGeo = true;
                }
                if (useGeo) {
					try {
						latAsFloat = Float.valueOf(lat);
						lonAsFloat = Float.valueOf(lon);
					} catch (NumberFormatException e) {
						geoError = true;
						throw new NumberFormatException("");
					}
				}
				String SUMMARY = this.eventTitle.getText();

                PrintWriter writer = null;
                try {
                    writer = new PrintWriter("symn.ics");
                    writer.print(startText);
                    writer.println("DTSTART:" + DTSTART);
                    writer.println("DTEND:" + DTEND);
                    writer.println("DTSTAMP:" + DTSTAMP);
                    writer.println("UID:" + UID);
                    writer.println("CREATED:" + CREATED);
                    writer.println("DESCRIPTION:" + DESCRIPTION);
                    writer.println("LAST-MODIFIED:" + LASTMODIFIED);
                    writer.println("LOCATION:" + LOCATION);
                    /*@author: Kalen
                     * if user entered in valid coordinates, then the program will write the geo coordinate data. If the user didn't input anything,
                     * it won't be used.
                     */
                    if (geoError == false && useGeo == true) {
                    	writer.println("GEO:" + lat + ";" + lon);
                    }
                    //@author: Ming
                    writer.println("CLASS:" + choice); //Adding CLASS (classification) to the .ics
                    writer.println("SEQUENCE:" + SEQUENCE);
                    writer.println("STATUS:" + STATUS);
                    writer.println("SUMMARY:" + SUMMARY);
                    writer.println("TRANSP:OPAQUE\n" +
                            "END:VEVENT\n" +
                            "END:VCALENDAR");
                    writer.close();
                } catch (Exception e) {
                    System.out.println("Error!");
                    e.printStackTrace();
                }



            } catch (Exception e) {
                System.out.println("Reading text field error");
                
                /*@author Kalen
                 * If the user inputted something that was invalid, the geo coordinate code block will set geoError to true, so the user knows that was
                 * the source of the error.
                 */
                if (geoError == true) {
                	System.out.println("Not valid geographic coordinates");
                }
            }
        }
    }

    private class Panel extends JPanel {
        static final int PANEL_WIDTH = 500;     // width of the panel in pixels
        static final int PANEL_HEIGHT = 500;    // height of the panel in pixels

        /* Constructor */
        public Panel() {
            this.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        }
    }
}
