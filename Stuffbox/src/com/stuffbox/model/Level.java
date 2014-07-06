package com.stuffbox.model;

/**
 * 
 * @author Dana Jenett, Elene Maier, Wilhelm, Aaron Joellenbeck
 * Klasse die die Levelgrenzen fuer das Hauptlevel und die Abzeichen enthaelt 
 *
 */
public class Level {
	
	private static final int badgeMark1 = 1; //Limit damit Badge 1 verliehen wird
	private static final int badgeMark2 = badgeMark1 + 1; //Limit damit Badge 2 verliehen wird
	private static final int badgeMark3 = badgeMark2 + 1; //Limit damit Badge 3 verliehen wird
	private static final int badgeMark4 = badgeMark3 + 1; //Limit damit Badge 4 verliehen wird
	private static final int badgeMark5 = badgeMark4 + 2; //Limit damit Badge 5 verliehen wird
	private static final int LevelMark = 2; //Badges bis zum Levelaufstieg
	
	public Level(){
		
	}

	public static int getBadgemark1() {
		return badgeMark1;
	}

	public static int getBadgemark2() {
		return badgeMark2;
	}

	public static int getBadgemark3() {
		return badgeMark3;
	}

	public static int getBadgemark4() {
		return badgeMark4;
	}

	public static int getBadgemark5() {
		return badgeMark5;
	}

	public static int getLevelmark() {
		return LevelMark;
	}

	
}
