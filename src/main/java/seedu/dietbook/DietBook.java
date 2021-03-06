package seedu.dietbook;

import seedu.dietbook.ui.Ui;
import seedu.dietbook.database.DataBase;
import seedu.dietbook.list.FoodList;
import seedu.dietbook.exception.DietException;
import seedu.dietbook.command.Command;
import seedu.dietbook.person.FitnessLevel;
import seedu.dietbook.person.Gender;
import seedu.dietbook.saveload.PersonSaveLoadManager;
import seedu.dietbook.saveload.FoodPortionDateSaveLoadManager;

//@@author tikimonarch
/**
 * Main class of the program.
 * The DietBook program is an application which can store, display and check your daily dietary intake.
 *
 * @author tikimonarch
 */

import java.io.FileNotFoundException;

public class DietBook {
    private FoodList foodList;
    private Ui ui;
    private Manager manager;
    private DataBase dataBase;
    private PersonSaveLoadManager loadPerson;
    private FoodPortionDateSaveLoadManager loadFood;
    public static boolean isExit = false;

    /**
     * Constructor for new DietBook.
     */
    public DietBook() {
        ui = new Ui();
        loadFood = new FoodPortionDateSaveLoadManager();
        loadPerson = new PersonSaveLoadManager();
        foodList = new FoodList();
        dataBase = new DataBase();
        dataBase.init();
        manager = new Manager(foodList, dataBase);
    }

    /**
     * Method to load Person data.
     */
    public void loadPerson() {
        try {
            System.out.println("Loading personal information...");
            loadPerson.load("UserInfo.txt");
            FitnessLevel fitLvl = FitnessLevel.NONE;
            int fitLvlInt = loadPerson.getFitnessLevel();
            if (fitLvlInt == 1) {
                fitLvl = FitnessLevel.NONE;
            } else if (fitLvlInt == 2) {
                fitLvl = FitnessLevel.LOW;
            } else if (fitLvlInt == 3) {
                fitLvl = FitnessLevel.MEDIUM;
            } else if (fitLvlInt == 4) {
                fitLvl = FitnessLevel.HIGH;
            } else if (fitLvlInt == 5) {
                fitLvl = FitnessLevel.EXTREME;
            }

            Gender gender;
            String genderString = loadPerson.getGender();
            if (genderString.equals("Male")) {
                gender = Gender.MALE;
            } else if (genderString.equals("Female")) {
                gender = Gender.FEMALE;
            } else {
                gender = Gender.OTHERS;
            }
            manager.getPerson().setAll(loadPerson.getName(), gender, loadPerson.getAge(),
                    loadPerson.getHeight(), loadPerson.getOriginalWeight(), loadPerson.getCurrentWeight(),
                    loadPerson.getTargetWeight(), fitLvl);
            Manager.commandCount = 3;
        } catch (FileNotFoundException e) {
            ui.printErrorMessage("Person data file not found! Creating new personal data....");
        } catch (IllegalAccessException e) {
            ui.printErrorMessage(e.getMessage());
        }
    }

    /**
     * Method to load DietBook FoodList data.
     */
    public void loadFood() {
        try {
            System.out.println("Loading personal food data...");
            loadFood.load("FoodList.txt");
            manager.setFoodList(loadFood.getFoodList());
            Manager.commandCount = 3;
        } catch (FileNotFoundException e) {
            ui.printErrorMessage("FoodList data file not found! Creating new food list...");
        } catch (IllegalAccessException e) {
            ui.printErrorMessage(e.getMessage());
        }

    }

    /**
     * Main method to run the program.
     */
    public static void main(String[] args) {
        DietBook dietBook = new DietBook();
        dietBook.loadPerson();
        dietBook.loadFood();
        if (Manager.commandCount > 2) {
            dietBook.ui.printWelcomeBackMessage(dietBook.manager.getPerson().getName());
        } else if (Manager.commandCount <= 2) {
            dietBook.ui.printWelcomeMessage();
        }

        while (!isExit) {
            try {
                String userInput = dietBook.ui.getCommand();
                Command c = dietBook.manager.manage(userInput);
                c.execute(dietBook.manager, dietBook.ui);
            } catch (DietException e) {
                dietBook.ui.printErrorMessage(e.getMessage());
            } catch (Exception e) {
                dietBook.ui.printErrorMessage("Oops something went wrong!");
            }
        }
    }
}
