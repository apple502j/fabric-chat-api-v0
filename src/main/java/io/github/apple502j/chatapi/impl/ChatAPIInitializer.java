package io.github.apple502j.chatapi.impl;

import io.github.apple502j.chatapi.api.MessageType;
import io.github.apple502j.chatapi.api.chat.ChatEvents;

import io.github.apple502j.chatapi.api.message.Message;

import net.fabricmc.api.ModInitializer;

import net.minecraft.server.network.ServerPlayerEntity;

import net.minecraft.text.Text;

import net.minecraft.util.Formatting;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.CompletableFuture;

public class ChatAPIInitializer implements ModInitializer {
	public static final Logger LOGGER = LogManager.getLogger("chatapi");

	@Override
	public void onInitialize() {
		LOGGER.info("Chat API v0 (EXPERIMENTAL) Loaded!");
		this.doExperiments();
	}

	private void doExperiments() {
		ChatEvents.SYNC_BLOCK.register(event -> {
			if (event.getType() != MessageType.CHAT) return;
			String contents = event.getContents();
			if (StringUtils.startsWith(contents, "# ")) {
				event.forceDeny();
				return;
			}
			if (StringUtils.startsWith(contents, "C:")) {
				event.setBlockLevel(-1);
				for (ServerPlayerEntity player : event.getServer().getPlayerManager().getPlayerList()) {
					LOGGER.info("{}: {}", player, player.isCreative());
					if (player.isCreative()) event.setBlockLevel(2, player);
				}
			}
		});
		ChatEvents.ASYNC_TRANSFORMATION.register(ChatEvents.CONTENTS_EDIT_PHASE, event -> {
			if (event.getType() != MessageType.CHAT) return null;
			String contents = event.getMessage().getContents();
			if (StringUtils.containsIgnoreCase(contents, "forge")) {
				contents = contents.replaceAll("forge", "froge");
			}
			if (StringUtils.containsIgnoreCase(contents, "spigot")) {
				event.setBlockLevel(-10);
			}
			return event.replacing(contents);
		});
		ChatEvents.ASYNC_TRANSFORMATION.register(ChatEvents.STYLE_PHASE, event -> CompletableFuture.completedFuture(new ColoredMessage(event.getMessage())));
		ChatEvents.ASYNC_BLOCK.register(event -> CompletableFuture.runAsync(() -> {
			if (StringUtils.containsIgnoreCase(event.getMessage().getContents(), "froge da best")) {
				event.setBlockLevel(-20);
			}
		}));
		ChatEvents.ASYNC_HANDLE.register(event -> CompletableFuture.runAsync(() -> {
			LOGGER.info("Message sent by {}: {}", event.getSender(), event.getMessage().getContents());
		}));
	}

	record ColoredMessage(Message original) implements Message {

		@Override
		public Text getText() {
			return this.original.getText().copy().formatted(Formatting.AQUA);
		}

		@Override
		public String getContents() {
			return this.original.getContents();
		}

		@Override
		public Message with(String newContents) {
			return new ColoredMessage(this.original.with(newContents));
		}
	}
}
