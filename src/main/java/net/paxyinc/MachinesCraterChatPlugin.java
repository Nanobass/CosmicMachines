package net.paxyinc;

import com.insertsoda.craterchat.api.v1.Command;
import com.insertsoda.craterchat.api.v1.CraterChatPlugin;
import net.paxyinc.commands.GiveCommand;
import net.paxyinc.commands.TestCommand;

import java.util.List;

public class MachinesCraterChatPlugin implements CraterChatPlugin {
    @Override
    public List<Class<? extends Command>> register() {
        return List.of(GiveCommand.class, TestCommand.class);
    }

}