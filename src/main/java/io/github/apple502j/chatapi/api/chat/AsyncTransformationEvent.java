package io.github.apple502j.chatapi.api.chat;

import io.github.apple502j.chatapi.api.BlockableEvent;

import io.github.apple502j.chatapi.api.MessageType;
import io.github.apple502j.chatapi.api.message.Message;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

@ApiStatus.NonExtendable
public interface AsyncTransformationEvent extends BlockableEvent {
	String getUnprocessedContents();
	Text getUnprocessedText();
	Message getMessage();
	MinecraftServer getServer();
	@Nullable
	ServerPlayerEntity getSender();
	MessageType getType();

	/**
	 * Returns a future of a message with the new contents.
	 * @implNote Calling this method may mutate {@link #getMessage() the message}.
	 * @apiNote This is intended to be returned from the event callback.
	 * @param newContents the new contents.
	 * @return a future
	 */
	CompletableFuture<Message> replacing(String newContents);

	@FunctionalInterface
	interface Callback {
		CompletableFuture<Message> transform(AsyncTransformationEvent event);
	}
}
