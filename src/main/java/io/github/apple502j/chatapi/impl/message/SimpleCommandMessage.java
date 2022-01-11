package io.github.apple502j.chatapi.impl.message;

import net.minecraft.text.TranslatableText;

public final class SimpleCommandMessage extends SimpleMessage {
	public SimpleCommandMessage(TranslatableText text, String contents) {
		super(text, contents);
	}

	@Override
	boolean isValidText(TranslatableText text) {
		return "commands.message.display.incoming".equals(text.getKey());
	}

	@Override
	void replaceText(String newContents) {
		this.text.getArgs()[1] = newContents;
	}
}
