package net.danh.mineregion.CMD;

import net.danh.mineregion.API.CMDBase;
import net.danh.mineregion.Listeners.BlockBreak;
import net.danh.mineregion.MineRegion;
import net.danh.mineregion.Utils.Chat;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MR extends CMDBase {
    public MR() {
        super("mr");
    }

    @Override
    public void execute(CommandSender c, String[] args) {
        if (c.hasPermission("mr.admin")) {
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("reload")) {
                    MineRegion.getFileSetting().reload("config.yml");
                    c.sendMessage(Chat.colorize("&8[&fMineRegion&8] &aReloaded"));
                }
                if (args[0].equalsIgnoreCase("bypass")) {
                    if (c instanceof Player) {
                        Player p = (Player) c;
                        if (BlockBreak.bypass.contains(p)) {
                            BlockBreak.bypass.remove(p);
                            p.sendMessage(Chat.colorize("&8[&fMineRegion&8] &cYou have been removed from bypass players"));
                        } else {
                            BlockBreak.bypass.add(p);
                            p.sendMessage(Chat.colorize("&8[&fMineRegion&8] &cYou have been added to bypass players"));
                        }
                    }
                }
            }
        }
    }

    @Override
    public List<String> TabComplete(CommandSender sender, String[] args) {
        List<String> completions = new ArrayList<>();
        List<String> commands = new ArrayList<>();
        if (args.length == 1) {
            if (sender.hasPermission("mr.admin")) {
                commands.add("reload");
                commands.add("bypass");
            }
            StringUtil.copyPartialMatches(args[0], commands, completions);
        }
        Collections.sort(completions);
        return completions;
    }
}
