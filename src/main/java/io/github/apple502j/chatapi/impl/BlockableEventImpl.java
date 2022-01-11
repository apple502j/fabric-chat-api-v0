package io.github.apple502j.chatapi.impl;

import io.github.apple502j.chatapi.api.BlockableEvent;

import it.unimi.dsi.fastutil.objects.Reference2IntMap;
import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Util;

import java.util.Objects;

public abstract class BlockableEventImpl implements BlockableEvent {
	public int level = DEFAULT;
	public Reference2IntOpenHashMap<ServerPlayerEntity> overrides = Util.make(new Reference2IntOpenHashMap<>(), map -> map.defaultReturnValue(DEFAULT));

	@Override
	public int getBlockLevel() {
		return this.level;
	}

	@Override
	public int getBlockLevel(ServerPlayerEntity player) {
		Objects.requireNonNull(player, "player is null");
		return this.overrides.getOrDefault(player, DEFAULT);
	}

	@Override
	public void setBlockLevel(int level) {
		if (Math.abs(level) >= Math.abs(this.level)) {
			this.level = level;
		}
	}

	@Override
	public void setBlockLevel(int level, ServerPlayerEntity ...players) {
		for (ServerPlayerEntity player : players) {
			this.overrides.computeInt(player, (p, current) -> {
				if (current == null || Math.abs(level) >= Math.abs(current)) {
					return level;
				}
				return current;
			});
		}
	}

	public boolean isBlocked() {
		if (this.level == FORCE_DENY) return true;
		if (this.level >= DEFAULT) return false;
		for (int overriddenLevel : this.overrides.values()) {
			if (overriddenLevel > DEFAULT) return false;
		}
		return true;
	}

	public boolean isBlocked(ServerPlayerEntity player) {
		ChatAPIInitializer.LOGGER.info("{}, {}, {}", player, this.level, this.overrides.getOrDefault(player, DEFAULT));
		if (this.level == FORCE_DENY) return true;
		int overriddenLevel = this.overrides.getOrDefault(player, DEFAULT);
		if (Math.abs(overriddenLevel) >= Math.abs(this.level)) {
			return overriddenLevel < DEFAULT;
		}
		return this.level < DEFAULT;
	}

	public BlockableEventImpl copying(BlockableEventImpl event) {
		this.level = event.level;
		this.overrides = event.overrides;
		return this;
	}

	public String debugString() {
		StringBuilder sb = new StringBuilder("%d : [".formatted(this.level));
		for (Reference2IntMap.Entry<ServerPlayerEntity> entry : this.overrides.reference2IntEntrySet()) {
			sb.append(entry.getKey().getDisplayName());
			sb.append("=");
			sb.append(entry.getIntValue());
		}
		return sb.append("]").toString();
	}
}
