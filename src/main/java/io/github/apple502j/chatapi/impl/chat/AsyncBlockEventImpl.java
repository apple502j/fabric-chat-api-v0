package io.github.apple502j.chatapi.impl.chat;

import io.github.apple502j.chatapi.api.MessageType;
import io.github.apple502j.chatapi.api.message.Message;
import io.github.apple502j.chatapi.api.chat.AsyncBlockEvent;
import io.github.apple502j.chatapi.impl.BlockableEventImpl;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

import org.jetbrains.annotations.Nullable;

public class AsyncBlockEventImpl extends BlockableEventImpl implements AsyncBlockEvent {
	private final MinecraftServer server;
	@Nullable
	private final ServerPlayerEntity sender;
	private final Text unprocessedText;
	private final String unprocessedContents;
	private final Message message;
	private final MessageType type;

	public AsyncBlockEventImpl(MinecraftServer server, @Nullable ServerPlayerEntity sender, TranslatableText unprocessedText, String unprocessedContents, Message message, MessageType type) {
		this.server = server;
		this.sender = sender;
		this.unprocessedContents = unprocessedContents;
		this.unprocessedText = unprocessedText;
		this.message = message;
		this.type = type;
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
	public MessageType getType() {
		return this.type;
	}
}
