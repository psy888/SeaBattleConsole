package com.psy888;

public class Game {
    //Field UI
    static final char CHAR_SHIP = 'o';
    static final char CHAR_SEA = '.';
    static final char CHAR_MISS = '-';
    static final char CHAR_HIT = '+';
    static final char CHAR_KILL = 'x';

    //Game field size 10x10
    static final int FIELD_SIZE = 10;

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
        printField();
        fillGameField(userField);
        printField();
        fillGameField(compField);
        printField();


    }

    /**
     * print game fields to console
     */
    public void printField() {
        for (int i = 0; i < FIELD_SIZE; i++) {
            for (int j = 0; j < FIELD_SIZE; j++) {
                System.out.print(userField[i][j] + " ");
            }
            System.out.print("\t\t");
            for (int j = 0; j < FIELD_SIZE; j++) {
                System.out.print(compField[i][j] + " ");
            }
            System.out.println();
        }
    }

    /**
     * make Shot
     */
    public void shot(int[] coordinates) {

        //todo: добавить проверку выиграша
        isUserTurn = !isUserTurn; // смена хода после выстрела

    }

    /**
     * parse user input
     */
    public int[] parseUserInput(String str) {
        return new int[2]; //x,y (0-line,1-column)
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
        for (int i = shipLength; i > 0; i--) {

            //отдельный корабль
            for (int j = 0; j <= shipLength - i; j++) {
               /* System.out.println("Введите координы " +
                        ((j==0)?"начала ":"")+
                        (shipLength-i+1) +
                        " палубного корабля");*/
                //координаты начала нового корабля
                int column;
                int row;
                do {
                    row = (int) (Math.random() * (10 - 1) + 1);
                    column = (int) (Math.random() * (10 - 1) + 1);
                }while (isItClear(column,row,gameField));
                //длинна текущего корабля
                int curShipLength = shipLength - (shipLength - i);

                //Палубы отдельного корабля
                gameField[row][column] = curShipLength; //начало корабля
                int[] prevMove = new int[]{column,row};
                for (int k = 0; k < curShipLength-1; k++) {
                    //todo определить следущие координаты
                    int[] nextMove = findNextCoordinates(column, row, gameField);
                    if(nextMove==null){
                        nextMove = findNextCoordinates(prevMove[1],prevMove[0],gameField);
                    }
                    prevMove = nextMove;

                    gameField[nextMove[1]][nextMove[0]] = curShipLength; //row column
                }

            }
        }

    }

    /**
     * find next coordinates
     */
    int[] findNextCoordinates(int curColumn, int curRow, int[][] gameField) {

        /*
            Варианты хода
                y--
             x-- 0 x++
                y++

                Варианты (другие корабли)
            |x-1,y-2| x,y-2 | x+1,y-2 |
            ---------------------------
     x-2,y-1|x-1,y-1| x,y-1 | x+1,y-1 | x+2,y-1
     ------------------------------------------
     x-2,y  |x-1,y  |   0   | x+1,y   | x+2,y
     ------------------------------------------
     x-2,y+1|x-1,y+1| x,y+1 | x+1,y+1 | x+2,y+1
     ------------------------------------------
            |x-1,y+2| x,y+2 | x+1,y+2 |

         */

        int[][] result = new int[4][2];
        int chanceCnt = 0;

        int curShipLength = gameField[curColumn][curRow];

        //--------------------------------------------------------------------------
        if (isUpClear(curColumn, curRow, gameField)) {
            result[chanceCnt] = new int[]{curColumn, curRow - 1};
            chanceCnt++;
        }
        if (isRightClear(curColumn, curRow, gameField)) {
            result[chanceCnt] = new int[]{curColumn + 1, curRow};
            chanceCnt++;
        }
        if (isDownClear(curColumn, curRow, gameField)) {
            result[chanceCnt] = new int[]{curColumn, curRow + 1};
            chanceCnt++;
        }
        if (isLeftClear(curColumn, curRow, gameField)) {
            result[chanceCnt] = new int[]{curColumn - 1, curRow};
            chanceCnt++;
        }


        if (chanceCnt > 0) {
            int select = (int) (Math.random() * chanceCnt); //[0,chanceCnt]
            return result[select];
        } else {
            return null;
        }
    }

    /**
     * check up from coordinates
     *
     * @param row       - y
     * @param column    - x
     * @param gameField
     * @return
     */
    boolean isUpClear(int column, int row, int[][] gameField) {
        try {
            if (gameField[column][row - 1] == 0) {
                if (
                        gameField[column - 1][row - 1] == 0 &&
                                gameField[column - 1][row - 2] == 0 &&
                                gameField[column][row - 2] == 0 &&
                                gameField[column + 1][row - 2] == 0 &&
                                gameField[column + 1][row - 1] == 0
                ) {
                    return true;
//                    result[chanceCnt++] = new int[]{column, row - 1};
                }
            }
        } catch (IndexOutOfBoundsException ex) {/*ignore*/}
        return false;
    }

    /**
     * check right of coordinates
     *
     * @param row
     * @param column
     * @param gameField
     * @return
     */
    boolean isRightClear(int column, int row, int[][] gameField) {
        try {
            if (gameField[column + 1][row] == 0) {
                if (
                        gameField[column + 1][row - 1] == 0 &&
                                gameField[column + 2][row - 1] == 0 &&
                                gameField[column + 2][row] == 0 &&
                                gameField[column + 1][row + 1] == 0 &&
                                gameField[column + 2][row + 1] == 0
                ) {
//                    result[chanceCnt++] = new int[]{column + 1, row};
                    return true;
                }
            }
        } catch (IndexOutOfBoundsException ex) {/*ignore*/}
        return false;
    }

    /**
     * Check down of coordinates
     *
     * @param row
     * @param column
     * @param gameField
     * @return
     */
    boolean isDownClear(int column, int row, int[][] gameField) {
        try {
            if (gameField[column][row + 1] == 0) {
                if (
                        gameField[column + 1][row + 1] == 0 &&
                                gameField[column + 1][row + 2] == 0 &&
                                gameField[column][row + 2] == 0 &&
                                gameField[column - 1][row + 2] == 0 &&
                                gameField[column - 1][row + 1] == 0
                ) {
//                    result[chanceCnt++] = new int[]{column, row + 1};
                    return true;
                }
            }
        } catch (IndexOutOfBoundsException ex) {/*ignore*/}
        return false;
    }

    /**
     * Check left of coordinates
     *
     * @param row
     * @param column
     * @param gameField
     * @return
     */
    boolean isLeftClear(int column, int row, int[][] gameField) {
        try {
            if (gameField[column - 1][row] == 0) {
                if (
                        gameField[column - 1][row + 1] == 0 &&
                                gameField[column - 2][row + 1] == 0 &&
                                gameField[column - 2][row] == 0 &&
                                gameField[column - 2][row - 1] == 0 &&
                                gameField[column - 1][row - 1] == 0
                ) {
//                    result[chanceCnt++] = new int[]{curX - 1, curY};
                    return true;
                }
            }
        } catch (IndexOutOfBoundsException ex) {/*ignore*/}
        return false;
    }

    boolean isItClear(int column, int row, int[][] gameField) {
        try {
            int leftDown = gameField[column - 1][row + 1];
            int left = gameField[column - 1][row];
            int leftUp = gameField[column - 1][row - 1];
            int up = gameField[column][row - 1];
            int rightUp = gameField[column + 1][row - 1];
            int right = gameField[column + 1][row];
            int rightDown = gameField[column + 1][row + 1];
            int down = gameField[column][row + 1];

            if (gameField[column][row] == 0) {
                if (leftDown < 0&&
                        left<0&&
                        leftUp<0&&
                        up<0&&
                        rightUp<0&&
                        right<0&&
                        rightDown<0&&
                        down<0
                ) {
//                    result[chanceCnt++] = new int[]{curX - 1, curY};
                    return true;
                }
            }
        } catch (IndexOutOfBoundsException ex) {/*ignore*/}
        return false;
    }


}
