package io.github.apple502j.chatapi.impl.message;

import io.github.apple502j.chatapi.api.message.Message;

import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

import java.util.Objects;

abstract class SimpleMessage implements Message {
	final TranslatableText text;
	String contents;

	SimpleMessage(TranslatableText text, String contents) {
		Objects.requireNonNull(text, "text is null");
		Objects.requireNonNull(contents, "contents is null");
		if (!this.isValidText(text)) throw new IllegalArgumentException("text must be a chat message");
		this.text = text;
		this.contents = contents;
	}

	abstract boolean isValidText(TranslatableText text);

	abstract void replaceText(String newContents);

	@Override
	public Text getText() {
		return this.text;
	}

	@Override
	public String getContents() {
		return this.contents;
	}

	@Override
	public SimpleMessage with(String newContents) {
		Objects.requireNonNull(newContents, "newContents is null");
		this.replaceText(newContents);
		this.contents = newContents;
		return this;
	}
}
