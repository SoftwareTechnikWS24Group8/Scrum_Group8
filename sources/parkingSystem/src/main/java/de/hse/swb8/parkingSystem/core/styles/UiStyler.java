package de.hse.swb8.parkingSystem.core.styles;

import atlantafx.base.theme.*;
import javafx.application.Application;

public class UiStyler {

        /*
    cupertionoDark
    cupertionoLight
    Dracula
    NordDark
    NordLight
    PrimerDark
    PrimerLight
     */
    public static void setUIStyle(Uistyles uiStyle) {
        switch (uiStyle) {
            case cupertionoDark -> {
                Application.setUserAgentStylesheet(new CupertinoDark().getUserAgentStylesheet());
            }
            case cupertionoLight -> {
                Application.setUserAgentStylesheet(new CupertinoLight().getUserAgentStylesheet());
            }
            case Dracula -> {
                Application.setUserAgentStylesheet(new Dracula().getUserAgentStylesheet());
            }
            case NordDark -> {
                Application.setUserAgentStylesheet(new NordDark().getUserAgentStylesheet());
            }
            case NordLight -> {
                Application.setUserAgentStylesheet(new NordLight().getUserAgentStylesheet());
            }
            case PrimerDark -> {
                Application.setUserAgentStylesheet(new PrimerDark().getUserAgentStylesheet());
            }
            case PrimerLight -> {
                Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());
            }
        }
    }
}
