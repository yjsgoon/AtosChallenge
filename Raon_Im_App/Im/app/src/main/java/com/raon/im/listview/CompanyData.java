package com.raon.im.listview;


import java.util.StringTokenizer;

/**
 * Created by EunBin on 2016-03-11
 *
 * This class shows the list of the companies the user is interacting with.
 */
public class CompanyData {
    String name;  // name of the company
    String[] permissionItem; // requested data
    int permissionSize; // number of the requested data fields
    boolean isDataProvision;  // used to check whether the user agreed on providing personal information
    String timer;  // timer
    int year, month, day; // year, month, day of the timer

    public CompanyData(String name, boolean isDataProvision, String[] permissionItem) {
        this.name = name;
        this.permissionSize = permissionItem.length;
        this.isDataProvision = isDataProvision;
        this.permissionItem = permissionItem;
    }

    public CompanyData(String name, boolean isDataProvision, String[] permissionItem, String timer) {
        this.name = name;
        this.permissionSize = permissionItem.length;
        this.isDataProvision = isDataProvision;
        this.permissionItem = permissionItem;
        this.timer = timer;

        // used to tokenize the date type yyyy-mm-dd to year, month, day
        StringTokenizer token = new StringTokenizer(getTimer());
        year = Integer.parseInt(token.nextToken("-"));
        month = Integer.parseInt(token.nextToken("-")) - 1;
        day = Integer.parseInt(token.nextToken("-"));
    }

    public String getName() {
        return name;
    }

    public int getPermissionSize() {
        return permissionSize;
    }

    public boolean getDataProvision() {
        return isDataProvision;
    }

    public void setDataProvision(boolean DataProvision) {
        this.isDataProvision = DataProvision;
    }

    public String getPermissionItem(int position) {
        return permissionItem[position];
    }

    public String getTimer() {
        return timer;
    }

    public void setTimer(String timer) {
        this.timer = this.year + "-" + this.month + "-" + this.day;
    }

    public int getYear() {  return year; }

    public void setYear(int year) { this.year = year; }

    public int getDay() { return day; }

    public void setDay(int day) { this.day = day; }

    public int getMonth() { return month; }

    public void setMonth(int month) { this.month = month; }
}
