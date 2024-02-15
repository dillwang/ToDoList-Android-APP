package edu.ucsd.cse110.successorator.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

//import edu.ucsd.cse110.successorator.lib.domain.Goal;

@Database(entities = {GoalEntity.class}, version = 1)
public abstract class SuccessoratorDatabase extends RoomDatabase {
    public abstract GoalDao goalDao();
}