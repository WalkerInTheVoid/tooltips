package net.bdew.wurm.tooltip;

import javassist.ClassPool;
import org.gotti.wurmunlimited.modloader.classhooks.HookManager;
import org.gotti.wurmunlimited.modloader.interfaces.Initable;
import org.gotti.wurmunlimited.modloader.interfaces.PreInitable;
import org.gotti.wurmunlimited.modloader.interfaces.WurmMod;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TooltipMod implements WurmMod, Initable, PreInitable {
    private static final Logger logger = Logger.getLogger("TooltipMod");

    public static void logException(String msg, Throwable e) {
        if (logger != null)
            logger.log(Level.SEVERE, msg, e);
    }

    private String loadString(String id) {
        try (InputStream is = getClass().getResourceAsStream(id)) {
            if (is == null) throw new RuntimeException("Failed to load resource: " + id);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
                builder.append(System.lineSeparator());
            }
            return builder.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void init() {
        logger.fine("Initializing");
        try {
            ClassPool classPool = HookManager.getInstance().getClassPool();

            classPool.getCtClass("com.wurmonline.client.renderer.TilePicker")
                    .getMethod("getHoverName", "()Ljava/lang/String;")
                    .setBody(loadString("tilePicker.txt"));

            classPool.getCtClass("com.wurmonline.client.renderer.cave.CaveWallPicker")
                    .getMethod("getHoverName", "()Ljava/lang/String;")
                    .setBody(loadString("caveWallPicker.txt"));


            logger.fine("Loaded");
        } catch (Throwable e) {
            logException("Error loading mod", e);
        }
    }

    @Override
    public void preInit() {

    }
}
