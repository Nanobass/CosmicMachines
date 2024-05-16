package net.paxyinc.machines;

import com.insertsoda.craterchat.api.v1.Command;
import com.insertsoda.craterchat.api.v1.CraterChatPlugin;
import net.paxyinc.machines.commands.GiveCommand;
import net.paxyinc.machines.commands.TestCommand;

import java.util.List;

public class MachinesCraterChatPlugin implements CraterChatPlugin {
    @Override
    public List<Class<? extends Command>> register() {
        return List.of(GiveCommand.class, TestCommand.class);
    }

}