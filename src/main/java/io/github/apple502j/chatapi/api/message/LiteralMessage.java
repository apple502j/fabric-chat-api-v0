package io.github.apple502j.chatapi.api.message;

import net.minecraft.text.Text;

/**
 * Represents a message of a literal text, without sender, formatting, etc.
 */
public class LiteralMessage implements Message {
	private final Text text;
	private final String contents;

	public LiteralMessage(String contents) {
		this.contents = contents;
		this.text = Text.of(contents);
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
	public LiteralMessage with(String newContents) {
		return new LiteralMessage(newContents);
	}
}
