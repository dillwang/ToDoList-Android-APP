package edu.ucsd.cse110.successorator;

import static androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewmodel.ViewModelInitializer;

import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import edu.ucsd.cse110.successorator.lib.data.GoalRepository;
import edu.ucsd.cse110.successorator.lib.domain.Goal;
import edu.ucsd.cse110.successorator.lib.util.MutableSubject;
import edu.ucsd.cse110.successorator.lib.util.SimpleSubject;
import edu.ucsd.cse110.successorator.lib.util.Subject;

public class MainViewModel extends ViewModel {
    // Domain state (true "Model" state)
    private final GoalRepository goalRepository;

    // UI state
    private final MutableSubject<List<Goal>> orderedGoals;
    private final MutableSubject<String> displayedText;
    private final MutableLiveData<Boolean> isGoalListEmpty = new MutableLiveData<>();

    public static final ViewModelInitializer<MainViewModel> initializer =
            new ViewModelInitializer<>(
                    MainViewModel.class,
                    creationExtras -> {
                        var app = (SuccessoratorApplication) creationExtras.get(APPLICATION_KEY);
                        assert app != null;
                        return new MainViewModel(app.getGoalRepository());
                    });

    public MainViewModel(GoalRepository goalRepository) {
        this.goalRepository = goalRepository;

        // Create the observable subjects.
        this.orderedGoals = new SimpleSubject<>();
        this.displayedText = new SimpleSubject<>();

        // When the goals change, update the ordering.
        goalRepository.getAllGoals().observe(goals -> {
            if (goals == null) return;
            var ordered = goals.stream()
                    .sorted()
                    .collect(Collectors.toList());
            this.orderedGoals.setValue(ordered);
            isGoalListEmpty.setValue(goals.isEmpty());
        });
    }

    public Subject<String> getDisplayedText() {
        return displayedText;
    }

    public Subject<List<Goal>> getOrderedGoals() {
        return orderedGoals;
    }


    public void rollOver(){
        goalRepository.rollOver();
    }

    public void toggleGoalStatus(int id){
        goalRepository.toggleIsCompleteStatus(id);
    }

    public void deleteGoal(int id){
        goalRepository.deleteGoal(id);
    }

    public int addGoal(String content){
        return goalRepository.addGoal(content);
    }

    public LiveData<Boolean> getIsGoalListEmpty() {
        return isGoalListEmpty;
    }
}