package io.github.apple502j.chatapi.api.message;

import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

import org.jetbrains.annotations.ApiStatus;

/**
 * Represents a translatable message, such as advancements or death messages.
 *
 * These messages use the English text as the contents. Calling {@link #with(String)}
 * does not manipulate the text; if such behavior is needed, a custom subclass should be used.
 */
public class TranslatableMessage implements Message {
	protected TranslatableText text;
	protected String contents;

	@ApiStatus.Internal
	public TranslatableMessage(TranslatableText text) {
		text = text.copy();
		this.text = text;
		this.contents = text.getString();
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
	public Message with(String newContents) {
		this.contents = newContents;
		return this;
	}

	public TranslatableText getTranslatableText() {
		return this.text;
	}
}
