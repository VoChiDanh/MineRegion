package net.danh.mineregion;

import net.danh.mineregion.CMD.MR;
import net.danh.mineregion.Listeners.BlockBreak;
import net.danh.mineregion.Utils.CooldownManager;
import net.danh.mineregion.WorldGuard.WorldGuard;
import net.xconfig.bukkit.model.SimpleConfigurationManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public final class MineRegion extends JavaPlugin {

    private static MineRegion mineRegion;

    public static MineRegion getMineRegion() {
        return mineRegion;
    }

    public static SimpleConfigurationManager getFileSetting() {
        return SimpleConfigurationManager.get();
    }

    @Override
    public void onLoad() {
        mineRegion = this;
        WorldGuard.register(mineRegion);
    }

    @Override
    public void onEnable() {
        SimpleConfigurationManager.register(mineRegion);
        getFileSetting().build("", false, "config.yml");
        getServer().getPluginManager().registerEvents(new BlockBreak(), mineRegion);
        new MR();
        Bukkit.getScheduler().scheduleSyncRepeatingTask(mineRegion, () -> new BukkitRunnable() {
            @Override
            public void run() {
                if (!BlockBreak.locations.isEmpty()) {
                    for (int i = 0; i < BlockBreak.locations.size(); i++) {
                        Location location = BlockBreak.locations.get(i);
                        int times = CooldownManager.getCooldown(location);
                        if (Math.abs(times) > 0) {
                            CooldownManager.setCooldown(location, --times);
                            if (Math.abs(times) == 0) {
                                Material block_type = BlockBreak.blocks.get(location);
                                location.getBlock().setType(block_type);
                                BlockBreak.blocks.remove(location, block_type);
                                BlockBreak.locations.remove(location);
                            }
                        } else {
                            Material block_type = BlockBreak.blocks.get(location);
                            location.getBlock().setType(block_type);
                            BlockBreak.blocks.remove(location, block_type);
                            BlockBreak.locations.remove(location);
                        }
                    }
                }
            }
        }.runTask(MineRegion.getMineRegion()), 20L, 20L);
    }

    @Override
    public void onDisable() {
        getFileSetting().save("config.yml");
        for (Location location : BlockBreak.locations) {
            location.getBlock().setType(BlockBreak.blocks.get(location));
        }
        BlockBreak.bypass.clear();
    }
}
