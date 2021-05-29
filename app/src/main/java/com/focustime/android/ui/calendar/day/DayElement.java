package com.focustime.android.ui.calendar.day;

public class DayElement {

    private String title;
    private String comment;
    private String type;

    public DayElement(String newTitle, String newComment, String newType){
        title = newTitle;
        comment = newComment;
        type = newType;
    }

    public String getTitle() {
        return title;
    }

    public String getComment() {
        return comment;
    }

    public String getType() {
        return type;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setType(String type) {
        this.type = type;
    }
}
