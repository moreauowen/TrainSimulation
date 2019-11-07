/*
 * Owen Moreau, Jamie Kerr, Griffin Campbell
 * Comp 2000 - Data Structures
 * Lab 3: Queue application - Train Simulation
 * Spring, 2019
 *
 */
package edu.wit.dcsn.comp2000.queueapp;


/**
 * @author Owen Moreau
 * @version 1.0.0
 */
public enum RouteStyle {
	/**
	 * the route has two parallel tracks (inbound and outbound) where the next
	 * station following the last outbound location is the same/corresponding
	 * location on the inbound track and the next location following the first
	 * inbound location is the first location on the outbound track
	 */
	LINEAR("Linear"),
	/** synonym for LINEAR */
	POINT_TO_POINT("Point-to-point"),
	/**
	 * the route has two concentric tracks where the next station following the last
	 * location is the first location and vice versa - trains don't change direction
	 * (clockwise and counterclockwise)
	 */
	CIRCULAR("Circular");

	// instance variables

	private final String displayText; // user-friendly version for display

	/**
	 * Sets the corresponding user-friendly text for display
	 * 
	 * @param textToDisplay the user-friendly text
	 */
	private RouteStyle(String textToDisplay) {
		displayText = textToDisplay;
	} // end constructor

	/**
	 * @return this route style's display text
	 */
	public String getDisplayText() {
		return displayText;
	} // end getDisplayText()

	// public utility methods

	/**
	 * Parses a string representing a RouteStyle. The string is matched against the
	 * display text for each member of the enumeration in order of declaration. The
	 * match is case-insensitive and is restricted to a substring of the display
	 * text starting with the first character and ending with the number of
	 * characters specified in the provided string.
	 * 
	 * <p>
	 * NOTE: Substring matching will succeed on the first match, which for this
	 * enumeration will behave correctly when only the first character/a single
	 * character string is provided.
	 * 
	 * @param styleText the text to parse
	 * @return if successfully parsed, the corresponding enumeration element;
	 *         otherwise null
	 */
	public static RouteStyle parse(String styleText) {
		RouteStyle parsedStyle = null; // default value (parse failed)

		// loop through the RouteStyles
		for (RouteStyle aRouteStyle : RouteStyle.values()) {
			// only match as many characters as provided in the shorter string
			// (parameter or RouteStyle displayText)
			int compareLength = Math.min(styleText.length(), aRouteStyle.displayText.length());
			if (styleText.equalsIgnoreCase( // case-blind comparison
					aRouteStyle.displayText.substring(0, compareLength))) {
				parsedStyle = aRouteStyle;

				break; // found one so done
			}
		}

		return parsedStyle;
	} // end parse()

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return displayText;
	} // end toString()

	/**
	 * Unit test driver
	 * 
	 * @param args -unused-
	 */
	public static void main(String[] args) {
		// display column headers
		System.out.printf("%-5s %-20s %-20s %-20s %-20s%n", "#", "Name", "Style", "Display Text", "Parsed");

		// display each element of the enumeration
		for (RouteStyle aRouteStyle : RouteStyle.values()) {
			System.out.printf("%-5d %-20s %-20s %-20s %-20s%n", aRouteStyle.ordinal(), aRouteStyle.name(), aRouteStyle,
					aRouteStyle.displayText, RouteStyle.parse(aRouteStyle.displayText));
		}

		// test the parser
		System.out.println();
		System.out.println("parse(\"L\") returns " + RouteStyle.parse("L"));
		System.out.println("parse(\"Long\") returns " + RouteStyle.parse("Long"));
		System.out.println("parse(\"Linearly\") returns " + RouteStyle.parse("Linearly"));
		System.out.println("parse(\"nonesuch\") returns " + RouteStyle.parse("nonesuch"));
		System.out.println("parse(\"a very long string which may or may not match\") returns "
				+ RouteStyle.parse("a very long string which may or may not match"));

	} // end test driver main()

} // end enum RouteStyle
