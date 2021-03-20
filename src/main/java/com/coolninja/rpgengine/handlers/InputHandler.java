package com.coolninja.rpgengine.handlers;

import com.coolninja.rpgengine.Colors;
import com.coolninja.rpgengine.ConsoleFunc;
import com.coolninja.rpgengine.Vars;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

/**
 * The built-in input handler
 *
 * @version 1.0
 * @since 1.0
 * @author Ben Ballard
 */
public class InputHandler implements NativeKeyListener {

    public String[] menu;
    private int menuIndex;

    //-1 = None, 0 = Menu
    private int currentMode = -1;
    private boolean enterPressed = false;

    //TODO: fix problem where, upon pressing enter and if the program exits, it will attempt to run the input as a command.
    public void init() {
        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException ex) {
            Logger.getLogger(InputHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        GlobalScreen.addNativeKeyListener(this);
        Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.ALL);
    }

    public int doMenu(String[] m) {
        return doMenu(m, "");
    }

    public int doMenu(String[] m, String printFirst) {
        currentMode = 0;
        menuIndex = 0;
        menu = m;
        boolean run = true;
        while (run) {
            if (menuIndex > m.length - 1) {
                menuIndex = 0;
            }
            if (menuIndex < 0) {
                menuIndex = m.length - 1;
            }
            System.out.println(printFirst);
            for (int i = 0; i < menu.length; i++) {
                System.out.println((menuIndex == i ? Vars.Selected_Color : "") + "-" + menu[i] + Colors.reset());
            }
            try {
                Thread.sleep(300);
            } catch (InterruptedException ex) {
                Logger.getLogger(InputHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
            ConsoleFunc.clear();
            run = !enterPressed;
        }
        enterPressed = false;
        currentMode = -1;
        return menuIndex;
    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent nke) {

    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent nke) {
        if (currentMode == 0) {
            if (nke.getKeyCode() == Vars.Controls[0]) {
                menuIndex--;
            }
            if (nke.getKeyCode() == Vars.Controls[2]) {
                menuIndex++;
            }
            if (nke.getKeyCode() == Vars.Controls[4]) {
                enterPressed = true;
            }
        }
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent nke) {

    }

}