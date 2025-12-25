import java.util.Scanner;

public class TicTacToe {
    public static void main(String[] args) {
        char[][] board = {{' ', ' ', ' '}, {' ', ' ', ' '}, {' ', ' ', ' '}};
        char player = 'X';
        Scanner sc = new Scanner(System.in);
        boolean gameOn = true;

        while (gameOn) {
            for (char[] row : board) {
                System.out.println("|" + row[0] + "|" + row[1] + "|" + row[2] + "|");
            }
            System.out.print("Player " + player + ", enter row & col (0-2): ");
            int r = sc.nextInt(), c = sc.nextInt();

            if (r >= 0 && r < 3 && c >= 0 && c < 3 && board[r][c] == ' ') {
                board[r][c] = player;
                if (checkWin(board, player)) {
                    System.out.println("Player " + player + " wins!");
                    gameOn = false;
                } else if (isFull(board)) {
                    System.out.println("Tie!");
                    gameOn = false;
                } else {
                    player = (player == 'X') ? 'O' : 'X';
                }
            } else {
                System.out.println("Invalid move!");
            }
        }
    }

    static boolean checkWin(char[][] b, char p) {
        for (int i = 0; i < 3; i++) {
            if ((b[i][0] == p && b[i][1] == p && b[i][2] == p) ||
                (b[0][i] == p && b[1][i] == p && b[2][i] == p)) return true;
        }
        return (b[0][0] == p && b[1][1] == p && b[2][2] == p) ||
               (b[0][2] == p && b[1][1] == p && b[2][0] == p);
    }

    static boolean isFull(char[][] b) {
        for (char[] row : b) for (char c : row) if (c == ' ') return false;
        return true;
    }
}