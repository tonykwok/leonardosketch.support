/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.apple.eawt;

import java.io.File;
import java.util.List;

/**
 *
 * @author joshmarinacci
 */
public abstract class AppEvent extends java.util.EventObject {

    public static class AboutEvent {
        public AboutEvent() {
        }
    }

    public static class QuitEvent {
        public QuitEvent() {
        }
    }

    public static class PreferencesEvent {
        public PreferencesEvent() {
        }
    }

    public static class FullScreenEvent extends com.apple.eawt.AppEvent {
        final java.awt.Window window;
        
        public FullScreenEvent(java.awt.Window window) {
            this.window = window;
        }
        
        public java.awt.Window getWindow() {
            return null;
        }
    }
    

    AppEvent() {
        super(null);
    }
    public static class OpenFilesEvent extends FilesEvent {
        final java.lang.String searchTerm = null;

        OpenFilesEvent(java.util.List<java.io.File> files, java.lang.String s) {
            super(files);
            /* compiled code */ }

        public java.lang.String getSearchTerm() { return null; /* compiled code */ }
    }

    public static abstract class FilesEvent extends AppEvent {
        final java.util.List<java.io.File> files = null;

        FilesEvent(List<java.io.File> files) {
            /* compiled code */
        }

        public List<File> getFiles() { return null; /* compiled code */ }
    }
}
