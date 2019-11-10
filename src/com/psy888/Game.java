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


    }

    /**
     * print game fields to console
     */
    public void printField() {
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
                //координаты начала
                int x = (int) (Math.random() * (10 - 1) + 1);
                int y = (int) (Math.random() * (10 - 1) + 1);
                //длинна текущего корабля
                int curShipLength = shipLength - (shipLength - i);
                //Палубы отдельного корабля
                for (int k = 0; k < curShipLength; k++) {
                    (gameField[y][x] = curShipLength;
                    //todo определить следущие координаты
                    int[] nextMove = findNextCoordinates(x, y, gameField);

                }

            }
        }

    }

    /**
     * find next coordinates
     */
    int[] findNextCoordinates(int curX, int curY, int[][] gameField) {

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

        int[] resultUp;  //4 варианта по 2 координаты
        int[] resultRight; //4 варианта по 2 координаты
        int[] resultDown;//4 варианта по 2 координаты
        int[] resultLeft; //4 варианта по 2 координаты
        int[][] result = new int[4][2];
        int chanceCnt = 0;

        int curShipLength = gameField[curX][curY];

        //--------------------------------------------------------------------------
        //up - 0
        try {
            if (gameField[curX][curY - 1] == 0) {
                if (
                        gameField[curX - 1][curY - 1] == 0 &&
                                gameField[curX - 1][curY - 2] == 0 &&
                                gameField[curX][curY - 2] == 0 &&
                                gameField[curX + 1][curY - 2] == 0 &&
                                gameField[curX + 1][curY - 1] == 0
                ) {
                    result[chanceCnt++] = new int[]{curX, curY - 1};
                }
            }
        } catch (IndexOutOfBoundsException ex) {/*ignore*/}
        //right - 1
        try {
            if (gameField[curX + 1][curY] == 0) {
                if (
                        gameField[curX + 1][curY - 1] == 0 &&
                                gameField[curX + 2][curY - 1] == 0 &&
                                gameField[curX + 2][curY] == 0 &&
                                gameField[curX + 1][curY + 1] == 0 &&
                                gameField[curX + 2][curY + 1] == 0
                ) {
                    result[chanceCnt++] = new int[]{curX + 1, curY};

                }
            }
        } catch (IndexOutOfBoundsException ex) {/*ignore*/}

        //down - 2
        try {
            if (gameField[curX][curY + 1] == 0) {
                if (
                        gameField[curX + 1][curY + 1] == 0 &&
                                gameField[curX + 1][curY + 2] == 0 &&
                                gameField[curX][curY + 2] == 0 &&
                                gameField[curX - 1][curY + 2] == 0 &&
                                gameField[curX - 1][curY + 1] == 0
                ) {
                    result[chanceCnt++] = new int[]{curX, curY + 1};

                }
            }
        } catch (IndexOutOfBoundsException ex) {/*ignore*/}
        //left - 3
        try {
            if (gameField[curX - 1][curY] == 0) {
                if (
                        gameField[curX - 1][curY + 1] == 0 &&
                                gameField[curX - 2][curY + 1] == 0 &&
                                gameField[curX - 2][curY] == 0 &&
                                gameField[curX - 2][curY - 1] == 0 &&
                                gameField[curX - 1][curY - 1] == 0
                ) {
                    result[chanceCnt++] = new int[]{curX - 1, curY};
                }
            }
        } catch (IndexOutOfBoundsException ex) {/*ignore*/}

        if (chanceCnt > 0) {
            int select = (int) (Math.random() * chanceCnt + 1); //[0,chanceCnt]
            return result[select];
        } else {
            return null;
        }
    }

    /**
     * check up from coordinates
     * @param row - y
     * @param column - x
     * @param gameField
     * @return
     */
    boolean isUpClear(int row, int column, int[][] gameField) {
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
     * @param row
     * @param column
     * @param gameField
     * @return
     */
    boolean isRightClear(int row, int column, int[][] gameField) {
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
     * @param row
     * @param column
     * @param gameField
     * @return
     */
    boolean isDownClear(int row, int column, int[][] gameField) {
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
     * @param row
     * @param column
     * @param gameField
     * @return
     */
    boolean isLeftClear(int row, int column, int[][] gameField) {
        try {
            if (gameField[column - 1][row] == 0) {
                if (
                        gameField[column - 1][row + 1] == 0 &&
                                gameField[column - 2][row + 1] == 0 &&
                                gameField[column - 2][row] == 0 &&
                                gameField[column - 2][row - 1] == 0 &&
                                gameField[column - 1][row - 1] == 0
                ) {
                    result[chanceCnt++] = new int[]{curX - 1, curY};
                }
            }
        } catch (IndexOutOfBoundsException ex) {/*ignore*/}
        return false;
    }


}
