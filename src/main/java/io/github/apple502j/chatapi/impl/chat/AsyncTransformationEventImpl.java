package io.github.apple502j.chatapi.impl.chat;

import io.github.apple502j.chatapi.api.MessageType;
import io.github.apple502j.chatapi.api.message.Message;
import io.github.apple502j.chatapi.api.chat.AsyncTransformationEvent;

import io.github.apple502j.chatapi.impl.BlockableEventImpl;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class AsyncTransformationEventImpl extends BlockableEventImpl implements AsyncTransformationEvent {
	private final MinecraftServer server;
	@Nullable
	private final ServerPlayerEntity sender;
	private final Text unprocessedText;
	private final String unprocessedContents;
	private Message message;
	private final MessageType type;

	public AsyncTransformationEventImpl(MinecraftServer server, @Nullable ServerPlayerEntity sender, TranslatableText unprocessedText, String unprocessedContents, Message message, MessageType type) {
		this.server = server;
		this.sender = sender;
		this.unprocessedContents = unprocessedContents;
		this.unprocessedText = unprocessedText;
		this.message = message;
		this.type = type;
	}

	public static AsyncTransformationEvent with(AsyncTransformationEvent event, Message message) {
		if (message == null) return event;
		return ((AsyncTransformationEventImpl)event).with(message);
	}

	private AsyncTransformationEvent with(Message message) {
		this.message = message;
		return this;
	}

	@Override
	public MinecraftServer getServer() {
		return this.server;
	}

	@Override
	public @Nullable ServerPlayerEntity getSender() {
		return this.sender;
	}

	@Override
	public Text getUnprocessedText() {
		return this.unprocessedText;
	}

	@Override
	public String getUnprocessedContents() {
		return this.unprocessedContents;
	}

	@Override
	public Message getMessage() {
		return this.message;
	}

	@Override
	public CompletableFuture<Message> replacing(String newContents) {
		return CompletableFuture.completedFuture(this.message.with(newContents));
	}

	@Override
	public MessageType getType() {
		return this.type;
	}
}
