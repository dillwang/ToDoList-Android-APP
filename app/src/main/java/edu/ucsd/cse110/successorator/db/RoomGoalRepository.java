package edu.ucsd.cse110.successorator.db;

import androidx.lifecycle.Transformations;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import edu.ucsd.cse110.successorator.lib.data.GoalRepository;
import edu.ucsd.cse110.successorator.lib.domain.Goal;
import edu.ucsd.cse110.successorator.lib.util.Subject;
import edu.ucsd.cse110.successorator.util.LiveDataSubjectAdapter;

public class RoomGoalRepository implements GoalRepository {
    protected final GoalDao goalDao;

    public RoomGoalRepository(GoalDao goalDao) {
        this.goalDao = goalDao;
    }

    public void changeIsCompleteStatus(int id, boolean isComplete) {
        goalDao.updateGoalStatus(id, isComplete);
    }

    public void rollOver() {
        goalDao.rollOver();
    }

    public int addGoal(String content) {
        var newGoalEntity = new GoalEntity(content, false, 0);
        return goalDao.append(newGoalEntity);
    }

    public Subject<List<Goal>> getAllGoals() {
        var entityLiveList = goalDao.getAllGoalEntitiesAsLiveData();
        if(entityLiveList == null) return new LiveDataSubjectAdapter<>(null);
        var goalLiveList = Transformations.map(entityLiveList,  entities -> {
            return entities.stream()
                        .map(GoalEntity::toGoal)
                        .collect(Collectors.toList());
        });
        return new LiveDataSubjectAdapter<>(goalLiveList);
    }
}
