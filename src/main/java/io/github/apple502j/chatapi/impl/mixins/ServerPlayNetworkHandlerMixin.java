package io.github.apple502j.chatapi.impl.mixins;

import io.github.apple502j.chatapi.api.chat.ChatEvents;

import io.github.apple502j.chatapi.impl.AsyncBlockedException;
import io.github.apple502j.chatapi.impl.ChatAPIInitializer;
import io.github.apple502j.chatapi.impl.chat.AsyncBlockEventImpl;
import io.github.apple502j.chatapi.impl.chat.AsyncHandleEventImpl;
import io.github.apple502j.chatapi.impl.chat.AsyncTransformationEventImpl;
import io.github.apple502j.chatapi.impl.chat.SyncBlockEventImpl;

import io.github.apple502j.chatapi.impl.message.SimpleChatMessage;

import net.minecraft.network.MessageType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.filter.TextStream;
import net.minecraft.server.network.ServerPlayNetworkHandler;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import net.minecraft.text.TranslatableText;
import net.minecraft.util.Util;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerPlayNetworkHandlerMixin {
	@Shadow
	public abstract ServerPlayerEntity getPlayer();

	@Redirect(method = "handleMessage", at = @At(value = "INVOKE", target="Lnet/minecraft/server/PlayerManager;broadcast(Lnet/minecraft/text/Text;Ljava/util/function/Function;Lnet/minecraft/network/MessageType;Ljava/util/UUID;)V"))
	private void handleMessage(PlayerManager playerManager, Text serverMessage, Function<ServerPlayerEntity, Text> playerMessageFactory, MessageType type, UUID sender, TextStream.Message message) {
		MinecraftServer server = playerManager.getServer();
		ServerPlayerEntity player = this.getPlayer();
		TranslatableText unprocessedText = (TranslatableText)serverMessage.copy();
		String unprocessedContents = message.getFiltered();
		SyncBlockEventImpl syncBlockEvent = new SyncBlockEventImpl(server, player, unprocessedText, unprocessedContents, io.github.apple502j.chatapi.api.MessageType.CHAT);
		ChatEvents.SYNC_BLOCK.invoker().process(syncBlockEvent);
		ChatAPIInitializer.LOGGER.info(syncBlockEvent.debugString());
		if (syncBlockEvent.isBlocked()) return;
		SimpleChatMessage chatMessage = new SimpleChatMessage(unprocessedText, unprocessedContents);
		AsyncTransformationEventImpl transformationEvent = (AsyncTransformationEventImpl) new AsyncTransformationEventImpl(server, player, unprocessedText, unprocessedContents, chatMessage, io.github.apple502j.chatapi.api.MessageType.CHAT).copying(syncBlockEvent);
		CompletableFuture.runAsync(() -> {
		}, Util.getIoWorkerExecutor()).thenCompose((void_) -> ChatEvents.ASYNC_TRANSFORMATION.invoker().transform(transformationEvent)).thenCompose(transformedMessage -> {
			ChatAPIInitializer.LOGGER.info(transformationEvent.debugString());
			if (transformationEvent.isBlocked()) throw new AsyncBlockedException();
			AsyncBlockEventImpl asyncBlockEvent = (AsyncBlockEventImpl)new AsyncBlockEventImpl(server, player, unprocessedText, unprocessedContents, transformedMessage, io.github.apple502j.chatapi.api.MessageType.CHAT).copying(transformationEvent);
			return ChatEvents.ASYNC_BLOCK.invoker().process(asyncBlockEvent).thenApply((void_) -> asyncBlockEvent);
		}).thenAcceptAsync((asyncBlockEvent) -> {
			ChatAPIInitializer.LOGGER.info(asyncBlockEvent.debugString());
			if (asyncBlockEvent.isBlocked()) throw new AsyncBlockedException();
			Text text = asyncBlockEvent.getMessage().getText();
			server.execute(() -> playerManager.broadcast(text, receiver -> asyncBlockEvent.isBlocked(receiver) ? null : text, type, sender));
			ChatEvents.ASYNC_HANDLE.invoker().process(new AsyncHandleEventImpl(server, player, unprocessedText, unprocessedContents, asyncBlockEvent.getMessage(), io.github.apple502j.chatapi.api.MessageType.CHAT));
		}).exceptionally((throwable) -> {
			//if (throwable instanceof AsyncBlockedException) return null;
			ChatAPIInitializer.LOGGER.warn("Exception while handling chat: ", throwable);
			return null;
		});
	}
}
