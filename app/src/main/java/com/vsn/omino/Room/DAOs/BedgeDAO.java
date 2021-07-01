package com.vsn.omino.Room.DAOs;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.vsn.omino.Room.BedgeEntity;

import java.util.List;

@Dao
public interface BedgeDAO {
    @Query("SELECT * FROM BedgeEntity")
    List<BedgeEntity> getAll();

    @Insert
    void insertAll(BedgeEntity... bedgeEntities);

    @Update
    void update(BedgeEntity bedgeEntity);

    @Delete
    void delete(BedgeEntity bedgeEntities);

    @Query("UPDATE BedgeEntity SET home_unread_posts = (:bedgeCount) WHERE id = '1'")
    void update_bedge(int bedgeCount);

    @Query("UPDATE BedgeEntity SET home_unread_chat = (:bedgeCount) WHERE id = '1'")
    void update_bedge_chat(int bedgeCount);
}