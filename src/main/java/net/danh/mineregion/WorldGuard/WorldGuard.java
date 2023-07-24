package net.danh.mineregion.WorldGuard;

import net.danh.mineregion.MineRegion;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.codemc.worldguardwrapper.WorldGuardWrapper;
import org.codemc.worldguardwrapper.flag.IWrappedFlag;
import org.codemc.worldguardwrapper.flag.WrappedState;

import java.util.Optional;
import java.util.logging.Level;

public class WorldGuard {

    static boolean registered = false;

    public static void register(final JavaPlugin plugin) {
        Plugin worldGuard = Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");
        if (worldGuard != null) {
            WorldGuardWrapper wrapper = WorldGuardWrapper.getInstance();
            Optional<IWrappedFlag<WrappedState>> miningFlag = wrapper.registerFlag("mine-region", WrappedState.class, WrappedState.DENY);
            miningFlag.ifPresent(wrappedStateIWrappedFlag -> {
                plugin.getLogger().log(Level.INFO, "Registered flag " + wrappedStateIWrappedFlag.getName());
                registered = true;
            });
        }
    }

    public static boolean handleForLocation(Player player, Location loc) {
        IWrappedFlag<WrappedState> flag = getStateFlag("mine-region");
        if (flag == null) {
            return true;
        }

        WrappedState state = WorldGuardWrapper.getInstance().queryFlag(player, loc, flag).orElse(WrappedState.DENY);
        return state.equals(WrappedState.ALLOW);
    }

    public static IWrappedFlag<WrappedState> getStateFlag(String flagName) {
        Optional<IWrappedFlag<WrappedState>> flagOptional = WorldGuardWrapper.getInstance().getFlag(flagName, WrappedState.class);
        if (flagOptional.isEmpty() && !registered) {
            MineRegion.getMineRegion().getLogger().log(Level.INFO, "Failed to get net.danh.mineregion.WorldGuard state flag '" + flagName + "'.");
            MineRegion.getMineRegion().getLogger().log(Level.INFO, "net.danh.mineregion.WorldGuard state flag '" + flagName + "' is not present!");
            return null;
        } else if (flagOptional.isPresent() && registered) {
            return flagOptional.get();
        } else {
            return null;
        }
    }
}
