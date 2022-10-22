package PvMTickCounter;

import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.Player;
import net.runelite.api.events.AnimationChanged;
import net.runelite.api.kit.KitType;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@PluginDescriptor(name="PvM Tick Counter")

public class TickCounterPlugin extends Plugin{
    @Inject
    private OverlayManager overlayManager;
    @Inject
    private TickCounterConfig config;
    @Inject
    private Client client;

    private TickCounterUtil id;

    @Provides
    TickCounterConfig provideConfig(ConfigManager configManager)
    {
        return configManager.getConfig(TickCounterConfig.class);
    }
    @Inject
    private TickCounterOverlay overlay;

    Map<String, Integer> activity = new HashMap<>();

    private HashMap<Player, Boolean> blowpiping = new HashMap<>();
    boolean instanced = false;
    boolean prevInstance = false;

    @Override
    protected void startUp() throws Exception
    {
        id = new TickCounterUtil();
        id.init();
        overlayManager.add(overlay);
    }

    @Override
    protected void shutDown() throws Exception
    {
        overlayManager.remove(overlay);
        activity.clear();
    }

    @Subscribe
    public void onAnimationChanged(AnimationChanged e){
        if (!(e.getActor() instanceof Player))
            return;
        Player p = (Player) e.getActor();
        int weapon = -1;
        if (p.getPlayerComposition() != null)
            weapon = p.getPlayerComposition().getEquipmentId(KitType.WEAPON);
        int delta = 0;

        delta = id.getTicks(p.getAnimation(),weapon);

        if (delta > 0)
        {
            String name = p.getName();
            this.activity.put(name, this.activity.getOrDefault(name, 0) + delta);
        }
    }
}