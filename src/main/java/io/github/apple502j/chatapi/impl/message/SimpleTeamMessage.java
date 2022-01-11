package io.github.apple502j.chatapi.impl.message;

import net.minecraft.text.TranslatableText;

public final class SimpleTeamMessage extends SimpleMessage {
	public SimpleTeamMessage(TranslatableText text, String contents) {
		super(text, contents);
	}

	@Override
	boolean isValidText(TranslatableText text) {
		return "chat.type.team.sen".equals(text.getKey());
	}

	@Override
	void replaceText(String newContents) {
		this.text.getArgs()[2] = newContents;
	}
}
