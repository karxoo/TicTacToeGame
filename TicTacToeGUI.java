import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TicTacToeGUI extends JFrame implements ActionListener {
    public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> new TicTacToeGUI());
}
    private JButton[][] buttons = new JButton[3][3]; // 3x3 grid of buttons
    private boolean xTurn = true; // Track whose turn it is (true = X, false = O)

    // 🎨 Default colors (Dark/Teal theme)
    private Color bgColor = new Color(0, 128, 128);   // Teal background
    private Color btnColor = new Color(0, 150, 150);  // Slightly lighter teal for buttons
    private Color xColor = new Color(50, 50, 50);     // Dark grey for Player X
    private Color oColor = new Color(240, 240, 240);  // Off-white for Player O

    // Labels and panels
    private JLabel titleLabel;   // Title at the top
    private JLabel scoreLabel;   // Scoreboard at the bottom
    private JPanel boardPanel;   // Panel for the game board
    private JPanel bottomPanel;  // Panel to hold scoreboard + buttons

    // Track scores
    private int playerXWins = 0;
    private int playerOWins = 0;

    // Winning line coordinates
    private int[][] winLine = null;

    // Control buttons
    private JButton resetScoresButton;
    private JButton newGameButton;
    private JButton themeSwitchButton; // NEW: Theme switcher button

    public TicTacToeGUI() {
        // Window setup
        setTitle("Tic Tac Toe");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(bgColor);

        // Title label
        titleLabel = new JLabel("Tic Tac Toe", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(titleLabel, BorderLayout.NORTH);

        // Board panel with custom painting for win line
        boardPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Draw red line if a winning combination exists
                if (winLine != null) {
                    g.setColor(Color.RED);
                    g.drawLine(winLine[0][0], winLine[0][1], winLine[1][0], winLine[1][1]);
                }
            }
        };
        boardPanel.setLayout(new GridLayout(3, 3, 5, 5));
        boardPanel.setBackground(bgColor);

        // Create 3x3 buttons for the board
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j] = new JButton("");
                buttons[i][j].setFont(new Font("Arial", Font.BOLD, 60));
                buttons[i][j].setFocusPainted(false);
                buttons[i][j].setBorder(BorderFactory.createEmptyBorder());
                buttons[i][j].setBackground(btnColor);
                buttons[i][j].setOpaque(true);
                buttons[i][j].addActionListener(this); // Listen for clicks
                boardPanel.add(buttons[i][j]);
            }
        }
        add(boardPanel, BorderLayout.CENTER);

        // Bottom panel for scoreboard + control buttons
        bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(bgColor);

        // Scoreboard label
        scoreLabel = new JLabel("Player X: 0 | Player O: 0", SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        scoreLabel.setForeground(Color.YELLOW);
        scoreLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        bottomPanel.add(scoreLabel, BorderLayout.CENTER);

        // Reset Scores button
        resetScoresButton = new JButton("Reset Scores");
        resetScoresButton.setFont(new Font("Arial", Font.BOLD, 16));
        resetScoresButton.setBackground(Color.ORANGE);
        resetScoresButton.setFocusPainted(false);
        resetScoresButton.addActionListener(e -> resetScores());
        bottomPanel.add(resetScoresButton, BorderLayout.EAST);

        // New Game button
        newGameButton = new JButton("New Game");
        newGameButton.setFont(new Font("Arial", Font.BOLD, 16));
        newGameButton.setBackground(Color.GREEN);
        newGameButton.setFocusPainted(false);
        newGameButton.addActionListener(e -> resetBoard());
        bottomPanel.add(newGameButton, BorderLayout.WEST);

        // Theme Switch button
        themeSwitchButton = new JButton("Switch Theme");
        themeSwitchButton.setFont(new Font("Arial", Font.BOLD, 16));
        themeSwitchButton.setBackground(Color.CYAN);
        themeSwitchButton.setFocusPainted(false);
        themeSwitchButton.addActionListener(e -> switchTheme()); // Toggle theme
        bottomPanel.add(themeSwitchButton, BorderLayout.NORTH);

        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton clicked = (JButton) e.getSource();
        if (clicked.getText().equals("")) {
            String symbol = xTurn ? "X" : "O";
            clicked.setText(symbol);
            clicked.setForeground(xTurn ? xColor : oColor);

            // Check win or tie after each move
            if (checkWin()) {
                if (xTurn) {
                    playerXWins++;
                    JOptionPane.showMessageDialog(this, "Player X Wins!");
                } else {
                    playerOWins++;
                    JOptionPane.showMessageDialog(this, "Player O Wins!");
                }
                updateScoreboard();
                resetBoard();
            } else if (isTie()) {
                JOptionPane.showMessageDialog(this, "It's a Tie!");
                resetBoard();
            }
            xTurn = !xTurn; // Switch turn
        }
    }

    private boolean checkWin() {
        String sym = xTurn ? "X" : "O";
        int w = buttons[0][0].getWidth();
        int h = buttons[0][0].getHeight();

        // Check rows and columns
        for (int i = 0; i < 3; i++) {
            // Row check
            if (buttons[i][0].getText().equals(sym) && buttons[i][1].getText().equals(sym) && buttons[i][2].getText().equals(sym)) {
                winLine = new int[][]{{0, i * h + h / 2}, {3 * w, i * h + h / 2}};
                boardPanel.repaint();
                return true;
            }
            // Column check
            if (buttons[0][i].getText().equals(sym) && buttons[1][i].getText().equals(sym) && buttons[2][i].getText().equals(sym)) {
                winLine = new int[][]{{i * w + w / 2, 0}, {i * w + w / 2, 3 * h}};
                boardPanel.repaint();
                return true;
            }
        }
        // Diagonal checks
        if (buttons[0][0].getText().equals(sym) && buttons[1][1].getText().equals(sym) && buttons[2][2].getText().equals(sym)) {
            winLine = new int[][]{{0, 0}, {3 * w, 3 * h}};
            boardPanel.repaint();
            return true;
        }
        if (buttons[0][2].getText().equals(sym) && buttons[1][1].getText().equals(sym) && buttons[2][0].getText().equals(sym)) {
            winLine = new int[][]{{3 * w, 0}, {0, 3 * h}};
            boardPanel.repaint();
            return true;
        }
        return false;
    }

    private boolean isTie() {
        for (JButton[] row : buttons) for (JButton b : row) if (b.getText().equals("")) return false;
        return true;
    }

    private void resetBoard() {
        for (JButton[] row : buttons) for (JButton b : row) b.setText("");
        xTurn = true;
        winLine = null;
        boardPanel.repaint();
    }

    private void updateScoreboard() {
        scoreLabel.setText("Player X: " + playerXWins + " | Player O: " + playerOWins);
    }

    private void resetScores() {
        playerXWins = 0;
        playerOWins = 0;
        updateScoreboard();
    }

    // 🌗 Theme switcher: toggles between Dark/Teal and Light mode
