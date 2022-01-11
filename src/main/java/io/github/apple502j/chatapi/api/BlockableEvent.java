package io.github.apple502j.chatapi.api;

import net.minecraft.server.network.ServerPlayerEntity;

/**
 * Represents an event that allows blocking messages.
 *
 * Blocking is done using an integer level. Negative level indicates the message should be blocked,
 * and positive level indicates it should not. Additionally, a level with higher absolute value
 * supersedes ones with lower absolute values - for example, setting a block level {@code 20}
 * when the current level is {@code -10} will mark the message as "allowed", while the opposite
 * will do nothing.
 *
 * Block levels can also be set per receiver. If the absolute value of the player's block level is
 * higher than the global block level, the player's block level will be used to determine if the
 * message should be sent to that specific player. Note that since {@link #FORCE_DENY} has the
 * highest absolute value, setting the block level to that will always block the message, regardless
 * of the per-player level.
 */
public interface BlockableEvent {
	/**
	 * Represents the unbypassable block level.
	 */
	public static final int FORCE_DENY = Integer.MIN_VALUE + 1;
	public static final int DEFAULT = 0;
	int getBlockLevel();
	int getBlockLevel(ServerPlayerEntity player);
	void setBlockLevel(int level);
	void setBlockLevel(int level, ServerPlayerEntity ...players);

	default void forceDeny() {
		this.setBlockLevel(FORCE_DENY);
	}

	default void forceDeny(ServerPlayerEntity ...players) {
		this.setBlockLevel(FORCE_DENY, players);
	}
}
