package io.github.apple502j.chatapi.api.chat;

import io.github.apple502j.chatapi.api.MessageType;
import io.github.apple502j.chatapi.api.message.Message;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

@ApiStatus.NonExtendable
public interface AsyncHandleEvent {
	String getUnprocessedContents();
	Text getUnprocessedText();
	MinecraftServer getServer();
	@Nullable
	ServerPlayerEntity getSender();
	Message getMessage();
	MessageType getType();

	@FunctionalInterface
	interface Callback {
		CompletableFuture<Void> process(AsyncHandleEvent event);
	}
}
