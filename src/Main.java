import java.util.Random;
import java.util.Scanner;

public class Main {
    private char[][] minefield;
    private char[][] playerBoard;
    private int rows;
    private int columns;
    private int totalMines;
    private boolean gameWon;

    public Main(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        this.minefield = new char[rows][columns];
        this.playerBoard = new char[rows][columns];
        this.totalMines = rows * columns / 4;
        this.gameWon = false;

        initializeMinefield();
        initializePlayerBoard();
        placeMines();
        calculateNumbers();
    }

    private void initializeMinefield() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                minefield[i][j] = '-';
            }
        }
    }

    private void initializePlayerBoard() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                playerBoard[i][j] = '-';
            }
        }
    }

    private void placeMines() {
        Random random = new Random();
        int minesPlaced = 0;

        while (minesPlaced < totalMines) {
            int randomRow = random.nextInt(rows);
            int randomCol = random.nextInt(columns);

            if (minefield[randomRow][randomCol] != '*') {
                minefield[randomRow][randomCol] = '*';
                minesPlaced++;
            }
        }
    }

    private void calculateNumbers() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (minefield[i][j] != '*') {
                    int minesCount = countAdjacentMines(i, j);
                    minefield[i][j] = (minesCount > 0) ? (char) (minesCount + '0') : '0';
                }
            }
        }
    }

    private int countAdjacentMines(int row, int col) {
        int count = 0;

        for (int i = row - 1; i <= row + 1; i++) {
            for (int j = col - 1; j <= col + 1; j++) {
                if (i >= 0 && i < rows && j >= 0 && j < columns && minefield[i][j] == '*') {
                    count++;
                }
            }
        }

        return count;
    }

    private void printBoard(char[][] board) {
        System.out.println("===========================");
        System.out.println("Mayın Tarlası Oyuna Hoşgeldiniz !");
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println("===========================");
    }

    private void revealCell(int row, int col) {
        if (row < 0 || row >= rows || col < 0 || col >= columns) {
            System.out.println("Geçersiz koordinat. Lütfen geçerli bir koordinat girin.");
        } else if (playerBoard[row][col] != '-') {
            System.out.println("Bu koordinat daha önce seçildi. Başka bir koordinat girin.");
        } else if (minefield[row][col] == '*') {
            gameWon = false;
            printBoard(minefield);
            System.out.println("Game Over!!");
        } else {
            playerBoard[row][col] = minefield[row][col];
            if (playerBoard[row][col] == '0') {
                revealEmptyCells(row, col);
            }
            checkGameWon();
        }
    }

    private void revealEmptyCells(int row, int col) {
        for (int i = row - 1; i <= row + 1; i++) {
            for (int j = col - 1; j <= col + 1; j++) {
                if (i >= 0 && i < rows && j >= 0 && j < columns && playerBoard[i][j] == '-') {
                    playerBoard[i][j] = minefield[i][j];
                    if (minefield[i][j] == '0') {
                        revealEmptyCells(i, j);
                    }
                }
            }
        }
    }

    private void checkGameWon() {
        int unrevealedCount = 0;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (playerBoard[i][j] == '-') {
                    unrevealedCount++;
                }
            }
        }

        if (unrevealedCount == totalMines) {
            gameWon = true;
            printBoard(playerBoard);
            System.out.println("Oyunu Kazandınız !");
        }
    }

    public void play() {
        Scanner scanner = new Scanner(System.in);

        while (!gameWon) {
            printBoard(playerBoard);

            System.out.print("Satır Giriniz : ");
            int row = scanner.nextInt();

            System.out.print("Sütun Giriniz : ");
            int col = scanner.nextInt();

            revealCell(row, col);
        }

        scanner.close();
    }
}