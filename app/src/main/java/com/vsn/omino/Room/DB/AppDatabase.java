package com.vsn.omino.Room.DB;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.vsn.omino.Room.BedgeEntity;
import com.vsn.omino.Room.DAOs.BedgeDAO;

@Database(entities = {BedgeEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract BedgeDAO BedgeDAO();
}