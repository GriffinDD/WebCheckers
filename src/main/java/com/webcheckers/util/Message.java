package com.webcheckers.util;

import java.util.logging.Logger;

/**
 * A UI-friendly representation of a message to the user.
 *
 * <p>
 * This is a <a href='https://en.wikipedia.org/wiki/Domain-driven_design'>DDD</a>
 * <a href='https://en.wikipedia.org/wiki/Value_object'>Value Object</a>.
 * This implementation is immutable and also supports a JSON representation.
 * </p>
 *
 * @author <a href='mailto:bdbvse@rit.edu'>Bryan Basham</a>
 */
public final class Message {
  private static final Logger LOG = Logger.getLogger(Message.class.getName());

  //
  // Static Factory methods
  //

  /**
   * A static helper method to create new error messages.
   *
   * @param message  the text of the message
   *
   * @return a new {@link Message}
   */
  public static Message error(final String message) {
    return new Message(message, Type.ERROR);
  }

  /**
   * A static helper method to create new informational messages.
   *
   * @param message  the text of the message
   *
   * @return a new {@link Message}
   */
  public static Message info(final String message) {
    return new Message(message, Type.INFO);
  }

  //
  // Inner Types
  //

  /**
   * The type of {@link Message}; either information or an error.
   */
  public enum Type {
    INFO, ERROR
  }

  //
  // Attributes
  //

  private final String text;
  private final Type type;

  //
  // Constructor
  //

  /**
   * Create a new message.
   *
   * @param message  the text of the message
   * @param type  the type of message
   */
  private Message(final String message, final Type type) {
    this.text = message;
    this.type = type;
    LOG.finer(this + " created.");
  }

  //
  // Public methods
  //

  /**
   * Get the text of the message.
   */
  public String getText() {
    return text;
  }

  /**
   * Get the type of the message.
   */
  public Type getType() {
    return type;
  }

  /**
   * Query whether this message was generated from a successful
   * action; ie, not an {@link Type#ERROR}.
   *
   * @return true if not an error
   */
  public boolean isSuccessful() {
    return !type.equals(Type.ERROR);
  }

  //
  // Object methods
  //

  @Override
  public String toString() {
    return "{Msg " + type + " '" + text + "'}";
  }

  /**
   * For debugging purposes
   */
  @Override
  public boolean equals(Object other){
    if(other instanceof Message){
      Message o = (Message)other;
      return o.getType().equals(this.type) && o.getText().equals(this.text);
    }
    return false;
  }
}
