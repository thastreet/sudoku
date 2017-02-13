import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

class Bot {
    private static final int START_X = 425;
    private static final int START_Y = 178;
    private static final int STEP = 90;

    private Map<Integer, Point> positions = new HashMap<>();
    private Robot robot;

    Bot() {
        positions.put(1, new Point(1300, 220));
        positions.put(2, new Point(1300, 385));
        positions.put(3, new Point(1300, 540));
        positions.put(4, new Point(1300, 700));
        positions.put(5, new Point(1300, 860));
        positions.put(6, new Point(1450, 220));
        positions.put(7, new Point(1450, 385));
        positions.put(8, new Point(1450, 540));
        positions.put(9, new Point(1450, 700));
    }

    int[][] getGrid(int gridSize) {
        final int[][] grid = new int[gridSize][gridSize];

        BufferedImage numberImage;
        String ocrResult;

        try {
            robot = new Robot();

            final ITesseract tesseract = new Tesseract();
            tesseract.setTessVariable("tessedit_char_whitelist", "123456789");

            final BufferedImage screenCapture = robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));

            for (int j = 0; j < 9; ++j) {
                for (int i = 0; i < 9; ++i) {
                    numberImage = screenCapture.getSubimage(START_X + i * STEP - 38, START_Y + j * STEP - 42, 80, 80);
                    ocrResult = tesseract.doOCR(numberImage).replaceAll(" ", "").replaceAll("\n", "");

                    if (ocrResult.length() > 0) {
                        grid[i][j] = Integer.parseInt(ocrResult);
                    } else {
                        grid[i][j] = 0;
                    }
                }
            }
        } catch (AWTException | TesseractException e) {
            e.printStackTrace();
        }

        return grid;
    }

    void play(int[][] solution) {
        final int waitTimeInMillis = 0;
        int lastNumber = 0;

        try {
            for (int j = 0; j < solution.length; ++j) {
                for (int i = 0; i < solution.length; ++i) {
                    if (solution[i][j] != lastNumber) {
                        Point numberPosition = positions.get(solution[i][j]);
                        robot.mouseMove(numberPosition.x, numberPosition.y);
                        Thread.sleep(waitTimeInMillis);
                        robot.mousePress(InputEvent.BUTTON1_MASK);
                        Thread.sleep(waitTimeInMillis);
                        robot.mouseRelease(InputEvent.BUTTON1_MASK);
                        Thread.sleep(waitTimeInMillis);
                    }

                    robot.mouseMove(START_X + i * STEP, START_Y + j * STEP);
                    Thread.sleep(waitTimeInMillis);
                    robot.mousePress(InputEvent.BUTTON1_MASK);
                    Thread.sleep(waitTimeInMillis);
                    robot.mouseRelease(InputEvent.BUTTON1_MASK);
                    Thread.sleep(waitTimeInMillis);

                    lastNumber = solution[i][j];
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
