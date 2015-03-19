package com.mygdx.tools;

import com.badlogic.gdx.tools.texturepacker.TexturePacker;

import org.apache.pivot.beans.BXML;
import org.apache.pivot.beans.Bindable;
import org.apache.pivot.collections.Map;
import org.apache.pivot.util.Resources;
import org.apache.pivot.wtk.Button;
import org.apache.pivot.wtk.ButtonPressListener;
import org.apache.pivot.wtk.FileBrowserSheet;
import org.apache.pivot.wtk.PushButton;
import org.apache.pivot.wtk.Sheet;
import org.apache.pivot.wtk.SheetCloseListener;
import org.apache.pivot.wtk.TextInput;
import org.apache.pivot.wtk.Window;

import java.io.File;
import java.net.URL;

public class TexturePackerGuiWindow extends Window implements Bindable {
    @BXML
    private TextInput inputDir;
    @BXML
    private TextInput outputDir;
    @BXML
    private TextInput outFileName;
    @BXML
    private PushButton selectInputDir;
    @BXML
    private PushButton selectOutputDir;
    @BXML
    private PushButton process;

    @Override
    public void initialize(Map<String, Object> namespace, URL location, Resources resources) {
        selectInputDir.getButtonPressListeners().add(new ButtonPressListener() {
            @Override
            public void buttonPressed(Button button) {
                selectFile(inputDir);
            }
        });

        selectOutputDir.getButtonPressListeners().add(new ButtonPressListener() {
            @Override
            public void buttonPressed(Button button) {
                selectFile(outputDir);
            }
        });

        process.getButtonPressListeners().add(new ButtonPressListener() {
            @Override
            public void buttonPressed(Button button) {
                processTextures();
            }
        });
    }

    public void processTextures() {
        TexturePacker.process(inputDir.getText(), outputDir.getText(), outFileName.getText());
    }

    public void selectFile(final TextInput target) {
        final FileBrowserSheet fileBrowserSheet = new FileBrowserSheet();
        fileBrowserSheet.setMode(FileBrowserSheet.Mode.SAVE_TO);
        fileBrowserSheet.setSelectedFile(new File(System.getProperty("user.dir")));
        fileBrowserSheet.open(this, new SheetCloseListener() {
            @Override
            public void sheetClosed(Sheet sheet) {
                target.setText(fileBrowserSheet.getSelectedFile().getPath());
            }
        });
    }
}
