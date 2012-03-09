/*
 * @author	Robert Beam
 * @date 	1-31-12
 * @title	CS62 Assignment 1: Welcome to My Day
 * @purpose Introduce the students to the basics of Java programming.
 */

package cs62;

import java.text.*;
import java.util.*;

public class Assignment1 {
	public static void main(String[] args) {
		// Create a Date object with the default (current) time.
		Date now = new Date();
		// Print the welcome message.
		System.out.println("Welcome to my day.");
		System.out.println("Daily planner for Robert Beam");
		// Grab an instance of a DateFormat object and use its 'format' method
		// to format a string that represents the Date object 'now' as a string.
		System.out.println("The date: " + DateFormat.getDateInstance().format(now));
	}

}
