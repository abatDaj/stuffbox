package com.stuffbox.model;
import java.io.Serializable;
import java.util.ArrayList;

import com.stuffbox.controller.Controller;

public class Badge implements Serializable {

	private Category category; //Kategoriename
	private String badgeIconSet; //Name des Kategorie Iconsets mit den Badges
	private int itemcount; //Anzahl Eintraege/Items
	
	private boolean badge1;
	private boolean badge2;
	private boolean badge3;
	private boolean badge4;
	private boolean badge5;
	
	public Badge(Category category, int itemcount, String badgeIconSet, boolean badge1, boolean badge2, boolean badge3, boolean badge4, boolean badge5){
		this.category = category;
		this.itemcount = itemcount;
		this.badgeIconSet = badgeIconSet;
		this.badge1 = badge1;
		this.badge2 = badge2;
		this.badge3 = badge3;
		this.badge4 = badge4;
		this.badge5 = badge5;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public String getBadgeIconSet() {
		return badgeIconSet;
	}

	public void setBadgeIconSet(String badgeIconSet) {
		this.badgeIconSet = badgeIconSet;
	}
	

	public boolean isBadge1() {
		return badge1;
	}

	public void setBadge1(boolean badge1) {
		this.badge1 = badge1;
	}

	public boolean isBadge2() {
		return badge2;
	}

	public void setBadge2(boolean badge2) {
		this.badge2 = badge2;
	}

	public boolean isBadge3() {
		return badge3;
	}

	public void setBadge3(boolean badge3) {
		this.badge3 = badge3;
	}

	public boolean isBadge4() {
		return badge4;
	}

	public void setBadge4(boolean badge4) {
		this.badge4 = badge4;
	}

	public boolean isBadge5() {
		return badge5;
	}

	public void setBadge5(boolean badge5) {
		this.badge5 = badge5;
	}

	public int getItemcount() {
		return itemcount;
	}

	public void setItemcount(int itemcount) {
		this.itemcount = itemcount;
	}
	
	public static ArrayList<Badge> getBadges(){
		/**TODO
		 * Nur Test um Umgang zu erlernen
		 */
		ArrayList<Badge> arrList = new ArrayList<Badge>();
		
		/**
		 * Rootkategorien fuer Badgesystem laden
		 */
		
		UserLevel level = new UserLevel();
		
		ArrayList<Category> allCat= Controller.getInstance().getCategories(null);
		ArrayList<Category> subCat= new ArrayList<Category>();
		ArrayList<Category> rootCat = new ArrayList<Category>();
		ArrayList<Category> interestCat = new ArrayList<Category>();
		
		//Dieser Part fuer alle Kategorien
		for(Category cat : allCat){
			interestCat.add(cat);
		}
		//Tracking und debugging infos
		System.out.println("AlleKategorien: " + allCat.size());
		System.out.println("RootKategorien: " + rootCat.size());
		System.out.println("SubKategorien: " + subCat.size());
		System.out.println("InteressierendeKategorien: " + interestCat.size());
		System.out.println("Badges: " + level.getBadgeCount());
		System.out.println("Level: " + level.getLvlCount());
		
		
		//Anzahl der Items pro Kategorie
		for(Category cat: interestCat){
			//Debugging Testing
			System.out.println(cat.getName() + " "+ "KatLevel: " + level.getBadgeLevel(cat.getId()));
			System.out.println(cat.getName() + " " + "KatLevelLeft: " + level.getBadgeLevelLeft(cat.getId()));
			ArrayList<Item> items = Controller.getInstance().getItemsOfACategory(cat.getId());
			System.out.println(cat.getName() + "_:anhzahl:_" + items.size() + "_lvl: " + UserLevel.getBadgemark5());
			arrList.add(new Badge(cat,items.size(), cat.getIcon().getName(),level.awardedBadge1(cat.getId()),level.awardedBadge2(cat.getId()),level.awardedBadge3(cat.getId()),level.awardedBadge4(cat.getId()),level.awardedBadge5(cat.getId())));
		}
		return arrList;
	}
	
}
