package com.vsn.omino.models;

public class CommentModel {

    String Username;
    String Comment;

    public CommentModel(String username, String comment) {
        Username = username;
        Comment = comment;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
    }
}
