package io.github.apple502j.chatapi.impl.chat;

import io.github.apple502j.chatapi.api.MessageType;
import io.github.apple502j.chatapi.api.chat.SyncBlockEvent;

import io.github.apple502j.chatapi.impl.BlockableEventImpl;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import org.jetbrains.annotations.Nullable;

public class SyncBlockEventImpl extends BlockableEventImpl implements SyncBlockEvent {
	private final MinecraftServer server;
	@Nullable
	private final ServerPlayerEntity sender;
	private final Text text;
	private final String contents;
	private final MessageType type;

	public SyncBlockEventImpl(MinecraftServer server, @Nullable ServerPlayerEntity sender, Text unprocessedText, String unprocessedContents, MessageType type) {
		this.server = server;
		this.sender = sender;
		this.contents = unprocessedContents;
		this.text = unprocessedText;
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
	public Text getText() {
		return this.text;
	}

	@Override
	public String getContents() {
		return this.contents;
	}

	@Override
	public MessageType getType() {
		return this.type;
	}
}
