package io.github.apple502j.chatapi.api.chat;

import io.github.apple502j.chatapi.api.message.Message;
import io.github.apple502j.chatapi.impl.chat.AsyncTransformationEventImpl;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;

public final class ChatEvents {
	public static final Identifier CONTENTS_EDIT_PHASE = new Identifier("fcapi","phase/content_edit");
	public static final Identifier STYLE_PHASE = new Identifier("fcapi", "phase/style");

	public static final Event<SyncBlockEvent.Callback> SYNC_BLOCK = EventFactory.createArrayBacked(SyncBlockEvent.Callback.class, handlers -> event -> {
		for (SyncBlockEvent.Callback handler : handlers) {
			handler.process(event);
		}
	});
	public static final Event<AsyncTransformationEvent.Callback> ASYNC_TRANSFORMATION = EventFactory.createArrayBacked(AsyncTransformationEvent.Callback.class, event -> CompletableFuture.completedFuture(event.getMessage()), handlers -> event -> {
		CompletableFuture<Message> future = CompletableFuture.completedFuture(event.getMessage());
		for (AsyncTransformationEvent.Callback handler : handlers) {
			future = future.thenComposeAsync(message -> handler.transform(AsyncTransformationEventImpl.with(event, message)));
		}
		return future;
	});
	public static final Event<AsyncBlockEvent.Callback> ASYNC_BLOCK = EventFactory.createArrayBacked(AsyncBlockEvent.Callback.class, event -> CompletableFuture.completedFuture(null), handlers -> event -> {
		CompletableFuture<Void> future = CompletableFuture.completedFuture(null);
		for (AsyncBlockEvent.Callback handler : handlers) {
			future = future.thenComposeAsync(void_ -> handler.process(event));
		}
		return future;
	});
	public static final Event<AsyncHandleEvent.Callback> ASYNC_HANDLE = EventFactory.createArrayBacked(AsyncHandleEvent.Callback.class, event -> CompletableFuture.completedFuture(null), handlers -> event -> {
		CompletableFuture<Void> future = CompletableFuture.completedFuture(null);
		for (AsyncHandleEvent.Callback handler : handlers) {
			future = future.thenComposeAsync(void_ -> handler.process(event));
		}
		return future;
	});

	static {
		ASYNC_TRANSFORMATION.addPhaseOrdering(CONTENTS_EDIT_PHASE, Event.DEFAULT_PHASE);
		ASYNC_TRANSFORMATION.addPhaseOrdering(Event.DEFAULT_PHASE, STYLE_PHASE);
	}
}
