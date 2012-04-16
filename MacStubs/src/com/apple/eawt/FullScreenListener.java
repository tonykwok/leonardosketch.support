/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apple.eawt;

/**
 *
 * @author josh
 */
public interface FullScreenListener extends java.util.EventListener {
    
    void windowEnteringFullScreen(com.apple.eawt.AppEvent.FullScreenEvent fullScreenEvent);
    
    void windowEnteredFullScreen(com.apple.eawt.AppEvent.FullScreenEvent fullScreenEvent);
    
    void windowExitingFullScreen(com.apple.eawt.AppEvent.FullScreenEvent fullScreenEvent);
    
    void windowExitedFullScreen(com.apple.eawt.AppEvent.FullScreenEvent fullScreenEvent);
}