private void switchTheme() {
    if (bgColor.equals(new Color(0, 128, 128))) {
        // Switch to Light Mode
        bgColor = Color.WHITE;                     // White background
        btnColor = new Color(220, 220, 220);       // Light grey buttons
        xColor = Color.BLUE;                       // Blue for Player X
        oColor = Color.RED;                        // Red for Player O
        titleLabel.setForeground(Color.BLACK);     // Dark text for title
        scoreLabel.setForeground(Color.BLACK);     // Dark text for scoreboard
    } else {
        // Switch back to Dark/Teal Mode
        bgColor = new Color(0, 128, 128);          // Teal background
        btnColor = new Color(0, 150, 150);         // Slightly lighter teal buttons
        xColor = new Color(50, 50, 50);            // Dark grey for Player X
        oColor = new Color(240, 240, 240);         // Off-white for Player O
        titleLabel.setForeground(Color.WHITE);     // White text for title
        scoreLabel.setForeground(Color.YELLOW);    // Yellow text for scoreboard
    }

    // Apply updated colors to components
    getContentPane().setBackground(bgColor);
    boardPanel.setBackground(bgColor);
    bottomPanel.setBackground(bgColor);

    // Update all buttons with new background color
    for (int i = 0; i < 3; i++) {
        for (int j = 0; j < 3; j++) {
            buttons[i][j].setBackground(btnColor);
            // Update text color if button already has a symbol
            if (buttons[i][j].getText().equals("X")) {
                buttons[i][j].setForeground(xColor);
            } else if (buttons[i][j].getText().equals("O")) {
                buttons[i][j].setForeground(oColor);
            }
        }
    }

    // Refresh the UI to apply changes immediately
    repaint();
}
}