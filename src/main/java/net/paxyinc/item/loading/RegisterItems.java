package net.paxyinc.item.loading;

import dev.crmodders.flux.engine.LoadStage;
import net.paxyinc.MachineRegistries;

import java.util.List;

public class RegisterItems extends LoadStage {

    @Override
    public List<Runnable> getGlTasks() {
        List<Runnable> tasks = super.getGlTasks();
        tasks.add(() -> loader.setupProgressBar(loader.progress1, 0, "Loading Items"));
        MachineRegistries.ITEM_FINALIZERS.access().forEach(tasks::add);
        MachineRegistries.FLUID_FINALIZERS.access().forEach(tasks::add);
        MachineRegistries.GAS_FINALIZERS.access().forEach(tasks::add);
        MachineRegistries.TOOLS_FINALIZERS.access().forEach(tasks::add);
        return tasks;
    }
}
