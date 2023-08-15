package net.danh.mineregion.Utils;

import net.danh.mineregion.MineRegion;
import net.danh.miningcontest.Data.PlayerData;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class MiningContest {
    public static void addMinePoints(Player p, Block block) {
        if (MineRegion.isMiningContestInstall()) {
            PlayerData.addMinePoints(p, block.getType().name());
        }
    }
}
