package Launcher;

import App.Fridge2ForkApp;

/**
 * Entry point for the application.
 * It acts as a simple launcher that forwards execution to the main application class, Fride2ForkApp.
 * @author Maya
 */
public class Launcher {
    /**
     * Starts the application by delegeting to Fridge2ForkApp.
     * @param args command-line arguments
     */
    public static void main (String[] args){
        Fridge2ForkApp.main(args);
    }
}
