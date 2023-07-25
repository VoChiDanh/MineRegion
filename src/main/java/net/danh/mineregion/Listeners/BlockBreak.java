package net.danh.mineregion.Listeners;

import net.danh.mineregion.API.MineManager;
import net.danh.mineregion.Utils.CooldownManager;
import net.danh.mineregion.WorldGuard.WorldGuard;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BlockBreak implements Listener {

    public static final List<Player> bypass = new ArrayList<>();

    public static final HashMap<Location, Material> blocks = new HashMap<>();
    public static final List<Location> locations = new ArrayList<>();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBreak(@NotNull BlockBreakEvent e) {
        Player p = e.getPlayer();
        Block block = e.getBlock();
        Location location = block.getLocation();
        if (!bypass.contains(p)) {
            if (WorldGuard.handleForLocation(p, e.getBlock().getLocation())) {
                Material replace = Material.getMaterial(new MineManager(block).getReplaceBlock());
                Material regen = Material.valueOf(new MineManager(block).getNextRegen());
                if (regen != Material.AIR) {
                    if (block.getBlockData() instanceof Ageable) {
                        Ageable ageable = (Ageable) block.getBlockData();
                        if (ageable.getAge() == ageable.getMaximumAge()) {
                            if (!new MineManager(block).checkBreak()) {
                                e.setCancelled(true);
                                return;
                            }
                            e.setCancelled(true);
                            e.setDropItems(false);
                            block.getDrops().clear();
                            locations.add(location);
                            blocks.put(location, regen);
                            CooldownManager.setCooldown(location, new MineManager(block).getTimeRegen());
                            new MineManager(block).runCommand(p);
                            block.setType(replace != null ? replace : Material.AIR);
                        }
                    } else {
                        if (new MineManager(block).checkBreak()) {
                            e.setCancelled(true);
                            e.setDropItems(false);
                            block.getDrops().clear();
                            locations.add(location);
                            blocks.put(location, regen);
                            CooldownManager.setCooldown(location, new MineManager(block).getTimeRegen());
                            new MineManager(block).runCommand(p);
                            block.setType(replace != null ? replace : Material.BEDROCK);
                        } else {
                            e.setCancelled(true);
                        }
                    }
                }
            }
        }
    }
}
