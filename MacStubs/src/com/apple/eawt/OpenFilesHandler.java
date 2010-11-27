/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.apple.eawt;

import com.apple.eawt.AppEvent.OpenFilesEvent;

/**
 *
 * @author joshmarinacci
 */
public interface OpenFilesHandler {
    public void openFiles(OpenFilesEvent openFilesEvent);
}
