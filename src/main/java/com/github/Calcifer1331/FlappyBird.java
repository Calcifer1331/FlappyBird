package com.github.Calcifer1331;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

public class FlappyBird extends JPanel implements ActionListener, KeyListener {
    int boardWidth = 360, boardHeight = 640;
    Image background, birdImage, topPipeImg, bottomPipeImg;
    int birdX = boardWidth / 8, birdY = boardHeight / 2, birdWidth = 34, birdHeight = 24;


    class Bird {
        int x = birdX, y = birdY, width = birdWidth, height = birdHeight;
        Image img;

        Bird(Image img) {
            this.img = img;
        }

    }

    int pipeX = boardWidth, pipeY = 0, pipeWidth = 64, pipeHeight = 512;

    class Pipe {
        int x = pipeX, y = pipeY, width = pipeWidth, height = pipeHeight;
        Image img;
        boolean passed = false;

        Pipe(Image img) {
            this.img = img;
        }
    }

    Bird bird;
    int velocidadY = 0, velocidadX = -4, gravity = 1;

    ArrayList<Pipe> pipes;
    Random rand = new Random();


    Timer gameLoop;
    Timer placePipeTimer;

    boolean gameOver = false;

    double score = 0;

    FlappyBird() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
//        setBackground(Color.BLUE);
        setFocusable(true);
        addKeyListener(this);

        background = new ImageIcon("src/main/resources/flappybirdbg.png").getImage();
        birdImage = new ImageIcon("src/main/resources/flappybird.png").getImage();
        topPipeImg = new ImageIcon("src/main/resources/toppipe.png").getImage();
        bottomPipeImg = new ImageIcon("src/main/resources/bottompipe.png").getImage();

        bird = new Bird(birdImage);
        pipes = new ArrayList<>();

        placePipeTimer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                placePipes();
            }
        });
        placePipeTimer.start();

        gameLoop = new Timer(1000 / 60, this);
        gameLoop.start();
    }

    public void placePipes() {

        int randomPipeY = (int) (pipeY - pipeHeight / 4 - Math.random() * (pipeHeight / 2));
        int openingSpace = boardHeight / 4;
        Pipe topPipe = new Pipe(topPipeImg);

        topPipe.y = randomPipeY;

        pipes.add(topPipe);
        Pipe bottomPipe = new Pipe(bottomPipeImg);
        bottomPipe.y = topPipe.y + pipeHeight + openingSpace;
        pipes.add(bottomPipe);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void move() {
        velocidadY += gravity;
        bird.y += velocidadY;
        bird.y = Math.max(bird.y, 0);

        for (int i = 0; i < pipes.size(); i++) {
            Pipe pipe = pipes.get(i);
            pipe.x += velocidadX;

            if (!pipe.passed && bird.x > pipe.x + pipe.width) {
                pipe.passed = true;
                score += 0.5;
            }

            if (collision(bird, pipe)) {
                gameOver = true;
            }
        }

        if (bird.y > boardHeight) {
            gameOver = true;
        }
    }

    private void draw(Graphics g) {
        g.drawImage(background, 0, 0, boardWidth, boardHeight, null);
        g.drawImage(bird.img, bird.x, bird.y, bird.width, bird.height, null);
        for (var i = 0; i < pipes.size(); i++) {
            Pipe pipe = pipes.get(i);
            g.drawImage(pipe.img, pipe.x, pipe.y, pipe.width, pipe.height, null);
        }

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 32));
        if (gameOver) {
            g.drawString("Game Over! " + (int) score, 10, 35);
        } else {
            g.drawString(String.valueOf((int) score), 10, 35);
        }
    }

    public boolean collision(Bird a, Pipe b) {
        return a.x < b.x + b.width &&
                a.x + a.width > b.x &&
                a.y < b.y + b.height &&
                a.y + a.height > b.y;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if (gameOver) {
            placePipeTimer.stop();
            gameLoop.stop();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            velocidadY = -9;
            if (gameOver) {
                bird.y = birdY;
                velocidadY = 0;
                pipes.clear();
                score = 0;
                gameOver = false;
                gameLoop.start();
                placePipeTimer.start();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
