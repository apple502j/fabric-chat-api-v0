package io.github.apple502j.chatapi.impl.message;

import net.minecraft.text.TranslatableText;

public final class SimpleChatMessage extends SimpleMessage {
	public SimpleChatMessage(TranslatableText text, String contents) {
		super(text, contents);
	}

	@Override
	boolean isValidText(TranslatableText text) {
		return "chat.type.text".equals(text.getKey());
	}

	@Override
	void replaceText(String newContents) {
		this.text.getArgs()[1] = newContents;
	}
}
