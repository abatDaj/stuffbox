package com.stuffbox.model;

public class Badge {
	private String catName; //Kategoriename
	private String badgeIconSet; //Name des Kategorie Iconsets mit den Badges
	
	private boolean badge1;
	private boolean badge2;
	private boolean badge3;
	private boolean badge4;
	private boolean badge5;
	
	public Badge(String catName, String badgeIconSet, boolean badge1, boolean badge2, boolean badge3, boolean badge4, boolean badge5){
		this.catName = catName;
		this.badgeIconSet = badgeIconSet;
		this.badge1 = badge1;
		this.badge2 = badge2;
		this.badge3 = badge3;
		this.badge4 = badge4;
		this.badge5 = badge5;
	}

	public String getCatName() {
		return catName;
	}

	public void setCatName(String catName) {
		this.catName = catName;
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
	
}
