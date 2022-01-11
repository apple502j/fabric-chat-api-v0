package io.github.apple502j.chatapi.impl;

public class AsyncBlockedException extends RuntimeException {
	public AsyncBlockedException() {
		super("This exception should've been caught!");
	}
}
