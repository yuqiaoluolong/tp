package seedu.dietbook.command;

import seedu.dietbook.Manager;
import seedu.dietbook.Ui;

public class ListCommand extends Command {
    @Override
    public void execute(Manager manager, Ui ui) {
        ui.printFoodList(manager.getFoodList().toString());
    }
}
