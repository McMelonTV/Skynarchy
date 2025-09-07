package ing.boykiss.skynarchy;

import ing.boykiss.skynarchy.command.IslandCommand;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Skynarchy implements ModInitializer {
    public static final String MOD_ID = "skynarchy";
    public static final Logger LOGGER = LoggerFactory.getLogger(Skynarchy.class);

    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> IslandCommand.register(dispatcher));
    }
}
