package net.danh.mineregion.API.Events;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class MiningEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private final Player p;
    private final Block block;
    private final Location playerLocation;
    private final Location blockLocation;
    private boolean cancel;

    public MiningEvent(Player player, Block block) {
        this.p = player;
        this.block = block;
        this.playerLocation = player.getLocation();
        this.blockLocation = block.getLocation();
        cancel = false;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public Player getPlayer() {
        return p;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        cancel = cancelled;
    }

    public Block getBlock() {
        return block;
    }

    public Location getPlayerLocation() {
        return playerLocation;
    }

    public Location getBlockLocation() {
        return blockLocation;
    }
}
