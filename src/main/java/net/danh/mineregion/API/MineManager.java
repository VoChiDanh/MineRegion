package net.danh.mineregion.API;

import dev.lone.itemsadder.api.CustomBlock;
import me.clip.placeholderapi.PlaceholderAPI;
import net.danh.mineregion.MineRegion;
import net.danh.mineregion.Utils.Number;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class MineManager {
    private final Block block;
    private final Material material;

    public MineManager(@NotNull Block block) {
        this.block = block;
        this.material = block.getType();
    }

    public int getRegenNumber() {
        return Number.getInteger(getBlockSetting("max_number"));
    }

    public List<String> getListRegen() {
        return getBlockListSetting("regen");
    }

    public int getTimeRegen() {
        return Number.getInteger(getBlockSetting("time_regen"));
    }

    public Material getReplaceBlock() {
        if (checkCustomBlock().equalsIgnoreCase("ITEMSADDER"))
            return Objects.requireNonNull(Objects.requireNonNull(CustomBlock.getInstance(getBlockSetting("replace").split("-")[1])).getBlock()).getType();
        return Material.valueOf(getBlockSetting("replace"));
    }

    public int getAmount() {
        return Number.getInteger(getBlockSetting("amount"));
    }

    public List<String> getListCommand() {
        return getBlockListSetting("command");
    }

    public Block getBlock() {
        return block;
    }

    public Material getMaterial() {
        return material;
    }

    public String checkCustomBlock() {
        if (Bukkit.getServer().getPluginManager().getPlugin("ItemsAdder") != null) {
            if (CustomBlock.byAlreadyPlaced(block) != null) return "ITEMSADDER";
        }
        return "VANILLA";
    }

    public boolean checkBreak() {
        if (checkCustomBlock().equalsIgnoreCase("VANILLA"))
            return MineRegion.getFileSetting().get("config.yml").contains("block." + material.name());
        else if (checkCustomBlock().equalsIgnoreCase("ITEMSADDER") && CustomBlock.byAlreadyPlaced(block) != null)
            return MineRegion.getFileSetting().get("config.yml").contains("block.ITEMSADDER;" + Objects.requireNonNull(CustomBlock.byAlreadyPlaced(block)).getId());
        return false;
    }

    public Material getNextRegen() {
        List<String> regenlist = getListRegen();
        Map<Integer, String> chance = new HashMap<>();
        for (String regen : regenlist) {
            String[] reg = regen.split(";");
            String block = reg[0];
            Integer c = Number.getInteger(reg[1]);
            chance.put(c, block);
        }

        Random random = new Random();
        int randomNum = random.nextInt(getRegenNumber()) + 1;
        List<Integer> numbers = new ArrayList<>(chance.keySet());
        int nearestNumber = 0;
        int nearestDifference = Integer.MAX_VALUE;
        for (int num : numbers) {
            int difference = Math.abs(randomNum - num);
            if (difference < nearestDifference) {
                nearestNumber = num;
                nearestDifference = difference;
            }
        }
        String material = chance.get(nearestNumber);
        if (checkCustomBlock().equalsIgnoreCase("ITEMSADDER")) {
            return Objects.requireNonNull(Objects.requireNonNull(CustomBlock.getInstance(material.split("-")[1])).getBlock()).getType();
        }
        return Material.valueOf(material);
    }

    public void runCommand(Player p) {
        getListCommand().forEach(cmd -> {
            String cmd_1 = PlaceholderAPI.setPlaceholders(p, cmd);
            int fortune_level = 0;
            if (p.getInventory().getItemInMainHand().containsEnchantment(Enchantment.LOOT_BONUS_BLOCKS)) {
                fortune_level = p.getInventory().getItemInMainHand().getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS) + 2;
            }
            String cmd_2 = cmd_1.replace("#amount#", String.valueOf(Number.getRandomInteger(getAmount(), getAmount() + fortune_level)));
            new BukkitRunnable() {
                @Override
                public void run() {
                    MineRegion.getMineRegion().getServer().dispatchCommand(MineRegion.getMineRegion().getServer().getConsoleSender(), cmd_2);
                }
            }.runTask(MineRegion.getMineRegion());
        });
    }

    public String getBlockSetting(String setting) {
        if (checkCustomBlock().equalsIgnoreCase("ITEMSADDER") && CustomBlock.byAlreadyPlaced(block) != null)
            return MineRegion.getFileSetting().get("config.yml").getString("block.ITEMSADDER;" + Objects.requireNonNull(CustomBlock.byAlreadyPlaced(block)).getId() + "." + setting);
        return MineRegion.getFileSetting().get("config.yml").getString("block." + material.name() + "." + setting);
    }

    public List<String> getBlockListSetting(String setting) {
        if (checkCustomBlock().equalsIgnoreCase("ITEMSADDER") && CustomBlock.byAlreadyPlaced(block) != null)
            return MineRegion.getFileSetting().get("config.yml").getStringList("block.ITEMSADDER;" + Objects.requireNonNull(CustomBlock.byAlreadyPlaced(block)).getId() + "." + setting);
        return MineRegion.getFileSetting().get("config.yml").getStringList("block." + material.name() + "." + setting);
    }
}
