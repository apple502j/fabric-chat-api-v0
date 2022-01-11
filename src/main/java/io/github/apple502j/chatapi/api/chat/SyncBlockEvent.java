package io.github.apple502j.chatapi.api.chat;

import io.github.apple502j.chatapi.api.BlockableEvent;

import io.github.apple502j.chatapi.api.MessageType;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

@ApiStatus.NonExtendable
public interface SyncBlockEvent extends BlockableEvent {
	String getContents();
	Text getText();
	MinecraftServer getServer();
	@Nullable
	ServerPlayerEntity getSender();
	MessageType getType();

	@FunctionalInterface
	interface Callback {
		void process(SyncBlockEvent event);
	}
}
