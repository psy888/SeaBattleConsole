package com.psy888;

import java.util.Scanner;

public class Game {


    //Game field size 10x10
    static final int FIELD_SIZE = 10;
    private static final int SHOT_MISS = 10;


    UIOut out;

    //Game field ARRAY
    int[][] userField = new int[FIELD_SIZE][FIELD_SIZE];
    int[][] compField = new int[FIELD_SIZE][FIELD_SIZE];

    /*
    0       - пусто

    1       - однопалубный  (целый)
    2,2     - двухпалубный (целый)
    3,3,3   - трехпалубный (целый)
    4,4,4,4 - четырехпалубный (целый)

    -1       - однопалубный  (убит)
    -2,2     - двухпалубный (подбит)
    -3,3,3   - трехпалубный (подбит)
    -4,-4,4,4 - четырехпалубный (подбит)
     */

    boolean isUserTurn = true; //чей ход

    //constructor
    public Game() {
        //todo fill game fields
        out = new UIOut(userField, compField);
        fillGameField(userField);
        fillGameField(compField);
        out.printField();


    }


    void start() {
        Scanner userInput = new Scanner(System.in);

        out.msg("\t\t\tSEA BATTLE");
        do {


            out.printField();

            if (isUserTurn) {
                out.msg("Input coordinates of shot (Example \"A1\" :");
                int[] userShotCoordinates = parseUserInput(userInput.nextLine());
                if (shot(userShotCoordinates)) {
                    out.msg("-----------Hit!-----------");
                } else {
                    out.msg("-----------Miss!-----------");
                }
            } else {
                String compShot;
                int[] compShotCoordinates;
                do {
                    compShot = out.letters[(int) (Math.random() * 10)];
                    compShot += (int) (Math.random() * 10) + 1;
                    compShotCoordinates = parseUserInput(compShot);
                } while (userField[compShotCoordinates[0]][compShotCoordinates[1]] < 0 ||
                        userField[compShotCoordinates[0]][compShotCoordinates[1]] == 10);//не стрелять повторно в ту же точку
                //todo add ai logic

                out.msg("Comp shot is : " + compShot);
                if (shot(compShotCoordinates)) {
                    out.msg("-----------Hit!-----------");
                } else {
                    out.msg("-----------Miss!-----------");
                }
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (!isGameEnd());
        out.msg("\t\t-=====================-");
        out.msg("\t\t-======GAME OVER======-");
        out.msg("\t\t-=====================-");
        out.msg("\t\t-======"+ ((isUserTurn)?"USER ":"COMP ")+"WIN!======-");
    }

    /**
     * make Shot
     */
    public boolean shot(int[] coordinates) {

        int[][] curGameField = (isUserTurn) ? compField : userField;

        if (curGameField[coordinates[0]][coordinates[1]] > 0 &&
                curGameField[coordinates[0]][coordinates[1]] < 5) {

            curGameField[coordinates[0]][coordinates[1]] = -curGameField[coordinates[0]][coordinates[1]];
            killOrHit(coordinates[0], coordinates[1], curGameField);

            return true;
        } else {
            curGameField[coordinates[0]][coordinates[1]] = SHOT_MISS;
            isUserTurn = !isUserTurn; // смена хода после неудачного выстрела
            return false;
        }


    }

    /**
     * Check kill or hit ship
     *
     * @param row
     * @param column
     */
    void killOrHit(int row, int column, int[][] gameField) {
        int shipLength = Math.abs(gameField[row][column]);

        int[][] shipCoordinates = new int[shipLength][2];

        //null all coordinates
        for (int i = 0; i < shipCoordinates.length; i++) {
            shipCoordinates[i] = null;
        }
        //add first coordinates
        shipCoordinates[0] = new int[]{row, column};
        // search current ship coordinates
        searchShip(row, column, shipCoordinates, gameField);


        int hitCnt = 0;
        for (int i = 0; i < shipCoordinates.length; i++) {
            if (gameField[shipCoordinates[i][0]][shipCoordinates[i][1]] < 0) {
                hitCnt++;
            }
        }
        // mark ship as kill or hint
        if (hitCnt == shipCoordinates.length) {
            for (int i = 0; i < shipCoordinates.length; i++) {
                gameField[shipCoordinates[i][0]][shipCoordinates[i][1]] -= 5;
            }
        }

    }

    void searchShip(int row, int col, int[][] shipCoordinates, int[][] gameField) {
        if(isShipFound(shipCoordinates)) {return;}
        try {
            if (gameField[row - 1][col] != 0 && gameField[row - 1][col] != 10) {
                // check is new coordinates
                if (isNewCoordinates(row - 1, col, shipCoordinates)) {
                    //lunch search with new coordinates
                        searchShip(row - 1, col, shipCoordinates, gameField);
                }
            }
        } catch (IndexOutOfBoundsException e) {/*ignore*/}
        try {
            if (gameField[row + 1][col] != 0 && gameField[row + 1][col] != 10) {
                //check is new coordinates
                if (isNewCoordinates(row + 1, col, shipCoordinates)) {
                    //lunch search with new coordinates
                        searchShip(row + 1, col, shipCoordinates, gameField);
                }
            }
        } catch (IndexOutOfBoundsException e) {/*ignore*/}
        try {
            if (gameField[row][col - 1] != 0 && gameField[row][col - 1] != 10) {
                //check is new coordinates
                if (isNewCoordinates(row, col - 1, shipCoordinates)) {
                    //lunch search with new coordinates
                        searchShip(row, col - 1, shipCoordinates, gameField);
                }
            }
        } catch (IndexOutOfBoundsException e) {/*ignore*/}
        try {
            if (gameField[row][col + 1] != 0 && gameField[row][col + 1] != 10) {
                // check is new coordinates
                if (isNewCoordinates(row, col + 1, shipCoordinates)) {
                    //lunch search with new coordinates
                        searchShip(row, col + 1, shipCoordinates, gameField);
                }
            }
        } catch (IndexOutOfBoundsException e) {/*ignore*/}

    }


    boolean isNewCoordinates(int row, int col, int[][] shipCoordinates) {
        int found = 0;
        for (int i = 0; i < shipCoordinates.length; i++) {
            if (shipCoordinates[i] == null) { // если новые координаты
                shipCoordinates[i] = new int[]{row, col};
                return true;
            } else if (shipCoordinates[i][0] == row &&
                    shipCoordinates[i][1] == col) { //если дубликат
                found++;
                return false;
            }
        }
        return true;
    }

    boolean isShipFound(int[][] shipCoordinates) {
        boolean isFound = true;
        for (int i = 0; i < shipCoordinates.length; i++) {
            if (shipCoordinates[i] == null) {
                return !isFound;
            }
        }

        return isFound;
    }

    boolean isGameEnd() {
        if (isUserTurn) {
            for (int i = 0; i < compField.length; i++) {
                for (int j = 0; j < compField[i].length; j++) {
                    if (compField[i][j] > 0 && compField[i][j] < 5) {
                        return false;
                    }
                }
            }
        } else {
            for (int i = 0; i < userField.length; i++) {
                for (int j = 0; j < userField[i].length; j++) {
                    if (userField[i][j] > 0 && userField[i][j] < 5) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    /**
     * parse user input
     *
     * @return int[]{row,column}
     */
    public int[] parseUserInput(String str) {
        int rowNum;
        String row = String.valueOf(str.toUpperCase().charAt(0));
        int colNum;
        try {
            colNum = Integer.parseInt(str.substring(1)) - 1;
        } catch (NumberFormatException e) {
            out.msg("Wrong column number : \"" + str.charAt(1) + "\"");
            colNum = -1;
        }
        switch (row) {
            case "A":
                rowNum = 0;
                break;
            case "B":
                rowNum = 1;
                break;
            case "C":
                rowNum = 2;
                break;
            case "D":
                rowNum = 3;
                break;
            case "E":
                rowNum = 4;
                break;
            case "F":
                rowNum = 5;
                break;
            case "G":
                rowNum = 6;
                break;
            case "H":
                rowNum = 7;
                break;
            case "I":
                rowNum = 8;
                break;
            case "J":
                rowNum = 9;
                break;
            default:
                rowNum = -1;

        }

        if (rowNum == -1) {
            out.msg("Wrong row letter : \"" + str.charAt(0) + "\"");
        }
        return new int[]{rowNum, colNum}; //x,y (0-line,1-column)
    }

    /**
     * fill game field
     */
    public void fillGameField(int[][] gameField) {
        // 4 палубы - 1 шт 4-4 => 0
        // 3 палубы - 2 шт 4-3 => 0,1
        // 2 палубы - 3 шт 4-2 => 0,1,2
        // 1 палуба - 4 шт 4-1 => 0,1,2,3

        int shipLength = 4;

        //все корабли
        for (int i = shipLength; i > 0; i--) { // 4

            //отдельный корабль
            for (int j = 0; j <= shipLength - i; j++) {

                //координаты начала нового корабля
                int row;
                int column;
                do {
//                    row = (int) (Math.random() * (10 - 1) + 1);
                    row = (int) (Math.random() * 10);
//                    column = (int) (Math.random() * (10 - 1) + 1);
                    column = (int) (Math.random() * 10);
                } while (!checkField(row - 1, column - 1, row + 1, column + 1, gameField));
                System.out.println("row= " + row + " col= " + column);
                //длинна текущего корабля
                int curShipLength = shipLength - (shipLength - i);

                //Палубы отдельного корабля
                gameField[row][column] = curShipLength; //начало корабля
                int[] prevMove = new int[]{row, column};

                for (int k = 0; k < curShipLength - 1; k++) {
                    //find next move point
                    int[] nextMove = findNextCoordinates(row, column, gameField);
                    //save history
                    prevMove[0] = row;
                    prevMove[1] = column;
                    //move cursor
                    row = nextMove[0];
                    column = nextMove[1];

                    gameField[nextMove[0]][nextMove[1]] = curShipLength; //row column
                }

            }
        }

    }

    /**
     * find next coordinates
     */
    int[] findNextCoordinates(int curRow, int curColumn, int[][] gameField) {

        /*
            Варианты хода
                y--
             x-- 0 x++
                y++

                Варианты (другие корабли)
                |row-2,col-1| row-2,col | row-2,col+1 |
            ---------------------------
     row-1,col-2|row-1,col-1| row-1,col | row-1,col+1| row-1,col+2
     ------------------------------------------
     row,col-2  |row,col-1   |   0       | row,col+1  | row,col+2
     ------------------------------------------
     row+1,col-2|row+1,col-1 | row+1,col | row+1,col+1 | row+1,col+2
     ------------------------------------------
                |row+2,col-1 |  row+2,col| row+2,col+1 |

         */

        int[][] result = new int[4][2];
        int chanceCnt = 0;

        //--------------------------------------------------------------------------
//        Check up from point
        try {
            int test = gameField[curRow - 1][curColumn]; // если нет Exception то ячейка свободна
            if (checkField(curRow - 2, curColumn - 1, curRow - 1, curColumn + 1, gameField)) {

                result[chanceCnt] = new int[]{curRow - 1, curColumn};//row , col
                chanceCnt++;

            }
        } catch (IndexOutOfBoundsException e) {/*ignore*/}
//        Check right from point
        try {
            int test = gameField[curRow][curColumn + 1];// если нет Exception то ячейка свободна
            if (checkField(curRow - 1, curColumn + 1, curRow + 1, curColumn + 2, gameField)) {

                result[chanceCnt] = new int[]{curRow, curColumn + 1};
                chanceCnt++;
            }
        } catch (IndexOutOfBoundsException e) {/*ignore*/}
//        Check down from point
        try {
            int test = gameField[curRow + 1][curColumn];// если нет Exception то ячейка свободна
            if (checkField(curRow + 1, curColumn - 1, curRow + 2, curColumn + 1, gameField)) {
                result[chanceCnt] = new int[]{curRow + 1, curColumn};
                chanceCnt++;
            }
        } catch (IndexOutOfBoundsException e) {/*ignore*/}
//        Check left from point
        try {
            int test = gameField[curRow][curColumn - 1];// если нет Exception то ячейка свободна
            if (checkField(curRow - 1, curColumn - 2, curRow + 1, curColumn - 1, gameField)) {
                result[chanceCnt] = new int[]{curRow, curColumn - 1};
                chanceCnt++;
            }
        } catch (IndexOutOfBoundsException e) {/*ignore*/}

        //Choice direction
        if (chanceCnt > 0) {
            int select = (int) (Math.random() * chanceCnt); //[0,chanceCnt]
            return result[select];
        } else {
            return null;
        }
    }

    /**
     * Check is available next move
     *
     * @param rowStart
     * @param colStart
     * @param rowEnd
     * @param colEnd
     * @param gameField
     * @return
     */
    boolean checkField(int rowStart, int colStart, int rowEnd, int colEnd, int[][] gameField) {
        for (int i = rowStart; i <= rowEnd; i++) { //rows
            for (int j = colStart; j <= colEnd; j++) {
                try {
                    if (gameField[i][j] != 0) {
                        return false;
                    }
                } catch (IndexOutOfBoundsException ex) {
                    /*ignore*/
                }
            }
        }
        return true;
    }

}
