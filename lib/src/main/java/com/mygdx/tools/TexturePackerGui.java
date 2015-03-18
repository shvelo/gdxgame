package com.mygdx.tools;

import org.apache.pivot.beans.BXMLSerializer;
import org.apache.pivot.collections.Map;
import org.apache.pivot.serialization.SerializationException;
import org.apache.pivot.wtk.Application;
import org.apache.pivot.wtk.DesktopApplicationContext;
import org.apache.pivot.wtk.Display;
import org.apache.pivot.wtk.Window;

public class TexturePackerGui implements Application {
    private Window window = null;

    public static void main(String[] args) {
        DesktopApplicationContext.main(TexturePackerGui.class, args);
    }

    @Override
    public void startup(Display display, Map<String, String> properties)
            throws Exception {
        BXMLSerializer bxmlSerializer = new BXMLSerializer();
        try {
            window = (Window) bxmlSerializer.readObject(TexturePackerGui.class, "/texture_packer.bxml");
        } catch(SerializationException ex) {
            ex.printStackTrace();
        }
        window.open(display);
    }

    @Override
    public boolean shutdown(boolean optional) {
        if (window != null) {
            window.close();
        }

        return false;
    }

    @Override
    public void suspend() {
    }

    @Override
    public void resume() {
    }
}