package view;

import controller.listeners.EpsilonAiming;
import controller.listeners.EpsilonCirculation;
import controller.listeners.EpsilonMovement;
import view.objectViews.FrameView;
import view.objectViews.ObjectView;
import view.gamePanels.ImaginaryPanel;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class ViewData {
    private static ArrayList<ImaginaryPanel> panels = new ArrayList<>();
    private static ArrayList<FrameView> frames = new ArrayList<>();
    private static ArrayList<ObjectView> views = new ArrayList<>();
    private static HashMap<ObjectView ,FrameView> localViews = new HashMap<>();
    private static double time;
    private static double hp;
    private static double xp;
    private static int wave;

    public static ArrayList<FrameView> getFrames() {
        return frames;
    }

    public static ArrayList<ObjectView> getViews() {
        return views;
    }

    public static void setFrames(ArrayList<FrameView> frames) {
        ViewData.frames = frames;
    }

    public static void setViews(ArrayList<ObjectView> views) {
        ViewData.views = views;
    }

    public static void addImaginaryPanel(ImaginaryPanel imaginaryPanel){
        frames.getLast().add(imaginaryPanel);

        imaginaryPanel.addMouseListener(new EpsilonAiming());
//        imaginaryPanel.addMouseMotionListener(new EpsilonCirculation());
        imaginaryPanel.addKeyListener(new EpsilonMovement());
        imaginaryPanel.grabFocus();

        panels.add(imaginaryPanel);
    }

    public static ArrayList<ImaginaryPanel> getPanels() {
        return panels;
    }

    public static void setPanels(ArrayList<ImaginaryPanel> panels) {
        ViewData.panels = panels;
    }

    public static void addFrame(FrameView newFrame) {
        frames.add(newFrame);
    }

    public static void addObject(ObjectView objectView){
        views.add(objectView);
    }

    public static double getTime() {
        return time;
    }

    public static void setTime(double time) {
        ViewData.time = time;
    }

    public static double getHp() {
        return hp;
    }

    public static void setHp(double hp) {
        ViewData.hp = hp;
    }

    public static double getXp() {
        return xp;
    }

    public static void setXp(double xp) {
        ViewData.xp = xp;
    }

    public static int getWave() {
        return wave;
    }

    public static void setWave(int wave) {
        ViewData.wave = wave;
    }

    public static HashMap<ObjectView, FrameView> getLocalViews() {
        return localViews;
    }

    public static void setLocalViews(HashMap<ObjectView, FrameView> localViews) {
        ViewData.localViews = localViews;
    }
}
