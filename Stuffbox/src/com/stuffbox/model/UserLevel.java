package com.stuffbox.model;


import java.util.ArrayList;

import com.stuffbox.controller.Controller;

/**
 * 
 * @author Dana Jenett, Elene Maier, Wilhelm, Aaron Joellenbeck
 * Klasse die die Levelgrenzen fuer das Hauptlevel und die Abzeichen enthaelt 
 *
 */
public class UserLevel {
	
	private static final int badgeMark1 = 1; //Limit damit Badge 1 verliehen wird
	private static final int badgeMark2 = badgeMark1 + 1; //Limit damit Badge 2 verliehen wird
	private static final int badgeMark3 = badgeMark2 + 1; //Limit damit Badge 3 verliehen wird
	private static final int badgeMark4 = badgeMark3 + 1; //Limit damit Badge 4 verliehen wird
	private static final int badgeMark5 = badgeMark4 + 2; //Limit damit Badge 5 verliehen wird
	private static final int LevelMark = 2; //Badges bis zum Levelaufstieg
	
	private int lvlCount;
	private int badgeCount;
	private ArrayList<Category> allCat;
	
	public UserLevel(){
		this.lvlCount = 0;
		this.badgeCount = 0;
		allCat= Controller.getInstance().getCategories(null);
		//Badges zaehlen
		for(Category cat: allCat){
			ArrayList<Item> items = Controller.getInstance().getItemsOfACategory(cat.getId());
			for(int i = 0; i < items.size(); i++){
				badgeCount++;
			}
		}
		
		//Level bestimmen
		lvlCount = (int) badgeCount / LevelMark;
		
		
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

	public int getLvlCount() {
		return lvlCount;
	}

	public void setLvlCount(int lvlCount) {
		this.lvlCount = lvlCount;
	}

	public int getBadgeCount() {
		return badgeCount;
	}

	public void setBadgeCount(int badgeCount) {
		this.badgeCount = badgeCount;
	}
	
	private int getItemAmount(long categoryID){
		ArrayList<Item> catItems = Controller.getInstance().getItemsOfACategory(categoryID);
		return catItems.size();
	}

	public boolean awardedBadge1(long categoryID) {
		if (getItemAmount(categoryID) >= badgeMark1) {
			return true;
		} else {

			return false;
		}
	}
	
	public boolean awardedBadge2(long categoryID) {
		if (getItemAmount(categoryID) >= badgeMark2) {
			return true;
		} else {

			return false;
		}
	}
	
	public boolean awardedBadge3(long categoryID) {
		if (getItemAmount(categoryID) >= badgeMark3) {
			return true;
		} else {

			return false;
		}
	}

	public boolean awardedBadge4(long categoryID) {
		if (getItemAmount(categoryID) >= badgeMark4) {
			return true;
		} else {

			return false;
		}
	}
	
	public boolean awardedBadge5(long categoryID) {
		if (getItemAmount(categoryID) >= badgeMark5) {
			return true;
		} else {

			return false;
		}
	}
	
	/**
	 * Gibt zurueck ob ein neues Badge/Abzeichen verliehen wurde
	 */
	public boolean newBadgeAwarded(long categoryID){
		if (getItemAmount(categoryID) == badgeMark1) {
			return true;
		}else if (getItemAmount(categoryID) == badgeMark2) {
			return true;
		}else if (getItemAmount(categoryID) == badgeMark3) {
			return true;
		}else if (getItemAmount(categoryID) == badgeMark4) {
			return true;
		}else if (getItemAmount(categoryID) == badgeMark5) {
			return true;
		}else{
			return false;
		}
	}
}
