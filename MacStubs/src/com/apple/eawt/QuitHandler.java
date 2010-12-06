/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.apple.eawt;

/**
 *
 * @author joshmarinacci
 */
public interface QuitHandler {
    public void handleQuitRequestWith(AppEvent.QuitEvent quitEvent, QuitResponse quitResponse);
}
