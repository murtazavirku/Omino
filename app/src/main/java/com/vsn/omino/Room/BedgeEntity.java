package com.vsn.omino.Room;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class BedgeEntity {
    @PrimaryKey
    public int id;

    @ColumnInfo(name = "home_post_last_id")
    public String home_post_last_id;

    @ColumnInfo(name = "home_unread_posts")
    public int home_unread_posts;

    @ColumnInfo(name = "home_unread_chat")
    public int home_unread_chat;

}