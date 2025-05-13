package com.webcheckers.ui;

import com.webcheckers.util.Message;

public class Attributes {

    public static class PostSigninAttributes{
        public final static Message PLAYER_LOGGED_IN_MESSAGE = Message.info("You have successfully logged into webcheckers");
        public final static String PLAYER_NAME = "playerName";
        public final static Message NAME_ALREADY_EXISTS_MESSAGE = Message.error("Try a new name, that one is taken.");
        public final static Message INVALID_NAME = Message.error("Name must contain at least one alphanumeric character, and no special characters.");
        public final static String PLAYER_LOGIN_PROMPT = "Log in.";

    }

    public static class GetHomeRouteAttributes{
        public final static Message PLAY_A_GAME_MESSAGE = Message.info("Challenge an open opponent to start a game!");
        public static final Message WELCOME_MSG = Message.info("Welcome to the world of online Checkers.");
        public static final String WELCOME_TITLE = "Welcome!";
        public static final String CHALLENGE = "Challenge an opponent";



    }
    public static class GetSigninRouteAttributes{
        public static final String PLAYER_WELCOME_MESSAGE = "Please log in to play a game of checkers. Your name must be " +
        "unique and will be reserved until you log out";
        public static final String PLAYER_LOGIN_PROMPT = "Log in.";
        public static final String TITLE = "Welcome to player registration";
    }
}
