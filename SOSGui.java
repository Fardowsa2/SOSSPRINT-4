package jonin;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.util.*;

public class SOSGui extends JFrame {
    private static final long serialVersionUID = 1L;
	private AbstractSOSGame game;
    private CellButton[][] cells;
    private PlayerStrategy blueStrategy, redStrategy;
    private JRadioButton simpleModeRadio, generalModeRadio;
    private JComboBox<Integer> boardSizeCombo;
    private JRadioButton blueHuman, blueComputer, blueS, blueO;
    private JRadioButton redHuman, redComputer, redS, redO;
    private JLabel statusLabel;

    private java.util.List<Line2D> sosLines = new ArrayList<>();
    private java.util.List<Color> sosColors = new ArrayList<>();

    
    public SOSGui() {
        super("SOS Game - Sprint 4");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(new JLabel("SOS"));  // title label

        simpleModeRadio = new JRadioButton("Simple game", true);
        generalModeRadio = new JRadioButton("General game");
        ButtonGroup modeGroup = new ButtonGroup();
        modeGroup.add(simpleModeRadio);
        modeGroup.add(generalModeRadio);
        top.add(simpleModeRadio);
        top.add(generalModeRadio);

        top.add(new JLabel("Board size"));
        boardSizeCombo = new JComboBox<>(new Integer[]{3, 4, 5, 6, 7, 8});
        boardSizeCombo.setSelectedIndex(5);  // default to 8×8
        top.add(boardSizeCombo);

        JButton newGame = new JButton("New Game");
        newGame.addActionListener(e -> startNewGame());
        top.add(newGame);

        add(top, BorderLayout.NORTH);

        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.add(Box.createVerticalStrut(20));  // spacing
        left.add(new JLabel("Blue player"));

        blueHuman = new JRadioButton("Human", true);
        blueComputer = new JRadioButton("Computer");
        ButtonGroup blueType = new ButtonGroup();
        blueType.add(blueHuman);
        blueType.add(blueComputer);
        left.add(blueHuman);
        left.add(blueComputer);

        blueS = new JRadioButton("S", true);
        blueO = new JRadioButton("O");
        ButtonGroup blueLetter = new ButtonGroup();
        blueLetter.add(blueS);
        blueLetter.add(blueO);
        left.add(blueS);
        left.add(blueO);

        add(left, BorderLayout.WEST);

        JPanel right = new JPanel();
        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
        right.add(Box.createVerticalStrut(20));  // spacing
        right.add(new JLabel("Red player"));

        redHuman = new JRadioButton("Human", true);
        redComputer = new JRadioButton("Computer");
        ButtonGroup redType = new ButtonGroup();
        redType.add(redHuman);
        redType.add(redComputer);
        right.add(redHuman);
        right.add(redComputer);

        redS = new JRadioButton("S", true);
        redO = new JRadioButton("O");
        ButtonGroup redLetter = new ButtonGroup();
        redLetter.add(redS);
        redLetter.add(redO);
        right.add(redS);
        right.add(redO);

        add(right, BorderLayout.EAST);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER));
        statusLabel = new JLabel("Game not started.");
        bottom.add(statusLabel);
        add(bottom, BorderLayout.SOUTH);

        startNewGame();
        setVisible(true);
    }

   
    private void startNewGame() {
        int size = (Integer) boardSizeCombo.getSelectedItem();

        if (simpleModeRadio.isSelected()) {
            game = new SimpleSOSGame(size);
        } else {
            game = new GeneralSOSGame(size);
        }

        blueStrategy = blueComputer.isSelected() ? new ComputerStrategy() : null;
        redStrategy  = redComputer .isSelected() ? new ComputerStrategy() : null;

        blueS.setEnabled(blueHuman.isSelected());
        blueO.setEnabled(blueHuman.isSelected());
        redS .setEnabled(redHuman.isSelected());
        redO .setEnabled(redHuman.isSelected());

        sosLines.clear();
        sosColors.clear();

        // rebuild the board UI
        rebuildBoard(size);

        // update status
        statusLabel.setText("Game started. Current turn: " + game.getCurrentPlayer());

        // if AI starts, let it make its move
        maybePerformAIMove();
    }

    /**
     * Builds or rebuilds the grid of CellButton components.
     * @param size number of rows/columns
     */
    private void rebuildBoard(int size) {
        // remove old board if exists
        Container c = getContentPane();
        Component old = ((BorderLayout) c.getLayout()).getLayoutComponent(BorderLayout.CENTER);
        if (old != null) c.remove(old);

        // layered pane to allow drawing lines on top
        JLayeredPane layer = new JLayeredPane();
        int cellSize = 60;
        layer.setPreferredSize(new Dimension(size * cellSize, size * cellSize));
        cells = new CellButton[size][size];

        // create grid of buttons
        for (int r = 0; r < size; r++) {
            for (int col = 0; col < size; col++) {
                CellButton btn = new CellButton(r, col);
                btn.setBounds(col * cellSize, r * cellSize, cellSize, cellSize);
                btn.setFont(btn.getFont().deriveFont(20f));
                // add click handler for human moves
                btn.addActionListener(e -> performHumanMove(btn.row, btn.col));
                layer.add(btn, JLayeredPane.DEFAULT_LAYER);
                cells[r][col] = btn;
            }
        }

        // transparent panel to draw SOS lines
        JPanel draw = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                // draw each line in its color
                for (int i = 0; i < sosLines.size(); i++) {
                    g2.setStroke(new BasicStroke(4));
                    g2.setColor(sosColors.get(i));
                    g2.draw(sosLines.get(i));
                }
            }
        };
        draw.setOpaque(false);
        draw.setBounds(0, 0, size * cellSize, size * cellSize);
        layer.add(draw, JLayeredPane.PALETTE_LAYER);

        // add the layered board to the center of the frame
        c.add(layer, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    
    private void performHumanMove(int row, int col) {
        // ignore if game over
        if (game.isGameOver()) return;
        // ignore if it's AI's turn
        if ((game.getCurrentPlayer() == AbstractSOSGame.Player.BLUE && blueStrategy != null)
         || (game.getCurrentPlayer() == AbstractSOSGame.Player.RED  && redStrategy  != null)) {
            return;
        }
        // determine which player and letter
        AbstractSOSGame.Player player = game.getCurrentPlayer();
        char letter = (player == AbstractSOSGame.Player.BLUE)
                      ? (blueS.isSelected() ? 'S' : 'O')
                      : (redS .isSelected() ? 'S' : 'O');
        // apply the move
        applyMove(row, col, letter, player);
    }

    private void maybePerformAIMove() {
        if (game.isGameOver()) return;
        AbstractSOSGame.Player cur = game.getCurrentPlayer();
        PlayerStrategy strat = (cur == AbstractSOSGame.Player.BLUE) ? blueStrategy : redStrategy;
        if (strat != null) {
            Move m = strat.chooseMove(game, cur);
            applyMove(m.getRow(), m.getCol(), m.getLetter(), cur);
        }
    }

   
    private void applyMove(int row, int col, char letter, AbstractSOSGame.Player player) {
        // try to place letter; return if invalid
        if (!game.placeLetter(row, col, letter)) return;

        // update the button text and owner field
        CellButton btn = cells[row][col];
        btn.setText(String.valueOf(letter));
        btn.owner = player;

        // detect and draw any new SOS lines
        detectSOSLines(row, col, player);

        // update status text
        statusLabel.setText(game.getGameStatus());

        // if game not over, maybe let AI move next
        if (!game.isGameOver()) {
            maybePerformAIMove();
        } else {
            // show result dialog
            JOptionPane.showMessageDialog(this, game.getGameStatus());
        }
    }

   
    private void detectSOSLines(int row, int col, AbstractSOSGame.Player player) {
        int size = game.getBoardSize();
        int[] dr = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] dc = {-1, 0, 1, -1, 1, -1, 0, 1};
        int cellSize = 60;

        for (int i = 0; i < 8; i++) {
            String now = cells[row][col].getText();

            if (now.equals("S")) {
                int r1 = row + dr[i], c1 = col + dc[i];
                int r2 = row + 2*dr[i], c2 = col + 2*dc[i];
                if (inBounds(r1, c1, size) && inBounds(r2, c2, size)
                 && cells[r1][c1].getText().equals("O")
                 && cells[r2][c2].getText().equals("S")) {
                    addLine(row, col, r2, c2, player);
                }
            }

            if (now.equals("O")) {
                int r0 = row - dr[i], c0 = col - dc[i];
                int r1 = row + dr[i], c1 = col + dc[i];
                if (inBounds(r0, c0, size) && inBounds(r1, c1, size)
                 && cells[r0][c0].getText().equals("S")
                 && cells[r1][c1].getText().equals("S")) {
                    addLine(r0, c0, r1, c1, player);
                }
            }

            if (now.equals("S")) {
                int r0 = row - 2*dr[i], c0 = col - 2*dc[i];
                int r1 = row - dr[i],   c1 = col - dc[i];
                if (inBounds(r0, c0, size) && inBounds(r1, c1, size)
                 && cells[r0][c0].getText().equals("S")
                 && cells[r1][c1].getText().equals("O")) {
                    addLine(r0, c0, row, col, player);
                }
            }
        }

        repaint();
    }

    
    private boolean inBounds(int r, int c, int size) {
        return r >= 0 && r < size && c >= 0 && c < size;
    }

    
    private void addLine(int r1, int c1, int r2, int c2, AbstractSOSGame.Player player) {
        int cellSize = 60;
        double x1 = c1 * cellSize + cellSize / 2.0;
        double y1 = r1 * cellSize + cellSize / 2.0;
        double x2 = c2 * cellSize + cellSize / 2.0;
        double y2 = r2 * cellSize + cellSize / 2.0;
        sosLines.add(new Line2D.Double(x1, y1, x2, y2));
        sosColors.add(player == AbstractSOSGame.Player.BLUE ? Color.BLUE : Color.RED);
    }

    private static class CellButton extends JButton {
        int row, col;
        AbstractSOSGame.Player owner;
        CellButton(int r, int c) {
            super("");
            row = r;
            col = c;
        }
    }

    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(SOSGui::new);
    }
}
