package com.github.Calcifer1331;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        int boardWidth = 360, boardHeight = 640;
        JFrame frame = new JFrame("Flappy Bord");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setIconImage(new ImageIcon("flappybird.png").getImage());
        FlappyBird flappyBird = new FlappyBird();
        frame.add(flappyBird);
        frame.pack();
        flappyBird.requestFocus();
        frame.setVisible(true);
    }
}