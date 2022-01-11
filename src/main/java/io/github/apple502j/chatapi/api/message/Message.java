package io.github.apple502j.chatapi.api.message;

import net.minecraft.text.Text;

/**
 * Represents a message.
 *
 * A message has an associated text and the contents. Text is used for
 * sending the actual message, while the contents are used by the mods
 * to perform some transformations such as translation.
 *
 * To use a custom Text for the chat, implement this interface.
 * The most important method is {@link #with(String)}} which allows replacing
 * the message content inside the text.
 *
 * @see LiteralMessage
 * @see TranslatableMessage
 */
public interface Message {
	Text getText();
	String getContents();

	/**
	 * Replaces the chat contents. This may mutate the current message and return itself.
	 * @implNote Implementations must update the underlying text to use the new message.
	 * @param newContents the new contents
	 * @return a new message
	 */
	Message with(String newContents);
}
